package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.administracao.domain.CategoriaContrato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaContratoRepository extends QueryDslCrudRepository<CategoriaContrato> {
	List<CategoriaContrato> findCategoriaContratoByDescricaoAndTipo(String descricao, CategoriaContrato.TipoCategoriaContrato tipo);
}
