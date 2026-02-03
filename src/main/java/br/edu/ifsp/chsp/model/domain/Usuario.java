package br.edu.ifsp.chsp.model.domain;

import java.io.Serializable;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Usuario implements Serializable {
    @Id
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(name="path_foto")
    private String pathFoto;

    public Usuario() {
    }

    public Usuario(String cpf, String nome, String senha, String pathFoto) {
        this.cpf = cpf;
        this.nome = nome;
        this.senha = senha;
        this.pathFoto = pathFoto;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }
}
