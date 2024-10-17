CREATE TABLE reserva (
    codigo_reserva VARCHAR(6) UNIQUE NOT NULL,
    codigo_voo VARCHAR(8) NOT NULL,
	data_reserva TIMESTAMPTZ NOT NULL,
    valor_reserva NUMERIC(10,2) NOT NULL,
    milhas_utilizadas INTEGER NOT NULL,
    quantidade_poltronas INTEGER NOT NULL,
    id_cliente INTEGER NOT NULL,
    sigla_estado_reserva VARCHAR(5) NOT NULL,
    tipo_estado_reserva VARCHAR(50) NOT NULL
);