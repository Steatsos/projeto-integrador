package br.edu.ifsp.chsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Usuario;

@Controller
public class Hospital {

    @Autowired
    PacienteDAO pacdao;

    @Autowired
    ProfissionalDAO prodao;

    @GetMapping("/home")
    public String principal(ModelMap map) {
        if (getUsername() != null) {
            if (prodao.buscarPorCpf(getUsername()) != null) {
                map.addAttribute("user", prodao.buscarPorCpf(getUsername()));
            } else {
                map.addAttribute("user", pacdao.buscarPorCpf(getUsername()));
            }
        }
        return "/home";
    }

    @GetMapping(name = "/")
    public String home() {
        return "/home";
    }

    @GetMapping("/login")
    public String login(Paciente paciente) {
        return ("logged/login");
    }

    @ModelAttribute("username")
    public String getUsername() {
        String nome = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            nome = userDetails.getUsername();
        }
        return nome;
    }

    @ModelAttribute("user")
    public Usuario getAuthenticatedUser() {
        String username = getUsername();
        if (username != null) {
            Paciente pac = pacdao.buscarPorCpf(username);
            if (pac != null) {
                return pac;
            }
            return prodao.buscarPorCpf(username);
        }
        return null;
    }

    public boolean validaCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11)
            return false;
        int soma = 0;
        for (int i = 0; i < 9; i++)
            soma += (cpf.charAt(i) - '0') * (10 - i);
        int digito1 = 11 - (soma % 11);
        if (digito1 > 9)
            digito1 = 0;
        soma = 0;
        for (int i = 0; i < 10; i++)
            soma += (cpf.charAt(i) - '0') * (11 - i);
        int digito2 = 11 - (soma % 11);
        if (digito2 > 9)
            digito2 = 0;
        return (cpf.charAt(9) - '0') == digito1 && (cpf.charAt(10) - '0') == digito2;
    }

    public boolean validaCrm(String crm) {
        if (crm.length() != 6) {
            return false;
        }
        if (!crm.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
}