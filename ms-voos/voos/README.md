## Exemplos de consumo dos endpoints

### GET http://localhost:8084/ms-voos/listar-voos-48h

Response:

``` json
[
    {
        "codigoVoo": "TADS0015",
        "dataVoo": "2024-11-08T18:30:00Z",
        "valorPassagem": 800.0,
        "quantidadePoltronasTotal": 60,
        "quantidadePoltronasOcupadas": 0,
        "estadoVoo": "CONFIRMADO",
        "aeroportoOrigem": {
            "codigoAeroporto": "BSB",
            "nome": "Aeroporto Internacional de Brasília",
            "cidade": "Brasília",
            "estado": "Distrito Federal"
        },
        "aeroportoDestino": {
            "codigoAeroporto": "POA",
            "nome": "Aeroporto Internacional de Porto Alegre",
            "cidade": "Porto Alegre",
            "estado": "Rio Grande do Sul"
        }
    },
    {
        "codigoVoo": "TADS0014",
        "dataVoo": "2024-11-07T16:50:00Z",
        "valorPassagem": 700.0,
        "quantidadePoltronasTotal": 120,
        "quantidadePoltronasOcupadas": 1,
        "estadoVoo": "CONFIRMADO",
        "aeroportoOrigem": {
            "codigoAeroporto": "MAO",
            "nome": "Aeroporto Internacional de Manaus",
            "cidade": "Manaus",
            "estado": "Amazonas"
        },
        "aeroportoDestino": {
            "codigoAeroporto": "CWB",
            "nome": "Aeroporto Internacional de Curitiba - Afonso Pena",
            "cidade": "Curitiba",
            "estado": "Paraná"
        }
    }
]
```

-----------------------------------------------------------------------------

### POST http://localhost:8084/ms-voos/cadastrar-voo

Body:

``` json
{
    "codigoVoo": "TADS0099",
    "dataHora": "2024-11-05T14:30:00+00:00",
    "valorPassagemReais": 400,
    "quantidadePoltronasTotal": 120,
    "aeroportoOrigem": "CWB",
    "aeroportoDestino": "GRU"
}
```

Response:

``` json
{
    "codigoVoo": "TADS0099",
    "dataVoo": "2024-11-05T14:30:00Z",
    "valorPassagem": 400.0,
    "quantidadePoltronasTotal": 120,
    "quantidadePoltronasOcupadas": 0,
    "estadoVoo": "CONFIRMADO",
    "aeroportoOrigem": {
        "codigoAeroporto": "CWB",
        "nome": "Aeroporto Internacional de Curitiba - Afonso Pena",
        "cidade": "Curitiba",
        "estado": "Paraná"
    },
    "aeroportoDestino": {
        "codigoAeroporto": "GRU",
        "nome": "Aeroporto Internacional de São Paulo-Guarulhos",
        "cidade": "Guarulhos",
        "estado": "São Paulo"
    }
}
```