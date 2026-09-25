package br.com.araujoabreu.sgi.service.impl.security;

import br.com.fasolti.commons.services.security.BaseSecurityService;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.security.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl extends BaseSecurityService<Usuario> {
	private static final QUsuario Q = QUsuario.usuario;
	@Autowired
	private UsuarioRepository usuarioRep;

	@Override
	public Usuario findUserByUsername(String username) {
		Usuario usuario = usuarioRep.findOne(Q.login.equalsIgnoreCase(username)).orElse(null);

		if (usuario == null)
			throw new UsernameNotFoundException("Usuário " + username + " não cadastrado");

		return usuario;
	}
}
