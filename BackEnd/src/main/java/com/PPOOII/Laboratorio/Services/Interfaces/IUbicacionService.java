package com.PPOOII.Laboratorio.Services.Interfaces;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import com.PPOOII.Laboratorio.Entities.Ubicacion;




public interface IUbicacionService {
	
	Ubicacion findById(int id);
    boolean guardar(Ubicacion ubicacion);
    boolean actualizar(Ubicacion ubicacion);
    boolean eliminar(int id);
    List<Ubicacion> consultarPersona(Pageable pageable);
    
    // Método para obtener coordenadas
    String obtenerCoordenadas(int idUbicacion) throws IOException, InterruptedException;
}