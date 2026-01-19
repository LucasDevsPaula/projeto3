package com.EBAC.biblioteca;

import java.io.Serializable;
import java.time.LocalDate;

public class Emprestimo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Livro livro;
  private Usuario usuario;
  private LocalDate dataAluguel;

  public Emprestimo(Livro livro, Usuario usuario) {
    this.livro = livro;
    this.usuario = usuario;
    this.dataAluguel = LocalDate.now();
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
    return "Empréstimo{"
        + livro
        + ", usuario="
        + usuario.getNome()
        + ", dataAluguel="
        + dataAluguel
        + '}';
  }
}
