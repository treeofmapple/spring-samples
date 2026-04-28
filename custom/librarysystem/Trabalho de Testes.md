# Plano de Teste: Aplicação Sistema de Biblioteca

**Sistema de Biblioteca**  
_Versão: R1.0_

## Histórico das Alterações

|Data|Versão|Descrição|Autor(a)|
|---|---|---|---|

## 1 - Introdução

A Aplicação Sistema de Biblioteca gerencia usuários, livros e itens de livros, oferecendo diversas funcionalidades, incluindo operações CRUD e cenários complexos de persistência de dados. Este plano de teste cobre testes de integração e de persistência de dados para verificar a funcionalidade da aplicação e a integridade dos dados.

#### Este plano de teste inclui:

1. **Testes de Persistência de Dados** - Verifica a inserção, recuperação, atualização e exclusão de dados no banco de dados.
2. **Testes de Integração do Controlador de Usuário** - Verifica os endpoints da API REST relacionados às operações de Usuário.

## 2 - Ambiente de Teste

- **Framework**: Java Spring Boot
- **Frameworks de Teste**: JUnit 5, AssertJ, Spring Boot Test, MockMvc, Swagger UI
- **Biblioteca de Dados Mock**: `net.datafaker.Faker` para gerar dados de teste aleatórios
- **Ferramentas**: ObjectMapper para manipulação de JSON

## 3 - Requisitos Funcionais a Testar

Esses requisitos verificam a capacidade do sistema de manipular dados de forma persistente, garantindo que os dados sejam salvos, recuperados, atualizados e excluídos corretamente no banco de dados.

- **RF001 - Inserção de Dados**
    - O sistema deve permitir a inserção de registros no banco de dados para as entidades `User`, `Book` e `BookItem`.
    - Cada entidade `BookItem` deve ser capaz de armazenar um status específico (ex.: `SOLD`, `RENT`, `AVAILABLE`, `RETURNED`) com informações de data de aluguel, quando aplicável.
- **RF002 - Recuperação de Dados**
    - O sistema deve permitir a recuperação de todos os registros para as entidades `User`, `Book` e `BookItem`.
    - A recuperação de dados deve retornar a quantidade correta de registros, garantindo que todos os dados inseridos estão acessíveis.
- **RF003 - Atualização de Dados**
    - O sistema deve permitir a atualização dos dados existentes para as entidades `User`, `Book` e `BookItem`.
    - Alterações como nome, email, senha e idade do `User`, e título, autor, quantidade e preço do `Book` devem ser atualizáveis e persistidas no banco de dados.
    - Cada `BookItem` deve poder atualizar suas referências a `User` e `Book` associadas.
- **RF004 - Exclusão de Dados**
    - O sistema deve permitir a exclusão de registros das entidades `User`, `Book` e `BookItem`.
    - Após a exclusão, os registros não devem mais estar presentes no banco de dados.

### Requisitos de API REST para Operações com Usuários

Esses requisitos garantem que os endpoints da API REST relacionados aos usuários funcionem conforme o esperado, permitindo operações CRUD de maneira controlada.

- **RF005 - Criação de Usuário via API**
    - O sistema deve permitir a criação de um usuário por meio do endpoint `/users/create`.
    - A resposta deve incluir um status HTTP `201 Created` e o ID do usuário criado.
- **RF006 - Recuperação de Usuário por ID**
    - O sistema deve permitir a recuperação de um usuário específico com base no ID por meio do endpoint `/users/get/{id}`.
    - A resposta deve retornar o status HTTP `200 OK` e incluir as informações corretas do usuário correspondente ao ID fornecido.
- **RF007 - Atualização de Usuário via API**
    - O sistema deve permitir a atualização dos dados de um usuário existente por meio do endpoint `/users/update/{id}`.
    - A atualização deve ser confirmada com o status HTTP `204 No Content`, sem retornar dados na resposta.
- **RF008 - Exclusão de Usuário via API**
    - O sistema deve permitir a exclusão de um usuário existente por meio do endpoint `/users/delete/{id}`.
    - A exclusão deve ser confirmada com o status HTTP `204 No Content` e o usuário não deve mais estar presente no banco de dados.

---

### Resumo dos Requisitos

|Código|Descrição|
|---|---|
|RF001|O sistema permite a inserção de dados para as entidades `User`, `Book`, e `BookItem`.|
|RF002|O sistema permite a recuperação de todos os registros para `User`, `Book`, e `BookItem`.|
|RF003|O sistema permite a atualização dos dados de `User`, `Book`, e `BookItem`.|
|RF004|O sistema permite a exclusão de dados de `User`, `Book`, e `BookItem`.|
|RF005|O sistema permite a criação de usuário via API no endpoint `/users/create`.|
|RF006|O sistema permite a recuperação de um usuário por ID no endpoint `/users/get/{id}`.|
|RF007|O sistema permite a atualização de um usuário via API no endpoint `/users/update/{id}`.|
|RF008|O sistema permite a exclusão de um usuário via API no endpoint `/users/delete/{id}`.|

### Casos de Uso

| Identificador do caso de uso | Nome do caso de uso              |
| ---------------------------- | -------------------------------- |
| **UC1**                      | Inserir dados no sistema         |
| **UC2**                      | Recuperar dados do sistema       |
| **UC3**                      | Atualizar dados no sistema       |
| **UC4**                      | Excluir dados do sistema         |
| **UC5**                      | Criar usuário via API            |
| **UC6**                      | Recuperar usuário por ID via API |
| **UC7**                      | Atualizar usuário via API        |
| **UC8**                      | Excluir usuário via API          |

## 4 - Tipos de Teste

Esta seção descreve os tipos de testes escolhidos para cada iteração do projeto. Serão utilizados diferentes tipos de testes para verificar a funcionalidade e a integridade dos componentes do sistema, conforme os requisitos da aplicação. Os testes foram selecionados com base no guia de testes, levando em consideração as características da aplicação e os recursos disponíveis.

### 4.1 - Teste de API REST

Teste para verificar o funcionamento correto das APIs REST utilizando o Swagger ([http://localhost:8000/api/swagger-ui/index.html](http://localhost:8000/api/swagger-ui/index.html)).

<table> <tr> <th>Objetivo</th> <th>Validar que a API REST responde corretamente às requisições, verificando o funcionamento dos endpoints para criar, recuperar, atualizar e excluir dados.</th> </tr> <tr> <td>Técnica</td> <td>Manual (x) Automática (x)</td> </tr> <tr> <td>Estágio do teste</td> <td>Integração ( ) Sistema (x) Unidade ( ) Aceitação ( )</td> </tr> <tr> <td>Abordagem do teste</td> <td>Caixa branca ( ) Caixa preta (x)</td> </tr> <tr> <td>Responsável(is)</td> <td>Programador(es) ou equipe de testes</td> </tr> </table>

### 4.2 - Teste de Persistência de Dados

Este teste verifica a integridade dos dados e do banco de dados, assegurando que as informações são mantidas mesmo após um desligamento súbito do programa.

<table> <tr> <th>Objetivo</th> <th>Verificar se os dados permanecem íntegros e disponíveis após um desligamento inesperado do programa, garantindo que o sistema consegue se recuperar e retomar do ponto de falha.</th> </tr> <tr> <td>Técnica</td> <td>Manual (x) Automática (x)</td> </tr> <tr> <td>Estágio do teste</td> <td>Integração ( ) Sistema (x) Unidade ( ) Aceitação ( )</td> </tr> <tr> <td>Abordagem do teste</td> <td>Caixa branca ( ) Caixa preta (x)</td> </tr> <tr> <td>Responsável(is)</td> <td>Programador(es) ou equipe de testes</td> </tr> </table>

### 4.3 - Teste de Exceção

Este teste verifica o tratamento de exceções e a resiliência da aplicação diante de entradas inesperadas ou falhas nas operações.

<table> <tr> <th>Objetivo</th> <th>Testar o tratamento de exceções no sistema, garantindo que ele responde de forma controlada a entradas e operações inválidas ou inesperadas.</th> </tr> <tr> <td>Técnica</td> <td>Manual (x) Automática (x)</td> </tr> <tr> <td>Estágio do teste</td> <td>Integração (x) Sistema ( ) Unidade ( ) Aceitação ( )</td> </tr> <tr> <td>Abordagem do teste</td> <td>Caixa branca (x) Caixa preta (x)</td> </tr> <tr> <td>Responsável(is)</td> <td>Programador(es) ou equipe de testes</td> </tr> </table>

### 4.4 - Teste de Integração dos Componentes

Este teste assegura que os diferentes componentes do sistema estão corretamente integrados, verificando a interação entre classes e métodos em uma sequência de ações do programa.

<table> <tr> <th>Objetivo</th> <th>Verificar se os componentes do sistema (classes e métodos) interagem corretamente, assegurando a funcionalidade em uma sequência de ações integrada.</th> </tr> <tr> <td>Técnica</td> <td>Manual (x) Automática (x)</td> </tr> <tr> <td>Estágio do teste</td> <td>Integração (x) Sistema ( ) Unidade ( ) Aceitação ( )</td> </tr> <tr> <td>Abordagem do teste</td> <td>Caixa branca ( ) Caixa preta (x)</td> </tr> <tr> <td>Responsável(is)</td> <td>Programador(es) ou equipe de testes</td> </tr> </table>
