package br.com.fasolti.sgi.repository;

import br.com.fasolti.corporativo.pessoa.domain.NaturezaJuridica;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturezaJuridicaRepository extends BaseQueryDslRepository<NaturezaJuridica, Long> {
	NaturezaJuridica findByCodigoRfb(String codigoRfb);
}
