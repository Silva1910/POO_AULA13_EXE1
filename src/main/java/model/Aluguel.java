package model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aluguel {

	
	private Aluno aluno;
	private Exemplar exemplar;
	private LocalDate dataRetirada;
	private LocalDate dataDevolucao;

}
