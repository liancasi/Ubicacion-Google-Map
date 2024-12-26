package com.PPOOII.Laboratorio.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Entities.UsuarioPK;
import com.PPOOII.Laboratorio.Services.UsuarioServiceImpl;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Laboratorio1")
@Tag(name = "Usuario", description = "API para gestionar usuarios")
public class UsuarioController {
	
    @Autowired
    @Qualifier("UsuarioService")
    UsuarioServiceImpl usuarioService;
    
    @Operation(summary = "Agregar un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @PostMapping("/usuario")
    public boolean agregarUsuario(@RequestBody @Validated Usuario usuario) {
        return usuarioService.guardar(usuario);
    }
    
    @Operation(summary = "Cambiar contraseña de usuario", description = "Cambia la contraseña de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/usuario/changePassword/{id}")
    public ResponseEntity<String> changePassword(
            @Parameter(description = "ID del usuario") @PathVariable int id,
            @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        boolean success = usuarioService.cambiarPassword(id, newPassword);
        if (success) {
            return ResponseEntity.ok("Contraseña cambiada exitosamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }

    @Operation(summary = "Editar un usuario", description = "Actualiza la información de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @PutMapping("/usuario")
    public boolean editarUsuario(@RequestBody @Validated Usuario usuario) {
        return usuarioService.actualizar(usuario);
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @DeleteMapping("/usuario/{login}/{persona}")
    public boolean eliminarUsuario(
            @Parameter(description = "Login del usuario") @PathVariable("login") String login,
            @Parameter(description = "ID de la persona asociada") @PathVariable("persona") int persona) {
        UsuarioPK id = new UsuarioPK(login, persona);
        return usuarioService.eliminar(id);
    }

    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista paginada de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(schema = @Schema(implementation = Usuario.class)))
    @GetMapping("/usuarios")
    public List<Usuario> listadoUsuario(
            @Parameter(description = "Información de paginación") Pageable pageable) {
        return usuarioService.consultarUsuario(pageable);
    }

    @Operation(summary = "Obtener usuario por login y persona", description = "Retorna un usuario específico basado en su login y persona asociada")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado", 
                 content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/usuario/{login}/{persona}")
    public ResponseEntity<?> getUsuarioByLoginAndPersona(
            @Parameter(description = "Login del usuario") @PathVariable("login") String login,
            @Parameter(description = "ID de la persona asociada") @PathVariable("persona") int persona) {
        UsuarioPK usuarioPK = new UsuarioPK(login, persona);
        Usuario usuario = usuarioService.getUsuarioById(usuarioPK);

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}