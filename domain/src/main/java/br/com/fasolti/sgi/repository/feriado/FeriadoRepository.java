package br.com.fasolti.sgi.repository.feriado;

import br.com.araujoabreu.sgi.administracao.domain.Feriado;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeriadoRepository extends QueryDslCrudRepository<Feriado> {

	@Query("SELECT f FROM Feriado f WHERE ( f.dia = :dia AND f.mes = :mes AND ( :ano IS NULL ) ) OR ( f.dia = :dia AND f.mes = :mes AND  ( f.ano = :ano OR f.ano IS NULL ) ) ")
	List<Feriado> findFeriadosByData(@Param(value = "dia") int dia, @Param(value = "mes") int mes, @Param(value = "ano") int ano);

}
