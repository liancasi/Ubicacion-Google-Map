package com.PPOOII.Laboratorio.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Entities.Ubicacion;
import com.PPOOII.Laboratorio.Entities.Usuario;
import com.PPOOII.Laboratorio.Repository.CoordenadasRepository;
import com.PPOOII.Laboratorio.Repository.PersonaRepository;
import com.PPOOII.Laboratorio.Repository.UbicacionRepository;
import com.PPOOII.Laboratorio.Repository.UsuarioRepository;
import com.PPOOII.Laboratorio.Services.Interfaces.ICoordenadasService;
import com.PPOOII.Laboratorio.Services.Interfaces.IPersonaService;
import com.PPOOII.Laboratorio.Services.Interfaces.IUsuarioService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.apache.logging.log4j.Logger;

@Service("PersonaService")
public class PersonaServiceImpl implements IPersonaService {

    @Autowired
    @Qualifier("IPersonaRepository")
    private PersonaRepository IPersonaRepository;
    
    @Autowired
    private ICoordenadasService coordenadasService;
    
    
    @Autowired
    @Qualifier("IUbicacionRepository")
    private UbicacionRepository ubicacionRepository;
    
    @Autowired
    @Qualifier("IUsuarioRepository")
    private UsuarioRepository usuarioRepository;
    
    
    @Autowired
	@Qualifier("ICoordenadasRepository")
	private CoordenadasRepository ICoordenadaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private IUsuarioService usuarioService; // Solo necesita una referencia a IUsuarioService

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PersonaServiceImpl.class);

    @Override
    @Transactional
    public boolean guardar(Persona persona) {
        try {
            if (persona == null) {
                logger.error("ERROR AGREGAR_PERSONA: LA PERSONA ES NULO!");
                return false;
            }

            // Validaciones
            if (usuarioService.existsById_Login(persona.getUsuario().getLogin())) {
                logger.error("ERROR AGREGAR_PERSONA: El username ya está registrado.");
                return false;
            }

            if (existsByEmail(persona.getEmail())) {
                logger.error("ERROR AGREGAR_PERSONA: El correo ya está registrado: " + persona.getEmail());
                return false;
            }

            if (existsByIdentificacion(persona.getIdentificacion())) {
                logger.error("ERROR AGREGAR_PERSONA: La identificación ya está registrada: " + persona.getIdentificacion());
                return false;
            }

            // Guardar o actualizar la persona
            IPersonaRepository.save(persona); // Esto maneja tanto guardar como actualizar

            // Manejar las coordenadas (si es necesario)
            if (persona.getCoordenadas() != null) {
                persona.getCoordenadas().setId(persona.getIdpersona());
                coordenadasService.saveCoordenadas(persona.getCoordenadas());
            }

            return true;
        } catch (Exception e) {
            logger.error("ERROR AGREGAR_PERSONA: LA PERSONA NO SE HA GUARDADO!", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean actualizar(Persona persona) {
        try {
            if (persona == null || persona.getIdpersona() == 0) {
                logger.error("ERROR EDITAR_PERSONA: LA PERSONA ES NULO O EL ID ES 0!");
                return false;
            } else {
                if (persona.getUsuario() != null) {
                    persona.getUsuario().setPersona(persona);
                }
                IPersonaRepository.save(persona);
                return true;
            }
        } catch(Exception e) {
            logger.error("ERROR EDITAR_PERSONA: LA PERSONA NO SE HA EDITADO!", e);
            return false;
        }
    }
    
	//DELETE
    @Override
    @Transactional
    public boolean eliminar(int id) {
        try {
            Persona persona = IPersonaRepository.findById(id);
            if (persona != null) {
                // Delete associated coordenadas
                if (persona.getCoordenadas() != null) {
                	ICoordenadaRepository.delete(persona.getCoordenadas());
                }

                // Delete associated usuario
                if (persona.getUsuario() != null) {
                    usuarioRepository.delete(persona.getUsuario());
                }

                // Delete associated ubicacion
                if (persona.getUbicacion() != null) {
                    ubicacionRepository.delete(persona.getUbicacion());
                }

                // Finally, delete the persona
                IPersonaRepository.delete(persona);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la persona y sus datos asociados: " + e.getMessage());
        }
    }


    @Override
    public List<Persona> consultarPersona(Pageable pageable) {
        return IPersonaRepository.findAll(pageable).getContent();
    }

    @Override
    public Persona findById(int id) {
    	 Persona persona = IPersonaRepository.findById(id);
    	    if (persona == null) {
    	        throw new EntityNotFoundException("Persona no encontrada con ID: " + id);
    	    }
    	    return persona;
    	}

    @Override
    public List<Persona> findByEdad(int edad) {
        return IPersonaRepository.findByEdad(edad);
    }

    @Override
    public Persona findByIdentificacion(int identificacion) {
        return IPersonaRepository.findByIdentificacion(identificacion);
    }

    @Override
    public List<Persona> findBySegundoNombre(String SNombre) {
        return IPersonaRepository.findBySegundoNombre(SNombre);
    }

    @Override
    public List<Persona> findByPrimerApellido(String PApellido) {
        return IPersonaRepository.findByPrimerApellido(PApellido);
    }

    @Override
    public List<Persona> findBySegundoApellido(String SApellido) {
        return IPersonaRepository.findBySegundoApellido(SApellido);
    }

    @Override
    public List<Persona> findByEmail(String email) {
        return IPersonaRepository.findByEmail(email);
    }

    @Override
    public List<Persona> findByFechaNacimiento(LocalDate fechaNacimiento) {
        return IPersonaRepository.findByFechaNacimiento(fechaNacimiento);
    }

    @Override
    public List<Persona> findByEdadClinica(String edadClinica) {
        return IPersonaRepository.findByEdadClinica(edadClinica);
    }

    @Override
    public List<Persona> findByPrimerNombre(String pnombre) {
        return IPersonaRepository.findByPrimerNombre(pnombre);
    }

    @Override
    public boolean existsByEmail(String email) {
        List<Persona> personas = IPersonaRepository.findByEmail(email);
        return personas != null && !personas.isEmpty();
    }

    @Override
    public boolean existsByIdentificacion(int identificacion) {
        return IPersonaRepository.findByIdentificacion(identificacion) != null;
    }
}
