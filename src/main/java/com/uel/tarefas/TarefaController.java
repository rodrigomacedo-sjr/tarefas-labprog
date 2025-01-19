package com.uel.tarefas;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
