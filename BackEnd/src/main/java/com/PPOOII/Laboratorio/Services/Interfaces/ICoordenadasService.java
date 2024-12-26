package com.PPOOII.Laboratorio.Services.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Entities.Persona;

public interface ICoordenadasService {


    List<Coordenadas> consultarAllCoordenadas(Pageable pageable);

    Coordenadas saveCoordenadas(Coordenadas coordenadas);
    
    Coordenadas getCoordenadaXPersona(int personaId);
    
    public List<Coordenadas> obtenerCoordenadasPorIds(List<Integer> ids);
    
    Coordenadas deleteByPersona_Id(int personaId);
    
	 List<Coordenadas> findByPersona_Id(int id);
	 
	 Optional<Coordenadas> obtenerCoordenadasPorId(Integer id);
}
