# GIED API - Sistema de Gestão Integrada de Estoque e Distribuição

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![Oracle](https://img.shields.io/badge/Oracle-Database-red)
![License](https://img.shields.io/badge/license-MIT-blue)

Sistema completo de gerenciamento de estoque desenvolvido com Spring Boot, oferecendo controle detalhado de inventário, rastreamento de lotes com validade e gestão de usuários com autenticação segura.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Guia de Instalação e Execução](#guia-de-instalação-e-execução)
- [Uso da API](#uso-da-api)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## 🎯 Sobre o Projeto

O GIED API é um sistema robusto de gerenciamento de estoque que implementa controle FIFO (First In, First Out) para saída de produtos, rastreamento completo de lotes com datas de validade e um sistema de alertas para itens com estoque baixo. Ideal para empresas que necessitam de controle rigoroso sobre inventário, especialmente em setores como farmacêutico, hospitalar e alimentício.

## ✨ Funcionalidades

### Gerenciamento de Estoque
- ✅ Registro de entrada de itens com lote e data de validade
- ✅ Registro de saída com controle FIFO automático
- ✅ Consulta detalhada de estoque por item
- ✅ Listagem completa do inventário
- ✅ Alertas de estoque baixo
- ✅ Rastreamento completo de movimentações

### Gestão de Usuários
- ✅ Cadastro de usuários com tipos (ADMIN/DEFAULT)
- ✅ Autenticação segura com BCrypt
- ✅ Alteração de senha
- ✅ Sistema de login
- ✅ Gerenciamento de perfis

### Controle de Lotes
- ✅ Vínculo de lotes com itens
- ✅ Controle de validade
- ✅ Rastreamento de quantidades por lote
- ✅ Histórico de movimentações

## 🚀 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **Spring Web** - Desenvolvimento de APIs REST
- **Spring Security Crypto** - Criptografia de senhas
- **Oracle Database** - Banco de dados
- **JDBC** - Acesso a dados
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação automática da API
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes
- **Maven** - Gerenciamento de dependências

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/fiap/giedapi/
├── config/              # Configurações (conexão com BD)
├── controller/          # Controladores REST
├── domain/
│   ├── enums/          # Enumerações
│   ├── model/          # Entidades de domínio
│   └── vo/             # Value Objects
├── dto/
│   ├── mappers/        # Conversores DTO ↔ Model
│   ├── request/        # DTOs de entrada
│   └── response/       # DTOs de saída
├── exception/          # Exceções customizadas
├── repository/
│   ├── jdbc/           # Implementações JDBC
│   └── interfaces      # Contratos dos repositórios
└── service/            # Lógica de negócio
```

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java JDK 21** ou superior - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Oracle Database 11g** ou superior
- **SQL Developer** - [Download](https://www.oracle.com/database/sqldeveloper/technologies/download/)
- **Git** - [Download](https://git-scm.com/downloads)
- **IDE** de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Guia de Instalação e Execução

Siga este guia passo a passo para configurar e executar a aplicação:

### Passo 1: Clone o Repositório

```bash
git clone https://github.com/seu-usuario/gied-api.git
cd gied-api
```

### Passo 2: Configure o Arquivo application.properties

Navegue até `src/main/resources/` e crie/edite o arquivo `application.properties`:

```properties
# Nome da Aplicação
spring.application.name=gied-api

# Configurações do Banco de Dados Oracle
db.url=jdbc:oracle:thin:@SEU_HOST:1521:orcl
db.user=SEU_USUARIO
db.password=SUA_SENHA
```

#### 📝 Modelo de Variáveis

Substitua os valores conforme seu ambiente:

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SEU_HOST` | Endereço do servidor Oracle | `localhost` ou `oracle.fiap.com.br` |
| `SEU_USUARIO` | Nome de usuário do banco | `rm555500` |
| `SUA_SENHA` | Senha do banco de dados | `sua_senha_aqui` |

**Exemplo completo:**
```properties
spring.application.name=gied-api
db.url=jdbc:oracle:thin:@localhost:1521:orcl
db.user=rm558084
db.password=123456
```

### Passo 3: Execute os Scripts SQL no SQL Developer

1. **Abra o SQL Developer** e conecte-se ao seu banco de dados Oracle

2. **Crie as tabelas** executando os scripts da pasta **SQL**:

3. **Verifique a criação** executando:

```sql
-- Verificar tabelas criadas
SELECT table_name FROM user_tables 
WHERE table_name IN ('ITEM', 'LOTE', 'MOVIMENTACAO', 'USUARIO');

-- Verificar sequences
SELECT sequence_name FROM user_sequences 
WHERE sequence_name IN ('SEQ_LOTE', 'SEQ_MOV', 'SEQ_USUARIO');

-- Verificar dados de exemplo
SELECT * FROM ITEM;
```

### Passo 4: Compile e Execute a Aplicação

#### Opção A: Usando Maven via Terminal

```bash
# Compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

#### Opção B: Usando IDE (IntelliJ IDEA)

1. Abra o projeto na IDE
2. Aguarde o Maven baixar as dependências
3. Localize a classe `GiedApiApplication.java`
4. Clique com botão direito → `Run 'GiedApiApplication'`

#### Opção C: Usando IDE (Eclipse)

1. Importe o projeto como Maven Project
2. Aguarde a sincronização das dependências
3. Clique com botão direito no projeto → `Run As` → `Spring Boot App`

**Saída esperada no console:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.5.6)

2025-10-15 INFO  Starting GiedApiApplication...
2025-10-15 INFO  Tomcat started on port(s): 8080 (http)
2025-10-15 INFO  Started GiedApiApplication in 3.456 seconds
```

✅ **A aplicação estará rodando em:** `http://localhost:8080`

### Passo 5: Teste a API no Swagger

1. **Acesse o Swagger UI** no navegador:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Você verá a documentação interativa** com todos os endpoints disponíveis

3. **Teste os endpoints principais:**

#### 🔹 Testando Criação de Usuário

1. Expanda `POST /api/user`
2. Clique em **"Try it out"**
3. Insira o JSON:
   ```json
   {
     "nome": "Administrador",
     "login": "admin",
     "senha": "admin123",
     "tipoUsuario": "admin"
   }
   ```
4. Clique em **"Execute"**
5. Verifique a resposta **201 Created**

#### 🔹 Testando Login

1. Expanda `POST /api/user/login`
2. Clique em **"Try it out"**
3. Insira o JSON:
   ```json
   {
     "login": "admin",
     "senha": "admin123"
   }
   ```
4. Clique em **"Execute"**
5. Verifique a resposta **200 OK** com os dados do usuário

#### 🔹 Testando Listagem de Estoque

1. Expanda `GET /api/item`
2. Clique em **"Try it out"**
3. Clique em **"Execute"**
4. Verifique a resposta **200 OK** com a lista de itens

#### 🔹 Testando Entrada de Estoque

1. Expanda `POST /api/item/entrada`
2. Clique em **"Try it out"**
3. Insira o JSON:
   ```json
   {
     "id": 1,
     "quantidade": 100,
     "descricao": "Entrada inicial",
     "numeroLote": "LOTE001",
     "dataValidade": "31/12/2025"
   }
   ```
4. Clique em **"Execute"**
5. Verifique a resposta **201 Created**

#### 🔹 Testando Consulta de Estoque por ID

1. Expanda `GET /api/item/{id}`
2. Clique em **"Try it out"**
3. Insira o ID: `1`
4. Clique em **"Execute"**
5. Verifique a resposta **200 OK** com detalhes do item e seus lotes

#### 🔹 Testando Saída de Estoque

1. Expanda `POST /api/item/saida`
2. Clique em **"Try it out"**
3. Insira o JSON:
   ```json
   {
     "id": 1,
     "quantidade": 20
   }
   ```
4. Clique em **"Execute"**
5. Verifique a resposta **200 OK**

#### 🔹 Testando Estoque Baixo

1. Expanda `GET /api/item/estoque-baixo`
2. Clique em **"Try it out"**
3. Clique em **"Execute"**
4. Verifique a resposta **200 OK** (ou **404** se não houver itens com estoque baixo)

## 📚 Uso da API

### Endpoints Disponíveis

#### 👤 Usuários (`/api/user`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/user` | Criar novo usuário |
| POST | `/api/user/login` | Realizar login |
| GET | `/api/user/{id}` | Buscar usuário por ID |
| PUT | `/api/user/alterar-senha/{id}` | Alterar senha |
| DELETE | `/api/user/{id}` | Deletar usuário |

#### 📦 Itens (`/api/item`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/item` | Listar todo estoque |
| GET | `/api/item/{id}` | Buscar item por ID |
| POST | `/api/item/entrada` | Registrar entrada |
| POST | `/api/item/saida` | Registrar saída |
| GET | `/api/item/estoque-baixo` | Listar estoque baixo |

## 🧪 Testes

Execute os testes unitários:

```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=ItemServiceTests
mvn test -Dtest=UsuarioServiceTests

# Com relatório de cobertura
mvn test jacoco:report
```

## 🐛 Solução de Problemas

### Erro de Conexão com Banco

```
Erro: Could not create connection to database server
```

**Solução:**
- Verifique se o Oracle está rodando
- Confirme as credenciais no `application.properties`
- Teste a conexão no SQL Developer

### Erro de Porta em Uso

```
Erro: Port 8080 is already in use
```

**Solução:**
- Altere a porta no `application.properties`:
  ```properties
  server.port=8081
  ```

### Erro ao Executar Scripts SQL

```
Erro: Table or view does not exist
```

**Solução:**
- Execute os scripts na ordem correta
- Verifique permissões do usuário no banco

## 📖 Documentação da API

Documentação completa disponível em:
```
http://localhost:8080/swagger-ui.html
```

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT.

## 👥 Autores

- **Giulia Rocha Barbizan Alves** 

## 📧 Contato

Para dúvidas, abra uma issue no GitHub.
