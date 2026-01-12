-- DQL

WITH base AS (
  SELECT
    d.nome                AS disciplina,
    d.codigo              AS cod_disciplina,
    t.ano, t.semestre,
    al.matricula,
    al.nome               AS aluno,
    av.unidade,
    av.ordem,
    av.peso,
    n.nota
  FROM Lab1.notas n
  JOIN Lab1.avaliacoes av   ON av.id_avaliacao = n.id_avaliacao
  JOIN Lab1.matriculas m    ON m.id_matricula  = n.id_matricula
  JOIN Lab1.turmas t        ON t.id_turma      = av.id_turma
  JOIN Lab1.disciplinas d   ON d.id_disciplina = t.id_disciplina
  JOIN Lab1.alunos al       ON al.id_aluno     = m.id_aluno
  -- Filtros 
  WHERE d.codigo = 'BD02'  
    AND t.ano = 2025
    AND t.semestre = 1
)
, pivot AS (
  SELECT
    disciplina, cod_disciplina, ano, semestre, matricula, aluno,
    MAX(CASE WHEN unidade=1 AND ordem=1 THEN nota END) AS u1_a1,
    MAX(CASE WHEN unidade=1 AND ordem=2 THEN nota END) AS u1_a2,
    MAX(CASE WHEN unidade=2 AND ordem=1 THEN nota END) AS u2_a1,
    MAX(CASE WHEN unidade=2 AND ordem=2 THEN nota END) AS u2_a2,
    -- Resultados por unidade
    LEAST(
      SUM(CASE WHEN unidade=1 THEN nota * peso END), 10
    ) AS resultado_u1,
    LEAST(
      SUM(CASE WHEN unidade=2 THEN nota * peso END), 10
    ) AS resultado_u2
  FROM base
  GROUP BY disciplina, cod_disciplina, ano, semestre, matricula, aluno
)
SELECT
  disciplina,
  matricula,
  aluno AS nome_aluno,
  u1_a1 AS "1a atv U1",
  u1_a2 AS "2a atv U1",
  u2_a1 AS "1a atv U2",
  u2_a2 AS "2a atv U2",
  ROUND(resultado_u1,2) AS "Resultado U1",
  ROUND(resultado_u2,2) AS "Resultado U2",
  ROUND( (resultado_u1 + resultado_u2)/2.0 , 2) AS "Media  Final",
  CASE
    WHEN (resultado_u1 + resultado_u2)/2.0 >= 6 THEN 'APROVADO'
    WHEN (resultado_u1 + resultado_u2)/2.0 >= 4 THEN 'EXAME FINAL'
    ELSE 'REPROVADO'
  END AS status
FROM pivot
ORDER BY disciplina, aluno;
