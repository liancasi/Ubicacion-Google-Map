package com.PPOOII.Laboratorio.Repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PPOOII.Laboratorio.Entities.Persona;

@Repository("IPersonaRepository")
public interface PersonaRepository extends JpaRepository<Persona, Integer>, CrudRepository<Persona, Integer>{
	
	//Hay Métodos que JPA ya los tiene desarrollados, se pueden crear para tener
	//una manipulación más especifica a la hora de usarlos en el service	


    Persona findById(int id);
    
    
    Persona findByIdentificacion( int identificacion);

    
    Page<Persona> findAll(Pageable pageable);
	   
    List<Persona> findByPrimerNombre(String pnombre);

  
    List<Persona> findBySegundoNombre( String SNombre);

   
    List<Persona> findByPrimerApellido(@Param("primerApellido") String PApellido);

  
    List<Persona> findBySegundoApellido(@Param("segundoApellido") String SApellido);

    
    List<Persona> findByEmail(@Param("email") String email);

   
    List<Persona> findByFechaNacimiento(@Param("fechaNacimiento") LocalDate fechaNacimiento);

   
    List<Persona> findByEdad(@Param("edad") int edad);

   
    List<Persona> findByEdadClinica(@Param("edadClinica") String edadClinica);

    @Query("SELECT p FROM Persona p")
    List<Persona> getPersonas();
}
