# Cobransys - Ferramenta de Cálculo Financeiro

Projeto desenvolvido como parte de estudos na faculdade, com foco em automatizar e simplificar cálculos recorrentes do setor financeiro de faturamento de clientes. A aplicação oferece uma interface gráfica interativa, permitindo que atendentes realizem operações com maior agilidade e precisão, conforme regras de negócio específicas da empresa.

---

## Objetivo

Facilitar, por meio de uma GUI intuitiva, a realização de cálculos simples e frequentes no atendimento ao cliente. O sistema gera resultados formatados automaticamente, prontos para cópia ou uso direto.

Embora tecnicamente simples, o volume e a recorrência dessas operações justificam a automatização.

---

## Estrutura do Projeto

```plaintext
Cobransys/
├── Cobran/                     # Pasta raiz
│   ├── src/                    # Código-fonte principal (.java)
│   │   ├── App.java
│   │   ├── controllers.java    # Controladores JavaFX
│   │   ├── fxml/               # Interfaces gráficas (FXML - Scene Builder)
│   │   ├── estilo/             # Folhas de estilo CSS
│   │    
│   ├── bin/                    # Arquivos compilados (.class)
│   ├── lib/                    # Bibliotecas externas (.jar)
├── .vscode/                    # Configurações do VSCode (launch.json etc.)
```

### Tecnologias Utilizadas

| Tecnologia    | Versão                                   |
| ------------- | ---------------------------------------- |
| Java          | 23.0.2 (2025-01-21)                      |
| JavaFX        | 23.0.2                                   |
| Scene Builder | Compatível com JavaFX 23.x               |
| Editor        | Visual Studio Code (Java Extension Pack) |

---

## Arquitetura do Código

* `App.java`: Classe principal da aplicação. Responsável por iniciar o JavaFX, carregar a primeira interface (`FXML`).

* `CalculoControllerBase.java`: Superclasse de apoio que fornece infraestrutura comum aos controladores de cálculo. Centraliza:

  * Validação e formatação de entradas do usuário
  * Conversão e coleta de dados dos campos da interface
  * Lógica reutilizável de controle e fluxo

* Controladores especializados:

  * `MultaController.java`
  * `RenegociarController.java`
  * `ProporcionalController.java`

  Cada um desses controladores **herda de `CalculoControllerBase`** e implementa os cálculos conforme as regras de negócio da empresa, relacionados à cobrança de multas, renegociações ou valores proporcionais.

* `ResultadoController.java`: Classe auxiliar responsável por **exibir o resultado final ao usuário**. Possui três métodos sobrecarregados (`setDados`) que recebem diferentes combinações de dados e formatam a mensagem para exibição na interface.

* `fxml/`: Contém os arquivos `.fxml` gerados no Scene Builder. Cada interface é vinculada a um controlador correspondente.

* `estilo/`: Contém os arquivos `.css` aplicados às interfaces JavaFX, utilizados para padronizar visualmente a aplicação (cores, fontes, espaçamentos etc.).

---

## 🧹 Dependências

A aplicação utiliza **Maven** como gerenciador de dependências, o que simplifica a inclusão automática das bibliotecas necessárias, especialmente o **JavaFX SDK**.

O arquivo `pom.xml` já está configurado para incluir os módulos essenciais do JavaFX:

```xml
<dependencies>
  <dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>23.0.2</version>
  </dependency>
  <dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>23.0.2</version>
  </dependency>
</dependencies>
```

> O Maven irá automaticamente baixar e gerenciar essas dependências quando o projeto for compilado ou empacotado via `mvn install` ou `mvn package`.

Para garantir a execução correta com JavaFX, também são definidos plugins específicos para **compilação**, **empacotamento com jpackage** e **criação do runtime personalizado**.

---

## Execução

### Usando o Visual Studio Code

* Certifique-se de que a extensão *Java Extension Pack* está instalada.
* O Maven cuidará das dependências, e os scripts `.bat` facilitarão tanto a execução quanto o empacotamento.

### Compilar com Maven:

```bash
mvn clean compile
```

### Executar com Maven:

```bash
mvn javafx:run
```
---

## Scripts `.bat` para Empacotamento e Execução

Os arquivos `.bat` utilizados para empacotar e executar o **Cobransys** foram organizados em uma **pasta externa** à raiz do projeto, localizada no mesmo diretório do projeto principal (`Cobransys/`). Essa separação visa facilitar a manutenção e a reutilização dos scripts em diferentes ambientes.

### `runtime.bat` – Empacotamento com Maven + Criador de Instalador

Esse script realiza o processo completo de:

1. Compilação e empacotamento da aplicação via **Maven** (`mvn package`), gerando o arquivo `.jar`.
2. Criação de um instalador `.msix` utilizando o `jpackage`, com base no JDK que oferece suporte a essa ferramenta.

> **Pré-requisitos**:
>
> * JDK 17 ou superior (com `jpackage` disponível)
> * Maven instalado e configurado corretamente
> * `pom.xml` corretamente preenchido com plugins JavaFX e `jpackage`

---

### `executar.bat` – Execução Direta Sem Instalação

Este script permite **executar o sistema sem instalá-lo**, diretamente a partir do `.jar` gerado pelo Maven. É últil para:

* Realizar testes rápidos
* Verificar se há erros de inicialização ou carregamento da interface
* Executar o programa de forma portátil

> **Modo de uso**:
>
> * Copie os arquivos necessários para a pasta `Cobran/` (onde está o projeto)
> * Execute o script `executar.bat` com duplo clique ou via terminal

---

## Observações Finais

* O projeto **não utiliza pacotes (`package`)** nas classes Java.
* **Não há banco de dados** ou persistência de dados: o foco está na automação de **cálculos recorrentes**.
* Os resultados são **exibidos formatados**, prontos para cópia ou envio.
* Projeto acadêmico.