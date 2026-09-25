package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

//FIXME Provavelmente esse serviço deva ser desmembrado de acordo com as entidades envolvidas
public interface ChequesService {

	Long imprimirChequesEndossoSelecionados(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria);

	Long imprimirTodosCheques(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador);

	Long imprimirTodosChequesEndosso(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador);

	Long imprimirTodosChequesCopia(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador);

	Long imprimirChequesCopiaSelecionados(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria);

	Long imprimirChequesSelecionadosIndividual(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria);

	List<String> obterTextoChequeCopiaIndividual(List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria);

	String obterChequeCopia(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, ItemRelacaoTO itemRelacao, boolean selecionado);

	String finalChequeCopia();

	String endossoCheque(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, boolean copia);

	String obterDestinaSeEndossoCheque(List<ItemRelacaoTO> list);

	String obterInformacaosCheque(RelacaoBancariaTO relacaoBancaria, Long NumCheque, BigDecimal valorTotal);

	String obterDataCheque(String dataVenc);

	String obterTextoChequesEndossoIndividual(List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria);

	String endossoChequeSelect(RelacaoBancariaTO relacaoBancaria, ItemRelacaoTO itemRelacao, boolean copia);

	String endossoTexto(String destinaSe, String espacoEndosso, String espacoAssinat, String favorecido, String assinatura, Date vencimento, DadosBancariosTO dadosBancarios, String tipoDocumentoPagamento);

	List<DadosBancariosTO> obterDadosBancariosPorItemRelacao(ItemRelacaoTO itemRelacao);

	Map<String, String> createInformacoesCheque(RelacaoBancariaTO relacaoBancaria);

	String montarCodigoBanco(String codBanco);

	String obterTextoChequeIndividual(List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria);

	String obterImpressaoCheque(ContaCorrenteTO cc, BigDecimal total, String pagamentoAAlguem, String cidadeDataVenc);

	String obterValorPorExtenso(BigDecimal valor, ContaCorrenteTO cc, boolean copia);

	String obterPagamentoACheque(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, boolean copia);

	String obterPagamentoACheque(ItemRelacaoTO itemRelacao, boolean copia);

	String obterPagamentoA(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria);

	String montarDataCheque(String endereco, Date dataVenc, boolean copia);
}
