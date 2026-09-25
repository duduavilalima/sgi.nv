package br.com.fasolti.sgi.repository.cidade;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends QueryDslCrudRepository<Cidade> {

	String LETRAS_COM_ACENTO = "áàãâäÁÀÃÂÄéèêëÉÈÊËíìîïÍÌÎÏóòõôöÓÒÕÔÖúùûüÚÙÛÜñÑçÇÿýÝ";
	String LETRAS_SEM_ACENTO = "aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUnNcCyyY";

	@Query("SELECT cidade FROM Cidade cidade WHERE UPPER(cidade.nome) LIKE CONCAT('%', UPPER(:nome) ,'%')"
		+ " OR UPPER( TRANSLATE (cidade.nome, '" + LETRAS_COM_ACENTO + "', '" + LETRAS_SEM_ACENTO + "' ) ) LIKE CONCAT('%', UPPER(:nomeSemAcento) ,'%') "
		+ " ORDER BY cidade.nome ")
	List<Cidade> findCidadesByNome(@Param("nome") String nome, @Param("nomeSemAcento") String nomeSemAcento);

	@Query("select c from Cidade c where translate(nome, '" + LETRAS_COM_ACENTO + "', '" + LETRAS_SEM_ACENTO + "') = :nomeSemAcento or nome = :nome and uf = :uf")
	List<Cidade> findByNomeQuery(@Param("nomeSemAcento") String nomeSemAcento, @Param("nome") String nome, @Param("uf") String uf);

	Cidade findByCodigo(Long codigo);
}
