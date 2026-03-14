package com.EBAC.services;

import com.EBAC.conections.ConectionDB;
import com.EBAC.biblioteca.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
  public void criarUsuario(Usuario usuario) {
    String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

    try (Connection connection = ConectionDB.conectar();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, usuario.getNome());
      stmt.setString(2, usuario.getEmail());

      int linhas = stmt.executeUpdate();
      try (ResultSet keys = stmt.getGeneratedKeys()) {
        if (keys.next()) {
          usuario.setId(keys.getInt(1));
        }
      }
      if (linhas > 0) {
        System.out.println("Usuário cadastrado com sucesso");
      } else {
        System.out.println("Erro ao cadastrar usuário.");
      }
    } catch (SQLException e) {
      System.out.println("Erro ao cadastrar usuário.");
      e.printStackTrace();
    }
  }

  public List<Usuario> listaUsuario() {
    String sql = "SELECT * FROM usuarios";
    List<Usuario> usuarios = new ArrayList<>();

    try (Connection connection = ConectionDB.conectar();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
      ResultSet resultSet = stmt.executeQuery();
      System.out.println("\nLista de usuários");
      while (resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuarios.add(usuario);
      }
    } catch (SQLException e) {
      System.out.println("Erro ao listar usuários");
      e.printStackTrace();
    }
    return usuarios;
  }

  public Optional<Usuario> buscarUsuario(String email) {
    String sql = "SELECT * FROM usuarios WHERE email = ?";
    try (Connection conn = ConectionDB.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, email);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getObject("id", Integer.class));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        return Optional.of(usuario);
      }
    } catch (SQLException e) {
      System.out.println("Erro ao localizar usuário.");
      e.printStackTrace();
    }
    return Optional.empty();
  }

  public Integer idUsuarioObj(String email) {
    Optional<Usuario> optUsuario = buscarUsuario(email);
    if (optUsuario.isEmpty()) {
      System.out.println("Usuário não encontrado para o email: " + email);
      return 0;
    }

    Usuario usuario = optUsuario.get();
    Integer idUsuarioObj = usuario.getId();

    if (idUsuarioObj == null) {
      System.out.println("Usuário encontrado, mas ID não está preenchido. Operação cancelada.");
      return 0;
    }

    return idUsuarioObj;
  }
}
