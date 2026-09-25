package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface CentroFinanceiroRepository extends QueryDslCrudRepository<CentroFinanceiro> {

	@Query("Select centroFinanceiro FROM CentroFinanceiro centroFinanceiro " +
		"INNER JOIN centroFinanceiro.unidade unidade WHERE centroFinanceiro.situacao = 1 " +
		"AND centroFinanceiro.pai is null AND unidade.codigo = :codigo ORDER by centroFinanceiro.nome")
	List<CentroFinanceiro> findGerenciasBySituacaoCfAndUnidade(@Param("codigo") Long codigo);

	@Query("Select centroFinanceiro FROM CentroFinanceiro centroFinanceiro " +
		"WHERE centroFinanceiro.situacao = 1 " +
		"AND centroFinanceiro.pai is not null AND centroFinanceiro.pai.codigo = :codigo ORDER by centroFinanceiro.nome")
	List<CentroFinanceiro> findCentrosFinanceirosBySituacaoAndGerencia(@Param("codigo") Long codigo);

	@Query("Select centroFinanceiro FROM CentroFinanceiro centroFinanceiro " +
		"WHERE centroFinanceiro.unidade.codigo = :codigoUnidade ")
	List<CentroFinanceiro> findCentrosFinanceirosByUnidade(@Param("codigoUnidade") Long codigoUnidade);

	List<CentroFinanceiro> findBySituacao(CentroFinanceiro.SituacaoCentroFinanceiro ativo);

	List<CentroFinanceiro> findByContrato(Contrato contrato);

	List<CentroFinanceiro> findByPai(Long codigo);

	List<CentroFinanceiro> findByContratoCodigo(Long codigo);

	@Modifying
	@Transactional()
	@Query("update CentroFinanceiro c set c.situacao = ?1 where c.codigo = ?2")
	void updateSituacao(CentroFinanceiro.SituacaoCentroFinanceiro paralisado, Long codigo);

	@Modifying
	@Transactional()
	@Query("update CentroFinanceiro c set c.dataAtualizacao = ?1 where c.codigo = ?2")
	void updateDataAtualizacao(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update CentroFinanceiro c set c.atualizador = ?1 where c.codigo = ?2")
	void updateAtualizador(String login, Long codigo);


	@Query("SELECT cf "
		+ "   FROM CentroFinanceiro cf "
		+ "  WHERE cf.nome = 'PESSOAL AFASTADO'"
		+ "    AND cf.tipo = :tipo")
	CentroFinanceiro findOneCentroFinanceiroPessoaAfastadoByTipo(@Param("tipo") CentroFinanceiro.TipoCentroFinanceiro tipo);

	@Modifying
	@Transactional()
	@Query("update CentroFinanceiro c set c.dataEncerramento = ?1 where c.codigo = ?2")
	void updateDataEncerramento(Date date, Long codigo);

	List<CentroFinanceiro> findByContratoAndSituacao(Contrato contrato, CentroFinanceiro.SituacaoCentroFinanceiro ativo);
}
