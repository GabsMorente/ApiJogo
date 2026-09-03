# ApiJogo

API REST para cadastro, avaliação e consulta de jogos, desenvolvida com Java e
Spring Boot.

## Funcionalidades

- cadastro, consulta, atualização e exclusão de jogos;
- validação dos dados recebidos;
- consulta por nome, tipo ou nota mínima;
- respostas de erro padronizadas;
- persistência com Spring Data JPA;
- banco H2 para desenvolvimento e PostgreSQL em produção;
- testes automatizados das camadas web e de serviço.

## Tecnologias

- Java 21;
- Spring Boot 3;
- Spring Web;
- Spring Data JPA;
- Bean Validation;
- H2 e PostgreSQL;
- JUnit 5, Mockito e MockMvc;
- Maven.

## Executando localmente

### Requisitos

- Java 21 instalado;
- Git.

Clone o projeto e entre na pasta:

```bash
git clone https://github.com/GabsMorente/ApiJogo.git
cd ApiJogo
```

No Linux ou macOS, execute:

```bash
./mvnw spring-boot:run
```

No Windows, execute:

```powershell
mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

## Executando os testes

No Linux ou macOS:

```bash
./mvnw test
```

No Windows:

```powershell
mvnw.cmd test
```

## Modelo de jogo

| Campo | Tipo | Obrigatório | Regras |
|---|---|---:|---|
| `nome` | texto | Sim | Entre 1 e 100 caracteres |
| `tipo` | texto | Sim | Entre 1 e 50 caracteres |
| `nota` | inteiro | Sim | Valor entre 0 e 10 |
| `review` | texto | Não | Até 1.000 caracteres |

Exemplo de requisição:

```json
{
  "nome": "Celeste",
  "tipo": "Plataforma",
  "nota": 10,
  "review": "Jogabilidade precisa e ótima trilha sonora."
}
```

## Endpoints

### Listar jogos

```http
GET /jogos
```

### Consultar jogo por ID

```http
GET /jogos/{id}
```

Quando o ID não existe, a API responde com o status `404 Not Found`.

### Criar jogo

```http
POST /jogos
Content-Type: application/json
```

Corpo:

```json
{
  "nome": "Hades",
  "tipo": "Roguelike",
  "nota": 9,
  "review": "Combate rápido e narrativa envolvente."
}
```

Uma criação bem-sucedida retorna `201 Created` e o cabeçalho `Location` com o
endereço do novo recurso.

### Atualizar jogo

```http
PUT /jogos/{id}
Content-Type: application/json
```

O corpo segue o mesmo formato usado na criação.

### Excluir jogo

```http
DELETE /jogos/{id}
```

Uma exclusão bem-sucedida retorna `204 No Content`. Caso o jogo não exista, a
API retorna `404 Not Found`.

### Filtrar jogos

Use somente um filtro por chamada:

```http
GET /jogos/buscar?nome=mine
GET /jogos/buscar?tipo=RPG
GET /jogos/buscar?notaMinima=8
```

A busca por nome aceita trechos e ignora diferenças entre letras maiúsculas e
minúsculas. A busca por tipo também ignora essas diferenças.

## Erros de validação

Dados inválidos retornam `400 Bad Request`. A propriedade `campos` informa o
motivo associado a cada campo:

```json
{
  "timestamp": "2026-09-03T12:00:00Z",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Um ou mais campos estão inválidos",
  "caminho": "/jogos",
  "campos": {
    "nome": "O nome do jogo é obrigatório",
    "nota": "A nota máxima é 10"
  }
}
```

## Variáveis de ambiente

| Variável | Valor padrão | Finalidade |
|---|---|---|
| `PORT` | `8080` | Porta HTTP da aplicação |
| `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:testdb` | URL de conexão do banco |
| `SPRING_DATASOURCE_USERNAME` | `sa` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | vazio | Senha do banco |
| `SPRING_JPA_SHOW_SQL` | `false` | Exibe comandos SQL no console |
| `H2_CONSOLE_ENABLED` | `false` | Habilita o console web do H2 |

Em produção, configure as credenciais por variáveis de ambiente. Não adicione
senhas ou tokens ao repositório.

## Aplicação publicada

- [Endpoint de jogos](https://apijogo-2.onrender.com/jogos)
- [Endpoint de login](https://apijogo-2.onrender.com/login)
