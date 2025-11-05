CREATE TABLE Usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Perfis (
    id_usuario INT NOT NULL REFERENCES Usuarios(id) ON DELETE CASCADE,
    perfil VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_usuario, perfil)
);

CREATE TABLE Espacos_fisicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    metragem NUMERIC(5, 2) NOT NULL,
    disponibilidade VARCHAR(25) NOT NULL CHECK (disponibilidade IN ('DISPONIVEL', 'INDISPONIVEL'))
);

CREATE TABLE Equipamentos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL
);

CREATE TABLE Espacos_Equipamentos (
    id INT NOT NULL REFERENCES Espacos_fisicos(id) ON DELETE CASCADE,
    equipamento_id INT NOT NULL REFERENCES Equipamentos(id) ON DELETE CASCADE,
    PRIMARY KEY (id, equipamento_id)
);

CREATE TABLE Avaliador (
    id SERIAL PRIMARY KEY,
    id_avaliador INT NOT NULL REFERENCES Usuarios(id)
);

CREATE TABLE Solicitacoes (
    id SERIAL PRIMARY KEY,
    id_espaco INT NOT NULL REFERENCES Espacos_Fisicos(id), 
    id_solicitante INT NOT NULL REFERENCES Usuarios(id),
	id_avaliador INT REFERENCES Avaliador(id),
    nome VARCHAR(255) NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
	data_solicitacao TIMESTAMP NOT NULL,
	data_avaliacao TIMESTAMP,
    status VARCHAR(50) NOT NULL CHECK (status IN ('APROVADA', 'REJEITADA', 'PENDENTE')), 
    justificativa TEXT
);

CREATE TABLE Auditoria (
    id SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES Usuarios(id),
    acao VARCHAR(255) NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    detalhes TEXT
);

CREATE TABLE Feriados (
    id SERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL,
    data DATE NOT NULL UNIQUE
);

CREATE OR REPLACE FUNCTION processa_usuario_para_avaliador()
RETURNS VOID AS $$
DECLARE
    usuario RECORD;
BEGIN
    FOR usuario IN
        SELECT p.id_usuario
        FROM Perfis p
        LEFT JOIN Avaliador a ON p.id_usuario = a.id_avaliador
        WHERE p.perfil IN ('GESTOR', 'ADMINISTRADOR')
          AND a.id_avaliador IS NULL
    LOOP
        INSERT INTO Avaliador (id_avaliador)
        VALUES (usuario.id_usuario);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trigger_perfis_to_avaliador()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.perfil IN ('GESTOR', 'ADMINISTRADOR') THEN
        IF NOT EXISTS (
            SELECT 1
            FROM Avaliador
            WHERE id_avaliador = NEW.id_usuario
        ) THEN
            INSERT INTO Avaliador (id_avaliador)
            VALUES (NEW.id_usuario);
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_perfil_insert
AFTER INSERT OR UPDATE ON Perfis
FOR EACH ROW
EXECUTE FUNCTION trigger_perfis_to_avaliador();

CREATE OR REPLACE FUNCTION verifica_sobreposicao()
RETURNS TRIGGER AS $$
DECLARE
    dia_semana INT;
    data_hora_inicio TIMESTAMP;
    data_hora_fim TIMESTAMP;
    feriado DATE;
    solicitacao RECORD;
BEGIN
    data_hora_inicio := NEW.data_inicio + NEW.hora_inicio;
    data_hora_fim := NEW.data_fim + NEW.hora_fim;

    IF (EXTRACT(HOUR FROM data_hora_inicio) < 7 OR EXTRACT(HOUR FROM data_hora_fim) > 22) THEN
        RAISE EXCEPTION 'Horário fora do intervalo permitido (07:00 - 22:00)';
    END IF;
    dia_semana := EXTRACT(DOW FROM data_hora_inicio);
    IF dia_semana = 0 THEN
        RAISE EXCEPTION 'Reservas não são permitidas aos domingos';
    END IF;

    FOR feriado IN
        SELECT data FROM Feriados
    LOOP
        IF NEW.data_inicio::DATE = feriado THEN
            RAISE EXCEPTION 'Reservas não são permitidas em feriados';
        END IF;
    END LOOP;

    FOR solicitacao IN
        SELECT * 
        FROM Solicitacoes
        WHERE id_espaco = NEW.id_espaco
          AND status = 'APROVADA'
    LOOP
        IF (solicitacao.data_inicio + solicitacao.hora_inicio, solicitacao.data_fim + solicitacao.hora_fim) OVERLAPS (data_hora_inicio, data_hora_fim) THEN
            RAISE EXCEPTION 'Reserva sobreposta detectada!';
        END IF;
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION processa_avaliacao()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Auditoria (id_usuario, acao, detalhes, data)
    VALUES (
        NEW.id_avaliador, 
        'Avaliou solicitação', 
        'Solicitação ' || NEW.id || ' foi ' || NEW.status,
        CURRENT_TIMESTAMP 
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_avaliar_solicitacao
AFTER UPDATE OF status ON Solicitacoes
FOR EACH ROW
WHEN (NEW.status IN ('Aprovada', 'Rejeitada'))
EXECUTE FUNCTION processa_avaliacao();

CREATE TRIGGER trigger_sobreposicao
BEFORE INSERT OR UPDATE ON Solicitacoes
FOR EACH ROW
EXECUTE FUNCTION verifica_sobreposicao();

