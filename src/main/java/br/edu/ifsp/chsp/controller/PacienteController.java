package br.edu.ifsp.chsp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Usuario;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteDAO pdao;
    @Autowired
    private ProfissionalDAO prodao;

    @GetMapping("/cadastrar")
    public String cadastrar(ModelMap map) {
        map.addAttribute("paciente", new Paciente());
        return ("/paciente/cadastro");
    }

    @PostMapping("/salvar")
    public String salvar(Paciente paciente, @RequestParam("foto") MultipartFile foto, ModelMap map,
            RedirectAttributes attr) {
        if (pdao.jaExisteCpf(paciente.getCpf())) {
            attr.addFlashAttribute("fail", "Já existe um paciente cadastrado com esse CPF!");
            return ("redirect:/pacientes/cadastrar");
        }
        if (paciente.getDatanasc().isAfter(LocalDate.now().minusYears(18))) {
            attr.addFlashAttribute("fail",

                    "Não é possível cadastrar um usuário com idade menor do que 18 anos. Nesse caso, a consulta será feita pelo cpf de um responsável legal.");
            return ("redirect:/pacientes/cadastrar");
        }
        if (!foto.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/uploads/", filename);
                Files.createDirectories(path.getParent());
                Files.copy(foto.getInputStream(), path);
                paciente.setPathFoto("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                map.addAttribute("message", "Falha ao salvar a foto.");
                return "/paciente/cadastro";
            }
        }
        if (prodao.jaExisteCpf(paciente.getCpf())) {
            paciente.setNome(prodao.buscarPorCpf(paciente.getCpf()).getNome());
            pdao.save(paciente);
            attr.addFlashAttribute("success",
                    "O CPF que você digitou é o mesmo de um profissional. Portanto, o nome que você inseriu não foi salvo.");
            return ("redirect:/home");
        } else {
            pdao.save(paciente);
            attr.addFlashAttribute("success", "Seu perfil foi criado com sucesso!");
            return ("redirect:/home");
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("pacientes", pdao.findAll());
        return ("/paciente/lista");
    }

    @PostMapping("/editar")
    public String alterar(Paciente paciente, RedirectAttributes attr) {
        pdao.save(paciente);
        attr.addFlashAttribute("success", "Seu perfil foi editado!");
        return ("redirect:/pacientes/perfil/" + paciente.getCpf());
    }

    @GetMapping("/editar/{cpf}")
    public String editar(@PathVariable("cpf") String cpf, ModelMap map) {
        map.addAttribute("paciente", pdao.getReferenceById(cpf));
        return ("/paciente/cadastro");
    }

    @GetMapping("/excluir/{cpf}")
    public String excluir(@PathVariable("cpf") String cpf, RedirectAttributes attr) {
        if (pdao.getReferenceById(cpf).getConsultas().isEmpty()) {
            pdao.deleteById(cpf);
            attr.addAttribute("success", "Paciente excluído.");
            return ("/home");
        } else {
            attr.addAttribute("fail", "Paciente não pode ser excluído. Possui consultas!");
            return ("redirect:/pacientes/perfil/" + cpf);
        }
    }

    @GetMapping("/perfil")
    public String perfil(ModelMap map) {
        return ("/paciente/perfil");
    }

    @ModelAttribute("paciente")
    public Paciente getPaciente() {
        Paciente p = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            p = pdao.buscarPorCpf(username);
        }
        return p;
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
            Paciente pac = pdao.buscarPorCpf(username);
            if (pac != null) {
                return pac;
            }
            return pdao.buscarPorCpf(username);
        }
        return null;
    }
}
