package com.PPOOII.Laboratorio.Entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "Coordenadas", schema = "PPOOII")
public class Coordenadas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_coordenada")
	private int id;

	@Column(name = "marca")
	public String marca;

	@Column(name = "longitud")
	public double longitud;

	@Column(name = "latitud")
	public double latitud;

	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "id_persona")
	private Persona persona;

	;

	public Coordenadas() {
	}

	public Coordenadas(Persona persona, String marca, double longitud, double latitud) {

		this.marca = marca;
		this.longitud = longitud;
		this.latitud = latitud;
		this.persona = persona;

	}

	public Coordenadas(int id, Persona persona, String marca, double longitud, double latitud) {
		this.id = id;
		this.marca = marca;
		this.longitud = longitud;
		this.latitud = latitud;
		this.persona = persona;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}
