package com.PPOOII.Laboratorio.Entities;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity(name = "UsrANDPer")
@Table(schema = "ppooii", name = "usuario")
public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private UsuarioPK id;
	@Column(name = "password")
	private String password;
	@Column(name = "apikey")
	private String apikey;
	
	@JsonBackReference
	@OneToOne
	@MapsId("persona")
	@JoinColumn(name = "idpersona", referencedColumnName = "ID")
	private Persona persona;
	
	
	
	public Usuario(String login, String password, int Persona) {
	    this.id = new UsuarioPK();
	    this.id.setLogin(login);
	    this.id.setPersona(Persona); 
	    this.password = password; 
	}

	
	public Usuario() {
        this.apikey = RandomStringUtils.randomAlphanumeric(20);
    }
	public UsuarioPK getId() {
		return id;
	}
	public void setId(UsuarioPK id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
	    if (password == null || password.trim().isEmpty()) {
	        throw new IllegalArgumentException("Password cannot be null or empty");
	    }
	    this.password = password;
	}
	
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
    public String getLogin() {
        return id != null ? id.getLogin() : null;
    }

    public void setLogin(String login) {
        if (id == null) {
            id = new UsuarioPK();
        }
        id.setLogin(login);
    }
    public void setIdPersona(int personaId) {
        if (id == null) {
            id = new UsuarioPK();
        }
        id.setPersona(personaId);
    }
}