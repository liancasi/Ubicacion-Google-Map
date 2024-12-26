package com.PPOOII.Laboratorio.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.PPOOII.Laboratorio.Config.JWTAuthenticationConfig;
import com.PPOOII.Laboratorio.Config.Model.JwtRequest;
import com.PPOOII.Laboratorio.Config.Model.JwtResponse;
import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Services.UsuarioServiceImpl;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Autenticación", description = "API para la autenticación de usuarios")
public class JwtAuthenticationController {
    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    @Qualifier("UsuarioService")
    private UsuarioServiceImpl usuarioServiceImp;

    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa", 
                 content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(
            @Parameter(description = "Credenciales del usuario") @RequestBody JwtRequest authenticationRequest,
            @Parameter(description = "API Key del usuario") @RequestHeader("apikey") String apikey) throws Exception {
        
        Usuario user = usuarioServiceImp.findByUsernameANDAPIKey(authenticationRequest.getUsername(), apikey);

        if (user == null || !user.getPassword().equals(authenticationRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(user.getId().getLogin());
        final String token = jwtAuthenticationConfig.getJWTToken(user.getId().getLogin());

        return ResponseEntity.ok(new JwtResponse(token, apikey));
    }
}