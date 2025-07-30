package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    //  Usado para buscar um autor pelo nome (retorna Optional)
    Optional<Autor> findByNomeContainingIgnoreCase(String nome);

    // Usado no menu "Buscar autor por nome" (retorna lista ordenada)
    List<Autor> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    // Listar autores que estavam vivos em determinado ano
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosNoAno(@Param("ano") Integer ano);

    // Listar autores nascidos a partir de um ano específico
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento >= :ano")
    List<Autor> findAutoresNascidosDepoisDe(@Param("ano") Integer ano);

    // Listar autores falecidos até um ano específico
    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento <= :ano")
    List<Autor> findAutoresFalecidosAntesDe(@Param("ano") Integer ano);

    // Listar autores por ano exato de nascimento
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento = :ano")
    List<Autor> listarAutoresPorAnoNascimento(@Param("ano") Integer ano);

    // Listar autores por ano exato de falecimento
    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento = :ano")
    List<Autor> listarAutoresPorAnoFalecimento(@Param("ano") Integer ano);
}





