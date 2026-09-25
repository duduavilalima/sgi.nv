package br.com.araujoabreu.sgi.service.unidade;

import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface UnidadeService extends CrudService<UnidadeTO> {

	List<UnidadeTO> listarUnidadesPorEmpresaExceto(List<Long> codigosUnidadesIgnoradas);

	UnidadeTO obterUnidadeSimples(Long codigo);

	String obterCodNomeUnidade(Long codigo);

	UnidadeTO obterUnidade(Long codigo);

	List<UnidadeTO> listarUnidadesPorEmpresa();

	List<UnidadeTO> listarUnidadesFaturamento();

	List<UnidadeTO> listarUnidadeMatrizAtiva();

	boolean zerarNumeracaoNfAno(Long codigoUndiade);

	UnidadeTO obterUnidadeParaVerificacaoDeNumeracao(Long codigoUndiade);

	void atualizarNumeracaoProximaNf(Long codigo, Integer numeracao, Short ano);

	JPAQuery getSelect();

	void atualizarNumeroProximaNf(Long codigo, Integer numeracao);

	void atualizarNumeroProximaNfMaterial(Long codigo, Integer numeracao);

	boolean obterUnidadeInterfacePrefeitura(Long codigo);

    void atualizarNumeracaoProximaNd(Long codigo, Integer numeracao, Short ano);

    void atualizarNumeroProximaNd(Long codigo, Integer numero);
}
