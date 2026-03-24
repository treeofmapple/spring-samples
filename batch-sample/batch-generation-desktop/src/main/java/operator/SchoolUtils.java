package operator;

import lombok.RequiredArgsConstructor;
import school.Aluno;
import school.Avaliacao;
import school.Disciplina;
import school.Matricula;
import school.Nota;
import school.Turma;

@RequiredArgsConstructor
public class SchoolUtils extends GenerateData {

	private final IdGenerator generator;

	private short year_MIN = 2020, gender = 50;

	public Aluno generateSingleAluno() {
		Aluno alunos = new Aluno();
		alunos.setId(generator.nextId());
		alunos.setMatricula(generator.generateRandomUUID());
		alunos.setNome(genderChance(gender));
		return alunos;
	}

	public Disciplina generateSingleDisciplina() {
		Disciplina disciplinas = new Disciplina();
		disciplinas.setId(generator.nextId());
		disciplinas.setCodigo("DISC-" + generateRandomShort(0, 999));
		disciplinas.setNome(generateBookTitle());
		return disciplinas;
	}

	public Turma generateSingleTurma(Disciplina disciplinas) {
		Turma turmas = new Turma();
		turmas.setId(generator.nextId());
		turmas.setId_disciplina(disciplinas.getId());
		turmas.setAno(generateRandomShort(year_MIN, 2025));
		turmas.setSemestre(generateRandomShort(1, 4));
		return turmas;
	}

	public Matricula generateSingleMatricula(Aluno alunos, Turma turmas) {
		Matricula matriculas = new Matricula();
		matriculas.setId(generator.nextId());
		matriculas.setId_aluno(alunos.getId());
		matriculas.setId_turma(turmas.getId());
		return matriculas;
	}

	public Avaliacao generateSingleAvaliacao(Turma turmas) {
		Avaliacao avaliacoes = new Avaliacao();
		avaliacoes.setId(generator.nextId());
		avaliacoes.setId_turma(turmas.getId());
		avaliacoes.setOrdem(generateRandomShort(1, 5));
		avaliacoes.setPeso(generateRandomBigDecimal(1, 5));
		avaliacoes.setUnidade(generateRandomShort(1, 4));
		avaliacoes.setDescricao("Prova: " + avaliacoes.getOrdem() + "\n Unidade: " + avaliacoes.getUnidade());
		return avaliacoes;
	}

	public Nota generateSingleNotas(Avaliacao avaliacoes) {
		Nota notas = new Nota();
		notas.setId(generator.nextId());
		notas.setId_avaliacao(avaliacoes.getId());
		notas.setNota(generateRandomBigDecimal(1, 10));
		return notas;
	}

}
