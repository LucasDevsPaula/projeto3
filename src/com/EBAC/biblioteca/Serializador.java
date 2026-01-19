package com.EBAC.biblioteca;

import java.io.*;

public class Serializador {
  public static void salvar(String nomeArquivo, Object object) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
      out.writeObject(object);
      System.out.println("Salvo");
    } catch (IOException e) {
      System.out.println("Erro ao salvar: " + e.getMessage());
    }
  }

  public static Object carregar(String nomeArquivo) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
      return in.readObject();
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Erro ao carregar: " + e.getMessage());
      return null;
    }
  }
}
