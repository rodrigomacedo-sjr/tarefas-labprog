-- source /src/main/resources/db/script.sql
CREATE DATABASE tarefasdb;

USE tarefasdb;

CREATE table tarefas (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(999), 
    prazo DATE,
    concluido BOOLEAN NOT NULL DEFAULT FALSE
);
