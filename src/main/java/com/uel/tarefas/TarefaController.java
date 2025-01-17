package com.uel.tarefas;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
 
@Controller
public class TarefaController {
   @Autowired
   com.uel.tarefas.TarefaRepository tarefaRepository;
 
   @GetMapping("/nova-tarefa")
   public String mostrarFormNovaTarefa(Tarefa tarefa) {
       return "/nova-tarefa";
   }

   @GetMapping(value={"/index", "/"})
   public String mostrarListaTarefas(Model model) {
      model.addAttribute("tarefas", tarefaRepository.findAll());
      return "index";
   }

 
   @PostMapping("/adicionar-tarefa")
   public String adicionarTarefa(@Valid Tarefa tarefa, 
                                         BindingResult result) {
       if (result.hasErrors()) {
           return "/nova-tarefa";
       }
 
       tarefaRepository.save(tarefa);
       return "redirect:/nova-tarefa";
   }
}
