package com.PPOOII.Laboratorio.Entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class UsuarioPK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "login")
	private String login;
	
	
	@Column(name = "persona")
	private int persona;
	
	public UsuarioPK () {}
	
	public UsuarioPK (String login, int persona) {
		this.login = login;
		this.persona = persona;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getPersona() {
		return persona;
	}

	public void setPersona(int persona) {
		this.persona = persona;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioPK usuarioPK = (UsuarioPK) o;
        return Objects.equals(login, usuarioPK.login) &&
               Objects.equals(persona, usuarioPK.persona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, persona);
    }
}
