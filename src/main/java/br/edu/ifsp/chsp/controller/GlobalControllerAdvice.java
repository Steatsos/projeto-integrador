package br.edu.ifsp.chsp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Usuario;


@ControllerAdvice
public class GlobalControllerAdvice {

    private final PacienteDAO pacdao;

    private final ProfissionalDAO prodao;

    GlobalControllerAdvice(PacienteDAO pacdao, ProfissionalDAO prodao) {
        this.pacdao = pacdao;
        this.prodao = prodao;
    }

    @ModelAttribute("username")
    public String getUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
            authentication.isAuthenticated() &&
            !"anonymousUser".equals(authentication.getPrincipal())) {

            return authentication.getName();
        }

        return null;
    }

    @ModelAttribute("user")
    public Usuario getAuthenticatedUser() {
        String username = getUsername();

        if (username == null) {
            return null;
        }

        Paciente paciente = pacdao.buscarPorCpf(username);

        if (paciente != null) {
            return paciente;
        }

        return prodao.buscarPorCpf(username);
    }
}