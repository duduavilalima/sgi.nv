package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteSimpleTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RetornoBancarioTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RelacaoBancariaService extends CrudService<RelacaoBancariaTO> {

	Long obterChequeAnteriorDeContaCorrente(long codigoContaCorrente);

	Long obterChequeAnteriorDeUmaRelacaoBancaria(long codigoRelacaobancaria);

	String obterNomeTributo(List<PagamentoTO> pagamentos);

	String obterNomeTributo(PagamentoTO pagamento);

	BancoTO obterBancoCredorSelecionado(ItemRelacaoTO itemRelacao, List<PagamentoTO> pagamentos);

	RelacaoBancariaTO enviarRB(Long relacaoBancariaCod) throws Exception;

	boolean dadosBancariosHomologados(ItemRelacaoTO itemRelacao, List<PagamentoTO> pagamentos, RelacaoBancariaTO relacaoBancaria, BancoTO bancoTO);

	RelacaoBancariaTO cancelarRelacaoBancaria(Long id) throws Exception;

	RelacaoBancariaTO enviarRelacaoBancaria(Long id) throws Exception;

	void removerPagamentoRB(Long id) throws Exception;

	RelacaoBancariaPagamentosVO editarPagamentoRB(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) throws Exception;

	void realizarBaixaBancaria(RelacaoBancariaTO relacaoBancariaTO) throws Exception;

	RelacaoBancariaTO realizarBaixaBancariaManual(RelacaoBancariaTO to) throws Exception;

	RelacaoBancariaTO estornarBaixaBancariaManual(RelacaoBancariaTO to) throws Exception;

	RelacaoBancariaTO adicionarPagamentos(RelacaoBancariaTO rbTO);

	RelacaoBancariaTO pesquisarPagamentos(RelacaoBancariaTO rbTO);

	List<RetornoBancarioTO> obterRetornosBancarios() throws Exception;

	ContaCorrenteSimpleTO obterUltimoChequeRB(Long id) throws Exception;

	void imprimirCheques(RelacaoBancariaTO to, String tipo) throws Exception;

	List<Object> obterItensRelacao(Long id) throws Exception;

	List<RelacaoBancariaPagamentosVO> obterItensDaRelacaoBancaria(Long id) throws Exception;

	void baixarComprovante(String id, HttpServletRequest request, HttpServletResponse response) throws Exception;

	void baixarComprovanteExiste(String id) throws Exception;

	RelacaoBancariaPagamentosVO criarRelacaoBancariaPagamentosVO(RelacaoBancariaPagamentosVO relacaoBancariaPagamentosVO) throws ValidationException;

	long updateAtualizadorAndDataAtualizacao(long codigo);

	long updateDataEnvio(long codigo);

	long updateColaboradorEnvio(long codigo);

	long updateNumero(long codigo);

	long updateStatusABERTA(long codigo);

	long updateStatusEMPROCESSAMENTO(long codigo);

	long updateStatusAGUARDANDORETORNO(long codigo);

	long updateStatusPROCESSANDORETORNO(long codigo);

	long updateStatusFINALIZADA(long codigo);

	long updateStatusCANCELADA(long codigo);

	long updateStatusAPROCESSAR(long codigo);

	long updateImpressaoCheque(long codigo, boolean value);

	long updateImpressaoEndosso(long codigo, boolean value);

	long updateImpressao2via(long codigo, boolean value);

	long updateAnterior(long codigo, long value);
}
