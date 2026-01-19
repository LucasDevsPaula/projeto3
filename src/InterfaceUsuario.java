import com.EBAC.biblioteca.Biblioteca;
import com.EBAC.biblioteca.Emprestimo;
import com.EBAC.errors.*;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class InterfaceUsuario {
  Biblioteca biblioteca;
  private Scanner scanner;

  public InterfaceUsuario(Biblioteca biblioteca, Scanner scanner) {
    this.biblioteca = biblioteca;
    this.scanner = scanner;
  }

  public void mostrarFuncoes() {
    System.out.println(
        """
            GERENCIAR LIVROS
            1 - Cadastrar livros
            2 - Listar todos os livros
            3 - Buscar livro

            GERENCIAR USUÁRIOS
            4 - Cadastrar usuário
            5 - Listar usuário

            GERENCIAR EMPRÉSTIMOS
            6 - Realizar Empréstimo
            7 - Buscar empréstimo por usuário
            8 - Devolver livro

            9 - Sair
            """);
  }

  public void cadastrarLivro() throws LivroCadastroException {
    System.out.println("Digite o titulo do livro: ");
    String titulo = scanner.nextLine();
    System.out.println("Digite o nome do autor: ");
    String autor = scanner.nextLine();
    System.out.println("Digite o ano de publicação: ");
    int ano;
    try {
      ano = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Ano inválido. Operação cancelada");
      return;
    }
    boolean alugou = biblioteca.cadastrarLivro(titulo, autor, ano);
    if (!alugou) {
      throw new LivroCadastroException("Não é possível cadastrar o livro no momento.");
    }
    System.out.println("Livro cadastrado com sucesso!");
  }

  public void listarLivros() {
    System.out.println("LIVROS CADASTRADOS: ");
    biblioteca.listarLivros().forEach(System.out::println);
  }

  public void buscarLivro() {
    System.out.println("Digite o livro que você quer buscar: ");
    String livro = scanner.nextLine();
    if (livro.isBlank()) {
      System.out.println("Entrada inválida. Operação cancelada");
      return;
    }

    try {
      Optional<?> resultado = biblioteca.buscarLivro(livro);
      if (resultado.isEmpty()) {
        throw new LivroNaoEncontrado("Livro '" + livro + "' não encontrado.");
      }else {
        System.out.println(resultado);
      }
    } catch (LivroNaoEncontrado e) {
      System.out.println("Erro ao buscar livro: " + e.getMessage());
    }
  }

  public void cadastrarUsuario() throws UsuarioCadastroException {
    System.out.println("Digite seu nome: ");
    String nome = scanner.nextLine();
    System.out.println("Digite seu email: ");
    String email = scanner.nextLine();
    boolean cadastro = biblioteca.CadastrarUsuario(nome, email);
    if (!cadastro) {
      throw new UsuarioCadastroException("Não é possível cadastrar esse usuário no momento.");
    }
    System.out.println("Usuário cadastrado com sucesso!");
  }

  public void listarUsuarios() {
    System.out.println("USUÁRIOS CADASTRADOS: ");
    biblioteca.listarUsuarios().forEach(System.out::println);
  }

  public void alugarLivro() throws LivroIndisponivelException {
    System.out.println("Digite o titulo do livro para alugar: ");
    String titulo = scanner.nextLine();
    System.out.println("Digite seu email de login: ");
    String email = scanner.nextLine();
    boolean alugou = biblioteca.AlugarLivro(titulo, email);
    if (!alugou) {
      throw new LivroIndisponivelException("Livro '" + titulo + "' indisponível para aluguel.");
    }
    System.out.println("Livro alugado com sucesso!");
  }

  public void buscarEmprestimos() {
    System.out.println("Digite seu email para buscar os empréstimos na sua conta: ");
    String email = scanner.nextLine();
    if (email.isBlank()) {
      System.out.println("Email inválido. Operação cancelada.");
      return;
    }

    try {
      var emprestimos = biblioteca.buscarEmprestimos(email);
      if (emprestimos == null || emprestimos.isEmpty()) {
        System.out.println("Nenhum empréstimo encontrado para esse usuário.");
        return;
      }
      emprestimos.forEach(System.out::println);
    } catch (InputMismatchException e) {
      System.out.println("Entrada inválida: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Erro ao buscar empréstimos: " + e.getMessage());
    }
  }

  public void devolverLivro() throws LivroIndisponivelException {
    System.out.println("Digite o titulo do livro para alugar: ");
    String titulo = scanner.nextLine();
    System.out.println("Digite seu email de login: ");
    String email = scanner.nextLine();
    //    System.out.println(
    //        biblioteca.devolverLivro(titulo, email)
    //            ? "Livro devolvido com sucesso!"
    //            : "Não foi possível devolver esse!");
    boolean devolveu = biblioteca.devolverLivro(titulo, email);
    if (!devolveu) {
      throw new LivroIndisponivelException(
          "Não é possível devolver o livro '" + titulo + "' no momento.");
    }
    System.out.println("Livro devolvido com sucesso!");
  }
}
