package br.edu.ifsp.chsp.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "consulta")
public class Consulta implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cpf_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "cpf_profissional", nullable = false)
    private Profissional profissional;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datahora;

    private String motivo;

    private Boolean concluida;

    public Consulta() {
    }

    public Consulta(Integer id, Paciente paciente, Profissional profissional, LocalDateTime datahora, String motivo, Boolean concluida) {
        this.id = id;
        this.paciente = paciente;
        this.profissional = profissional;
        this.datahora = datahora;
        this.motivo = motivo;
        this.concluida = concluida;
    }

    public String gerarFicha(String receita) {
        String str = "Desculpe, a consulta ainda não foi concluída :(";
        if (concluida) {
            return ("----------------------------\nnome: " + paciente + "\natendido " + datahora + "\n receita: "
                    + receita
                    + "assinatura: " + profissional);
        }
        return str;
    }

    public String converterConcluida() {
        if (concluida == null) {
            return "Em andamento";
        } else if (concluida){
            return "Concluída";
        } else {
            return "Em andamento";
        }
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public LocalDateTime getDatahora() {
        return datahora;
    }

    @Override
    public String toString() {
        return "Consulta [paciente=" + paciente + ", profissional=" + profissional + ", data=" + datahora + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public void setDatahora(LocalDateTime datahora) {
        this.datahora = datahora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
