package br.com.araujoabreu.sgi.service.dadosBancarios;

import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface DadosBancariosService extends CrudService<DadosBancariosTO> {

	List<DadosBancariosTO> obterDadosBancariosPessoaCodigo(Long codigoPessoao);

	DadosBancariosTO restHomologarDadosBancariosManual(DadosBancariosTO dbto);

	List<DadosBancariosTO> getDadosBancariosPessoa(Long idPessoa);

	DadosBancariosTO obterDadosBancarioCodigoComBanco(Long codigoDadosBancarios);

	DadosBancariosTO obterDadosBancarioSimples(Long codigoDadosBancarios);

	List<DadosBancariosTO> obterListaDadosBancarioSimplesPeloCodigoCredor(Long codigoCredor);

	JPAQuery jpaQueryDadosBancarios();
}
