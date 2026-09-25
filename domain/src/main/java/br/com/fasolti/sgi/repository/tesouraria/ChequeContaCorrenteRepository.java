package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrente;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface ChequeContaCorrenteRepository extends QueryDslCrudRepository<ChequeContaCorrente> {

	List<ChequeContaCorrente> findByContaCorrenteCodigo(Long codigo);

	List<ChequeContaCorrente> findByContaCorrenteCodigoAndChequeAndSituacao(Long codigo, Long numeroCheque, ChequeContaCorrente.StatusChequeContaCorrente situacao);

	ChequeContaCorrente findByCheque(Long cheque);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.dataCompensacao = ?1 where p.codigo = ?2")
	void updateDataCompensacao(Date dataCompensacao, Long codigo);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.atualizador = ?1 where p.codigo = ?2")
	void updateAtualizador(String atualizador, Long codigo);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.dataAtualizacao = ?1 where p.codigo = ?2")
	void updateDataAtualizacao(Date dataAtualizacao, Long codigo);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.situacao = ?1 where p.codigo = ?2")
	void updateSituacao(ChequeContaCorrente.StatusChequeContaCorrente cancelado, Long codigoChequeContaCorrente);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.responsavelCancelamento = ?1 where p.codigo = ?2")
	void updateResponsavelCancelamento(Pessoa responsavel, Long codigoChequeContaCorrente);

	@Modifying
	@Transactional()
	@Query("update ChequeContaCorrente p set p.dataCancelamento = ?1 where p.codigo = ?2")
	void updateDataCancelamento(Date date, Long codigoChequeContaCorrente);

	List<ChequeContaCorrente> findByChequeAndContaCorrenteCodigo(Long cheque, Long codigo);
}
