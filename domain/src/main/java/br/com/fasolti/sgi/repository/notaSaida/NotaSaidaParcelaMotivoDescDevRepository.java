package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaMotivoDescDev;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaSaidaParcelaMotivoDescDevRepository extends QueryDslCrudRepository<NotaSaidaParcelaMotivoDescDev>, JpaRepository<NotaSaidaParcelaMotivoDescDev, Long> {

}

