package com.PPOOII.Laboratorio.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Laboratorio.APIs.GoogleMaps.Geocoder;
import com.PPOOII.Laboratorio.Component.ScheduledTasks;
import com.PPOOII.Laboratorio.Config.JWTAuthenticationConfig;
import com.PPOOII.Laboratorio.Config.Model.JwtResponse;
import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Entities.Ubicacion;
import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Entities.UsuarioPK;
import com.PPOOII.Laboratorio.Repository.CoordenadasRepository;
import com.PPOOII.Laboratorio.Repository.PersonaRepository;
import com.PPOOII.Laboratorio.Repository.UsuarioRepository;
import com.PPOOII.Laboratorio.Services.PersonaServiceImpl;
import com.PPOOII.Laboratorio.Services.UsuarioServiceImpl;
import com.PPOOII.Laboratorio.Services.Interfaces.IPersonaService;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/Laboratorio1")
@CrossOrigin(origins = "http://localhost:3000")
public class PersonaController {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	// ==========INYECCION DEL SERVICE==========
	@Autowired
	@Qualifier("PersonaService")
	private PersonaServiceImpl personaService;
	@Autowired
	@Qualifier("UsuarioService")
	private UsuarioServiceImpl usuarioService;
	

	
    @Autowired
    @Qualifier("IPersonaRepository")
    private PersonaRepository IPersonaRepository;


    @Autowired
    @Qualifier("ICoordenadasRepository")
    private CoordenadasRepository ICoordenadaRepository;
    
    
    @Autowired
    private JWTAuthenticationConfig jwtAuthenticationConfig;
    
    
	// ==========MÉTODOS HTTP====================
	// METODO POST
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/persona")
    @Operation(summary = "Agregar una nueva persona", description = "Crea una nueva persona en el sistema.")
    public ResponseEntity<String> agregarPersona(@RequestBody Persona persona) {
        try {
            if (persona.getUsuario() == null || persona.getUsuario().getLogin() == null) {
                return ResponseEntity.badRequest().body("El usuario o el login no pueden ser nulos");
            }

            Usuario usuarioExistente = usuarioService.findByLogin(persona.getUsuario().getLogin());
            if (usuarioExistente != null) {
                return ResponseEntity.badRequest().body("El usuario ya existe");
            }

            // Encriptar la contraseña
            String hashedPassword = BCrypt.hashpw(persona.getUsuario().getPassword(), BCrypt.gensalt(12));
            persona.getUsuario().setPassword(hashedPassword);

            personaService.guardar(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body("Persona registrada exitosamente.");
        } catch (Exception e) {
            logger.error("Error al registrar persona", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al registrar la persona: " + e.getMessage());
        }
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/migrar-passwords")
    public ResponseEntity<?> migrarPasswords() {
        try {
            List<Usuario> usuarios = usuarioService.consultarUsuario(null);
            int migratedCount = 0;
            for (Usuario usuario : usuarios) {
                if (!usuario.getPassword().startsWith("$2a$")) {
                    String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(12));
                    usuario.setPassword(hashedPassword);
                    usuarioService.actualizar(usuario);
                    migratedCount++;
                }
            }
            logger.info("Migración de contraseñas completada. {} contraseñas actualizadas.", migratedCount);
            return ResponseEntity.ok("Migración de contraseñas completada. " + migratedCount + " contraseñas actualizadas.");
        } catch (Exception e) {
            logger.error("Error durante la migración de contraseñas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error durante la migración de contraseñas: " + e.getMessage());
        }
    }
    
	
 

	// Método para calcular la edad
	private int calcularEdad(LocalDate fechaNacimiento) {
	    LocalDate now = LocalDate.now();
	    return now.getYear() - fechaNacimiento.getYear() - (now.getDayOfYear() < fechaNacimiento.getDayOfYear() ? 1 : 0);
	}

	// Método para calcular la edad clínica
	private String calcularEdadClinica(int edad) {
	    // Implementa la lógica para calcular la edad clínica en función de la edad
	    // Ejemplo: Puedes devolver una cadena que describa la edad clínica
	    return "Edad clínica: " + edad + " años"; // Personaliza esta lógica según tus necesidades
	}

	// MÉTODO PUT
	@CrossOrigin(origins = "http://localhost:3000")
	@Operation(summary = "Actualizar una persona", description = "Actualiza los detalles de una persona existente.")
	@PutMapping("/persona/{id}")
	public boolean editarPersona(@RequestBody @Validated Persona persona) {
		return personaService.actualizar(persona);

	}

	
	// MÉTODO DELETE
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/persona/{id}")
	 @Operation(summary = "Eliminar una persona", description = "Elimina una persona del sistema.")
    public ResponseEntity<?> eliminarPersona(@PathVariable("id") int id) {
        try {
            boolean isDeleted = personaService.eliminar(id);
            if (isDeleted) {
                return ResponseEntity.ok("Persona y sus datos asociados eliminados exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la persona con el ID proporcionado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la persona y sus datos asociados: " + e.getMessage());
        }
    }
	
	// MÉTODO GET
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/personas")
	 @Operation(summary = "Obtener todas las personas", description = "Devuelve una lista de todas las personas registradas.")
	public List<Persona> listadoPersona(Pageable pageable) {
	    // Actualizar coordenadas antes de devolver la lista de personas
	    actualizarCoordenadasDePersonas();

	    // Consultar y devolver la lista de personas
	    return personaService.consultarPersona(pageable);
	}

	private void actualizarCoordenadasDePersonas() {
	    try {
	        // Obtener todas las personas
	        List<Persona> listPersonas = IPersonaRepository.getPersonas();
	        
	        if (listPersonas != null && !listPersonas.isEmpty()) {
	            Geocoder geocoder = new Geocoder();

	            for (Persona persona : listPersonas) {
	                String direccion = persona.getUbicacion().getDireccionCompleta();
	                
	                try {
	                    // Obtener coordenadas a partir de la dirección
	                    String LatLng = geocoder.getLatLng(direccion);
	                    String[] coor = LatLng.split(",");
	                    logger.info(LatLng + " - {}", dateTimeFormatter.format(LocalDateTime.now()));

	                    // Verificar si ya existen coordenadas para la persona
	                    Coordenadas coorXper = ICoordenadaRepository.getCoordenadaXPersona(persona.getIdpersona());

	                    if (coorXper == null) {
	                        // Si no existe, crear nuevas coordenadas
	                        Coordenadas newCoordenadas = new Coordenadas(
	                            persona.getIdpersona(),
	                            persona,
	                            persona.getPrimerNombre() + " " + persona.getPrimerApellido(),
	                            Double.parseDouble(coor[0]),
	                            Double.parseDouble(coor[1])
	                        );
	                        ICoordenadaRepository.save(newCoordenadas);
	                    } else {
	                        // Si ya existe, actualizar las coordenadas
	                        coorXper.setMarca(persona.getPrimerNombre() + " " + persona.getPrimerApellido());
	                        coorXper.setLongitud(Double.parseDouble(coor[0]));
	                        coorXper.setLatitud(Double.parseDouble(coor[1]));
	                        ICoordenadaRepository.save(coorXper);
	                    }
	                } catch (IOException | InterruptedException e) {
	                    logger.error("Error al obtener coordenadas para la dirección {}: {}", direccion, e.getMessage());
	                } catch (NumberFormatException e) {
	                    logger.error("Error al parsear las coordenadas para la dirección {}: {}", direccion, e.getMessage());
	                }
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred: {}", e.getMessage());
	    }
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/personass")
	@Operation(summary = "Valida el usuario", description = "Genera una validación de los datos del usuario.")
	public ResponseEntity<?> validarUsuario(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        if (login == null || password == null) {
            return ResponseEntity.badRequest().body("Login y contraseña son requeridos");
        }

        logger.info("Intento de login para el usuario: {}", login);

        try {
            Usuario usuario = usuarioService.findByLogin(login);
            if (usuario == null) {
                logger.warn("Usuario no encontrado: {}", login);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }

            if (BCrypt.checkpw(password, usuario.getPassword())) {
                String token = jwtAuthenticationConfig.getJWTToken(login);
                logger.info("Login exitoso para el usuario: {}", login);
                return ResponseEntity.ok(new JwtResponse(token, usuario.getApikey()));
            } else {
                logger.warn("Contraseña incorrecta para el usuario: {}", login);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error al verificar la contraseña para el usuario: {}", login, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
	// ==============MÉTODOS HTTP DE BÚSQUEDA =============
	// ---GET---
	@GetMapping("/persona/id/{id}")
	@Operation(summary = "Obtiene informacion por ID", description = "Listado por Id de la persona resgistrada.")
	public Persona getById(@PathVariable("id") int id) {
		return personaService.findById(id);
	}
	// ---GET---
	@GetMapping("/persona/primerNombre/{primerNombre}")
	@Operation(summary = "Obtiene informacion por Primer Nombre", description = "Listado por Primer Nombre de la personas resgistradas.")
	public List<Persona> getByPnombre(@PathVariable("primerNombre") String pnombre) {
		return personaService.findByPrimerNombre(pnombre);
	}
	// ---GET---
	@GetMapping("/persona/edad/{edad}")
	@Operation(summary = "Obtiene informacion por la Edad", description = "Listado por la Edad de la personas resgistradas.")
	public List<Persona> getByEdad(@PathVariable("edad") int edad) {
		return personaService.findByEdad(edad);
	}
	
	@GetMapping("/persona/identificacion/{identificacion}")
    @Operation(summary = "Obtiene informacion por Identificación.", description = "Listado por Identificación de la personas resgistradas.")
	public Persona getByIdentificacion(@PathVariable("identificacion") int identificacion) {
        return personaService.findByIdentificacion(identificacion);
    }

   
    @GetMapping("/persona/segundoNombre/{segundoNombre}")
    @Operation(summary = "Obtiene informacion por Segundo Nombre", description = "Listado por Segundo Nombre de la personas resgistradas.")
    public List<Persona> getBySNombre(@PathVariable("segundoNombre") String snombre) {
        return personaService.findBySegundoNombre(snombre);
    }

    @GetMapping("/persona/primerApellido/{primerApellido}")
    @Operation(summary = "Obtiene informacion por Primer Apellido", description = "Listado por Primer Apellido de la personas resgistradas.")
    public List<Persona> getByPApellido(@PathVariable("primerApellido") String papellido) {
        return personaService.findByPrimerApellido(papellido);
    }

    @GetMapping("/persona/segundoApellido/{segundoApellido}")
    @Operation(summary = "Obtiene informacion por Segundo Apellido", description = "Listado por Segundo Apellido de la personas resgistradas.")
    public List<Persona> getBySApellido(@PathVariable("segundoApellido") String sapellido) {
        return personaService.findBySegundoApellido(sapellido);
    }

    @GetMapping("/persona/email/{email}")
    @Operation(summary = "Obtiene informacion por Email", description = "Listado por Email de la personas resgistradas.")
    public List<Persona> getByEmail(@PathVariable("email") String email) {
        return personaService.findByEmail(email);
    }

    @GetMapping("/persona/fechanacimiento/{fechaNacimiento}")
    @Operation(summary = "Obtiene informacion por Fecha de Nacimiento", description = "Listado por Fecha de Nacimiento de la personas resgistradas.")
    public List<Persona> getByFechaNacimiento(@PathVariable("fechaNacimiento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento) {
        return personaService.findByFechaNacimiento(fechaNacimiento);
    }


    @GetMapping("/persona/edadClinica/{edadClinica}")
    @Operation(summary = "Obtiene informacion por Edad Clinica", description = "Listado por Edad Clinica de la personas resgistradas.")
    public List<Persona> getByEdadClinica(@PathVariable("edadClinica") String edadClinica) {
        return personaService.findByEdadClinica(edadClinica);
    }
    
 
}

