-- Creates the 'empresas' table
CREATE TABLE empresas (
    id_empresa BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    endereco TEXT
);

-- Creates the 'funcionario_cliente' table
CREATE TABLE funcionario_cliente (
    id_funcionario BIGINT PRIMARY KEY,
    id_empresa BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    data_nascimento DATE,
    cargo VARCHAR(255),
    FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa)
);

-- Creates the 'funcionario_clinica' table
CREATE TABLE funcionario_clinica (
    id_funcionario_clinica BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(50),
    funcao VARCHAR(255)
);

-- Creates the 'tipos_exames' table
CREATE TABLE tipos_exames (
    id_tipos_exames BIGINT PRIMARY KEY,
    nome_tipo VARCHAR(255) NOT NULL,
    descricao TEXT
);

-- Creates the 'risco_ocupacional' table
CREATE TABLE risco_ocupacional (
    id_risco_ocupacional BIGINT PRIMARY KEY,
    nome_risco VARCHAR(255) NOT NULL,
    descricao TEXT
);

-- Creates the 'exames' table
CREATE TABLE exames (
    id_exames BIGINT PRIMARY KEY,
    id_funcionario BIGINT NOT NULL,
    id_funcionario_clinica BIGINT NOT NULL,
    id_tipos_exames BIGINT NOT NULL,
    data_exame DATE,
    FOREIGN KEY (id_funcionario) REFERENCES funcionario_cliente(id_funcionario),
    FOREIGN KEY (id_funcionario_clinica) REFERENCES funcionario_clinica(id_funcionario_clinica),
    FOREIGN KEY (id_tipos_exames) REFERENCES tipos_exames(id_tipos_exames)
);

-- Creates the join table 'risco_ocupacional_exames'
CREATE TABLE risco_ocupacional_exames (
    id_risco_ocupacional_exames BIGINT PRIMARY KEY,
    id_exames BIGINT NOT NULL,
    id_risco_ocupacional BIGINT NOT NULL,
    FOREIGN KEY (id_exames) REFERENCES exames(id_exames),
    FOREIGN KEY (id_risco_ocupacional) REFERENCES risco_ocupacional(id_risco_ocupacional)
);
