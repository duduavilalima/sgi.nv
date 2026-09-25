package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.enums.TipoInformacaoNumeroBoleto;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.BoletoService;
import br.com.araujoabreu.sgi.tesouraria.lang.CodigoDeBarrasException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelValidateException;
import br.com.araujoabreu.sgi.tesouraria.util.ConcessionariaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.GuiaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.BoletoUtil;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.sgi.repository.banco.BancoRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class BoletoServiceImpl implements BoletoService {

	private static final String FORMAT_LINHA_BOLETO = "_______________________________________________";
	private static final String VAZIO = "";
	@Autowired
	protected Mapper mapper;
	@Autowired
	private BancoRepository bancoRepository;

	@Override
	public Object validarLinhaDigitavel(@PathVariable String tipoDocumentoPagamento, @PathVariable String numero) throws ValidationException {
		Object validacao = null;
		try {
			numero = numero.replace(".", "");
			numero = numero.replace("-", "");
			numero = numero.replace("/", "");
			validacao = validar(numero, tipoDocumentoPagamento, TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL);
		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
		return validacao;
	}

	@Override
	public Object validarBoleto(@PathVariable String tipoDocumentoPagamento, @PathVariable String numero) {
		Object validacao = null;
		try {
			numero = numero.replace(".", "");
			numero = numero.replace("-", "");
			numero = numero.replace("/", "");
			validacao = validar(numero, tipoDocumentoPagamento, TipoInformacaoNumeroBoleto.CODIGO_BARRAS);
		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
		return validacao;
	}

	public boolean validar(String numero, String tipoDocumentoPagamento, TipoInformacaoNumeroBoleto infBoleto) throws LinhaDigitavelException, LinhaDigitavelValidateException, CodigoDeBarrasException, ValidationException {
		Pagamento pagamento = new Pagamento();
		for (Pagamento.TipoDocumentoPagamento tipo : Pagamento.TipoDocumentoPagamento.values()) {
			if (tipo.toString().equalsIgnoreCase(tipoDocumentoPagamento)) {
				pagamento.setTipoDocumentoPagamento(tipo);
			}
		}
		return validarLinhaAndCodigoBarrasInterno(numero, infBoleto, mapper.map(pagamento, PagamentoTO.class));
	}

	public boolean validarLinhaAndCodigoBarrasInterno(String linhaCodigo, TipoInformacaoNumeroBoleto tipo, PagamentoTO pagamento) throws NullPointerException, IllegalArgumentException, ValidationException {
		if (pagamento == null) {
			pagamento = new PagamentoTO();
		}

		if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.equals(tipo)) {

			if (linhaCodigo != null && !linhaCodigo.equals(FORMAT_LINHA_BOLETO) && !linhaCodigo.equals(VAZIO)) {

				if (Pagamento.TipoDocumentoPagamento.BOLETO.toString().equals(pagamento.getTipoDocumentoPagamento())) {
					// Verificar se a linha digitável não é nula, não é vazia e é numérica.
					try {
						BoletoUtil.checkFormatoLinhaDigitavelNumerica(linhaCodigo.replace(".", ""));
						BoletoUtil.validarLinhaDigitavelNumerica(linhaCodigo);
					} catch (Exception e) {
						throw new ValidationException("ValidationException",e.getMessage());
					}
					return Boolean.TRUE;
				} else if (Pagamento.TipoDocumentoPagamento.GUIA.toString().equals(pagamento.getTipoDocumentoPagamento()) && linhaCodigo != null && !linhaCodigo.trim().equals(VAZIO)) {
					try {
						GuiaUtil.validarLinhaDigitavel(linhaCodigo, pagamento.getValorPagar());
					} catch (Exception e) {
						throw new ValidationException("ValidationException",e.getMessage());
					}
					return Boolean.TRUE;
				} else if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.toString().equals(pagamento.getTipoDocumentoPagamento())) {
					try {
						ConcessionariaUtil.validarLinhaDigitavel(linhaCodigo, pagamento.getValorPagar());
					} catch (Exception e) {
						throw new ValidationException("ValidationException",e.getMessage());
					}
					return Boolean.TRUE;
				}


			}
		} else if (linhaCodigo != null && !linhaCodigo.equals(FORMAT_LINHA_BOLETO)) {

			if (Pagamento.TipoDocumentoPagamento.BOLETO.toString().equals(pagamento.getTipoDocumentoPagamento())) {

				try {
					BoletoUtil.checkFormatoCodigoDeBarras(linhaCodigo);
					BoletoUtil.validarLinhaDigitavelNumerica(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(linhaCodigo));
				} catch (Exception e) {
					throw new ValidationException("ValidationException",e.getMessage());
				}
				return Boolean.TRUE;
			} else if (Pagamento.TipoDocumentoPagamento.GUIA.toString().equals(pagamento.getTipoDocumentoPagamento()) && linhaCodigo != null && !linhaCodigo.equals(VAZIO)) {

				try {
					GuiaUtil.validarCodigoBarras(linhaCodigo, pagamento.getValorPagar());
				} catch (Exception e) {
					e.printStackTrace();
					throw new ValidationException("ValidationException",e.getMessage());
				}
				return Boolean.TRUE;
			} else if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.toString().equals(pagamento.getTipoDocumentoPagamento())) {

				try {
					ConcessionariaUtil.validarCodigoBarras(linhaCodigo, pagamento.getValorPagar());
				} catch (Exception e) {
					e.printStackTrace();
					throw new ValidationException("ValidationException",e.getMessage());
				}
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public boolean validarLinhaAndCodigoBarras(String linhaCodigo, Enum tipo, PagamentoTO pagamento) throws Exception {
		return validarLinhaAndCodigoBarrasInterno(linhaCodigo, (TipoInformacaoNumeroBoleto) tipo, pagamento);
	}

	@Override
	public boolean validar(String numero, String tipoDocumentoPagamento, Enum infBoleto) {
		return validar(numero, tipoDocumentoPagamento, infBoleto);
	}

	@Override
	public boolean validarBoletoLinhaDigitavel(String linhaCodigo) throws Exception {
		if (linhaCodigo != null && !linhaCodigo.equals(FORMAT_LINHA_BOLETO) && !linhaCodigo.equals(VAZIO)) {
			// Verificar se a linha digitável não é nula, não é vazia e é numérica.
			BoletoUtil.checkFormatoLinhaDigitavelNumerica(linhaCodigo.replace(".", ""));
			try {
				BoletoUtil.validarLinhaDigitavelNumerica(linhaCodigo);
			} catch (Exception e) {
				throw new ValidationException("ValidationException","Linha digitável inválida: " + e.getMessage());
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public boolean validarBoletoCodigoBarras(String linhaCodigo) throws Exception {
		if (linhaCodigo != null && !linhaCodigo.equals(FORMAT_LINHA_BOLETO)) {
			BoletoUtil.checkFormatoCodigoDeBarras(linhaCodigo);
			try {
				BoletoUtil.validarLinhaDigitavelNumerica(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(linhaCodigo));
			} catch (Exception e) {
				throw new ValidationException("ValidationException","O código de barras é inválido, pois " + e.getMessage());
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public BancoTO getCodDoBancoDoCodigoDeBarras(String codBarras) throws Exception {
		Banco banco = bancoRepository.findById(new Long(BoletoUtil.getCodigoDoBancoDoCodigoDeBarras(codBarras))).orElse(null);

		return (mapper.map(banco, BancoTO.class));

	}

	@Override
	public boolean validarBancoDoCodigoDeBarras(String codBarras) throws Exception {
		if (getCodDoBancoDoCodigoDeBarras(codBarras) == null) {
			return false;
		} else {
			return true;
		}
	}

}
