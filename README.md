# Sistema de Cálculo de Descontos para Marketplace

## Kayler de Freitas Moura

## Visão Geral do Projeto

O **Marketplace Discount Calculator** é uma aplicação web simples desenvolvida em **Java com Spring Boot**, cujo objetivo é simular um pequeno marketplace de acessórios para celulares e calcular descontos aplicados a um carrinho de compras.

O sistema foi projetado para fins acadêmicos, servindo como base para análise e comparação de arquiteturas de software. Com separação entre apresentação, serviço, persistência e modelo de domínio.

A aplicação permite visualizar produtos cadastrados localmente em memória, selecionar itens para compor um carrinho e gerar um resumo da compra contendo subtotal, percentual de desconto, valor descontado e total final.

As regras de negócio consideram desconto por quantidade de itens, desconto por categoria de produto e um limite máximo de desconto acumulado. O projeto também inclui testes unitários para validar o comportamento esperado da funcionalidade principal.

Além do uso prático em Spring Boot, este sistema foi estruturado para apoiar atividades experimentais de implementação e compreensão arquitetural.

## Tecnologias
- Java 17
- Spring Boot 3
- Thymeleaf
- JUnit 5
- Maven
- ArchUnit 1.2.1 (Testes de Arquitetura)

## Como executar

```Instalar dependências e compilar o projeto
mvn clean install
```

```bash
mvn spring-boot:run
```

Acesse:
```text
http://localhost:8080
```

## Como executar os testes

### Executar todos os testes
```bash
mvn test
```

### Executar apenas os testes de arquitetura (ArchUnit)
```bash
mvn test -Dtest=ArquiteturaTest
```

### Executar testes com relatório de cobertura
```bash
mvn clean test jacoco:report
```

O relatório de cobertura será gerado em:
```
target/site/jacoco/index.html
```

## Cobertura de Testes

Para visualizar a cobertura de testes do projeto:

1. Execute os testes com cobertura:
```bash
mvn clean test jacoco:report
```

2. Abra o relatório HTML gerado:
```bash
# No Windows
start target/site/jacoco/index.html

# No Linux/Mac
open target/site/jacoco/index.html
```

## Resultado esperado no experimento
Inicialmente, parte dos testes falhará, porque a lógica de desconto ainda não foi implementada.
O objetivo do participante é localizar o ponto correto da modificação, implementar a feature e fazer todos os testes passarem.
Realizar um teste de ponta a ponta para concretizar todas as regras de negocios.

## Diagrama de arquitetura
Os arquivos do diagrama estão em `docs/`.

## Regras de negocios e cenarios de testes
Os arquivos estão em `docs/`.

## Responda o formulario de feedback (OBRIGATÓRIO)
Após a conclusão da atividade, por favor, responda o formulário de feedback para ajudar na avaliação do experimento:
[Formulário de Feedback](https://forms.gle/UyJUz9aqmeyx8L5R9)
