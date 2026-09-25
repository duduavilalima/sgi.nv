package br.com.araujoabreu.sgi.service.faturamento;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoRetencaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalExibicaoTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.fasolti.commons.client.service.CrudService;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface FaturamentoService extends CrudService<FaturamentoTO> {

	NotaDebitoTO findNotaDebitoFaturamento(FaturamentoEtapaTO etapa) throws Exception;

	NotaFiscalExibicaoTO findNotaFiscalFaturamento(FaturamentoEtapaTO etapa) throws Exception;

	List<FaturamentoRetencaoTO> findFaturamentoRetencaoByCodFaturamento(Long codigo);

	List<FaturamentoTO> pesquisarFaturamento(FaturamentoTO filtroFaturamento);

	FaturamentoTO faturamentoAnterior(Long id);

	FaturamentoTO obterNomeClientePorNotaSaida(Long codigoNotaSaida);

	void changeClienteSubcliente(FaturamentoTO obj);

	FaturamentoTO obterFaturamentoSimpleTelaNotaFiscal(Long codigoFaturamento);

	PessoaTO obterClienteDeUmFaturamento(Long codigo);

	void updateSituacaoAberto(Long codigo);

	void updateSituacaoEmFaturamento(Long codigo);

	void updateSituacaoEmRecebimento(Long codigo);

	void updateSituacaoRecebido(Long codigo);

	void updateSituacaoCancelado(Long codigo);

	void updateDataAtualizacao(Long codigo, Date date);

	void updateAtualizador(Long codigo, String username);

	boolean isIssRetidoCliente(Long codigoFaturamento);

	boolean isIssRetidoClienteByCodNotaSaida(Long codigoNotaSaida);

	Iterable<NotaFiscalExibicaoTO> montarNotaFiscalFaturamento(FaturamentoTO exampleObj, PageRequest of);

	Iterable<NotaDebitoTO> montarNotaDebitoFaturamento(FaturamentoTO exampleObj, PageRequest of);

	ValueTO notaFiscalAbertaCheck(Long valueOf);

	MenssagemPadraoTO possuiContaContabilAssociada(Long codigoFaturamento);
}
