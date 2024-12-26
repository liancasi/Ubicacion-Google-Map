package com.PPOOII.Laboratorio.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ubicacion", schema = "ppooii")
public class Ubicacion {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion") 
    private int id;

	@Column(name = "lugar")
	private String lugar;
	
	@Column(name = "ciudad")
	private String ciudad;
	
	@Column(name = "departamento")
	private String departamento;
	
	@JsonBackReference
	@OneToOne(mappedBy ="ubicacion", cascade = CascadeType.MERGE)
	private Persona persona;
	
	public Ubicacion() {}
	
	public Ubicacion(String lugar, String ciudad, String departamento, Persona persona) {
		this.lugar = lugar;
		this.ciudad = ciudad;
		this.departamento = departamento;
		this.persona = persona;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	 public String getDireccionCompleta() {
	        return lugar + ", " + ciudad + ", " + departamento;
	    }
}
