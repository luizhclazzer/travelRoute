# Exercício: Rota de Viagem #
Um turista deseja viajar pelo mundo pagando o menor preço possível independentemente do número de conexões necessárias. Vamos construir um programa que facilite ao nosso turista, escolher a melhor rota para sua viagem.

Para isso precisamos inserir as rotas através de um arquivo de entrada.

# Input Example # 
```shell
GRU,BRC,10
BRC,SCL,5
GRU,CDG,75
GRU,SCL,20
GRU,ORL,56
ORL,CDG,5
SCL,ORL,20
```
# Build #
É necessário ter o Java [15.0.1](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html) instalado no Windows ou Linux.
## Linha de comando ##
* Navegar para a pasta raíz do repositório e executar via shell:
```shell
$ mvn package
```
## Pela IDE ##
No menu do Maven >> Travel Route >> Lifecycle e executar duplo click em package.
### Execução do sistema ###
Windows:
```shell
java -jar -Dparam="c:\routes\routes.csv" travelRoute-0.0.1.jar
```
Linux:
```shell
java -jar -Dparam="/home/luiz.clazzer/routes/files/routes.csv" travelRoute-0.0.1.jar
```

### Estrutura de pasta ###

* `com.dijkstra.travelRoute`: raiz do projeto, contém o arquivo TravelRouteApplication que inicia a execução da aplicação e pede os input's.
* `com.dijkstra.travelRoute.batchService`: configuração do Spring Batch, que é utilizado para ler o arquivo CSV e inserir seus dados em um banco H2 em memória.
* `com.dijkstra.travelRoute.controller`: contém o controller que recebe as requisições rest.
* `com.dijkstra.travelRoute.model`: classe model e DTO's.
* `com.dijkstra.travelRoute.repository`: camada de acesso ao banco de dados H2.
* `com.dijkstra.travelRoute.service`: service de controle com a regra de negócio e validações.
* `com.dijkstra.travelRoute.utils`: contém as exceptions personalizadas, operações de linha de comando e validações com o arquivo.
* `src\main\resources`: contém o arquivo de propriedades da aplicação, SQL para criação do banco em memória, e o arquivo routes.csv para ser utilizado na execução dos testes unitários via JUnit.
* `src\main\test`: contém os testes unitários que utilizam o JUnit.

## API ##
A API do projeto conta com 4 endpoints: 
1. GET `/routes/origin/PTO/destination/ORL`: endpoint que realizará consulta da melhor rota, onde nesse exemplo, PTO e ORL são os parâmetros, os quais são obrigatórios. Exemplo de retorno:
```json
{
    "path": "PTO - SCL - ORL",
    "cost": 27.0
}
```
Status codes possíveis:
* 200: OK
* 404: Not Found, para o caso da rota passada não existir.
---
2. POST `/routes`: faz o cadastro de rota no arquivo CSV e no banco de dados em memória H2. Exemplo de body para envio:
```json
{
    "origin": "PTO",
    "destination": "ORL",
    "cost": 7.0
}
```
Status codes possíveis:
* 201: Created
* 409: Conflict, para o caso da rota já existir.
---
3. DELETE `/routes/origin/PTO/destination/ORL`: endpoint que realizará o delete da rota no arquivo CSV e no banco de dados em memória H2.

Status codes possíveis:
* 204: Delete foi executado
* 404: Not found, para o caso da rota não existir.
---
4. GET `http://localhost:8080/routes/{id}`: consulta a rota de acordo com o ID passado no parâmetro. Exemplo de retorno:
```json
{
    "origin": "PTO",
    "destination": "SCL",
    "cost": 7.0
}
```
Status codes possíveis:
* 200: OK
* 404: Not Found, para o caso da rota passada não existir.
---
Todos os endpoint's, bem como a possibilidade de sua execução estão disponíveis no Swagger: `http://127.0.0.1:8080//swagger-ui.html#/`
## Testes unitários ##
Os testes foram escritos com o JUnit e estão disponíveis em src\test\java\com.dijkstra.travelRoute.
São baseados nos dados que estão no arquivo src\main\resources\routes.csv.
## Banco de dados ##
As informações armazenadas em memória no H2 podem ser consultadas através do console: `http://localhost:8080/h2-console/`.
## Frameworks ##
Os frameworks Spring listados abaixo foram utilizados com o intuito de disponibilizar a mesma aplicação para linha de comando e requisições REST, com código limpo:
* SpringBoot
* SpringWeb (Api Rest)
* SpringJpa (Base de dados em memória H2)
* SpringBatch (Carregamento do arquivo CSV de entrada e armazenamento na base)
