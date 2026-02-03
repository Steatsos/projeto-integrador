package br.edu.ifsp.chsp.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifsp.chsp.model.domain.Profissional;

public interface ProfissionalDAO extends JpaRepository<Profissional, String> {
    @Query(value = "select count(p) > 0 from Profissional p where p.cpf = ?1")
    public boolean jaExisteCpf(String cpf);

    @Query(value = "select count(p) > 0 from Profissional p where p.registro = ?1")
    public boolean jaExisteRegistro(String registro);

    @Query(value = "select count(p) > 0 from Profissional p where p.email = ?1")
    public boolean jaExisteEmail(String email);

    @Query(value = "select p from Profissional p where p.nome like %?1%")
    public List<Profissional> findLikeNome (String nome);

    @Query(value = "select p from Profissional p where p.profissao like %?1%")
    public List<Profissional> findLikeProfissao(String profissao);

    @Query(value = "select p from Profissional p where p.especializacao like %?1%")
    public List<Profissional> findLikeEspecializacao(String especializacao);

    @Query(value = "select p from Profissional p where p.cpf = ?1")
    public Profissional buscarPorCpf(String cpf);

    @Query(value = "select p from Profissional p where p.cpf = ?1")
    public List<Profissional> findListByCpf(String cpf);

    @Query(value = "select p from Profissional p where p.sexo = ?1")
    public List<Profissional> findLikeSexo(char sexo);
}
