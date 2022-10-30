# crud-spring-pedidos
Backend de pedidos utilizando Spring, Java 8+, Maven, Postgres, JPA e REST com JSON

Como utilizar

1º passo

Crie um banco de dados no postgres chamado "pdv"

2º passo

Subir o backend, execute a classe PdvApplication com botão direto na mesma, "Run As" -> "Java Application" no Eclipse.

Endpoints

Item (produto/serviço): 
Método GET, url: http://localhost:8080/item/listar

Lista um item por UUID, método GET, url: http://localhost:8080/item/{id}

Salvar um item novo, método POST, url: http://localhost:8080/item/
JSON a ser enviado:
{
    "descricao": "REFRIGERANTE COCA COLA 2LT",
    "tipo": 1,
    "valor": 6.99,
    "situacao": true
}

Alterar um item existente, método PUT, url: http://localhost:8080/item/{id}
JSON a ser enviado:
{
    "descricao": "BOLACHA NESTLE",
    "tipo": 1,
    "valor": 8.5,
    "situacao": false
}

Deletar um item, método DELETE, url: http://localhost:8080/item/{id}
