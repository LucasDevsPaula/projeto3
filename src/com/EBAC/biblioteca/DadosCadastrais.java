package com.EBAC.biblioteca;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class DadosCadastrais implements Serializable {
    private static final long serialVersionUID = 1L;
    private Set<Livro> livros;
    private Map<String, Usuario> usuarios;

    public DadosCadastrais(Set<Livro> livros, Map<String, Usuario> usuarios) {
        this.livros = livros;
        this.usuarios = usuarios;
    }

    public Set<Livro> getLivros() {
        return livros;
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }
}
