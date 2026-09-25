package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroPrevisaoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface CentroFinanceiroPrevisaoService extends CrudService<CentroFinanceiroPrevisaoTO> {

	List<CentroFinanceiroPrevisaoTO> buscarPrevisoes(CentroFinanceiroPrevisaoPesquisaTO centroFinanceiroPrevisaoPesquisaTO);

	CentroFinanceiroPrevisaoTO findByCodCentroFinanceiroAndCompetencia(Long codigoCentroFinanceiro, String Competencia);

	BigDecimal calcularOSInicial(Long codigoCentroFinanceiro);

	void updatePrevisoes(List<CentroFinanceiroPrevisaoTO> lista);

	void updateDataAtualizacao(Long codigoCentroFinanceiroPrevisao);

	void updateCompetencia(Long codigoCentroFinanceiroPrevisao, String competencia);

	void updateValorContratual(Long codigoCentroFinanceiroPrevisao, BigDecimal valorContratual);

	void updateValorExtra(Long codigoCentroFinanceiroPrevisao, BigDecimal valorExtra);

	void updateCodigoCentroFinanceiro(Long codigoCentroFinanceiroPrevisao, Long CodigoCentroFinanceiro);

	void updateCentroFinanceiro(Long codigoCentroFinanceiroPrevisao, CentroFinanceiroTO centroFinanceiro);

}
