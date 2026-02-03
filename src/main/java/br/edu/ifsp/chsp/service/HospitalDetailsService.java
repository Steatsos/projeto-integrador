package br.edu.ifsp.chsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Profissional;

@Service
public class HospitalDetailsService implements UserDetailsService {

    @Autowired
    private PacienteDAO pacdao;
    @Autowired
    private ProfissionalDAO prodao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Paciente pac = pacdao.buscarPorCpf(username);
        Profissional pro = prodao.buscarPorCpf(username);

        if (pro != null) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            UserDetails user = User.withUsername(username)
                    .password(encoder.encode(pro.getSenha()))
                    .build();
            return user;
        } else if (pac != null) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            UserDetails user = User.withUsername(username)
                    .password(encoder.encode(pac.getSenha()))
                    .build();
            return user;
        } 
        throw new UsernameNotFoundException("usuário não encontrado!");
    }

}
