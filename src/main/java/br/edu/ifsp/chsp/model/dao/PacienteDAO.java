package br.edu.ifsp.chsp.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifsp.chsp.model.domain.Paciente;

public interface PacienteDAO extends JpaRepository<Paciente, String>{
    @Query(value = "select p from Paciente p where p.cpf = ?1")
    public Paciente buscarPorCpf(String cpf);

    @Query(value = "select count(p) > 0 from Paciente p where p.cpf = ?1")
    public boolean jaExisteCpf(String cpf);
}