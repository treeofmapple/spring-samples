package com.tom.service.datagen.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.service.datagen.common.Operations;
import com.tom.service.datagen.exception.InternalException;
import com.tom.service.datagen.model.school.Aluno;
import com.tom.service.datagen.model.school.Avaliacao;
import com.tom.service.datagen.model.school.Disciplina;
import com.tom.service.datagen.model.school.Matricula;
import com.tom.service.datagen.model.school.Nota;
import com.tom.service.datagen.model.school.Turma;
import com.tom.service.datagen.service.interfaces.BatchCallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class SchoolService {

	private final Map<String, byte[]> csvStorage = new ConcurrentHashMap<>();

	private final Operations operations;
	private final SchoolUtils schoolUtils;

	public byte[] generateSchoolData(int quantity) {
		log.info("Started to generate: {} data", quantity);
		try {
			clearPreviousData();

			List<Aluno> alunos = generateAlunos(quantity, (batch, batchNum, totalBatches) -> {
				log.info("Batch {} of {} saved (Size: {})", batchNum, totalBatches, batch.size());
			});
			List<Disciplina> disciplinas = generateDisciplinas((quantity / 5), (batch, batchNum, totalBatches) -> {
				log.info("Batch {} of {} saved (Size: {})", batchNum, totalBatches, batch.size());
			});
			
            List<Turma> turmas = generateTurmas(disciplinas);
            List<Matricula> matriculas = generateMatriculas(alunos, turmas);
            List<Avaliacao> avaliacoes = generateAvaliacoes(turmas);
            List<Nota> notas = generateNotas(matriculas, avaliacoes);
            
			byte[] alunosCsv = operations.convertToCSV(alunos, "id_aluno,matricula,nome",
					d -> d.getId() + "," + d.getMatricula() + ",\"" + d.getNome().replace("\"", "\"\"") + "\"");

			byte[] disciplinasCsv = operations.convertToCSV(disciplinas, "id_disciplina,codigo,nome",
					d -> d.getId() + "," + d.getCodigo() + ",\"" + d.getNome().replace("\"", "\"\"") + "\"");

			byte[] turmasCsv = operations.convertToCSV(turmas, "id_turma,id_disciplina,ano,semestre",
					d -> d.getId() + "," + d.getId_disciplina() + "," + d.getAno() + "," + d.getSemestre());

			byte[] matriculasCsv = operations.convertToCSV(matriculas, "id_matricula,id_aluno,id_turma",
					d -> d.getId() + "," + d.getId_aluno() + "," + d.getId_turma());

			byte[] avaliacoesCsv = operations.convertToCSV(avaliacoes, "id_avaliacao,id_turma,unidade,ordem,peso,descricao",
					d -> d.getId() + "," + d.getId_turma() + "," + d.getUnidade() + "," + d.getOrdem() + ","
							+ d.getPeso() + ",\"" + d.getDescricao() + "\"");

			byte[] notasCsv = operations.convertToCSV(notas, "id_matricula,id_avaliacao,nota",
					d -> d.getId() + "," + d.getId_avaliacao() + "," + d.getNota());

			return operations.resourceToBytes(Map.of("1_alunos.csv", alunosCsv, "2_disciplinas.csv", disciplinasCsv,
					"3_turmas.csv", turmasCsv, "4_matriculas.csv", matriculasCsv, "5_avaliacoes.csv", avaliacoesCsv,
					"6_notas.csv", notasCsv));

		} catch (Exception e) {
			log.error("Error generating school data", e);
			throw new InternalException("System Internal Error");
		}
	}

    private <T> List<T> generateInBatches(int quantity, Function<Integer, T> generator, String entityName, BatchCallback<T> callback) {
        if (quantity <= 0) return Collections.emptyList();
        
        log.info("Generating {} {} records in batches...", quantity, entityName);
        List<T> fullList = new ArrayList<>(quantity);
        final int batchSize = schoolUtils.getBatchSize();
        int totalBatches = (int) Math.ceil((double) quantity / batchSize);

        for (int i = 0; i < totalBatches; i++) {
            int currentBatchSize = Math.min(batchSize, quantity - (i * batchSize));
            List<T> batch = new ArrayList<>(currentBatchSize);
            for (int j = 0; j < currentBatchSize; j++) {
                batch.add(generator.apply(j));
            }

            if (callback != null) {
                callback.onBatchComplete(batch, i + 1, totalBatches);
            }

            fullList.addAll(batch);
        }
        return fullList;
    }

    private List<Aluno> generateAlunos(int quantity, BatchCallback<Aluno> callback) {
        return generateInBatches(quantity, i -> schoolUtils.generateSingleAluno(), "Alunos", callback);
    }
    
    private List<Disciplina> generateDisciplinas(int quantity, BatchCallback<Disciplina> callback) {
        return generateInBatches(quantity, i -> schoolUtils.generateSingleDisciplina(), "Disciplinas", callback);
    }
    
    private List<Turma> generateTurmas(List<Disciplina> disciplinas) {
        if (disciplinas.isEmpty()) return Collections.emptyList();
        List<Turma> turmas = new ArrayList<>();
        for (Disciplina disciplina : disciplinas) {
            int numTurmas = schoolUtils.getRandomNumber(1, 4);
            for (int i = 0; i < numTurmas; i++) {
                turmas.add(schoolUtils.generateSingleTurma(disciplina));
            }
        }
        log.info("Generated {} Turmas from {} Disciplinas", turmas.size(), disciplinas.size());
        return turmas;
    }
    
    private List<Matricula> generateMatriculas(List<Aluno> alunos, List<Turma> turmas) {
        if (alunos.isEmpty() || turmas.isEmpty()) return Collections.emptyList();
        List<Matricula> matriculas = new ArrayList<>();
        Set<String> uniqueEnrollments = new HashSet<>();
        for (Aluno aluno : alunos) {
            int numMatriculas = schoolUtils.getRandomNumber(3, 7);
            Collections.shuffle(turmas);
            for (int i = 0; i < numMatriculas && i < turmas.size(); i++) {
                Turma turma = turmas.get(i);
                if (uniqueEnrollments.add(aluno.getId() + "-" + turma.getId())) {
                    matriculas.add(schoolUtils.generateSingleMatricula(aluno, turma));
                }
            }
        }
        log.info("Generated {} Matriculas", matriculas.size());
        return matriculas;
    }

    private List<Avaliacao> generateAvaliacoes(List<Turma> turmas) {
        if (turmas.isEmpty()) return Collections.emptyList();
        List<Avaliacao> avaliacoes = new ArrayList<>();
        for (Turma turma : turmas) {
            int numAvaliacoes = schoolUtils.getRandomNumber(2, 5);
            for(int i = 0; i < numAvaliacoes; i++){
                avaliacoes.add(schoolUtils.generateSingleAvaliacao(turma));
            }
        }
        log.info("Generated {} Avaliacoes", avaliacoes.size());
        return avaliacoes;
    }
    
    private List<Nota> generateNotas(List<Matricula> matriculas, List<Avaliacao> avaliacoes) {
        if (matriculas.isEmpty() || avaliacoes.isEmpty()) return Collections.emptyList();
        
        List<Nota> notas = new ArrayList<>();
        Map<Long, List<Avaliacao>> avaliacoesPorTurma = avaliacoes.stream()
                .collect(Collectors.groupingBy(Avaliacao::getId_turma));

        for (Matricula matricula : matriculas) {
            List<Avaliacao> avaliacoesDaTurma = avaliacoesPorTurma.get(matricula.getId_turma());
            if (avaliacoesDaTurma != null) {
                for (Avaliacao avaliacao : avaliacoesDaTurma) {
                    Nota nota = schoolUtils.generateSingleNotas(avaliacao);
                    nota.setId(matricula.getId()); 
                    notas.add(nota);
                }
            }
        }
        log.info("Generated {} Notas", notas.size());
        return notas;
    }
    
	public byte[] retrieveFromTempStorage(String fileId) {
		log.info("Attempting to download data for fileId: {}", fileId);
		byte[] csvData = csvStorage.get(fileId);
		if (csvData != null) {
			log.info("Successful data retrieval for fileId: {}", fileId);
		} else {
			log.warn("Data not found or expired for fileId: {}", fileId);
		}
		return csvData;
	}

	public byte[] deleteCsvFromTempStorage(String fileId) {
		log.info("Deleting Data from Storage");
		byte[] removedData = csvStorage.remove(fileId);
		if (removedData != null) {
			log.info("Successful data deletion from Storage");
		} else {
			log.warn("No data found for deletion with fileId: {}", fileId);
		}
		return removedData;
	}

	private void clearPreviousData() {
		if (!csvStorage.isEmpty()) {
			csvStorage.clear();
		}
	}

}
