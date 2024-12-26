package com.PPOOII.Laboratorio.Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "Persona", schema = "ppooii")
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "ID") 
	public int idpersona;
	

	@Column(name = "identificacion")
	private int identificacion;
	@Column(name = "primerNombre")
	private String primerNombre;

	@Column(name = "segundoNombre")
	private String segundoNombre;

	@Column(name = "primerApellido")
	private String primerApellido;

	@Column(name = "segundoApellido")
	private String segundoApellido;
	
	@Column(name = "fechaNacimiento")
	private LocalDate fechaNacimiento;

	@Column(name = "edadClinica")
	private String edadClinica;
	

	@Column(name = "edad")
	private int edad;

	
	@Column(name = "email")
	private String email;
	

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    @JsonManagedReference
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Usuario usuario;

    @JsonManagedReference
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Coordenadas coordenadas;



	public Persona(int idpersona, String primerNombre, int edad, Ubicacion ubicacion, int identificacion, String segundoNombre,
			String primerApellido, String segundoApellido, String email, LocalDate fechaNacimiento, String edadClinica,
			Usuario usuario, Coordenadas coordenadas) {
		this.idpersona = idpersona;
		this.primerNombre = primerNombre;
		this.edad = edad;
		this.ubicacion = ubicacion;
		this.identificacion = identificacion;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.edadClinica = edadClinica;
		this.usuario = usuario;
		this.coordenadas = coordenadas;
	}

	public Persona() {
	}



	public Persona(int idpersona, String primerNombre, int edad, Ubicacion ubicacion) {
		this.idpersona = idpersona;
		this.primerNombre = primerNombre;
		this.edad = edad;
		this.ubicacion = ubicacion;
	}



    @PrePersist
    @PreUpdate
    private void calcularEdad() {
        if (fechaNacimiento != null) {
            LocalDate hoy = LocalDate.now();
            Period periodo = Period.between(fechaNacimiento, hoy);
            
            this.edad = periodo.getYears();
            this.edadClinica = String.format("%d años %d meses %d días", 
                                             periodo.getYears(), 
                                             periodo.getMonths(), 
                                             periodo.getDays());
        }
    }



	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public int getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(int identificacion) {
		this.identificacion = identificacion;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEdadClinica() {
		return edadClinica;
	}

	public void setEdadClinica(String edadClinica) {
		this.edadClinica = edadClinica;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Coordenadas getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Coordenadas coordenadas) {
		this.coordenadas = coordenadas;
	}

	public int getIdpersona() {
		return idpersona;
	}

	public void setIdpersona(int idpersona) {
		this.idpersona = idpersona;
	}
	
}
