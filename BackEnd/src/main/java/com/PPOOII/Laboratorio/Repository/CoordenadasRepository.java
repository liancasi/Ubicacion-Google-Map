package com.PPOOII.Laboratorio.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Entities.Persona;

@Repository("ICoordenadasRepository")
public interface CoordenadasRepository extends JpaRepository<Coordenadas, Integer>{
	//Hay MÃ©todos que JPA ya los tiene desarrollados, se pueden crear para tener
	//una manipulaciÃ³n mÃ¡s especifica a la hora de usarlos en el service	

	public abstract Page<Coordenadas> findAll(Pageable pageable);
	

	@Query("SELECT c FROM Coordenadas c WHERE c.persona.id = :persona")
	Coordenadas getCoordenadaXPersona(@Param("persona") int persona);
	
    @Modifying
    @Query("DELETE FROM Coordenadas c WHERE c.persona.idpersona = :id")
    void deleteByPersona_Id(@Param("id") int id);

    List<Coordenadas> findByPersona_idpersona(int idpersona);
}

