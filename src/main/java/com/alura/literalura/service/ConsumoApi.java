package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsumoApi {

    private final HttpClient client;

    public ConsumoApi() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public String obterDados(String endereco) {
        try {
            if (endereco == null || endereco.isBlank()) {
                System.out.println("⚠️ URL inválida para consulta da API.");
                return null;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .GET()
                    .timeout(Duration.ofSeconds(20))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("⚠️ Erro da API Gutendex: HTTP " + response.statusCode());
                return null;
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("❌ Falha ao conectar à API: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado ao buscar dados da API: " + e.getMessage());
        }
        return null;
    }
}

