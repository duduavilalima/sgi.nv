package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface NotaDebitoService extends CrudService<NotaDebitoTO> {

	NotaDebitoTO buscarNotaDebitoById(Long id);

	NotaDebitoTO receberNotaDebito(NotaDebitoTO notaDebitoTO);

    Object findLastNotaDebitoByUnidadeCodigo(NotaDebitoTO exampleObj);

    Object numerarND(NotaDebitoTO transferObj) throws Exception;

	NotaDebitoTO cancelarNumeracaoND(NotaDebitoTO transferObj) throws Exception;

	NotaDebitoTO salvarProtocolo(NotaDebitoTO transferObj);

	void removerND(NotaDebitoTO transferObj) throws Exception;

	void cancelamento(Long codigo);

	NotaDebitoTO getNotaDebitoByCodigo(Long codigo);

    NotaDebitoTO saveND(NotaDebitoTO transferObj) throws Exception;

	List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj);

	NotaDebitoTO findNotaDebitoParaImpressaoByCodigo(String id);

	NotaDebitoTO montarNovaNotaDebito(Long idEtapa);

	NotaDebitoTO buscarNotaDebitoByNumeroEmpresa(String numero, Long idEmpresa);
}
