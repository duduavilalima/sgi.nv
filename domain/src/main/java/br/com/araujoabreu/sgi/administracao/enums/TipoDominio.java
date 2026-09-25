package br.com.araujoabreu.sgi.administracao.enums;

import br.com.araujoabreu.sgi.administracao.domain.Fabricante;
import br.com.araujoabreu.sgi.administracao.domain.TipoNaturezaDespesa;
import br.com.araujoabreu.sgi.administracao.domain.TipoObra;
import br.com.araujoabreu.sgi.comercial.domain.CategoriaDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.ClassificacaoCliente;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.Cargo;
import br.com.fasolti.corporativo.pessoa.domain.Operadora;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;

/**
 * Para registrar uma entidade como Dominio, deve-se:
 * 1. Implementar a interface Dominio. Ex: NaturezaFornecimento implements Dominio {}
 * 2. Adicionar uma constante para a classe. Exemplo: NATUREZA_FORNECIMENTO(NaturezaFornecimento.class) em TipoDominio
 * 3. Registrar no messages_pt o toString() para essa constante.
 *
 * @author Sidharta Noleto
 * @author Marcus Lacerda
 */
public enum TipoDominio {
	BANCO(Banco.class),
	CARGO(Cargo.class),
	CATEGORIA_DESPESA_GERAL(CategoriaDespesaGeral.class),
	CATEGORIA_MAO_OBRA(CategoriaMaoDeObra.class),
	CLASSIFICACAO_CLIENTE(ClassificacaoCliente.class),
	FABRICANTE(Fabricante.class),
	OPERADORA(Operadora.class),
	TIPO_LOGRADOURO(TipoLogradouro.class),
	TIPO_NATUREZA_DESPESA(TipoNaturezaDespesa.class),
	TIPO_OBRA(TipoObra.class);

	private final Class<? extends Dominio> entidade;

	TipoDominio(Class<? extends Dominio> code) {
		this.entidade = code;
	}

	/**
	 * Retorna o nome simplificado da entidade.
	 *
	 * @return
	 */
	public String getEntidade() {
		return entidade.getSimpleName();
	}

	/**
	 * Retorna a classe do dominio.
	 *
	 * @return
	 */
	public Class<? extends Dominio> getEntidadeClass() {
		return entidade;
	}
}
