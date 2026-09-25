package br.com.araujoabreu.sgi.service.compras;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Date;
import java.util.List;

public interface NotaSaidaService extends CrudService<NotaSaidaTO> {
	NotaSaidaTO obterNotaSaidaApenasCodigoENumero(Long codigo);

	NotaSaidaTO obterNotaSaidaApenasCodigoNumeroStatusResponsavelNumeracao(Long codigo);

	JPAQuery<?> obterNotaSaisaBasica(Long codigo);

	List<NotaSaidaTO> obterNotasDeFaturamentoPorTipoPrefeitura(Date inicio, Date fim, String tipoLoteInterface);

	NotaSaidaTO getNotaSaidaByCodigo(Long codigo);

	List<NotaSaidaTO> getNotaSaidasByCodigoFaturamento(Long codigoFaturamento);

	JPAQuery jpaQueryNotaSaidasByCodigoFaturamento(Long codigoFaturamento);

	Long obterNumerosFaturamento(String numero, Long codigoUnidade, Integer ano);

	Long obterNumerosFaturamento(String numero, Long codigoUnidade);

	boolean isIssRetidoCliente(Long codigoNota);

	void updateCodigoAssociarNFSubstituta(Long codigo, Long codigoNotaAssociadaCancelamento);

	void updateReverterCancelamentoERemoverCodigoAssociarNFSubstituta(Long codigo);

	void updateInfoComplementarContribuinte(Long codigo, String infoComplementarContribuinte);

	void updateStatusAbertaFaturamento(Long codigo);

	void updateStatusEmRecebimentoFaturamento(Long codigo);

	String getProtocoloEntrega(Long codigoNota);

	Boolean temNotaAbertaDoFaturamento(Long codigoFaturamento);

	void cancelamento(Long codigo);

	List<NotaSaidaTO> obterNotasDeFaturamentoNFMaterial(Date inicio, Date fim, String tipoLoteInterface);

    List<NotaSaidaTO> obterNotasDeFaturamentoNotaNorteBelem(Date inicio, Date fim, String tipoLoteInterface);
}
