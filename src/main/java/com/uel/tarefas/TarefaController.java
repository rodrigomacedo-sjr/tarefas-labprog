package com.uel.tarefas;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
 
@Controller
public class TarefaController {
    @Autowired
    com.uel.tarefas.TarefaRepository tarefaRepository;

    private static final String SESSION_FILTROS = "sessionFiltros";

    private boolean aplicarFiltros(Tarefa tarefa, HashMap<String, Object> filtros) {
        if (filtros.containsKey("status") && !filtros.get("status").equals("")) {
            String status = (String) filtros.get("status");
            if ("concluido".equalsIgnoreCase(status) && !tarefa.getConcluido()) return false;
            else if ("pendente".equalsIgnoreCase(status) && tarefa.getConcluido()) return false;
        }


        if (filtros.containsKey("prazo")) {
            String prazoFiltro = (String) filtros.get("prazo"); 
            String prazoTarefa = tarefa.getPrazo() != null ? tarefa.getPrazo().toString() : "";

            if (prazoFiltro != null && !prazoFiltro.isEmpty() && !prazoTarefa.contains(prazoFiltro)) {
                return false;
            }
        }

        if (filtros.containsKey("chave")) {
            String chave = (String) filtros.get("chave");
            if (chave != null && !chave.isEmpty()) {
                String titulo = tarefa.getTitulo() != null ? tarefa.getTitulo().toLowerCase() : "";
                String descricao = tarefa.getDescricao() != null ? tarefa.getDescricao().toLowerCase() : "";
                if (!titulo.contains(chave.toLowerCase()) && !descricao.contains(chave.toLowerCase())) return false;
            }
        }

        return true;
    }
 
    @GetMapping("/nova-tarefa")
    public String mostrarFormNovaTarefa(Tarefa tarefa) {
        return "/nova-tarefa";
    }

    @GetMapping(value={"/index", "/"})
    public String mostrarListaTarefas(HttpServletRequest request, Model model) {
        HashMap<String, Object> filtros = (HashMap<String, Object>) request.getSession().getAttribute("SESSION_FILTROS");

        List<Tarefa> tarefas = new ArrayList<>();
        for (Tarefa tarefa : tarefaRepository.findAll()) {
            if (filtros == null || filtros.isEmpty() || aplicarFiltros(tarefa, filtros)) {
                tarefas.add(tarefa);
            }
        }

        model.addAttribute("tarefas", tarefas);

        return "index";
    }

    @PostMapping("/filtrar")
    public String filtrarTarefas(HttpServletRequest request) {
        String status = request.getParameter("status_filtro");
        String prazo = request.getParameter("prazo_filtro");
        String chave = request.getParameter("chave_filtro");

        HashMap<String, Object> filtros = (HashMap<String, Object>) request.getSession().getAttribute("SESSION_FILTROS");

        if (filtros == null) {
            filtros = new HashMap<>();
        }

        if (status != null && !status.isEmpty()) filtros.put("status", status);
        else filtros.remove("status");
        if (prazo != null && !prazo.isEmpty()) filtros.put("prazo", prazo);
        else filtros.remove("prazo");
        if (chave != null && !chave.isEmpty()) filtros.put("chave", chave);
        else filtros.remove("chave");

        request.getSession().setAttribute("SESSION_FILTROS", filtros);

        return "redirect:/index";
    }
 
    @PostMapping("/adicionar-tarefa")
    public String adicionarTarefa(@Valid Tarefa tarefa, BindingResult result) {
        if (result.hasErrors()) {
            return "/nova-tarefa";
        }
        tarefaRepository.save(tarefa);
        return "redirect:/nova-tarefa";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model) {
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                    "O id da tarefa é inválido: " + id));

        model.addAttribute("tarefa", tarefa);
        return "atualizar-tarefa";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarTarefa(@PathVariable("id") int id, @Valid Tarefa tarefa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            tarefa.setId(id);
            return "atualizar-tarefa";
        }

        tarefaRepository.save(tarefa);
        return "redirect:/index";
    }

    @GetMapping("/remover/{id}")
    public String removerTarefa(@PathVariable("id") int id) {
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id da tarefa é inválido: " + id));
        tarefaRepository.delete(tarefa);
        return "redirect:/index";
    }
}
