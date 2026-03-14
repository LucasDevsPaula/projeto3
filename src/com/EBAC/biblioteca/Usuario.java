package com.EBAC.biblioteca;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer id;
  private String nome;
  private String email;

  public Usuario() {}

  public Usuario(String nome, String email) {
    this.nome = nome;
    this.email = email;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return Objects.equals(email, usuario.email);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(email);
  }

  @Override
  public String toString() {
    return "Usuários{" + "nome='" + nome + '\'' + ", email='" + email + '\'' + '}';
  }
}
