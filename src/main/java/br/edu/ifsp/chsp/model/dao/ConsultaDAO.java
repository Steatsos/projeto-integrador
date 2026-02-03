package br.edu.ifsp.chsp.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsp.chsp.model.domain.Consulta;

import java.time.LocalDateTime;

public interface ConsultaDAO extends JpaRepository<Consulta, Integer> {
    @Query(value = "select c from Consulta c where c.id = ?1")
    public Consulta buscarPorId(Integer id);

    @Query(value = "select count(c) > 0 from Consulta c where c.id = ?1")
    public boolean jaExisteId(Integer id);

    @Query(value = "select c from Consulta c where c.datahora >=?1 and c.datahora <= ?2")
    public List<Consulta> findByDatahora(LocalDateTime inicio, LocalDateTime fim);

    @Query(value = "select c from Consulta c where c.paciente.cpf = ?1")
    public List<Consulta> findByCPF(String cpf);

    @Transactional
    @Modifying
    @Query(value = "update Consulta c set c.concluida = true where c.id = ?1")
    public void setConcluida(Integer id);
}
