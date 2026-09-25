package br.com.fasolti.sgi.repository.fecho;

import br.com.araujoabreu.sgi.comercial.domain.Fecho;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FechoRepository extends QueryDslCrudRepository<Fecho> {

	List<Fecho> findByCentroFinanceiroCodigo(Long id);

	@Query("SELECT f.percentualEncargosSociais FROM Fecho f WHERE f.codigo = ?1")
	BigDecimal getPercentualEncargosSociaisByCodigo(Long codigo);

	@Query("SELECT f.percentualBdiBeneficios FROM Fecho f WHERE f.codigo = ?1")
	BigDecimal getPercentualBdiBeneficiosByCodigo(Long codigo);

	@Query("SELECT f.percentualBdiDespesas FROM Fecho f WHERE f.codigo = ?1")
	BigDecimal getPercentualBdiDespesasByCodigo(Long codigo);

	@Query("SELECT f.percentualImposto FROM Fecho f WHERE f.codigo = ?1")
	BigDecimal getPercentualImpostoByCodigo(Long codigo);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET percentualBdiBeneficios = ?1 WHERE codigo = ?2")
	void updatePercentualBdiBeneficios(BigDecimal percentualBdiBeneficios, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET percentualBdiDespesas = ?1 WHERE codigo = ?2")
	void updatePercentualBdiDespesas(BigDecimal percentualBdiDespesas, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET percentualImposto = ?1 WHERE codigo = ?2")
	void updatePercentualImposto(BigDecimal percentualImposto, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET percentualEncargosSociais = ?1 WHERE codigo = ?2")
	void updatePercentualEncargosSociais(BigDecimal percentualEncargosSociais, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET valorMaoObra = ?1 WHERE codigo = ?2")
	void updateValorMaoObra(BigDecimal valorMaoObra, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET valorGastosGerais = ?1 WHERE codigo = ?2")
	void updateValorGastosGerais(BigDecimal valorGastosGerais, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET valorMaterial = ?1 WHERE codigo = ?2")
	void updateValorMaterial(BigDecimal valorMaterial, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE Fecho SET valorServicoTerceiro = ?1 WHERE codigo = ?2")
	void updateValorServicoTerceiro(BigDecimal valorServicoTerceiro, Long id);



}
