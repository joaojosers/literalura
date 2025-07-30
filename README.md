
# 📚 LiterAlura

Uma aplicação Java Spring Boot para consulta e gerenciamento de livros utilizando a API Gutendx (Project Gutenberg) com persistência em banco de dados PostgreSQL.

## 🎯 Sobre o Projeto

O **LiterAlura** é uma aplicação de linha de comando que permite buscar, cadastrar e gerenciar informações sobre livros e autores. A aplicação consome dados da API Gutendx, que disponibiliza o acervo do Project Gutenberg, e armazena as informações em um banco de dados PostgreSQL para consultas futuras.

## ✨ Funcionalidades

- 🔍 **Buscar livro pelo título** - Consulta a API Gutendx e salva no banco
- 📖 **Listar livros registrados** - Exibe todos os livros salvos localmente
- 👨‍💼 **Listar autores registrados** - Mostra todos os autores cadastrados
- 📅 **Listar autores vivos em um ano específico** - Filtra autores por período
- 🌍 **Listar livros por idioma** - Filtra livros por idioma (PT, EN, ES, FR)
- 📊 **Gerar estatísticas de downloads** - Análise estatística dos downloads
- 🏆 **Top 10 livros mais baixados** - Ranking dos livros populares
- 🔎 **Buscar autor por nome** - Busca específica de autores
- 📆 **Listar autores por ano de nascimento** - Filtro por nascimento
- ⚰️ **Listar autores por ano de falecimento** - Filtro por falecimento
- 💾 **Persistência de dados** - Armazena informações no PostgreSQL

## 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Jackson** - Serialização/deserialização JSON
- **Maven** - Gerenciamento de dependências
- **Gutendx API** - Fonte de dados dos livros

## 📁 Estrutura do Projeto

```
src/main/java/br/com/alura/literalura/
├── LiteraluraApplication.java          # Classe principal
├── model/                              # Modelos de dados
│   ├── Autor.java                      # Entidade Autor (JPA)
│   ├── Livro.java                      # Entidade Livro (JPA)
│   ├── DadosAutor.java                 # DTO para API
│   ├── DadosLivro.java                 # DTO para API
│   └── ResultadoBusca.java             # DTO de resposta da API
├── repository/                         # Repositórios JPA
│   ├── AutorRepository.java            # Operações de Autor
│   └── LivroRepository.java            # Operações de Livro
├── service/                            # Camada de serviços
│   ├── ConsumoApi.java                 # Cliente HTTP
│   ├── IConverteDados.java             # Interface de conversão
│   └── ConverteDados.java              # Conversão JSON ↔ Objetos
└── principal/                          # Interface do usuário
    └── Principal.java                  # Menu principal
```

## 🚀 Como Executar

### 📋 Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **PostgreSQL 12** ou superior
- **Git**

### 🔧 Configuração

1. **Clone o repositório:**
```bash
git clone https://github.com/seu-usuario/literalura.git
cd literalura
```

2. **Configure o PostgreSQL:**
```sql
-- Criar banco de dados
CREATE DATABASE literalura;
```

3. **Configure o `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

4. **Execute a aplicação:**
```bash
mvn spring-boot:run
```

## 🎮 Como Usar

Ao executar a aplicação, você verá o menu interativo:

```
Escolha o número de sua opção:
1 - buscar livro pelo título
2 - listar livros registrados
3 - listar autores registrados
4 - listar autores vivos em um determinado ano
5 - listar livros em um determinado idioma
6 - Gerar estatísticas de downloads
7 - Listar Top 10 livros mais baixados
8 - Buscar autor por nome
9 - Listar autores por ano de nascimento
10 - Listar autores por ano de falecimento
0 - sair
```

### 📖 Exemplos de Uso

**Funcionalidades Básicas:**
- **Opção 1:** Digite o título do livro (ex: "Dom Casmurro")
- **Opção 2:** Lista todos os livros salvos no banco
- **Opção 3:** Mostra todos os autores cadastrados
- **Opção 4:** Informe um ano para ver autores vivos (ex: 1850)
- **Opção 5:** Escolha o idioma: `pt`, `en`, `es`, ou `fr`

**Funcionalidades Avançadas:**
- **Opção 6:** Visualiza estatísticas gerais de downloads
- **Opção 7:** Ranking dos livros mais populares
- **Opção 8:** Busca por nome do autor (ex: "Shakespeare")
- **Opção 9:** Filtra autores nascidos a partir de  ano específico
- **Opção 10:** Filtra autores falecidos até um ano específico

## 🌐 API Utilizada

### Gutendx API
- **Base URL:** `https://gutendx.com/books/`
- **Busca:** `https://gutendx.com/books/?search={titulo}`
- **Documentação:** [Gutendx Documentation](https://gutendx.com/)

### Exemplo de Resposta JSON:
```json
{
  "count": 76395,
  "results": [
    {
      "id": 2701,
      "title": "Moby Dick; Or, The Whale",
      "authors": [
        {
          "name": "Melville, Herman",
          "birth_year": 1819,
          "death_year": 1891
        }
      ],
      "languages": ["en"],
      "download_count": 104393
    }
  ]
}
```

## 🏗️ Arquitetura

### Padrões Implementados:
- **Repository Pattern** - Abstração de acesso a dados
- **DTO Pattern** - Transferência de dados da API
- **Service Layer** - Lógica de negócio separada
- **Dependency Injection** - Gerenciamento de dependências

### Relacionamentos JPA:
- **Autor** `1:N` **Livro** - Um autor pode ter vários livros
- **Livro** `N:1` **Autor** - Cada livro tem um autor principal

### Consultas Implementadas:
- Busca de autores vivos em período específico
- Rankings e estatísticas de downloads
- Filtros por ano de nascimento/falecimento
- Buscas textuais por nome de autor

## 🗃️ Banco de Dados

### Tabelas Criadas:
```sql
-- Tabela de Autores
CREATE TABLE autores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE,
    ano_nascimento INTEGER,
    ano_falecimento INTEGER
);

-- Tabela de Livros
CREATE TABLE livros (
    id BIGINT PRIMARY KEY,
    titulo VARCHAR(255) UNIQUE,
    idioma VARCHAR(2),
    numero_downloads INTEGER,
    autor_id BIGINT REFERENCES autores(id)
);
```

### Principais Consultas:
```sql
-- Autores vivos em um ano específico
SELECT * FROM autores 
WHERE ano_nascimento <= ? AND (ano_falecimento >= ? OR ano_falecimento IS NULL);

-- Top 10 livros mais baixados
SELECT * FROM livros ORDER BY numero_downloads DESC LIMIT 10;

-- Estatísticas de downloads
SELECT COUNT(*), SUM(numero_downloads), AVG(numero_downloads) FROM livros;

-- Busca por nome de autor
SELECT * FROM autores WHERE nome ILIKE '%?%';
```


## 📞 Contato

- **Desenvolvedor:** João José Rocha de Sousa
- **Email:** joaojosers@hotmail.com
- **LinkedIn:** [(https://www.linkedin.com/in/joao-jose-sousa-developer/]

