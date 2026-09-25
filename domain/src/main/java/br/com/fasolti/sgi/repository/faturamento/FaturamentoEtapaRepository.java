package br.com.fasolti.sgi.repository.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FaturamentoEtapaRepository extends QueryDslCrudRepository<FaturamentoEtapa> {

	FaturamentoEtapa findByCodigo(Long codigo);

	List<FaturamentoEtapa> findByFaturamentoCodigoOrderByCodigoDesc(Long codigoFaturamento);

	FaturamentoEtapa findByNotaSaidaCodigo(Long codigoNota);

	List<FaturamentoEtapa> findByFaturamentoCodigo(Long codigo);

	@Query("Select fe.valor from FaturamentoEtapa fe where fe.codigo = :codigo")
	BigDecimal findValorEtapa(@Param("codigo") Long codigo);

	@Query("Select max(fe.numero) from FaturamentoEtapa fe where fe.faturamento.codigo = :codigo ")
	Long findUltimoNumeroEtapaByFatCod(@Param("codigo") Long codigo);

	@Modifying
	@Transactional()
	@Query("update FaturamentoEtapa fe set fe.notaDebito = null, fe.situacao = ?2 where fe.notaDebito.codigo = ?1")
	void removerRelacionamentoND(Long idNotaDebito, FaturamentoEtapa.Status situacao);
}
