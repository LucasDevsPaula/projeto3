package com.EBAC.biblioteca;

import com.EBAC.services.EmprestimoService;
import com.EBAC.services.LivroService;
import com.EBAC.services.UsuarioService;

import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {
  private UsuarioService usuarioService = new UsuarioService();
  private LivroService livroService = new LivroService();
  private EmprestimoService emprestimoService = new EmprestimoService();

  public boolean cadastrarLivro(String titulo, String autor, int anoDePublicacao) {
    if (titulo == null
        || titulo.isBlank()
        || autor == null
        || autor.isBlank()
        || anoDePublicacao <= 1000) return false;
    try {
      Livro livro = new Livro(titulo, autor, anoDePublicacao);
      livroService.cadastrarLivro(livro);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public List<Livro> listarLivros() {
    return livroService.listarLivros().stream()
        .sorted(Comparator.comparing(Livro::getAnoPublicacao))
        .toList();
  }

  public Optional<Livro> buscarLivro(String titulo) {
    if (titulo == null || titulo.isBlank()) return Optional.empty();
    return livroService.buscarLivro(titulo);
  }

  public boolean AlugarLivro(String titulo, String email) {
    return emprestimoService.alugarLivro(titulo, email);
  }

  public List<Emprestimo> buscarEmprestimos(String email) {
    if (email == null || email.isBlank()) return Collections.emptyList();
    return emprestimoService.buscarEmprestimosPorUsuario(email);
  }

  public boolean CadastrarUsuario(String nome, String email) {
    if (nome == null || email == null) return false;
    try {
      Usuario usuario = new Usuario(nome, email);
      usuarioService.criarUsuario(usuario);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public List<Usuario> listarUsuarios() {
    return usuarioService.listaUsuario().stream()
        .sorted(Comparator.comparing(Usuario::getNome))
        .toList();
  }

  public boolean devolverLivro(String titulo) {
    if (titulo == null || titulo.isBlank()) return false;
    return emprestimoService.devolverLivro(titulo);
  }

  public void livrosPorAutor() {
    // Buscar livros atualizados do banco
    List<Livro> lista = livroService.listarLivros();

    if (lista == null || lista.isEmpty()) {
      System.out.println("Nenhum livro cadastrado.");
      return;
    }
    Map<String, List<Livro>> livrosPorAutor =
        lista.stream()
            .collect(
                Collectors.groupingBy(
                    livro -> {
                      String autor = livro.getAutor();
                      if (autor == null || autor.isBlank()) return "<Sem autor>";
                      return autor;
                    },
                    TreeMap::new,
                    Collectors.toList()));

    livrosPorAutor.forEach(
        (autor, livrosAutor) -> {
          System.out.println("Autor: " + autor);
          livrosAutor.stream()
              .map(Livro::getTitulo)
              .filter(titulo -> titulo != null && !titulo.isBlank())
              .sorted(String::compareToIgnoreCase)
              .forEach(titulo -> System.out.println(" -" + titulo));
        });
  }
}
