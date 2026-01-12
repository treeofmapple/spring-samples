-- DML

-- Alunos
INSERT INTO Lab1.alunos (matricula, nome) VALUES
  ('200022939', 'Gabriel Mendonca'),
  ('200029109', 'Bruno Souza'),
  ('200022453', 'Carla Nunes');

-- Disciplinas
INSERT INTO Lab1.disciplinas (codigo, nome) VALUES
  ('BD02',  'Banco de Dados II'),
  ('ALGO1', 'Algoritmos I');

-- Turmas (semestre 2025/1)
INSERT INTO Lab1.turmas (id_disciplina, ano, semestre)
SELECT id_disciplina, 2025, 1
FROM Lab1.disciplinas
WHERE codigo IN ('BD02','ALGO1');

-- Matriculas: todos os alunos nas duas turmas
INSERT INTO Lab1.matriculas (id_aluno, id_turma)
SELECT a.id_aluno, t.id_turma
FROM Lab1.alunos a
CROSS JOIN Lab1.turmas t;

-- AV para cada turma: U1(A1=40%, A2=60%), U2(A1=40%, A2=60%)
-- Cria 4 AV por turma
INSERT INTO Lab1.avaliacoes (id_turma, unidade, ordem, peso, descricao)
SELECT t.id_turma, u.unidade, u.ordem, u.peso, u.descricao
FROM Lab1.turmas t
JOIN (
  VALUES
    (1,1,0.40,'U1 - Atividade 1 (40%)'),
    (1,2,0.60,'U1 - Atividade 2 (60%)'),
    (2,1,0.40,'U2 - Atividade 1 (40%)'),
    (2,2,0.60,'U2 - Atividade 2 (60%)')
) AS u(unidade, ordem, peso, descricao) ON TRUE;

-- Notas exemplo SO para a turma BD02
WITH bd AS (
  SELECT t.id_turma
  FROM Lab1.turmas t
  JOIN Lab1.disciplinas d ON d.id_disciplina = t.id_disciplina
  WHERE d.codigo = 'BD02' AND t.ano = 2025 AND t.semestre = 1
),
av AS (
  SELECT a.id_avaliacao, a.unidade, a.ordem
  FROM Lab1.avaliacoes a
  JOIN bd USING (id_turma)
),
mat AS (
  SELECT m.id_matricula, m.id_turma, al.matricula
  FROM Lab1.matriculas m
  JOIN bd USING (id_turma)
  JOIN Lab1.alunos al ON al.id_aluno = m.id_aluno
),
-- Tabela de valores com as notas por aluno/unidade/ordem
notas_input AS (
  SELECT * FROM (VALUES
    ('200022939',1,1,7.5),
    ('200022939',1,2,8.0),
    ('200022939',2,1,6.0),
    ('200022939',2,2,9.0),
    ('200029109',1,1,5.0),
    ('200029109',1,2,6.5),
    ('200029109',2,1,7.0),
    ('200029109',2,2,7.5),
    ('200022453',1,1,9.0),
    ('200022453',1,2,9.5),
    ('200022453',2,1,8.0),
    ('200022453',2,2,8.5)
  ) AS x(matricula, unidade, ordem, nota)
)
-- Insercao UNICA de todas as notas
INSERT INTO Lab1.notas (id_matricula, id_avaliacao, nota)
SELECT m.id_matricula, a.id_avaliacao, x.nota
FROM notas_input x
JOIN mat m ON m.matricula = x.matricula
JOIN av  a ON a.unidade   = x.unidade
          AND a.ordem     = x.ordem;
