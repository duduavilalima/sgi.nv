package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaBaixaRetencao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaSaidaParcelaBaixaRetencaoRepository extends QueryDslCrudRepository<NotaSaidaParcelaBaixaRetencao>, JpaRepository<NotaSaidaParcelaBaixaRetencao, Long> {

}
