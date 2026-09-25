package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.compras.domain.ItemRecebimento;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface ItemRecebimentoRepository extends BaseQueryDslRepository<ItemRecebimento, Long> {
	List<ItemRecebimento> findByRecebimentoCodigo(Long codigo);
}
