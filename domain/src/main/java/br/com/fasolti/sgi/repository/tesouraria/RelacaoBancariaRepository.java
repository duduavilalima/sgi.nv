package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelacaoBancariaRepository extends QueryDslCrudRepository<RelacaoBancaria> {

	RelacaoBancaria findByCodigo(@Param("codigo") Long codigo);

	List<RelacaoBancaria> findByContaCorrenteCodigo(Long codigo);

//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.numero = ?1 where rb.codigo = ?2")
//	void updateNumero(String numero, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.atualizador = ?1 where rb.codigo = ?2")
//	void updateAtualizador(String atualizador, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.dataAtualizacao = ?1 where rb.codigo = ?2")
//	void updateDataAtualizacao(Date dataAtualizacao, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.dataEnvio = ?1 where rb.codigo = ?2")
//	void updateDataEnvio(Date dataEnvio, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.colaboradorEnvio = ?1 where rb.codigo = ?2")
//	void updateColaboradorEnvio(Colaborador colaboradorEnvio, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.status = ?1 where rb.codigo = ?2")
//	void updateStatus(RelacaoBancaria.StatusRelacaoBancaria status, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.impressaoCheque = ?1 where rb.codigo = ?2")
//	void updateImpressaoCheque(Boolean aTrue, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.chequeAnterior = ?1 where rb.codigo = ?2")
//	void updateChequeAnterio(Long chequeAnterior, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.impressaoEndosso = ?1 where rb.codigo = ?2")
//	void updateImpressaoEndosso(Boolean aTrue, Long codigo);
//
//	@Modifying
//	@Transactional()
//	@Query("update RelacaoBancaria rb set rb.impressao2via = ?1 where rb.codigo = ?2")
//	void updateImpressao2via(Boolean aTrue, Long codigo);

//	@Transactional()
//	@Query("select relacaoBancaria.chequeAnterior from RelacaoBancaria relacaoBancaria where relacaoBancaria.codigo = :codigoRelacaobancaria")
//	Long obterChequeAnterior(@Param("codigoRelacaobancaria") long codigoRelacaobancaria);
}
