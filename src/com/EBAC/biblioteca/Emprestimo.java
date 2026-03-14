package com.EBAC.biblioteca;

import java.io.Serializable;
import java.time.LocalDate;

public class Emprestimo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer id;
  private Livro livro;
  private Usuario usuario;
  private LocalDate dataAluguel;
  private LocalDate dataDevolucao;

  public Emprestimo(Livro livro, Usuario usuario) {
    this.livro = livro;
    this.usuario = usuario;
    this.dataAluguel = LocalDate.now();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Livro getLivro() {
    return livro;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public LocalDate getDataAluguel() {
    return dataAluguel;
  }

  @Override
  public String toString() {
    String titulo = (livro != null && livro.getTitulo() != null) ? livro.getTitulo() : "<sem título>";
    String nomeUsuario = (usuario != null && usuario.getNome() != null) ? usuario.getNome() : "<sem nome>";
    return "Empréstimo{" + "livro='" + titulo + "', usuario='" + nomeUsuario + "', dataAluguel=" + dataAluguel + '}';
  }
}
