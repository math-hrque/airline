CREATE TABLE historico_alteracao_estado_reserva (
    id_historico_alteracao_estado_reserva SERIAL PRIMARY KEY,
    data_alteracao_estado_reserva TIMESTAMPTZ NOT NULL,
	codigo_reserva VARCHAR(6) NOT NULL UNIQUE,
    sigla_estado_reserva_origem VARCHAR(5) NOT NULL,
    tipo_estado_reserva_origem VARCHAR(50) NOT NULL,
    sigla_estado_reserva_destino VARCHAR(5) NOT NULL,
    tipo_estado_reserva_destino VARCHAR(50) NOT NULL
);