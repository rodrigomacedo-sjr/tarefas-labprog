package com.uel.tarefas;
 
import org.springframework.data.repository.CrudRepository;
 
public interface TarefaRepository extends CrudRepository<Tarefa,Integer> { }