package br.com.fasolti.sgi.repository.controlefiscal;

import br.com.araujoabreu.sgi.controleFiscal.domain.ReinfEvento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReinfEventoRepository extends QueryDslCrudRepository<ReinfEvento> {

	List<ReinfEvento> findAllByReinfRegistroEventoCodigoAndReinfCodigo(Long codigoRegistroEvento, Long codigoReinf);

	List<ReinfEvento> findByPrestador(Long codigoPrestador);

	List<ReinfEvento> findByIdTransmissao(String idTransmissao);

	ReinfEvento findByIdTransmissaoAndReinfRegistroEventoCodigo(String idTransmissao, Long codigoRegistroEvento);

}
