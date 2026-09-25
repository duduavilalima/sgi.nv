package br.com.araujoabreu.sgi.service.banco;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoPesquisaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BancoService {

	List<BancoTO> buscaTodosOrdenado();

	BancoTO obterbancoPeloCodigoDadosBancarios(Long codigo);

	BancoTO obterBancoSimpleTelaNotaFiscal(Long codigoBanco);

	JPAQuery<?> obterBancoDeContaCorrente(Long codigoContaCorrente);

	Iterable<BancoTO> buscaPorExemploMethod(BancoTO exampleObj);

	Page<BancoPesquisaTO> buscaPorExemploMethod(BancoPesquisaTO exampleObj, Integer page, String sortDir, Integer pageSize, String sortField, String sortFieldString);

	BancoTO buscaPorIdMethod(String id);

	BancoTO createMethod();

	BancoTO saveMethod(BancoTO transferObj);

	BancoTO updateMethod(BancoTO transferObj, Long id);

	void deleteMethod(String id);
}
