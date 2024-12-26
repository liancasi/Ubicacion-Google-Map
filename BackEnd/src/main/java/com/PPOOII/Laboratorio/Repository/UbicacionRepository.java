package com.PPOOII.Laboratorio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Entities.Ubicacion;

@Repository("IUbicacionRepository")
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    
    Ubicacion findById(int id);

    
}
