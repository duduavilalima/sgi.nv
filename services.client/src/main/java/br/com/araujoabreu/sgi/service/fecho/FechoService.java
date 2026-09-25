package br.com.araujoabreu.sgi.service.fecho;

import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface FechoService extends CrudService<FechoTO> {
	FechoTO fechoByCodCentroFinanceiro(String id) throws Exception;

	List<FechoTO> listaFechoByCodCentroFinanceiro(String id) throws Exception;

	void removerReajusteByCodCentroFinanceiro(String id) throws Exception;

	void addReajuste(FechoTO fechoTO) throws Exception;

	FechoTO getUltimoFechoByCodCentroFinanceiro(Long codigo);

	List<FechoTO> buscarListaFechoByCodCentroFinanceiro(Long id);

	void criarReajuste(FechoTO fechoTO);

	void criarFecho(FechoTO fechoTO, Long codigoCentroFinanceiro) throws ServiceException;

	void atualizarFecho(FechoTO fechoTO);

	BigDecimal custoMensal(Long codFecho);

	BigDecimal totalMaoDeObra(Long codFecho);

	BigDecimal totalGastosGerais(Long codFecho);

	BigDecimal totalMateriais(Long codFecho);

	BigDecimal totalServicosTerceiros(Long codFecho);

	BigDecimal percentualBDI(Long codFecho);

	BigDecimal valorBDI(Long codFecho);

	BigDecimal baseDeTributos(Long codFecho);

	BigDecimal percentualTributo(Long codFecho);

	BigDecimal valorDosTributos(Long codFecho);

	BigDecimal valorMensal(Long codFecho);

}
