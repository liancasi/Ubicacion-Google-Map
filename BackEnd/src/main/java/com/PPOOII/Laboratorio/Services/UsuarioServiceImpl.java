package com.PPOOII.Laboratorio.Services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Entities.UsuarioPK;
import com.PPOOII.Laboratorio.Repository.UsuarioRepository;
import com.PPOOII.Laboratorio.Services.Interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service("UsuarioService")
public class UsuarioServiceImpl implements IUsuarioService,UserDetailsService {
	// ========= INYECCIÓN DE DEPENDENCIAS ==========
	@Autowired
	@Qualifier("IUsuarioRepository")
	private UsuarioRepository IUsuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// ==================== LOGS ============================
	// LOGS DE ERROR
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PersonaServiceImpl.class);

	// INSERT
	@Override
    public boolean guardar(Usuario usuario) {
        try {
            if (usuario == null) {
                logger.error("ERROR AGREGAR_USUARIO: EL USUARIO ES NULO!");
                return false;
            }
            IUsuarioRepository.save(usuario);
            return true;
        } catch (Exception e) {
            logger.error("ERROR AGREGAR_USUARIO: EL USUARIO NO SE HA GUARDADO!", e);
            return false;
        }
    }
	// UPDATE
	@Override
	@Transactional
	public boolean actualizar(Usuario usuario) {
	    // Buscar el usuario existente en la base de datos
	    Usuario usuarioExistente = IUsuarioRepository.findById(usuario.getId()).orElse(null);
	    
	    if (usuarioExistente != null) {
	        // Comprobar si el login ha cambiado
	        if (!usuarioExistente.getLogin().equals(usuario.getLogin())) {
	            // Verificar si el nuevo login ya existe
	            if (IUsuarioRepository.existsById_Login(usuario.getLogin())) {
	                throw new IllegalArgumentException("El login ya está en uso por otro usuario.");
	            }
	            usuarioExistente.setLogin(usuario.getLogin()); // Actualiza el login
	        }
	        
	        // Si la contraseña ha cambiado, actualiza el hash
	        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
	            usuarioExistente.setPassword(hashPassword(usuario.getPassword()));
	        }
	        
	        // Actualiza la relación con Persona
	        if (usuario.getPersona() != null) {
	            usuarioExistente.setPersona(usuario.getPersona());
	        }
	        
	        // Guarda los cambios en la base de datos
	        IUsuarioRepository.save(usuarioExistente);
	        return true;
	    }
	    return false; // Usuario no encontrado
	}

	
	private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
	
	// DELETE
	@Override
    public boolean eliminar(UsuarioPK id) {
        try {
            if (id == null || id.getPersona() == 0 || id.getLogin() == null || id.getLogin().isEmpty()) {
                logger.error("ERROR ELIMINAR_USUARIO: EL ID DEL USUARIO ES INVÁLIDO!");
                return false;
            }
            Usuario usuario = IUsuarioRepository.getUsuarioANDPersona(id.getLogin(), id.getPersona());
            if (usuario != null) {
            	IUsuarioRepository.delete(usuario);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("ERROR ELIMINAR_USUARIO: EL USUARIO NO SE HA ELIMINADO!", e);
            return false;
        }
    }

	// LISTA DE PRODUCTOS
	@Override
	public List<Usuario> consultarUsuario(Pageable pageable) {
		return IUsuarioRepository.findAll(pageable).getContent();
	}

	@Override
	public Usuario getUsuarioById(UsuarioPK id) {
		return IUsuarioRepository.getUsuarioANDPersona(id.getLogin(), id.getPersona());
	}

	@Override
	public Usuario findByUsernameANDAPIKey(String login, String apikey) {
		return IUsuarioRepository.findByUsernameANDAPIKey(login, apikey);
	}



	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario appUser = IUsuarioRepository.findByLogin(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return new User(appUser.getId().getLogin(), appUser.getPassword(), new ArrayList<>());
    }

	@Override
	public Usuario getUsuarioANDPersona(String login, int persona) {
		return IUsuarioRepository.getUsuarioANDPersona(login, persona);
	}

	@Override
	public boolean cambiarPassword(int id, String newPassword) {
		Usuario usuario = IUsuarioRepository.findById(id);
		if (usuario != null) {
			usuario.setPassword(newPassword); // Asegúrate de que el método setPassword esté disponible
			IUsuarioRepository.save(usuario);
			return true;
		}
		return false; // Usuario no encontrado
	}

	
	@Override
	public Usuario findByLogin(String login) {
		return IUsuarioRepository.findByLogin(login);
	}
	@Override
	public boolean existsById_Login(String login) {
		return IUsuarioRepository.findByLogin(login) != null;
	}

	
	

}
