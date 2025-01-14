package com.gestaotarefas.repository;
 
import org.springframework.data.repository.CrudRepository;

import com.gestaotarefas.model.Tarefa;
 
public interface TarefaRepository extends CrudRepository<Tarefa,Integer> { }