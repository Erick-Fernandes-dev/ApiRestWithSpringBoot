package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

//    Utilizando Query method
    List<Topico> findByCursoNome(String nomeCurso);

//    Utilizando JPQL(Java Persistence Query language)
//    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
//    List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

//    Ele é usado para criar consultas contra entidades para armazenar
//    em um banco de dados relacional.
}
