\echo 'Step 0: Preparing database for fast import...'
\echo 'Step 1: Creating tables...'

CREATE TABLE empresas (
    id_empresa BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL,
    endereco TEXT,
    phone VARCHAR(20),
    quantidade_funcionarios_cliente INT,
    quantidade_funcionarios_clinica INT,
    registration_date DATE,
    status VARCHAR(20)
);

CREATE TABLE funcionario_cliente (
    id_funcionario BIGINT NOT NULL,
    id_empresa BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    email VARCHAR(255),
    senha VARCHAR(255),
    data_nascimento DATE,
    cargo VARCHAR(255),
    departamento VARCHAR(255),
    admission_date DATE,
    status VARCHAR(20),
    role VARCHAR(255)
);

CREATE TABLE funcionario_clinica (
    id_funcionario_clinica BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(50),
    funcao VARCHAR(255),
    senha VARCHAR(255),
    especialidade VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    data_contratacao DATE,
    status VARCHAR(20),
    role VARCHAR(255)
);

CREATE TABLE tipos_exames (
    id_tipos_exames BIGINT NOT NULL,
    nome_tipo VARCHAR(255) NOT NULL,
    descricao TEXT
);

CREATE TABLE risco_ocupacional (
    id_risco_ocupacional BIGINT NOT NULL,
    nome_risco VARCHAR(255) NOT NULL,
    descricao TEXT,
    severity VARCHAR(20),
    preventive_measures TEXT,
    risk_category VARCHAR(50),
    exposure_level VARCHAR(20),
    status VARCHAR(20)
);

CREATE TABLE exames (
    id_exames BIGINT NOT NULL,
    id_funcionario BIGINT NOT NULL,
    id_funcionario_clinica BIGINT NOT NULL,
    id_tipos_exames BIGINT NOT NULL,
    data_exame DATE,
    type_exam VARCHAR(50),
    horario_exame VARCHAR(10),
    result VARCHAR(50),
    status VARCHAR(50),
    observations TEXT
);

CREATE TABLE risco_ocupacional_exames (
    id_risco_ocupacional_exames BIGINT NOT NULL,
    id_exames BIGINT NOT NULL,
    id_risco_ocupacional BIGINT NOT NULL
);

\echo 'tables created successfully.'

\echo 'Step 2: Starting data import...'
\timing on
\echo 'Importing empresas...'
COPY empresas(
    id_empresa,
    nome,
    cnpj,
    endereco,
    phone,
    quantidade_funcionarios_cliente,
    quantidade_funcionarios_clinica,
    registration_date,
    status
)
FROM '/docker-entrypoint-initdb.d/empresas.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing funcionario_cliente...'
COPY funcionario_cliente(
    id_funcionario,
    id_empresa,
    nome,
    cpf,
    senha,
    email,
    data_nascimento,
    cargo,
    departamento,
    admission_date,
    status,
    role
)
FROM '/docker-entrypoint-initdb.d/funcionario_cliente.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing funcionario_clinica...'
COPY funcionario_clinica(
    id_funcionario_clinica,
    nome,
    crm,
    funcao,
    senha,
    especialidade,
    email,
    phone,
    data_contratacao,
    status,
    role
)
FROM '/docker-entrypoint-initdb.d/funcionario_clinica.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing tipos_exames...'
COPY tipos_exames(
    id_tipos_exames,
    nome_tipo,
    descricao
)
FROM '/docker-entrypoint-initdb.d/tipos_exames.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing risco_ocupacional...'
COPY risco_ocupacional(
    id_risco_ocupacional,
    nome_risco,
    descricao,
    severity,
    preventive_measures,
    risk_category,
    exposure_level,
    status
)
FROM '/docker-entrypoint-initdb.d/risco_ocupacional.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing exames...'
COPY exames(
    id_exames,
    id_funcionario,
    id_funcionario_clinica,
    id_tipos_exames,
    data_exame,
    type_exam,
    horario_exame,
    result,
    status,
    observations
)
FROM '/docker-entrypoint-initdb.d/exames.csv' WITH (FORMAT csv, HEADER true);

\echo 'Importing risco_ocupacional_exames...'
COPY risco_ocupacional_exames(
    id_risco_ocupacional_exames,
    id_exames,
    id_risco_ocupacional
)
FROM '/docker-entrypoint-initdb.d/risco_ocupacional_exames.csv' WITH (FORMAT csv, HEADER true);

\timing off
\echo 'All data imported successfully.'
\echo 'Step 3: Adding primary keys, unique constraints, and foreign keys...'

ALTER TABLE empresas ADD PRIMARY KEY (id_empresa);
ALTER TABLE empresas ADD CONSTRAINT empresas_cnpj_unique UNIQUE (cnpj);

ALTER TABLE funcionario_cliente ADD PRIMARY KEY (id_funcionario);
ALTER TABLE funcionario_cliente ADD CONSTRAINT funcionario_cliente_cpf_unique UNIQUE (cpf);

ALTER TABLE funcionario_clinica ADD PRIMARY KEY (id_funcionario_clinica);
ALTER TABLE tipos_exames ADD PRIMARY KEY (id_tipos_exames);
ALTER TABLE risco_ocupacional ADD PRIMARY KEY (id_risco_ocupacional);
ALTER TABLE exames ADD PRIMARY KEY (id_exames);
ALTER TABLE risco_ocupacional_exames ADD PRIMARY KEY (id_risco_ocupacional_exames);

ALTER TABLE funcionario_cliente ADD FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa);

ALTER TABLE exames ADD FOREIGN KEY (id_funcionario) REFERENCES funcionario_cliente(id_funcionario);
ALTER TABLE exames ADD FOREIGN KEY (id_funcionario_clinica) REFERENCES funcionario_clinica(id_funcionario_clinica);
ALTER TABLE exames ADD FOREIGN KEY (id_tipos_exames) REFERENCES tipos_exames(id_tipos_exames);

ALTER TABLE risco_ocupacional_exames ADD FOREIGN KEY (id_exames) REFERENCES exames(id_exames);
ALTER TABLE risco_ocupacional_exames ADD FOREIGN KEY (id_risco_ocupacional) REFERENCES risco_ocupacional(id_risco_ocupacional);

\echo 'All constraints and keys added.'
\echo 'Step 4: Setting up auto-increment for future inserts...'

ALTER TABLE empresas ALTER COLUMN id_empresa ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE funcionario_cliente ALTER COLUMN id_funcionario ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE funcionario_clinica ALTER COLUMN id_funcionario_clinica ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE tipos_exames ALTER COLUMN id_tipos_exames ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE risco_ocupacional ALTER COLUMN id_risco_ocupacional ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE exames ALTER COLUMN id_exames ADD GENERATED BY DEFAULT AS IDENTITY;
ALTER TABLE risco_ocupacional_exames ALTER COLUMN id_risco_ocupacional_exames ADD GENERATED BY DEFAULT AS IDENTITY;

\echo 'Auto-increment configured.'

CREATE INDEX idx_funcionario_clinica_email ON funcionario_clinica (email);
CREATE INDEX idx_funcionario_cliente_email ON funcionario_cliente (email);
CREATE INDEX idx_funcionario_cliente_id_empresa ON funcionario_cliente (id_empresa);

\echo 'Indexation Configured.'

\echo 'Step 5: Running ANALYZE...'
ANALYZE;
\echo 'Analyze complete.'

\echo 'Step 6: Resetting identity sequences...'

SELECT setval(pg_get_serial_sequence('empresas', 'id_empresa'), COALESCE((SELECT MAX(id_empresa) FROM empresas), 1));
SELECT setval(pg_get_serial_sequence('funcionario_cliente', 'id_funcionario'), COALESCE((SELECT MAX(id_funcionario) FROM funcionario_cliente), 1));
SELECT setval(pg_get_serial_sequence('funcionario_clinica', 'id_funcionario_clinica'), COALESCE((SELECT MAX(id_funcionario_clinica) FROM funcionario_clinica), 1));
SELECT setval(pg_get_serial_sequence('tipos_exames', 'id_tipos_exames'), COALESCE((SELECT MAX(id_tipos_exames) FROM tipos_exames), 1));
SELECT setval(pg_get_serial_sequence('risco_ocupacional', 'id_risco_ocupacional'), COALESCE((SELECT MAX(id_risco_ocupacional) FROM risco_ocupacional), 1));
SELECT setval(pg_get_serial_sequence('exames', 'id_exames'), COALESCE((SELECT MAX(id_exames) FROM exames), 1));
SELECT setval(pg_get_serial_sequence('risco_ocupacional_exames', 'id_risco_ocupacional_exames'), COALESCE((SELECT MAX(id_risco_ocupacional_exames) FROM risco_ocupacional_exames), 1));

\echo 'Identity sequences reset successfully.'

\echo 'Database initialization complete!'
