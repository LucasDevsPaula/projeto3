package com.EBAC.biblioteca;

import java.io.Serializable;
import java.util.Objects;

public class Livro implements Comparable<Livro> {
  private Integer id;
  private String titulo;
  private String autor;
  private int anoPublicacao;
  private boolean emprestado;

  public Livro() {}

  public Livro(String titulo, String autor, int anoPublicacao) {
    this.titulo = titulo;
    this.autor = autor;
    this.anoPublicacao = anoPublicacao;
    this.emprestado = false;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public int getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(int anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public boolean isEmprestado() {
    return emprestado;
  }

  public void setEmprestado(boolean emprestado) {
    this.emprestado = emprestado;
  }

  @Override
  public String toString() {
    return "Livro{"
        + "titulo='"
        + titulo
        + '\''
        + ", autor = '"
        + autor
        + '\''
        + ", ano de Publicação ="
        + anoPublicacao
        + ", emprestado = "
        + emprestado
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Livro livro = (Livro) o;
    return Objects.equals(titulo, livro.titulo) && Objects.equals(autor, livro.autor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(titulo, autor);
  }

  @Override
  public int compareTo(Livro livro) {
    return 0;
  }
}
