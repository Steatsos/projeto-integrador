package br.edu.ifsp.chsp.controller;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifsp.chsp.model.dao.ConsultaDAO;
import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Consulta;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Profissional;
import br.edu.ifsp.chsp.model.domain.Usuario;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaDAO cdao;

    @Autowired
    private ProfissionalDAO pdao;

    @Autowired
    private PacienteDAO pacdao;

    @GetMapping("/cadastrar")
    public String cadastrar(ModelMap map, RedirectAttributes attr) {
        Usuario user = getAuthenticatedUser();
        if (user instanceof Profissional) {
            attr.addFlashAttribute("fail", "Profissionais não podem criar consultas!");
            return "redirect:/home";
        }
        List<Profissional> profissionais = pdao.findAll();
        map.addAttribute("profissionais", profissionais);

        map.addAttribute("consulta", new Consulta());
        return ("/consulta/cadastro");
    }

    @GetMapping("/consultar/{cpf}")
    public String consultar(@PathVariable("cpf") String cpf, ModelMap map) {

        List<Profissional> profissional = pdao.findListByCpf(cpf);
        map.addAttribute("profissionais", profissional);

        List<Paciente> pacientes = pacdao.findAll();
        map.addAttribute("pacientes", pacientes);

        map.addAttribute("consulta", new Consulta());
        return ("/consulta/cadastro");
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Consulta consulta, @RequestParam("data") LocalDate data,
            @RequestParam("hora") LocalTime hora, RedirectAttributes attr) {
        Usuario user = getAuthenticatedUser();
        if (user instanceof Profissional) {
            attr.addFlashAttribute("fail", "Profissionais não podem criar consultas!");
            return "redirect:/home";
        }
        if (data == null || hora == null) {
            attr.addFlashAttribute("fail", "Data e hora são obrigatórias!");
            return ("redirect:/consultas/cadastrar");
        }
        if (cdao.jaExisteId(consulta.getId())) {
            attr.addFlashAttribute("fail", "Já existe uma consulta com este id!");
            return ("redirect:/consultas/cadastrar");
        }
        if (consulta.getPaciente().getCpf() == consulta.getProfissional().getCpf()) {
            attr.addFlashAttribute("fail",
                    "Você também é um profissional. Você não pode marcar uma consulta consigo mesmo!");
            return ("redirect:/consultas/cadastrar");
        }
        LocalDateTime loc = LocalDateTime.of(data, hora);
        consulta.setDatahora(loc);
        if (consulta.getDatahora().isBefore(LocalDateTime.now())) {
            attr.addFlashAttribute("fail", "Você não pode marcar uma consulta no passado!");
            return ("redirect:/consultas/cadastrar");
        }

        cdao.save(consulta);
        attr.addFlashAttribute("success", "Consulta marcada com sucesso!");
        return ("redirect:/home");
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        String username = getUsername();
        if (username != null) {
            Paciente pac = pacdao.buscarPorCpf(username);
            if (pac == null) {
                Profissional pro = pdao.buscarPorCpf(username);
                map.addAttribute("consultas", cdao.findByCPF(pro.getCpf()));
            } else {
                map.addAttribute("consultas", cdao.findByCPF(pac.getCpf()));
            }
        }
        return ("/consulta/lista");
    }

    @PostMapping("/editar")
    public String alterar(Consulta consulta, RedirectAttributes attr) {
        cdao.save(consulta);
        attr.addFlashAttribute("success", "Consulta salva com sucesso!");
        return ("redirect:/consultas/listar");
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, ModelMap map) {
        List<Profissional> profissionais = pdao.findAll();
        map.addAttribute("profissionais", profissionais);

        List<Paciente> pacientes = pacdao.findAll();
        map.addAttribute("pacientes", pacientes);

        map.addAttribute("consulta", cdao.getReferenceById(id));
        return ("/consulta/cadastro");
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {
        cdao.deleteById(id);
        attr.addFlashAttribute("success", "Consulta excluída!");
        return ("redirect:/consultas/listar");
    }

    @Transactional
    @GetMapping("/tornarconcluida/{id}")
    public String concluir(@PathVariable("id") Integer id) {
        cdao.setConcluida(id);
        return ("redirect:/consultas/listar");
    }

    @GetMapping("/buscar/data")
    public String pesquisarPorData(@RequestParam(name = "dataini") LocalDateTime inicio,
            @RequestParam(name = "datafim") LocalDateTime fim,
            ModelMap map) {
        map.addAttribute("consultas", cdao.findByDatahora(inicio, fim));
        return ("/consulta/lista");
    }

    @GetMapping("/buscar/cpf")
    public String pesquisarPorCPF(@RequestParam(name = "cpf") String cpf,
            ModelMap map) {
        map.addAttribute("consultas", cdao.findByCPF(cpf));
        return ("/consulta/lista");
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
            return pdao.buscarPorCpf(username);
        }
        return null;
    }

}
