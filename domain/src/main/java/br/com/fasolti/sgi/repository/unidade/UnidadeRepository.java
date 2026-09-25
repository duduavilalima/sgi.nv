package br.com.fasolti.sgi.repository.unidade;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UnidadeRepository extends QueryDslCrudRepository<Unidade> {

	@Modifying
	@Transactional()
	@Query("update Unidade u set u.proximaNf = ?2 where u.codigo = ?1")
	void updateProximaNf(Long codigo, Integer numeracao);

	@Modifying
	@Transactional()
	@Query("update Unidade u set u.proximaNfMaterial = ?2 where u.codigo = ?1")
	void updateProximaNfMaterial(Long codigo, Integer numeracao);

	@Modifying
	@Transactional()
	@Query("update Unidade u set u.anoProximaNf = ?2 where u.codigo = ?1")
	void updateAnoProximaNf(Long codigo, Short ano);

	@Modifying
	@Transactional()
	@Query("update Unidade u set u.proximaNd = ?2 where u.codigo = ?1")
	void updateProximaNd(Long codigo, Integer numeracao);

//	@Modifying
//	@Transactional()
//	@Query("update Unidade u set u.anoProximaNd = ?2 where u.codigo = ?1")
//	void updateAnoProximaNd(Long codigo, Short ano);
}
