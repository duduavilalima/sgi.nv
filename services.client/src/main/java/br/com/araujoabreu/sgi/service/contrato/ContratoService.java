package br.com.araujoabreu.sgi.service.contrato;

import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface ContratoService extends CrudService<ContratoTO> {

	List<ContratoEnderecoTO> buscarEnderecos(String codigoContrato);

	List<ContratoContatoTO> buscarContato(String codigoContrato);

	List<CentroFinanceiroTO> buscarCentroLucro(ContratoTO contrato) throws Exception;

	List<AditivoTO> buscarAditivos(String codigoContrato);

	List<ContratoAnexoTO> buscarAnexos(String codigoContrato);

	CidadeTO findCidadeUnidade(ContratoTO contratoTO) throws Exception;

	CidadeTO findCidadeUnidadeMatriz(ContratoTO contratoTO) throws Exception;

	PessoaTO obterClienteContrato(String id) throws Exception;

	void reativar(Long id) throws Exception;

	void encerrar(Long id) throws Exception;

	void paralisar(Long id) throws Exception;

	List<TipoLogradouroTO> buscarTipoLogradouro();

	ContratoTO consultarDocumento(ContratoTO contratoTO) throws Exception;

	void atualizarCustoMensalEhTotal(Long codigoContrato);

	ContratoTO totalCentrosFinanceirosResource(ContratoTO contrato) throws Exception;

	void removeCentroFinanceiro(Long id) throws Exception;

	void atualizarContratoDeCentroFinanceiro(Long codigoContrato);

	void adicionarCentroFinanceiroSituacao(ColaboradorTO map, CentroFinanceiroTO map1, String s);

	ContratoTO obterContratoSimpleNotaFiscal(Long codigoContrato);

	ContratoTO calcularPrazoContrato(ContratoTO contrato);

	JPAQuery getJpaQuery();

	Integer obterPrazo(Long codigo);
}
