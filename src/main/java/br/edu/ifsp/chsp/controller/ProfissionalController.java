package br.edu.ifsp.chsp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifsp.chsp.model.dao.PacienteDAO;
import br.edu.ifsp.chsp.model.dao.ProfissionalDAO;
import br.edu.ifsp.chsp.model.domain.Paciente;
import br.edu.ifsp.chsp.model.domain.Profissional;
import br.edu.ifsp.chsp.model.domain.Usuario;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalDAO pdao;

    @Autowired
    private PacienteDAO pacdao;

    @GetMapping("/cadastrar")
    public String cadastrar(ModelMap map) {
        map.addAttribute("profissional", new Profissional());
        return ("/profissional/cadastro");
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Profissional profissional, @RequestParam("foto") MultipartFile foto,
            @RequestParam("cod") String cod, @RequestParam("reg") String reg, @RequestParam("uf") String uf,
            ModelMap map, RedirectAttributes attr) {
                
        if (pdao.jaExisteCpf(profissional.getCpf())) {
            attr.addFlashAttribute("fail", "Já existe um profissional cadastrado com este CPF!");
            return ("redirect:/profissionais/cadastrar");
        }
        if (pdao.jaExisteRegistro(profissional.getRegistro())) {
            attr.addFlashAttribute("fail", "Já existe um profissional cadastrado com este registro!");
            return ("redirect:/profissionais/cadastrar");
        }
        if (pdao.jaExisteEmail(profissional.getEmail())) {
            attr.addFlashAttribute("fail", "Já existe um profissional cadastrado com este email!");
            return ("redirect:/profissionais/cadastrar");
        }
        String prof = profissional.getProfissao();
        if ("".equals(profissional.getEspecializacao())) {
            profissional.setEspecializacao(null);
        } else if (!"Médico".equals(prof) && profissional.getEspecializacao() != null) {
            attr.addFlashAttribute("fail",
                    "Você não pode selecionar especialização médica se não é médico! é médico =");
            return ("redirect:/profissionais/cadastrar");
        }
        if ("".equals(uf)) {
            attr.addFlashAttribute("fail", "Selecione um estado (UF).");
        }
        if ("Médico".equals(prof) && !"CRM".equals(cod) || "Psicólogo".equals(prof) && !"CRP".equals(cod)
                || "Dentista".equals(prof) && !"CRO".equals(cod)) {
            attr.addFlashAttribute("fail", "Selecione o registro corretamente adequado à sua profissão.");
            return ("redirect:/profissionais/cadastrar");
        }
        if (!foto.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/uploads/", filename);
                Files.createDirectories(path.getParent());
                Files.copy(foto.getInputStream(), path);
                profissional.setPathFoto("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                map.addAttribute("message", "Falha ao salvar a foto.");
                return "/profissional/cadastro";
            }
        }
        String registro = cod + "/" + uf + reg;
        profissional.setRegistro(registro);
        if (pacdao.jaExisteCpf(profissional.getCpf())) {
            profissional.setNome(pacdao.buscarPorCpf(profissional.getCpf()).getNome());
            pdao.save(profissional);
            attr.addFlashAttribute("success", "O CPF que você digitou é o mesmo de um paciente. Portanto, o nome que você inseriu não foi salvo.");
            return ("redirect:/home");
        } else {
            pdao.save(profissional);
            attr.addFlashAttribute("success", "Sua conta foi cadastrada!");
            return ("redirect:/home");
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("profissionais", pdao.findAll());
        return ("/profissional/lista");
    }

    @PostMapping("/editar")
    public String alterar(Profissional profissional, RedirectAttributes attr) {
        pdao.save(profissional);
        attr.addFlashAttribute("success", "Seu perfil foi editado!");
        return ("redirect:/home");
    }

    @GetMapping("/editar/{cpf}")
    public String editar(@PathVariable("cpf") String cpf, ModelMap map) {
        map.addAttribute("profissional", pdao.getReferenceById(cpf));
        return ("/profissional/cadastro");
    }

    @GetMapping("/excluir/{cpf}")
    public String excluir(@PathVariable("cpf") String cpf, RedirectAttributes attr) {
        if (pdao.getReferenceById(cpf).getConsultas().isEmpty()) {
            pdao.deleteById(cpf);
            attr.addAttribute("success", "Seu perfil profissional foi excluído.");
            return ("redirect:/home");
        } else {
            attr.addAttribute("fail", "Profissional não pode ser excluído. Possui consultas!");
            return ("redirect:/profissionais/perfil/" + cpf);
        }
    }

    @GetMapping("/buscar/nome")
    public String pesquisarPorNome(@RequestParam(name = "nome") String nome, ModelMap map) {
        map.addAttribute("profissionais", pdao.findLikeNome(nome));
        return "/profissional/lista";
    }

    @GetMapping("/buscar/profissao")
    public String pesquisarPorProfissao(@RequestParam(name = "profissao") String profissao, ModelMap map) {
        map.addAttribute("profissionais", pdao.findLikeProfissao(profissao));
        return "/profissional/lista";
    }

    @GetMapping("/buscar/sexo")
    public String pesquisarPorSexo(@RequestParam(name = "sexo") char sexo, ModelMap map) {
        map.addAttribute("profissionais", pdao.findLikeSexo(sexo));
        return "/profissional/lista";
    }

    @GetMapping("/perfil")
    public String perfil(ModelMap map) {
        return ("/profissional/perfil");
    }

    @ModelAttribute("username")
    public String getUsername(){
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

/*
 * String crpReal = "admin";
 * String senReal = "senha";
 * public boolean login(String crp, String senha) {
 * boolean ret = false;
 * if (crp == crpReal && senha == senReal) {
 * ret = true;
 * }
 * return ret;
 * }
 */