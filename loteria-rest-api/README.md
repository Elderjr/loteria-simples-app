# Documentação API Spring Rest - Caminhos

### GET /loteria
Obtém o estado atual da loteria. Saída:
| Nome | Descriçao |
| ------ | ------ |
| nome | nome da loteria |
| resultado | resultado da loteria caso estiver finalizada (null caso não estiver finalizada) |
| resultado.numerosSorteados | vetor de tamanho seis contendo os números sorteados da loteria |
| resultado.vencedores | lista dos vencedores |
| resultado.vencedores[n].id | id do apostador que ganhou |
| resultado.vencedores[n].nome | nome do apostador que ganhou |
| resultado.vencedores[n].premio | O tipo do prêmio que o apostador ganhou (SENA, QUINA ou QUADRA)

Exemplo de saída para loteria que ainda não foi encerrada
```json
{
    "nome": "Mega Sena",
    "resultado": null
}
```

Exemplo de saída para loteria que foi encerrada
```json
{
    "nome": "Mega Sena",
    "resultado": {
        "numerosSorteados": [1,2,3,4,5,6],
        "vencedores": [
            {
                "id": 3,
                "nome": "Exemplo A",
                "premio": "SENA"
            },
            {
                "id": 4,
                "nome": "Exemplo B",
                "premio": "QUADRA"
            }
        ]
    }
}
```

### POST /loteria
Inicializa uma nova loteria e substitui a antiga. Entrada:
| Nome | Descriçao |
| ------ | ------ |
| nome | nome da loteria |

Exemplo de entrada para inicializar uma nova loteria
```json
{
    "nome": "Mega Sena"
}
```
Saída: A mesma que GET /loteria

Validação:
- Inicializar uma nova loteria sem finalizar a anterior não é permitido. Caso invocações consecutivas de iniciar uma nova loteria sejam feitas o servidor irá retornar um erro.


### PUT /loteria/finalizar

Finaliza a loteria atual e retorna seu resultado (obs.: a loteria não é excluída até que uma criação de uma nova loteria seja requisitada). Saída: o resultado conforme mencionado em GET /loteria

Validação:
- Tentar finalizar uma loteria que já está finalizada irá retornar um erro.

### GET /loteria/apostas
Obtém uma paginação dos apostadores com suas apostas registradas na loteria. Entrada:

| Nome | Descriçao |
| ------ | ------ |
| pagina (parâmetro da url) | inteiro que indica a página que é desejado acessar (valor padrão é 0) |
| itensPorPagina (parâmetro da url) | inteiro que indica o total de apostadores por página que é desejado (valor padrão é 5) |

Exemplo de requisição: ``` /loteria/apostas?pagina=2&itensPorPagina=4```

Saída:
| Nome | Descriçao |
| ------ | ------ |
| itens | Lista dos apostadores da página desejada |
| itens[n].id | id do apostador |
| itens[n].nome | nome do apostador |
| itens[n].apostas | Lista das apostas registradas do apostador |
| itens[n].apostas[n].id | id da aposta |
| itens[n].apostas[n].numerosApostados | vetor de inteiros dos números apostados |
| itens[n].apostas[n].dataCriacao | A data (contendo horas e minutos) em que foi registrado a aposta |
| itensPorPagina | O valor de itens por página requisitado |
| totalItens | O total de itens (global, não apenas da página requisitada) |
| página | A página que foi requisitada (a página começa em 0) |
| totalPaginas | O total de páginas |

Exemplo de saída:
```json
{
    "itens": [
        {
            "id": 1,
            "nome": "Exemplo A",
            "apostas": [
                {
                    "id": 1,
                    "numerosApostados": [1,2,3,4,5,6],
                    "dataCriacao": "2020-10-05T18:46:22.319"
                }
            ]
        },
        {
            "id": 2,
            "nome": "Exemplo B",
            "apostas": [
                {
                    "id": 2,
                    "numerosApostados": [1,2,3,7,8,9],
                    "dataCriacao": "2020-10-05T18:46:31.521"
                }
            ]
        }
    ],
    "itensPorPagina": 5,
    "totalItens": 2,
    "pagina": 0,
    "totalPaginas": 1
}
```

### POST loteria/apostas

Cria uma nova aposta a um usuário pré-registrado ou não. Entrada:
| Nome | Descriçao |
| ------ | ------ |
| numerosApostados | Vetor de inteiros conténdo os números da aposta |
| idApostador | id do apostador caso desejado vincular a aposta a um usuário pré-registrado|
| nomeApostador | nome do apostador |

Caso seja informado apenas o nome do apostador, o servidor irá criar um novo usuário e vincular a aposta ao novo usuário. Caso o id do apostador seja informado então a nova aposta será vinculada ao usuário que contém tal id.

Exemplo de entrada:
```json
{
	"nomeApostador": "Exemplo A",
	"numerosApostados": [1,2,3,4,5,6]
}
```
Saída:
| Nome | Descriçao |
| ------ | ------ |
| numerosApostados | Vetor de inteiros conténdo os números da aposta |
| idApostador | id do apostador que está vinculado a aposta|
| idAposta | id da aposta |

Exemplo de saída:
```json
{
    "idApostador": 3,
    "idAposta": 1,
    "nomeApostador": "Exemplo A",
    "numerosApostados": [1,2,3,4,5,6]
}
```
Validações:
- Os números apostados não podem se repetir no vetor (ex.: [1,2,3,4,4,5])
- Os números apostados devem ser sempre inteiros maior ou igual a 1 e menor ou igual a 60
- O tamanho do vetor de números apostados deve ser exatamente seis

### Erros
Caso alguma requisição ao servidor seja inválida, o servidor irá retornar um erro em formato JSON. Por exemplo, caso uma requisição de uma nova aposta contém algum número maior que 60:
```json
{
    "timestamp": "2020-10-06T04:22:25.941+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Os números das apostas devem ser entre 1 e 60 (incluso 1 e 60). Encontrado: 61",
    "path": "/loteria/apostas"
}
```
