package school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

	private Long id;
	private Long id_aluno;
	private Long id_turma;

}
