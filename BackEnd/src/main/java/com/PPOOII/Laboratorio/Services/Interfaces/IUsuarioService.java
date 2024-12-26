package com.PPOOII.Laboratorio.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Entities.UsuarioPK;

import jakarta.transaction.Transactional;

public interface IUsuarioService {

	// METODOS CRUD
	@Transactional
	boolean guardar(Usuario usuario);
	@Transactional
	boolean actualizar(Usuario usuario);
	@Transactional
	boolean eliminar(UsuarioPK id);

	List<Usuario> consultarUsuario(Pageable pageable);

	Usuario getUsuarioById(UsuarioPK id);

	Usuario getUsuarioANDPersona(String login, int persona);

	Usuario findByLogin(String login);

	Usuario findByUsernameANDAPIKey(String login, String apikey);

	boolean cambiarPassword(int id, String newPassword);

	boolean existsById_Login(String login);



}
