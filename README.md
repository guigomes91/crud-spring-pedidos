# Sistema de Pedidos
Backend de pedidos utilizando Spring, Java 8+, Maven, Postgres, JPA e REST com JSON

## Branch MASTER

<h3>Como subir o backend</h3>

<ol>
    <li>Criar um banco de dados no postgres chamado **pdv**. Alterar o arquivo **application.properties** e alterar o parâmetro         <b>spring.datasource.password=postgres</b> para a senha do banco de dados.</li>
    <li>Subir o backend, execute a classe PdvApplication com botão direto na mesma, **"Run As" -> "Java Application"** no Eclipse.</li>
</ol>

** Endpoints **

### Item

**Consultar** diversos Item (produto/serviço): 
GET url: http://localhost:8080/item/listar

**Consultar** item por UUID, GET url: http://localhost:8080/item/{id}

**Salvar** um novo item, POST url: http://localhost:8080/item/
JSON a ser enviado:
{
    "descricao": "REFRIGERANTE COCA COLA 2LT",
    "tipo": 1,
    "valor": 6.99,
    "situacao": true
}
Campo "tipo" (1 - produto; 0 - serviço)

**Alterar** um item existente, PUT url: http://localhost:8080/item/{id}
JSON a ser enviado:
{
    "descricao": "BOLACHA NESTLE",
    "tipo": 1,
    "valor": 8.5,
    "situacao": false
}

**Deletar** um item, DELETE, url: http://localhost:8080/item/{id}
Ao tentar deletar um Item que esteja em Pedido, recebera retorno **400 Bad Request**.

### Pedido
**Salvar** um novo pedido, POST url: http://localhost:8080/pedido/
JSON a ser enviado: 
{
    "cpf": 1234567892,
    "total": 0,
    "desconto": 0,
    "status": "ABERTO"
}
ou
{
    "cpf": 1234567892,
    "total": 0,
    "desconto": 0,
    "status": "ABERTO",
    "emissao": "2022-11-02"
}

É possível enviar o campo "emissao" porém não é obrigatório, será gerado pelo backend se nulo.

**Alterar** um pedido, PUT url: http://localhost:8080/pedido/{id}
JSON a ser enviado:
{
    "cpf": 12345678910,
    "total": 200.0,
    "desconto": 10,
    "status": "ABERTO"
}

<h3>Regras ao alterar desconto do pedido</h3>
<ul>
    <li>Se informado o desconto e não ouver itens no pedido o retorno <b>400 Bad Request</b> será lançado.</li>
    <li>O desconto só será realizado com situação do Pedido em "ABERTO" caso contrário o retorno <b>400 Bad Request</b> será lançado.</li>
    <li>Se desconto maior que 100% o retorno <b>400 Bad Request</b> será lançado.</li>
    <li>O desconto só é aplicado em itens do tipo 1 (produto).</li>
</ul>

**Listar** diversos pedido, GET url: http://localhost:8080/pedido/listar

**Consultar** pedido por UUID, GET url: http://localhost:8080/pedido/{id}

**Deletar** pedido, DELETE url: http://localhost:8080/pedido/{id}
Só é possível deletar pedido que esteja com situação "ABERTO", caso contrário **400 Bad Request** será lançado.

### Item do Pedido

**Salvar** um novo item do pedido, POST url: http://localhost:8080/pedidoitem/
JSON a ser enviado:
{
    "idProduto": "7b5f26f4-28a6-40b1-bcf6-73d34d1b98aa",
    "idPedido": "6833c851-b09a-4787-a62d-bd6b5272d7c5",
    "quantidade": 1,
    "total": 10.5,
    "desconto": 0.0
}

<h3>Regras ao inserir item no pedido</h3>
<ul>
    <li>Não é possível inserir item não existente (quando informado uuid errado), caso contrário será lançado <b>400 Bad Request</b>.</li>
    <li>Não é possível inserir item desativado, caso contrário o retorno <b>400 Bad Request</b> será lançado.</li>
</ul>

**Alterar** um item do pedido, PUT url: http://localhost:8080/pedidoitem/{id}
Variável {id} é o uuid da tabela pedidoitem.

JSON a ser enviado: 
{
    "quantidade": 9,
    "total": 119.99,
    "desconto": 0.0
}

**Listar** diversos itens do pedido, GET url: http://localhost:8080/pedidoitem/listar

**Consultar** item do pedido por UUID do pedido, GET url: http://localhost:8080/pedidoitem/{id}

**Deletar** item do pedido, DELETE url: http://localhost:8080/pedidoitem/{id}
Só é possível deletar item do pedido quando situação esteja em "ABERTO", caso contrário **400 Bad Request** será lançado.
