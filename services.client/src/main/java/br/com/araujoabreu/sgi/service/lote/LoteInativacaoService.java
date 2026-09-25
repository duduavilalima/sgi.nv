package br.com.araujoabreu.sgi.service.lote;

import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.lote.transfer.LoteInativacaoTO;
import br.com.araujoabreu.sgi.service.lote.util.ArquivoLoteInativacao;

import java.util.List;

public interface LoteInativacaoService {

	/**
	 * Importa um lote de inativação manual (feita por um colaborador).
	 *
	 * @param arquivoImportacao
	 * @param usuarioLogado
	 * @return
	 * @throws AdministracaoException
	 */
	LoteInativacaoTO importar(
		ArquivoLoteInativacao arquivoImportacao,
		ColaboradorTO usuarioLogado);

	/**
	 * Importa um lote de inativação de um processo agendado (importação automática).
	 *
	 * @return
	 * @throws AdministracaoException
	 */
	LoteInativacaoTO importar(
		ArquivoLoteInativacao arquivoLoteImportacao);

	List<ColaboradorTO> findDestinatariosEmailParaImportacaoAutomatica();
}
