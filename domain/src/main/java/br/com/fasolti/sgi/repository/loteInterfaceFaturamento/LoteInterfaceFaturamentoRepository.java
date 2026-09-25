package br.com.fasolti.sgi.repository.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface LoteInterfaceFaturamentoRepository extends QueryDslCrudRepository<LoteInterface> {

	@Query("select count(distinct notaSaida) from Faturamento faturamento " +
		" JOIN faturamento.faturamentoEtapas faturamentoEtapas" +
		" JOIN faturamentoEtapas.notaSaida notaSaida " +
		" where notaSaida.empresa = :empresa " +
		" and notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" and notaSaida.dataEmissao >= :dataInicio and notaSaida.dataEmissao <= :dataFim " +
		" and ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida )")
	Long countLoteFATN(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					   @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					   @Param(value = "statusRecebida") Nota.StatusNota statusRecebida);

	@Query("SELECT COUNT (DISTINCT notaSaida)" +
		" FROM NotaSaida notaSaida, ContaContabil contaContabil, PessoaContaContabil pcc, NotaSaidaParcela nsp " +
		" WHERE notaSaida.codigo = nsp.notaSaida.codigo " +
		" AND pcc.contaContabil.codigo = contaContabil.codigo " +
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND pcc.tipoEspecial = 8 " +
		" AND contaContabil.pai.tipoEspecial = 10 " +
		" AND notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida )")
	Long countLoteDSFP(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					   @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					   @Param(value = "statusRecebida") Nota.StatusNota statusRecebida);

	@Query("SELECT COUNT(DISTINCT notaSaida) " +
		" FROM Faturamento faturamento " +
		" JOIN faturamento.faturamentoAliquotas faturamentoAliquotas " +
		" JOIN faturamentoAliquotas.aliquota aliquota " +
		" JOIN aliquota.categoriaTributo categoriaTributo " +
		" JOIN categoriaTributo.tributo tributo " +
		" JOIN faturamento.faturamentoEtapas faturamentoEtapas " +
		" JOIN faturamentoEtapas.notaSaida notaSaida " +
		" where notaSaida.empresa = :empresa and notaSaida.tipoNotaFiscal = :tipoNotaFiscal and tributo.codigo = 6 " +
		" and notaSaida.dataEmissao >= :dataInicio and notaSaida.dataEmissao <= :dataFim " +
		" and ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada )")
	Long countLoteINSS(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					   @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					   @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
					   @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);


	@Query("select COUNT(DISTINCT notaSaida) " +
		" from FaturamentoEtapa faturamentoEtapa " +
		"   join faturamentoEtapa.notaSaida notaSaida" +
		"   join notaSaida.empresa empresa" +
		"   join empresa.endereco endereco" +
		"   join endereco.cidade cidade" +
		"  where notaSaida.dataEmissao >= :dataInicio and notaSaida.dataEmissao <= :dataFim" +
		"    and notaSaida.status = :statusNota" +
		"    and notaSaida.tipoNotaFiscal = :tipoNotaFiscal" +
		"    and cidade.tipoLoteInterface = :tipoLoteInterface")
	Long countLoteNotaCarioca(@Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
							  @Param(value = "dataInicio") Date dataInicio,
							  @Param(value = "dataFim") Date dataFim,
							  @Param(value = "statusNota") Nota.StatusNota statusNota,
							  @Param(value = "tipoLoteInterface") LoteInterface.TipoLoteInterface tipoLoteInterface);

	@Query("select DISTINCT notaSaida " +
		" from FaturamentoEtapa faturamentoEtapa " +
		"   join faturamentoEtapa.notaSaida notaSaida" +
		"   join notaSaida.empresa empresa" +
		"  where notaSaida.dataEmissao = :dataInicio" +
		"    and notaSaida.status = :statusNota" +
		"    and notaSaida.tipoNotaFiscal = :tipoNotaFiscal" +
		"    and empresa.codigo = :codigoEmpresa" +
		"    and faturamentoEtapa.faturamento.tipoFornecimento = :tipoFornecimento" +
		"  order by notaSaida.numero")
	List<NotaSaida> selectJobNotaFiscalMaterial(
		@Param(value = "dataInicio") Date dataInicio,
		@Param(value = "statusNota") Nota.StatusNota statusNota,
		@Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
		@Param(value = "codigoEmpresa") Long codigoEmpresa,
		@Param(value = "tipoFornecimento") Faturamento.TipoFornecimentoNF tipoFornecimento
		);


	@Query("select COUNT(DISTINCT notaSaida) " +
		" from FaturamentoEtapa faturamentoEtapa " +
		"   join faturamentoEtapa.notaSaida notaSaida" +
		"   join notaSaida.empresa empresa" +
		"  where notaSaida.dataEmissao = :dataInicio" +
		"    and notaSaida.status = :statusNota" +
		"    and notaSaida.tipoNotaFiscal = :tipoNotaFiscal" +
		"    and empresa.codigo = :codigoEmpresa" +
		"    and faturamentoEtapa.faturamento.tipoFornecimento = :tipoFornecimento")
	Long countLoteNFMaterial(@Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
							 @Param(value = "dataInicio") Date dataInicio,
							 @Param(value = "statusNota") Nota.StatusNota statusNota,
							 @Param(value = "codigoEmpresa") Long codigoEmpresa,
							 @Param(value = "tipoFornecimento") Faturamento.TipoFornecimentoNF tipoFornecimento);

	@Query("select DISTINCT notaSaida " +
		" from FaturamentoEtapa faturamentoEtapa " +
		"   join faturamentoEtapa.notaSaida notaSaida" +
		"   join notaSaida.empresa empresa" +
		"   join empresa.endereco endereco" +
		"   join endereco.cidade cidade" +
		"  where notaSaida.dataEmissao >= :dataInicio and notaSaida.dataEmissao <= :dataFim" +
		"    and notaSaida.status = :statusNota" +
		"    and notaSaida.tipoNotaFiscal = :tipoNotaFiscal" +
		"    and cidade.codigo = :codigoCidadeNota")
	List<NotaSaida> loteNotaCarioca(@Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
									@Param(value = "dataInicio") Date dataInicio,
									@Param(value = "dataFim") Date dataFim,
									@Param(value = "statusNota") Nota.StatusNota statusNota,
									@Param(value = "codigoCidadeNota") Long codigoCidadeNota);


	@Query(
		" SELECT COUNT(DISTINCT parcela)" +
			" FROM NotaSaidaParcela parcela" +
			" JOIN parcela.notaSaida notaSaida" +
			" JOIN parcela.contaCorrentePrevista contaCorrentePrevista" +
			" JOIN contaCorrentePrevista.banco banco" +
			" JOIN notaSaida.empresa empresa" +
			" WHERE banco.codigo = :banco" +
			" AND notaSaida.dataEmissao >= :dataInicio" +
			" AND notaSaida.dataEmissao <= :dataFim" +
			" AND parcela.situacao = :situacaoEmRecebimento" +
			" AND parcela.tipoDocumentoRecebimento = :tipoDocumentoPagamento" +
			" AND parcela.codigoBarrasBoleto is null" +
			" AND notaSaida.empresa.codigo = :codigoEmpresa")
	Long countLoteBoletoItau(@Param(value = "banco") Long banco,
							 @Param(value = "situacaoEmRecebimento") NotaSaidaParcela.SituacaoParcela situacaoEmRecebimento,
							 @Param(value = "codigoEmpresa") Long codigoEmpresa,
							 @Param(value = "dataInicio") Date dataInicio,
							 @Param(value = "dataFim") Date dataFim,
							 @Param(value = "tipoDocumentoPagamento") Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento);

	@Query("SELECT COUNT(DISTINCT notaSaida) " +
		" FROM NotaSaida notaSaida " +
		" WHERE notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND notaSaida.dataEmissao >= :dataInicio " +
		" AND notaSaida.dataEmissao <= :dataFim " +
		" AND ( notaSaida.status = :statusEmProcessamento OR notaSaida.status = :statusEmRecebimento OR notaSaida.status = :statusRecebida OR notaSaida.status = :statusCancelada )")
	Long countLoteSSNI(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					   @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					   @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
					   @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);

	@Query("SELECT COUNT(DISTINCT notaSaida) " +
		" FROM NotaSaida notaSaida " +
		" WHERE notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND notaSaida.dataEmissao >= :dataInicio " +
		" AND notaSaida.dataEmissao <= :dataFim " +
		" AND ( notaSaida.status = :statusEmProcessamento OR notaSaida.status = :statusEmRecebimento OR notaSaida.status = :statusRecebida OR notaSaida.status = :statusCancelada )")
	Long countLoteIRRF(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					   @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					   @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
					   @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);


	@Query(" SELECT COUNT (DISTINCT notaSaida) " +
		" FROM NotaSaida notaSaida, NotaTributoAliquota nta, AliquotaAgregada aa " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.codigo = aa.aliquota.codigo " +
		" AND aa.codigo = 2 " +  //código COFINS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) OR (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +

		" AND notaSaida.codigo NOT IN " +

		" ( " +
		" SELECT notaSaida.codigo FROM NotaSaida notaSaida, NotaTributoAliquota nta, AliquotaAgregada aa " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.codigo = aa.aliquota.codigo " +
		" AND aa.codigo = 2 " +  //código COFINS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) AND (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +
		" ) ")
	Long countLoteCOFINS(@Param(value = "empresa") Unidade empresa,
						 @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
						 @Param(value = "dataInicio") Date dataInicio,
						 @Param(value = "dataFim") Date dataFim,
						 @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
						 @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
						 @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
						 @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);

	@Query(" SELECT COUNT(DISTINCT notaSaida) " +
		" FROM NotaSaida notaSaida, NotaTributoAliquota nta " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.categoriaTributo.tributo.codigo = 4 " + //código ISS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) OR (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +

		" AND notaSaida.codigo NOT IN " +

		" ( " +
		" SELECT notaSaida.codigo FROM NotaSaida notaSaida, NotaTributoAliquota nta " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.categoriaTributo.tributo.codigo = 4 " + //código ISS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) AND (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +
		" ) ")
	Long countLoteISS(@Param(value = "empresa") Unidade empresa,
					  @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					  @Param(value = "dataInicio") Date dataInicio,
					  @Param(value = "dataFim") Date dataFim,
					  @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					  @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					  @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
					  @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);

	@Query("SELECT COUNT(DISTINCT notaSaida)" +
		" FROM NotaSaida notaSaida, NotaTributoAliquota nta, AliquotaAgregada aa " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.codigo = aa.aliquota.codigo " +
		" AND aa.codigo = 1 " +  //código PIS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) OR (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +

		" AND notaSaida.codigo NOT IN " +

		" ( " +
		" SELECT notaSaida.codigo FROM NotaSaida notaSaida, NotaTributoAliquota nta, AliquotaAgregada aa " +
		" WHERE notaSaida.codigo = nta.nota.codigo " +
		" AND nta.aliquota.codigo = aa.aliquota.codigo " +
		" AND aa.codigo = 1 " +  //código PIS
		" AND notaSaida.empresa = :empresa " +
		" AND notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" AND ( notaSaida.status = :statusEmProcessamento or notaSaida.status = :statusEmRecebimento or notaSaida.status = :statusRecebida or notaSaida.status = :statusCancelada ) " +
		" AND ( (notaSaida.dataEmissao >= :dataInicio AND notaSaida.dataEmissao <= :dataFim) AND (notaSaida.dataCancelamento >= :dataInicio AND notaSaida.dataCancelamento <= :dataFim) ) " +
		" ) ")
	Long countLotePIS(@Param(value = "empresa") Unidade empresa,
					  @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					  @Param(value = "dataInicio") Date dataInicio,
					  @Param(value = "dataFim") Date dataFim,
					  @Param(value = "statusEmProcessamento") Nota.StatusNota statusEmProcessamento,
					  @Param(value = "statusEmRecebimento") Nota.StatusNota statusEmRecebimento,
					  @Param(value = "statusRecebida") Nota.StatusNota statusRecebida,
					  @Param(value = "statusCancelada") Nota.StatusNota statusCancelada);

	@Query(" SELECT COUNT(distinct notaSaida)"
		+ "   FROM NotaSaida notaSaida,"
		+ "   NotaTributoAliquota nta,"
		+ "   AliquotaAgregada aa"
		+ "   WHERE notaSaida.codigo = nta.nota.codigo "
		+ "   AND nta.aliquota.codigo = aa.aliquota.codigo "
		+ "   AND notaSaida.empresa = :empresa "
		+ "   AND aa.codigo = :codigoAliquotaCsll "
		+ "   AND (("
		+ "	  	notaSaida.status in (:statusNaoCancelado)"
		+ "     	AND notaSaida.dataEmissao >= :dataInicio "
		+ "     	AND notaSaida.dataEmissao <= :dataFim)"
		+ "     OR ("
		+ "         notaSaida.status = :statusCancelado"
		+ "         AND notaSaida.dataCancelamento >= :dataCancelamentoInicio"
		+ "         AND notaSaida.dataCancelamento <= :dataCancelamentoFim"
		+ "         AND notaSaida.dataEmissao < :dataCancelamentoEmissaoFim"
		+ "        ))")
	Long countLoteCSLL(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "codigoAliquotaCsll") Long codigoAliquotaCsll,
					   @Param(value = "statusNaoCancelado") List<Nota.StatusNota> statusNaoCancelado,
					   @Param(value = "dataInicio") Date dataInicio,
					   @Param(value = "dataFim") Date dataFim,
					   @Param(value = "statusCancelado") Nota.StatusNota statusCancelado,
					   @Param(value = "dataCancelamentoInicio") Date dataCancelamentoInicio,
					   @Param(value = "dataCancelamentoFim") Date dataCancelamentoFim,
					   @Param(value = "dataCancelamentoEmissaoFim") Date dataCancelamentoEmissaoFim);

//	@Query(value = "select count(distinct notaSaida)" +
//		"   from FaturamentoEtapa faturamentoEtapa" +
//		"   right join faturamentoEtapa.notaSaida notaSaida" +
//		"   join faturamentoEtapa.faturamento faturamento" +
//		"   join faturamento.cliente cliente " +
//		"	where notaSaida.dataEmissao >= :dataInicio and notaSaida.dataEmissao <= :dataFim " +
//		"		and notaSaida.status = :statusNota " +
//		"		and cliente.documento IN (:listaCnpjs)")

	@Query(value="SELECT " +
		"count(DISTINCT ns.CODIGO)  " +
		"FROM " +
		"faturamento_etapa fe JOIN nota_saida ns ON fe.codigo_nota_saida = ns.codigo " +
		"JOIN nota_fiscal nf ON nf.codigo = ns.codigo " +
		"JOIN nota ON nota.codigo = ns.codigo " +
		"JOIN faturamento f ON fe.codigo_faturamento = f.codigo " +
		"JOIN pessoa p ON f.codigo_cliente = p.codigo " +
		"WHERE " +
		"nf.data_emissao >= :dataInicio " +
		"AND nf.data_emissao <= :dataFim " +
		"AND nota.status = :statusNota " +
		"AND p.documento SIMILAR TO  :strCnpjsPortalOi "
	, nativeQuery = true)
	Long countLotePortalOi(@Param(value = "dataInicio") Date dataInicio,
						   @Param(value = "dataFim") Date dataFim,
						   @Param(value = "statusNota") Integer statusNota,
						   @Param(value = "strCnpjsPortalOi") String strCnpjsPortalOi
						   );


	@Query("select count(distinct notaSaida) from Faturamento faturamento " +
		" JOIN faturamento.faturamentoEtapas faturamentoEtapas " +
		" JOIN faturamentoEtapas.notaSaida notaSaida " +
		" where notaSaida.empresa = :empresa and notaSaida.dataCancelamento IS NOT NULL and notaSaida.tipoNotaFiscal = :tipoNotaFiscal " +
		" and notaSaida.dataCancelamento >= :dataInicioCancelamento and notaSaida.dataCancelamento <= :dataFimCancelamento " +
		" and notaSaida.dataEmissao <= :dataFim ")
	Long countLoteFATC(@Param(value = "empresa") Unidade empresa,
					   @Param(value = "tipoNotaFiscal") NotaFiscal.TipoNotaFiscal tipoNotaFiscal,
					   @Param(value = "dataInicioCancelamento") Date dataInicioCancelamento,
					   @Param(value = "dataFimCancelamento") Date dataFimCancelamento,
					   @Param(value = "dataFim") Date dataFim);

	@Query("select lote from LoteInterface lote where lote.dataEvento >= :dataInicio and lote.dataEvento <= :dataFim")
	List<LoteInterface> findLoteByPeriod(@Param(value = "dataInicio") Date dataInicio, @Param(value = "dataFim") Date dataFim);

	@Query("select lote from LoteInterface lote where lote.dataEvento >= :dataInicio and lote.dataEvento <= :dataFim and lote.tipoLoteInterface = :tipo")
	List<LoteInterface> findLoteByPeriodAndTipo(@Param(value = "dataInicio") Date dataInicio, @Param(value = "dataFim") Date dataFim, @Param(value = "tipo") LoteInterface.TipoLoteInterface tipo);

	/**
	 * Busta uma lista de código de notas de saida que são do Lote PortalOi.
	 *
	 * @param dataInicio
	 * @param dataFim
	 * @param statusNota
	 * @return
	 */
	@Query(value="SELECT " +
		"DISTINCT ns.codigo " +
		"FROM " +
		"faturamento_etapa fe JOIN nota_saida ns ON fe.codigo_nota_saida = ns.codigo " +
		"JOIN nota_fiscal nf ON nf.codigo = ns.codigo " +
		"JOIN nota ON nota.codigo = ns.codigo " +
		"JOIN faturamento f ON fe.codigo_faturamento = f.codigo " +
		"JOIN pessoa p ON f.codigo_cliente = p.codigo " +
		"WHERE " +
		"nf.data_emissao >= :dataInicio " +
		"AND nf.data_emissao <= :dataFim " +
		"AND nota.status = :statusNota " +
		"AND p.documento SIMILAR TO  :strCnpjsPortalOi "
		, nativeQuery = true)
	List<Long> listaCodNotaSaidaLotePortalOi(@Param(value = "dataInicio") Date dataInicio,
											 @Param(value = "dataFim") Date dataFim,
											 @Param(value = "statusNota") Integer statusNota,
											 @Param(value = "strCnpjsPortalOi") String strCnpjsPortalOi
	);

}
