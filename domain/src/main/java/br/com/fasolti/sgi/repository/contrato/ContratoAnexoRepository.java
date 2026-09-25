package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoAnexo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoAnexoRepository extends QueryDslCrudRepository<ContratoAnexo> {

	List<ContratoAnexo> findByContratoCodigo(Long codigo);

	List<ContratoAnexo> findByAnexoCriador(String nome);

	@Query("SELECT COUNT(ca) FROM ContratoAnexo ca WHERE ca.aditivo.codigo = ?1")
	Long findQtdByAditivo(Long codAditivo);
}
