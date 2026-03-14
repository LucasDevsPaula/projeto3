package com.EBAC.services;

import com.EBAC.biblioteca.Livro;
import com.EBAC.conections.ConectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroService {
  public void cadastrarLivro(Livro livro) {
    String sql = "INSERT INTO livros(titulo, autor, ano_publicacao) VALUES (?, ?, ?)";
    try (Connection conn = ConectionDB.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, livro.getTitulo());
      stmt.setString(2, livro.getAutor());
      stmt.setInt(3, livro.getAnoPublicacao());

      int linhas = stmt.executeUpdate();
      try (ResultSet keys = stmt.getGeneratedKeys()) {
        if (keys.next()) {
          livro.setId(keys.getInt(1));
        }
      }
      if (linhas > 0) {
        System.out.println("Livro cadastrado com sucesso.");
      } else {
        System.out.println("Erro ao cadastrar livro.");
      }
    } catch (SQLException e) {
      System.out.println("Erro ao cadastrar livro.");
      e.printStackTrace();
    }
  }

  public List<Livro> listarLivros() {
    String sql = "SELECT * FROM livros";
    List<Livro> livros = new ArrayList<>();
    try (Connection conn = ConectionDB.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      ResultSet resultSet = stmt.executeQuery();
      System.out.println("\nLista de livros");
      while (resultSet.next()) {
        Livro livro = new Livro();
        livro.setId(resultSet.getObject("id", Integer.class));
        livro.setTitulo(resultSet.getString("titulo"));
        livro.setAutor(resultSet.getString("autor"));
        livro.setAnoPublicacao(resultSet.getInt("ano_publicacao"));
        livro.setEmprestado(resultSet.getBoolean("emprestado"));
        livros.add(livro);
      }
    } catch (SQLException e) {
      System.out.println("Erro ao listar livros");
      e.printStackTrace();
    }
    return livros;
  }

  public Optional<Livro> buscarLivro(String titulo) {
    String sql = "SELECT * FROM livros WHERE titulo = ? LIMIT 1";
    try (Connection conn = ConectionDB.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, titulo == null ? null : titulo.trim());
      ResultSet resultSet = stmt.executeQuery();
      System.out.println("\nLivro encontrado.");
      if (resultSet.next()) {
        Livro livro = new Livro();
        livro.setId(resultSet.getObject("id", Integer.class));
        livro.setTitulo(resultSet.getString("titulo"));
        livro.setAutor(resultSet.getString("autor"));
        livro.setAnoPublicacao(resultSet.getInt("ano_publicacao"));
        livro.setEmprestado(resultSet.getBoolean("emprestado"));
        return Optional.of(livro);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      System.out.println("Erro ao achar livro.");
      e.printStackTrace();
    }
    return Optional.empty();
  }

  public Integer idLivroObj(String titulo) {
    Optional<Livro> optLivro = buscarLivro(titulo);
    if (optLivro.isEmpty()) {
      System.out.println("Livro não encontrado para o título: " + titulo);
      return 0;
    }

    Livro livro = optLivro.get();
    Integer idLivroObj = livro.getId();

    if (idLivroObj == null) {
      System.out.println("Livro encontrado, mas ID não está preenchido. Operação cancelada.");
      return 0;
    }

    return idLivroObj;
  }
}
