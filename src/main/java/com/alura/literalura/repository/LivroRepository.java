package com.alura.literalura.repository;

import com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Livro l WHERE l.idioma = :idioma")
    List<Livro> findByIdioma(String idioma);

    // Lista os 10 livros mais baixados
    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();
}
