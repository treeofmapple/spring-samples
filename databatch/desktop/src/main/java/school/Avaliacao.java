package school;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

	private Long id;
	private Long id_turma;
	private Short unidade;
	private Short ordem;
	private BigDecimal peso;
	private String descricao;

}
