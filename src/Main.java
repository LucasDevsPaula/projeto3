import com.EBAC.biblioteca.Biblioteca;
import com.EBAC.errors.LivroCadastroException;
import com.EBAC.errors.LivroIndisponivelException;
import com.EBAC.errors.UsuarioCadastroException;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    int opcao = 0;
    Scanner scanner = new Scanner(System.in);
    Biblioteca biblioteca = new Biblioteca();
    InterfaceUsuario interfaceUsuario = new InterfaceUsuario(biblioteca, scanner);

    System.out.println("Bem vindo ao Sistema de Gerenciamento de Biblioteca Digital.");

    do {
      System.out.println("Escolha uma das opções. ");
      interfaceUsuario.mostrarFuncoes();
      try {
        opcao = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Por favor, digite apenas números.");
        opcao = 0;
        continue;
      }
      switch (opcao) {
        case 1 -> {
          try {
            interfaceUsuario.cadastrarLivro();
          } catch (LivroCadastroException e) {
            System.out.println(e.getMessage());
          }
        }
        case 2 -> interfaceUsuario.listarLivros();
        case 3 -> interfaceUsuario.buscarLivro();
        case 4 -> {
          try {
            interfaceUsuario.cadastrarUsuario();
          } catch (UsuarioCadastroException e) {
            System.out.println(e.getMessage());
          }
        }
        case 5 -> interfaceUsuario.listarUsuarios();
        case 6 -> {
          try {
            interfaceUsuario.alugarLivro();
          } catch (LayerInstantiationException | LivroIndisponivelException e) {
            System.out.println(e.getMessage());
          }
        }
        case 7 -> interfaceUsuario.buscarEmprestimos();
        case 8 -> interfaceUsuario.todosOsLivrosPorAutor();
        case 9 -> {
          try {
            interfaceUsuario.devolverLivro();
          } catch (LivroIndisponivelException e) {
            System.out.println(e.getMessage());
          }
        }
        case 10 -> System.out.println("Saindo...");
        default -> System.out.println("Tente novamente");
      }
    } while (opcao != 10);

    scanner.close();
  }
}
