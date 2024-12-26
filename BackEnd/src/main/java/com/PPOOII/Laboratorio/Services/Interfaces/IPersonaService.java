package com.PPOOII.Laboratorio.Services.Interfaces;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.PPOOII.Laboratorio.Entities.Persona;
import com.PPOOII.Laboratorio.Entities.Usuario;

import jakarta.transaction.Transactional;

public interface IPersonaService {

	//METODOS CRUD
	@Transactional
	boolean guardar(Persona persona);
	@Transactional
	boolean actualizar(Persona persona);
	@Transactional
	boolean eliminar(int id);
	
	List<Persona> consultarPersona(Pageable pageable);
	

	//LISTA DE PERSONA POR ID
	Persona findById(int id);
	
	
	//LISTA DE PERSONA POR IDENTIFICACION
	   public Persona findByIdentificacion(int identificacion);
	   
	   //LISTA DE PERSONA POR PRIMER NOMBRE
		public List<Persona> findByPrimerNombre(String pnombre);
		
	   //LISTA DE PERSONA POR SEGUNDO NOMBRE
		public List<Persona> findBySegundoNombre(String SNombre);
		
		//LISTA DE PERSONA POR PRIMER APELLIDO
		public List<Persona> findByPrimerApellido(String PApellido);
		
		//LISTA DE PERSONA POR SEGUNDO APELLIDO
		public List<Persona> findBySegundoApellido(String SApellido);
		
		//LISTA DE PERSONA POR EMAIL
		public List<Persona> findByEmail(String email);
		
		//LISTA DE PERSONA POR FECHA DE NACIMIENTO
		public List<Persona> findByFechaNacimiento(LocalDate fechaNacimiento);
	   
	   //LISTA DE PERSONA POR EDAD
	   public List<Persona> findByEdad(int edad);
	   
	   //LISTA DE PERSONA POR EDAD CLINICA
		public List<Persona> findByEdadClinica(String edadClinica);
		
		boolean existsByEmail(String email);

		boolean existsByIdentificacion(int identificacion);
		
		
}