set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL

    SELECT 'CREATE DATABASE $DB_VEHICLE'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_VEHICLE')\gexec

    SELECT 'CREATE DATABASE $DB_LIBRARY'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_LIBRARY')\gexec

    SELECT 'CREATE DATABASE $DB_GRADES'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_GRADES')\gexec

    SELECT 'CREATE DATABASE $DB_USERNAME'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_USERNAME')\gexec
EOSQL

echo "Databases created successfully"
