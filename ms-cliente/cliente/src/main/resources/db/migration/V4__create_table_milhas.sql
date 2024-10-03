CREATE TABLE milhas (
	id_milhas INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	data_transacao TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
	valor_reais NUMERIC(10,2) NOT NULL,
	quantidade_milhas INTEGER NOT NULL,
	descricao VARCHAR(50) NOT NULL,
	codigo_reserva VARCHAR(8),
	id_cliente INTEGER NOT NULL,
	id_transacao INTEGER NOT NULL,
	CONSTRAINT fk_milhas_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
	CONSTRAINT fk_milhas_transacao FOREIGN KEY (id_transacao) REFERENCES transacao(id_transacao)
);
