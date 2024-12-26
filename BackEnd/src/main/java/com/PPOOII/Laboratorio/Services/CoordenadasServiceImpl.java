package com.PPOOII.Laboratorio.Services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Repository.CoordenadasRepository;
import com.PPOOII.Laboratorio.Services.Interfaces.ICoordenadasService;

@Service("CoordenadasService")
public class CoordenadasServiceImpl implements ICoordenadasService{

	// ========= INYECCIÃ“N DE DEPENDENCIAS ==========
	@Autowired
	@Qualifier("ICoordenadasRepository")
	private CoordenadasRepository ICoordenadaRepository;
	//==================== LOGS ============================
	//LOGS DE ERROR
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PersonaServiceImpl.class);
		
	@Override
	public List<Coordenadas> consultarAllCoordenadas(Pageable pageable) {
		return  ICoordenadaRepository.findAll(pageable).getContent();
	}
	
    // Método para guardar coordenadas (nuevo método)
    public Coordenadas saveCoordenadas(Coordenadas coordenadas) {
        return ICoordenadaRepository.save(coordenadas);
    }
    

    @Override
    public Coordenadas getCoordenadaXPersona(int persona) {
        return ICoordenadaRepository.getCoordenadaXPersona(persona); // Llama al método del repositorio
    }

    @Override
    public List<Coordenadas> obtenerCoordenadasPorIds(List<Integer> ids) {
        return ICoordenadaRepository.findAllById(ids);
    }

    @Override
    public Coordenadas deleteByPersona_Id(int personaId) {
        try {
            // Busca las coordenadas asociadas a la persona
            List<Coordenadas> coordenadasList = ICoordenadaRepository.findByPersona_idpersona(personaId);

            if (coordenadasList.isEmpty()) {
                logger.warn("No se encontraron coordenadas para la persona con ID: " + personaId);
                return null; // o podrías lanzar una excepción personalizada
            }

            // Elimina las coordenadas
            for (Coordenadas coordenadas : coordenadasList) {
                ICoordenadaRepository.delete(coordenadas);
            }

            return coordenadasList.get(0); // Devuelve la primera coordenada eliminada
        } catch (Exception e) {
            logger.error("ERROR AL ELIMINAR COORDENADAS PARA PERSONA ID: " + personaId, e);
            return null; // o lanzar una excepción
        }
    }

    @Override
    public List<Coordenadas> findByPersona_Id(int id) {
        return ICoordenadaRepository.findByPersona_idpersona(id); 
    }
    
    @Override
    public Optional<Coordenadas> obtenerCoordenadasPorId(Integer id) {
        return ICoordenadaRepository.findById(id); // Utiliza el método proporcionado por JpaRepository
    }
}
