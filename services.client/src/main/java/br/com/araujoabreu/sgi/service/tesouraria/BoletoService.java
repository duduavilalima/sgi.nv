package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;

import javax.xml.bind.ValidationException;

public interface BoletoService {

	boolean validarLinhaAndCodigoBarras(String linhaCodigo, Enum tipo, PagamentoTO pagamento) throws Exception;

	boolean validar(String numero, String tipoDocumentoPagamento, Enum infBoleto);

	boolean validarBoletoLinhaDigitavel(String numero) throws Exception;

	boolean validarBoletoCodigoBarras(String numero) throws Exception;

	BancoTO getCodDoBancoDoCodigoDeBarras(String codBarras) throws Exception;

	boolean validarBancoDoCodigoDeBarras(String codBarras) throws Exception;

	Object validarLinhaDigitavel(String tipoDocumentoPagamento, String numero);

	Object validarBoleto(String tipoDocumentoPagamento, String numero);
}
