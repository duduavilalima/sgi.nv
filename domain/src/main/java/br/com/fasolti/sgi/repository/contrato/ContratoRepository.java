package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ContratoRepository extends QueryDslCrudRepository<Contrato> {
	List<Contrato> findByClienteCodigo(Long codigoCliente);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.situacao = ?1 where c.codigo = ?2")
	void updateSituacao(Contrato.SituacaoContrato paralisado, Long contrato);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.dataAtualizacao = ?1 where c.codigo = ?2")
	void updateDataAtualizacao(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.atualizador = ?1 where c.codigo = ?2")
	void updateAtualizador(String login, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Faturamento f set f.resumo = ?1 where f.codigo = ?2")
	void updateResumoFaturamento(String resumo, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.dataAssinatura = ?1 where c.codigo = ?2")
	void updateDataAssinatura(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.dataInicioVigencia = ?1 where c.codigo = ?2")
	void updateDataInicioVigencia(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.dataFimVigencia = ?1 where c.codigo = ?2")
	void updateDataFimVigencia(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.indiceReajuste = ?1 where c.codigo = ?2")
	void updateIndiceReajuste(String indice, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.resumo = ?1 where c.codigo = ?2")
	void updateResumo(String resumo, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.periodoReajuste = ?1 where c.codigo = ?2")
	void updatePeriodoReajuste(Contrato.PeriodoReajuste periodoReajuste, Long codigo);

	@Modifying
	@Transactional()
	@Query("update Contrato c set c.prazoAtual = ?1 where c.codigo = ?2")
	void updatePrazoAtual(Integer prazoAtual, Long codigo);
}
