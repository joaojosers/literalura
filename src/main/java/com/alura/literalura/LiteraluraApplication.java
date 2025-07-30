package com.alura.literalura;

import io.github.cdimascio.dotenv.Dotenv;
import com.alura.literalura.principal.Principal;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		// Carrega o .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Exporta para o System Environment (usado pelo Spring Boot)
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(LiteraluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
	Principal principal = new Principal(livroRepository, autorRepository);
	principal.exibeMenu();
	}
}
