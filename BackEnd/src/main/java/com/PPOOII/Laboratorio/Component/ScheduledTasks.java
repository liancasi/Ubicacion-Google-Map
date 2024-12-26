package com.PPOOII.Laboratorio.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.PPOOII.Laboratorio.APIs.GoogleMaps.Geocoder;
import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Entities.UsuarioPK;
import com.PPOOII.Laboratorio.Repository.CoordenadasRepository;
import com.PPOOII.Laboratorio.Repository.PersonaRepository;
import com.PPOOII.Laboratorio.Services.Interfaces.ICoordenadasService;

import io.jsonwebtoken.io.IOException;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    @Qualifier("IPersonaRepository")
    private PersonaRepository IPersonaRepository;


    @Autowired
    @Qualifier("ICoordenadasRepository")
    private CoordenadasRepository ICoordenadaRepository;

    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            // Obtiene la lista de personas
            List<Persona> listPersonas = IPersonaRepository.getPersonas();
            
            if (listPersonas != null && !listPersonas.isEmpty()) {
                Geocoder geocoder = new Geocoder();

                for (Persona persona : listPersonas) {
                    String direccion = persona.getUbicacion().getDireccionCompleta();
                    
                    try {
                        // Obtiene las coordenadas a partir de la dirección
                        String LatLng = geocoder.getLatLng(direccion);
                        String[] coor = LatLng.split(",");
                        logger.info(LatLng + " - {}", dateTimeFormatter.format(LocalDateTime.now()));

                        // Verifica si ya existen coordenadas para la persona
                        Coordenadas coorXper = ICoordenadaRepository.getCoordenadaXPersona(persona.getIdpersona());

                        if (coorXper == null) {
                            // Si no existe, crea nuevas coordenadas
                            Coordenadas newCoordenadas = new Coordenadas(
                                persona.getIdpersona(), 
                                persona, 
                                persona.getPrimerNombre() + " " + persona.getPrimerApellido(), 
                                Double.parseDouble(coor[0]), 
                                Double.parseDouble(coor[1]) 
                               
                            );
                            ICoordenadaRepository.save(newCoordenadas);
                        } else {
                            // Si ya existe, actualiza las coordenadas
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

}