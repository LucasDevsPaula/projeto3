package com.EBAC.biblioteca;

import java.io.Serializable;
import java.util.*;

public class Biblioteca implements Serializable {
  private static final long serialVersionUID = 1L;
  private Set<Livro> livros = new HashSet<>();
  private Map<String, Usuario> usuarios = new HashMap<>();
  private List<Emprestimo> emprestimos = new ArrayList<>();

  private final String ARQUIVOS_DADOS = "biblioteca_dados.dat";
  private final String ARQUIVOS_HISTORICO = "biblioteca_historico.dat";

  public boolean cadastrarLivro(String titulo, String autor, int anoDePublicacao) {
    if (titulo == null
        || titulo.isBlank()
        || autor == null
        || autor.isBlank()
        || anoDePublicacao <= 1000) return false;
    livros.add(new Livro(titulo, autor, anoDePublicacao));
    return true;
  }

  public List<Livro> listarLivros() {
    return livros.stream().sorted(Comparator.comparing(Livro::getAnoPublicacao)).toList();
  }

  public Optional<Livro> buscarLivro(String titulo) {
    if (titulo == null || titulo.isBlank()) return Optional.empty();
    return livros.stream()
        .filter(livro -> livro.getTitulo() != null && livro.getTitulo().equalsIgnoreCase(titulo))
        .findFirst();
  }

  public boolean AlugarLivro(String titulo, String email) {
    if (titulo == null || email == null) return false;
    Optional<Usuario> usuario = buscarUsuario(email);
    if (usuario.isEmpty()) return false;

    Optional<Livro> optLivro = buscarLivro(titulo);
    if (optLivro.isEmpty()) return false;
    Livro livro = optLivro.get();

    if (livro.isEmprestado()) return false;

    emprestimos.add(new Emprestimo(livro, usuario.get()));
    livro.setEmprestado(true);
    return true;
  }

  public List<Emprestimo> buscarEmprestimos(String email) {
    if (email == null || email.isBlank()) return Collections.emptyList();
    return emprestimos.stream()
        .filter(
            emprestimo -> {
              Usuario usuario = emprestimo.getUsuario();
              return usuario.getEmail() != null && usuario.getEmail().equalsIgnoreCase(email);
            })
        .sorted(
            Comparator.comparing(
                Emprestimo::getDataAluguel, Comparator.nullsLast(Comparator.naturalOrder())))
        .toList();
  }

  public boolean CadastrarUsuario(String nome, String email) {
    if(nome == null || email == null) return false;
    Usuario usuario = new Usuario(nome, email);

    String emailKey = usuario.getEmail();

    if (emailKey == null || emailKey.isBlank()) return false;

    if (usuarios.containsKey(emailKey)) return false;

    usuarios.put(emailKey, usuario);
    return true;
  }

  public List<Usuario> listarUsuarios() {
    return usuarios.values().stream().sorted(Comparator.comparing(Usuario::getNome)).toList();
  }

  public Optional<Usuario> buscarUsuario(String email) {
    if (email == null || email.isBlank()) return Optional.empty();
    return Optional.ofNullable(usuarios.get(email));
  }

  public boolean devolverLivro(String titulo, String email) {
    if (titulo == null || titulo.isBlank() || email == null || email.isBlank()) return false;

    List<Emprestimo> emprestimosDoUsuario = buscarEmprestimos(email);
    Optional<Emprestimo> emprestimoOpt =
        emprestimosDoUsuario.stream()
            .filter(
                emprestimo -> {
                  Livro livro = emprestimo.getLivro();
                  return livro != null
                      && livro.getTitulo() != null
                      && livro.getTitulo().equalsIgnoreCase(titulo);
                })
            .findFirst();

    if (emprestimoOpt.isEmpty()) return false;

    Emprestimo emprestimo = emprestimoOpt.get();
    Livro livro = emprestimo.getLivro();

    if (livro == null) return false;

    livro.setEmprestado(false);
    emprestimos.remove(emprestimo);
    return true;
  }

  public void salvarDados() {
    DadosCadastrais dados = new DadosCadastrais(this.livros, this.usuarios);

    Serializador.salvar(ARQUIVOS_DADOS, dados);

    Serializador.salvar(ARQUIVOS_HISTORICO, this.emprestimos);
  }

  public void carregarDados() {
    DadosCadastrais dados = (DadosCadastrais) Serializador.carregar(ARQUIVOS_DADOS);

    if (dados != null) {
      this.livros = dados.getLivros();
      this.usuarios = dados.getUsuarios();
      System.out.println(
          "Catálogo carregado: " + livros.size() + " livros, " + usuarios.size() + " usuários.");
    } else {
      System.out.println("Nenhum dado cadastral encontrado. Iniciando vazio.");
    }

    Object historico = Serializador.carregar(ARQUIVOS_HISTORICO);
    if (historico != null) {
      this.emprestimos = (List<Emprestimo>) historico;
      System.out.println("Histórico carregado: " + emprestimos.size() + " empréstimos.");
    }
  }
}
