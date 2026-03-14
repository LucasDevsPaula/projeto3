package com.EBAC.services;

import com.EBAC.biblioteca.Emprestimo;
import com.EBAC.biblioteca.Livro;
import com.EBAC.biblioteca.Usuario;
import com.EBAC.conections.ConectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoService {
  LivroService livroService = new LivroService();
  UsuarioService usuarioService = new UsuarioService();

  public boolean alugarLivro(String titulo, String email) {

    int id_livro = livroService.idLivroObj(titulo).intValue();

    int id_usuario = usuarioService.idUsuarioObj(email).intValue();

    String sql = "INSERT INTO emprestimos (id_livro, id_usuario, data_aluguel) VALUES(?, ?, ?)";
    String sqlAtualizacao = "UPDATE livros SET emprestado = true WHERE id = ?";

    Connection conn = null;

    try {
      conn = ConectionDB.conectar();
      conn.setAutoCommit(false);

      int linhaNovoEmprestimo;
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id_livro);
        stmt.setInt(2, id_usuario);
        stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        linhaNovoEmprestimo = stmt.executeUpdate();
      }

      if (linhaNovoEmprestimo <= 0) {
        System.out.println("Falha ao inserir empréstimo. Nenhuma linha afetada.");
        conn.rollback();
        return false;
      }

      int linhaUpdate;
      try (PreparedStatement stmtLivro = conn.prepareStatement(sqlAtualizacao)) {
        stmtLivro.setInt(1, id_livro);
        linhaUpdate = stmtLivro.executeUpdate();
      }

      if (linhaUpdate <= 0) {
        System.out.println("Falha ao marcar livro como emprestado. Nenhuma linha afetada.");
        conn.rollback();
        return false;
      }

      conn.commit();
      System.out.println("Empréstimo realizado com sucesso!");
      return true;
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
          System.out.println("Erro na operação. Empréstimo cancelado por segurança");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
      return false;
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public List<Emprestimo> buscarEmprestimosPorUsuario(String email) {
    String sql =
        """
                  SELECT u.nome, u.email, l.titulo, e.data_aluguel
                  FROM emprestimos AS e
                  INNER JOIN usuarios AS u ON e.id_usuario = u.id
                  INNER JOIN livros AS l ON e.id_livro = l.id
                  WHERE u.email = ?
                  """;

    List<Emprestimo> lista = new ArrayList<>();
    try (Connection conn = ConectionDB.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, email);
      try (ResultSet resultSet = stmt.executeQuery()) {
        while (resultSet.next()) {
          Usuario usuario = new Usuario(resultSet.getString("nome"), resultSet.getString("email"));
          // construir Livro apenas com título para não expor autor/ano/emprestado
          Livro livro = new Livro(resultSet.getString("titulo"), "", 0);

          Emprestimo emprestimo = new Emprestimo(livro, usuario);

          lista.add(emprestimo);
        }
      }
    } catch (SQLException e) {
      System.out.println("Erro ao realizar busca.");
      e.printStackTrace();
    }

    return lista;
  }

  public boolean devolverLivro(String titulo) {

    int id_livro = livroService.idLivroObj(titulo).intValue();

    String sqlAtualizacao = "UPDATE livros SET emprestado = false WHERE id = ?";
    String sql =
        "UPDATE emprestimos SET data_devolucao = ? WHERE id_livro = ? AND data_devolucao IS NULL";

    Connection conn = null;

    try {
      conn = ConectionDB.conectar();
      conn.setAutoCommit(false);

      int linhaAtualizacaoEmprestado;
      try (PreparedStatement stmt = conn.prepareStatement(sqlAtualizacao)) {
        stmt.setInt(1, id_livro);
        linhaAtualizacaoEmprestado = stmt.executeUpdate();
      }

      if (linhaAtualizacaoEmprestado <= 0) {
        System.out.println("Falha ao devolver empréstimo. Nenhuma linha afetada.");
        conn.rollback();
        return false;
      }

      int linhasAtualizacaoDevolucao;
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        stmt.setInt(2, id_livro);
        linhasAtualizacaoDevolucao = stmt.executeUpdate();
      }

      if (linhasAtualizacaoDevolucao <= 0) {
        System.out.println("Falha ao marcar devolução do empréstimo. Nenhuma linha afetada.");
        conn.rollback();
        return false;
      }

      conn.commit();
      System.out.println("Devolução realizada com sucesso!");
      return true;
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
          System.out.println("Erro na operação. Devolução cancelada por segurança.");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
      return false;
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
