package br.com.fasolti.sgi.repository.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends QueryDslCrudRepository<Colaborador> {

	List<Colaborador> findByCentroFinanceiroCodigo(Long id);

	long countByCentroFinanceiroCodigo(Long id);

	Optional<Colaborador> findOneByDocumento(String documento);

	@Query("select c from Colaborador c where c.documento in (?1)")
	List<Colaborador> findAllByDocumento(List<String> documento);

	@Query("SELECT DISTINCT c"
		+ " FROM Colaborador c "
		+ " JOIN c.usuario u "
		+ " JOIN u.usuarioPerfils uPerfis "
		+ " JOIN uPerfis.perfil perfil"
		+ " WHERE perfil.nome = ?1 "
		+ " ")
	List<Colaborador> findByNomePerfil(String nomePerfil);

	@Query("SELECT DISTINCT c"
		+ " FROM Colaborador c "
		+ " JOIN c.usuario u "
		+ " JOIN u.usuarioPerfils uPerfis "
		+ " JOIN uPerfis.perfil perfil"
		+ " WHERE perfil.nome = 'admin-demitidos' ")
	List<Colaborador> findDestinatariosEmailParaImportacaoArquivoInativacao();
}
