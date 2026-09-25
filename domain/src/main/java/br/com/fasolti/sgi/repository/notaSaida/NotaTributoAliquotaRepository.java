package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaTributoAliquotaRepository extends QueryDslCrudRepository<NotaTributoAliquota> {

	List<NotaTributoAliquota> findByNotaCodigoOrderByCodigoAsc(Long codigoNota);

	NotaTributoAliquota findByPagamentoCodigo(Long codigo);

	List<NotaTributoAliquota> findByNotaCodigo(Long codigo);

	List<NotaTributoAliquota> findByNotaCodigoAndOrdem(Long codigo, Integer ordem);

	@Query(
		"select notaTributoAliquota" +
			"  from NotaTributoAliquota notaTributoAliquota" +
			"  left join notaTributoAliquota.nota nota" +
			"  left join fetch notaTributoAliquota.aliquota aliquota" +
			"  left join fetch aliquota.categoriaTributo categoriaTributo" +
			"  left join fetch categoriaTributo.tributo tributo" +
			" where nota.codigo = :codigoNota")
	List<NotaTributoAliquota> findByNotaCodigoJoinTributo(@Param(value = "codigoNota") Long codigoNota);

}
