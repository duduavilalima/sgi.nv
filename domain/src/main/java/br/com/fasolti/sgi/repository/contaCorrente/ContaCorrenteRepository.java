package br.com.fasolti.sgi.repository.contaCorrente;

import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ContaCorrenteRepository extends QueryDslCrudRepository<ContaCorrente> {
	List<ContaCorrente> findBylistaRelacaoBancariaCodigo(Long codigo);

	List<ContaCorrente> findByPessoaCodigo(Long codigoUnidade);

	@Modifying
	@Transactional()
	@Query("update ContaCorrente cc set cc.chequeAnterior = ?1 where cc.codigo = ?2")
	void updateChequeAnterior(Long numeroCheque, Long codigo);
}
