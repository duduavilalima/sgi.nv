package br.com.fasolti.sgi.repository.contasApagar;

import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelaNotaRepository extends QueryDslCrudRepository<ParcelaNota> {
	ParcelaNota findByCodigo(Long codigo);

	List<ParcelaNota> findByNotaCodigo(Long codigo);

	List<ParcelaNota> findByNotaCodigo(Long codigo, Pageable pageable);

	@Query("SELECT COUNT(pn) FROM ParcelaNota pn WHERE pn.nota.codigo=:codigoNota")
	Long numParcelasByNota(@Param("codigoNota") Long id);
}
