package br.edu.ifsp.chsp.model.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.time.Period;

@Entity
@Table(name = "paciente")
public class Paciente extends Usuario {
    
    @Column(name = "data_nascimento", columnDefinition = "DATE")
    private LocalDate datanasc;

    private Long telefone;

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;

    public Paciente() {
    }

    public Paciente(String cpf, String nome, String senha, LocalDate datanasc, Long telefone, String pathFoto) {
        super(cpf, nome, senha, pathFoto);
        this.datanasc = datanasc;
        this.telefone = telefone;
    }

    public LocalDate getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(LocalDate datanasc) {
        this.datanasc = datanasc;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public LocalDate stringToDate(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(data, formatter);
        return date;
    }

    public String dateToString(LocalDate data) {
        Date date = Date.valueOf(data);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataS = format.format(date);
        return dataS;
    }

    public int getIdade() {
        LocalDate agora = LocalDate.now();
        if ((datanasc != null) && (agora != null)) {
            return Period.between(datanasc, agora).getYears();
        } else {
            return 0;
        }
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }
    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public void addConsulta(Consulta c){
        this.consultas.add(c);
    }
}