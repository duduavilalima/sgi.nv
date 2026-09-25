package br.com.fasolti.sgi.repository.controlefiscal;

import br.com.araujoabreu.sgi.controleFiscal.domain.ReinfRegistroEvento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;

public interface ReinfRegistroEventoRepository extends QueryDslCrudRepository<ReinfRegistroEvento> {

	ReinfRegistroEvento findByNome(String nome);

}
