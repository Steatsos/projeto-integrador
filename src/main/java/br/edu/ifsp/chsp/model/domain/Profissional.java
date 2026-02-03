package br.edu.ifsp.chsp.model.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "profissional")
public class Profissional extends Usuario {
    private String email;

    @Column(nullable = false)
    private String profissao;

    private String especializacao;

    @Column(nullable = false, unique = true)
    private String registro;

    @Column(nullable = false)
    private char sexo;

    @OneToMany(mappedBy = "profissional")
    private List<Consulta> consultas;

    public Profissional() {
    }

    public Profissional(String cpf, String nome, String senha, char sexo, String email, String profissao, String especializacao,
            String registro, String pathFoto) {
        super(cpf, nome, senha, pathFoto);
        this.email = email;
        this.profissao = profissao;
        this.especializacao = especializacao;
        this.registro = registro;
    }

    public String converterEspec() {
        if (especializacao == null) {
            return "";
        }
        return " / " + especializacao;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public void addConsulta(Consulta c) {
        this.consultas.add(c);
    }

    public String getEmail() {
        return email;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    
}