package br.com.fasolti.sgi.repository.nota;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface NotaRepository extends QueryDslCrudRepository<Nota> {

	List<Nota> findByCentroFinanceiroCodigo(Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Nota SET status = ?1 WHERE codigo = (SELECT lote.nota.codigo FROM LoteInterfaceNota lote INNER JOIN lote.nota WHERE lote.codigo = ?2 ) ")
	void updateStatusByLoteInterfaceNota(Nota.StatusNota status, Long codigoLoteInterfaceNota);

	@Modifying
	@Transactional()
	@Query("UPDATE Nota SET dataReversaoCancelamento = ?1 WHERE codigo = ?2")
	void updateDataReversaoCancelamento(Date date, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Nota SET responsavelReversaoCancelamento = ?1 WHERE codigo = ?2")
	void updateResponsavelReversaoCancelamento(PessoaFisica pessoa, Long id);


}
