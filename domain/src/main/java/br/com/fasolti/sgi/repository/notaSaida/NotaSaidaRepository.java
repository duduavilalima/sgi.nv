package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface NotaSaidaRepository extends QueryDslCrudRepository<NotaSaida>, JpaRepository<NotaSaida, Long> {

	/*@Query(
		"select notaSaida from NotaSaida notaSaida where notaSaida.empresa.codigo = ?1 and " +
		" notaSaida.tipoNotaFiscal = ?2 and not notaSaida.status = ?3 order by notaSaida.numero desc")
	List<NotaSaida> findLatestNotaByUnidadeCodigo(Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, Nota.StatusNota status);*/

	@Query(
		"select ns from NotaSaida ns JOIN FETCH ns.notaSaidaParcelas JOIN FETCH ns.servicoTipo " +
			" where ns.tipoNotaFiscal = ?2 and ns.codigo = ?1")
	NotaSaida findNotaFaturamentoByCodigo(Long codigo, NotaFiscal.TipoNotaFiscal tipo);

	@Query("select notaSaida.faturamentoEtapas from NotaSaida notaSaida where notaSaida.codigo = ?1")
	List<FaturamentoEtapa> findFaturamentoEtapasByCodigoNotaSaida(Long codigoNotaSaida);

	@Query(
		"select notaSaida from NotaSaida notaSaida where notaSaida.numero = ?1 and " +
			" notaSaida.empresa.codigo = ?2 and " +
			" notaSaida.tipoNotaFiscal = ?3 and not notaSaida.status = ?4 order by notaSaida.codigo desc")
	List<NotaSaida> findNotasByNumeroAndEmpresa(String numero, Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, Nota.StatusNota status);

	@Query(
		"select notaSaida.numero from NotaSaida notaSaida where notaSaida.numero = ?1 and " +
			" notaSaida.empresa.codigo = ?2 and notaSaida.tipoNotaFiscal = ?3 order by notaSaida.codigo desc ")
	List<String> findNumerosByNumeroAndEmpresa(String numero, Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo);

	@Query(
		"select notaSaida.numero from NotaSaida notaSaida where notaSaida.numero = ?1 and " +
			" notaSaida.empresa.codigo = ?2 and notaSaida.tipoNotaFiscal = ?3 and EXTRACT (year FROM notaSaida.dataEmissao) =?4 order by notaSaida.codigo desc ")
	List<String> findNumerosByNumeroAndEmpresaAndAno(String numero, Long codigoUnidade, NotaFiscal.TipoNotaFiscal tipo, int ano);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.status = ?2 where ns.codigo = ?1")
	void updateStatus(Long codigo, Nota.StatusNota statusNota);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataCancelamento = ?2 where ns.codigo = ?1")
	void updateDataCancelamento(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.responsavelCancelamento = ?2 where ns.codigo = ?1")
	void updateResponsavelCancelamento(Long codigo, PessoaFisica pessoaFisica);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataAtualizacao = ?2 where ns.codigo = ?1")
	void updateDataAtualizacao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.atualizador = ?2 where ns.codigo = ?1")
	void updateAtualizador(Long codigo, String username);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.numero = ?2 where ns.codigo = ?1")
	void updateNumero(Long codigo, String s);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataCancelamentoNumeracao = ?2 where ns.codigo = ?1")
	void updateDataCancelamentoNumeracao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.responsavelNumeracao = ?2 where ns.codigo = ?1")
	void updateResponsavelNumeracao(Long codigo, PessoaFisica pessoaFisica);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataNumeracao = ?2 where ns.codigo = ?1")
	void updateDataNumeracao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataEmissao = ?2 where ns.codigo = ?1")
	void updateDataEmissao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataEmissao = DATE(?2) where ns.codigo = ?1")
	void updateDataEmissao(Long codigo, String date);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.empresa = ?2 where ns.codigo = ?1")
	void updateEmpresa(Long codigo, Unidade unidade);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.protocoloEntrega = ?2 where ns.codigo = ?1")
	void updateProtocoloEntrega(Long codigo, String protocoloEntrega);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.servicoTipo = ?2 where ns.codigo = ?1")
	void updateServicoTipo(Long codigo, ServicoTipo servicoTipo);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.natureza = ?2 where ns.codigo = ?1")
	void updateNatureza(Long codigo, NotaFiscal.NaturezaNotaFiscal vendaAvista);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.formaPagamento = ?2 where ns.codigo = ?1")
	void updateFormaPagamento(Long codigo, Nota.FormaPagamento parcelada);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.responsavelCadastroPrefeitura = ?1 where ns.codigo = ?2")
	void updateResponsavelCadastroPrefeitura(PessoaFisica pessoaFisica, Long codigo);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.dataCadastroPrefeitura = ?1 where ns.codigo = ?2")
	void updateDataCadastroPrefeitura(Date date, Long codigo);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.responsavelCancelarNumeracao = ?2 where ns.codigo = ?1")
	void updateResponsavelCancelarNumeracao(Long codigo, PessoaFisica pessoaFisica);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.issRetidoCliente = ?2 where ns.codigo = ?1")
	void updateIssRetidoCliente(Long codigo, Boolean issRetidoCliente);

	@Modifying
	@Transactional()
	@Query("update NotaSaida ns set ns.issRetidoClienteRecebimento = ?2 where ns.codigo = ?1")
	void updateIssRetidoClienteRecebimento(Long codigo, Boolean issRetidoClienteRecebimento);

	NotaSaida findByDataEmissaoAndCredorDocumentoAndNumero(Date dataEmissao, String documento, String numero);

}
