package com.PPOOII.Laboratorio.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.PPOOII.Laboratorio.APIs.GoogleMaps.Geocoder;
import com.PPOOII.Laboratorio.Entities.Ubicacion;
import com.PPOOII.Laboratorio.Repository.UbicacionRepository;
import com.PPOOII.Laboratorio.Services.Interfaces.IUbicacionService;

import java.io.IOException;

@Service("UbicacionService")
public class UbicacionServiceImpl implements IUbicacionService {

    @Autowired
    @Qualifier("IUbicacionRepository")
    private UbicacionRepository ubicacionRepository;

    private Geocoder geocoder = new Geocoder(); // Instancia de Geocoder

    @Override
    public boolean guardar(Ubicacion ubicacion) {
        try {
            ubicacionRepository.save(ubicacion);
            return true;
        } catch (Exception e) {
            // Manejo de errores
            return false;
        }
    }

    @Override
    public boolean actualizar(Ubicacion ubicacion) {
        if (ubicacion == null || ubicacion.getId() == 0) {
            return false; // Validación simple
        }
        return guardar(ubicacion); // Reutiliza el método guardar
    }

    @Override
    public boolean eliminar(int id) {
        try {
            ubicacionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // Manejo de errores
            return false;
        }
    }

    @Override
    public List<Ubicacion> consultarPersona(org.springframework.data.domain.Pageable pageable) {
        return ubicacionRepository.findAll(pageable).getContent();
    }

    @Override
    public Ubicacion findById(int id) {
        return ubicacionRepository.findById(id);
    }

    // Nuevo método para obtener coordenadas
    @Override
    public String obtenerCoordenadas(int idUbicacion) throws IOException, InterruptedException {
        Ubicacion ubicacion = findById(idUbicacion);
        if (ubicacion == null) {
            throw new IllegalArgumentException("Ubicacion no encontrada con ID: " + idUbicacion);
        }
        String query = ubicacion.getDireccionCompleta();
        return geocoder.getLatLng(query);
    }

}
