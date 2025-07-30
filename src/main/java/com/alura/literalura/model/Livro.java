package com.alura.literalura.model;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DadosLivro;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    private Autor autor;

    private String idioma;

    private Integer numeroDownloads;

    public Livro() {}

    public Livro(DadosLivro dadosLivro) {
        this.id = dadosLivro.id();
        this.titulo = dadosLivro.titulo();
        this.idioma = dadosLivro.idiomas().get(0);
        this.numeroDownloads = dadosLivro.numeroDownloads();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Integer getNumeroDownloads() { return numeroDownloads; }
    public void setNumeroDownloads(Integer numeroDownloads) { this.numeroDownloads = numeroDownloads; }

    @Override
    public String toString() {
        return String.format("""
                ----------- LIVRO -----------
                Título: %s
                Autor: %s
                Idioma: %s
                Número de downloads: %d
                ----------------------------
                """, titulo, autor != null ? autor.getNome() : "Desconhecido", idioma, numeroDownloads);
    }
}