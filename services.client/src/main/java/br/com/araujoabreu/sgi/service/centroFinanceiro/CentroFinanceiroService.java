package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSituacaoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.CentroFinanceiroFechoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.FiltroGraficoCLTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CentroFinanceiroService extends CrudService<CentroFinanceiroTO> {

	CentroFinanceiroTO getGerenciaLogada();

	List<CentroFinanceiroTO> getCFsByUnidade(CentroFinanceiroPesquisaTO exampleObj);

	List<GerenciaTO> buscarGerenciasPorUnidade(Long codigoUnidade);

	List<GerenciaTO> buscarGerenciasPesquisa();

	List<GerenciaTO> buscarGerenciasEdicao(CentroFinanceiroTO centroFinanceiroTO);

	List<GerenciaTO> buscarGerenciasPorDiretoria(DiretoriaSimpleTO diretoria);

	List<CentroFinanceiroTO> buscarGerenciasPorRegional(Long codigoRegional);

	List<CentroFinanceiroTO> buscarGerenciasPorContratos(String idGerencia);

	List<CentroFinanceiroTO> buscarCFs(CentroFinanceiroTO exampleObj);

	CentroFinanceiroTO loadViewById(String id) throws Exception;

	CentroFinanceiroTO buscarCentroFinanceiroById(String id);

	void inativarById(String id) throws Exception;

	void inativarCentroFinanceiroById(Long id);

	void reativarById(String id) throws Exception;

	void reativarCentroFinanceiroById(Long id);

	void paralisarById(String id) throws Exception;

	void paralisarCentroFinanceiroById(Long id);

	void encerrarById(String id) throws Exception;

	void encerrarCentroFinanceiroById(Long id);

	void encerrarFatById(String id) throws Exception;

	void encerrarFatCentroFinanceiroById(Long id);

	void cancelarById(String id) throws Exception;

	void cancelarCentroFinanceiroById(Long id);

	void removerById(String id) throws Exception;

	BigDecimal totalCentrosFinanceirosDeContrato(ContratoTO contratoTO);

	BigDecimal getValorTotalCentroFinanceiro(CentroFinanceiroTO centroFinanceiro, ContratoTO contrato);

	BigDecimal calcularValorMensalCentroFinanceiro(CentroFinanceiroTO centroFinanceiro);

	BigDecimal calcularValorMensalCentroFinanceiro(long codigoCf);

	BigDecimal calcularValorTotal(long codigoCf);

	BigDecimal getTotal(FechoTO ultimoFecho);

	BigDecimal getSubTotalMaoDeObra(FechoTO ultimoFecho);

	BigDecimal getTotalEncargosSociais(FechoTO ultimoFecho);

	Integer getPrazo(CentroFinanceiroTO centroFinanceiro, ContratoTO contratoTO);

	List<CentroFinanceiroTO> obterCentroDeLucroPorContrato(ContratoTO contrato) throws Exception;

	List<CentroFinanceiroTO> obterCentrosFinanceirosPeloCodigoDoContrato(Long codigo);

	List<CentroFinanceiroSituacaoTO> criarSituacoesParaCentroFinanceiro(CentroFinanceiroTO map);

	void removerCentroFinanceiroById(Long id);

	void removerAssociacoesByCodigoCentroFinanceiro(Long id);

	CentroFinanceiroTO pesquisarCentroFinanceiroTOPorCodigoComCodigoUnidade(Long codigoCentroFinanceiro);

	CentroFinanceiroTO findByCodigo(Long codigo);

	List<CentroFinanceiroTO> getCFsByUnidadeTelaRelatorio(CentroFinanceiroPesquisaTO exampleObj);

	JPAQuery getJpaQuery();

	List<CentroFinanceiroTO> buscarClPorCliente(String id);

	List<CentroFinanceiroTO> buscarTodasGerencias();

	CentroFinanceiroTO obterCentroFinanceiroSimpleTelaNotaFiscal(Long codigoCentroFinanceiro);

	void salvarAlterarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO);

	void criarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO);

	void atualizarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO);

	List<CentroFinanceiroTO> getCFsByUnidadeMatriz(Long codigoMatriz);

	List<CentroFinanceiroTO> getCFsByUnidadeGerencia(FiltroGraficoCLTO filtroGraficoCLTO);

	List<CentroFinanceiroTO> buscaCcClPorGerencia(CentroFinanceiroTO exampleObj);

	JPAQuery jPAQueryObterCentroFinanceiroSimple(Long codigoCentroFinanceiro);

	JPAQuery jPAQueryObterCentroFinanceiroComUnidade(Long codigoCentroFinanceiro);

	List<CentroFinanceiroTO> buscarFaturamentoCFs(CentroFinanceiroTO exampleObj);

	List<CentroFinanceiroTO> findAllGerencias();

	List<CentroFinanceiroTO> obterListaCentrosLucrosAtivos();

	List<CentroFinanceiroTO> buscarCFsSimples(CentroFinanceiroTO exampleObj);

	CentroFinanceiroTO getCentroLucroByIndetificadorFinal(Long codigoGerencia, String identificadorFinal);

	CentroFinanceiroTO getCentroLucroByIndetificadorFinal(String identificadorFinal);

	BigDecimal custoMensal(Long codigoCl);

	List<CentroFinanceiroTO> cLsComDespesasAcimaLimite(Date dataAnalise, Double limite, String meses);

	List<CentroFinanceiroTO> getCentroFinanceiroPerfilFaturamento();
}
