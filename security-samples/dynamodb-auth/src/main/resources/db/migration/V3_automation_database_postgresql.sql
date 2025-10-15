CREATE TABLE login_history_default PARTITION OF login_history DEFAULT;

CREATE OR REPLACE FUNCTION create_monthly_login_history_partitions()
RETURNS void AS $$
DECLARE
    partition_date DATE;
    partition_name TEXT;
    start_of_month TEXT;
    end_of_month TEXT;
BEGIN
    FOR i IN 0..1 LOOP
        partition_date := date_trunc('month', NOW() + (i * INTERVAL '1 month'));
        partition_name := 'login_history_' || to_char(partition_date, 'YYYY_MM');
        start_of_month := to_char(partition_date, 'YYYY-MM-DD 00:00:00+00');
        end_of_month := to_char(partition_date + INTERVAL '1 month', 'YYYY-MM-DD 00:00:00+00');

        IF NOT EXISTS(SELECT 1 FROM pg_class WHERE relname = partition_name) THEN
            RAISE NOTICE 'Creating partition: %', partition_name;
            EXECUTE format(
                'CREATE TABLE %I PARTITION OF login_history FOR VALUES FROM (%L) TO (%L);',
                partition_name,
                start_of_month,
                end_of_month
            );
        ELSE
            RAISE NOTICE 'Partition % already exists, skipping.', partition_name;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION drop_old_login_history_partitions(
    retention_period INTERVAL
)
RETURNS void AS $$
DECLARE
    partition_name TEXT;
    partition_cutoff_date TIMESTAMPTZ;
BEGIN
    partition_cutoff_date := date_trunc('month', NOW() - retention_period);

    RAISE NOTICE 'Dropping partitions older than %', partition_cutoff_date;

    FOR partition_name IN
        SELECT
            child.relname
        FROM pg_inherits
            JOIN pg_class parent ON pg_inherits.inhparent = parent.oid
            JOIN pg_class child ON pg_inherits.inhrelid = child.oid
        WHERE
            parent.relname = 'login_history'
    LOOP
        DECLARE
            partition_end_date TIMESTAMPTZ;
        BEGIN
            partition_end_date := (regexp_matches(
                pg_get_expr(
                    (SELECT relpartbound FROM pg_class WHERE relname = partition_name),
                    (SELECT oid FROM pg_class WHERE relname = partition_name)
                ),
                'TO \(''(\d{4}-\d{2}-\d{2})'
            ))[1]::TIMESTAMPTZ;

            IF partition_end_date <= partition_cutoff_date THEN
                RAISE NOTICE 'Dropping old partition: %', partition_name;
                EXECUTE format('DROP TABLE IF EXISTS %I;', partition_name);
            END IF;
        EXCEPTION
            WHEN others THEN
                CONTINUE;
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Schedule the creation job to run every Sunday at 1 AM
SELECT cron.schedule(
    'create-monthly-partitions',
    '0 1 * * 0', -- Cron syntax for 1:00 AM every Sunday
    $$ SELECT create_monthly_login_history_partitions(); $$
);

-- Schedule the cleanup job to run every day at 2 AM
-- It will drop partitions older than 12 months
SELECT cron.schedule(
    'drop-old-partitions',
    '0 2 * * *', -- Cron syntax for 2:00 AM every day
    $$ SELECT drop_old_login_history_partitions('12 months'); $$
);