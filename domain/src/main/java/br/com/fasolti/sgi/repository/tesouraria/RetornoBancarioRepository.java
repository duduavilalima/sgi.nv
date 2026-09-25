package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.RetornoBancario;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetornoBancarioRepository extends BaseQueryDslRepository<RetornoBancario, Long> {

	List<RetornoBancario> findByStatusItemRelacaoAndUtilizado(ItemRelacao.StatusItemRelacao statusItemRelacao, Boolean utilizado);
}
