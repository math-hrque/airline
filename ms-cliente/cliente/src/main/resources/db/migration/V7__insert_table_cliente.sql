INSERT INTO cliente (cpf, email, nome, id_endereco) 
VALUES
('69065084070', 'leandro@gmail.com', 'Leandro Silva Costa', 1),
('32949911005', 'lucas@gmail.com', 'Lucas Oliveira Souza', 2),
('52368435026', 'leonardo@gmail.com', 'Leonardo Almeida Santos', 3),
('70164538046', 'lidia@gmail.com', 'Lídia Ferreira Almeida', 4),
('21472714032', 'larissa@gmail.com', 'Larissa Moreira Lima', 5),
('44085445073', 'leticia@gmail.com', 'Letícia Andrade Sousa', 6),
('90134873009', 'livia@gmail.com', 'Lívia Costa Pereira', 7),
('91622000030', 'lidio@gmail.com', 'Lídio Cardoso Farias', 8),
('21533623007', 'laudren@gmail.com', 'Laudren Melo Santos', 9),
('84964318001', 'laercio@gmail.com', 'Laércio Barros Rodrigues', 10);

UPDATE cliente SET saldo_milhas = 70 WHERE id_cliente = 1;
UPDATE cliente SET saldo_milhas = 150 WHERE id_cliente = 2;
UPDATE cliente SET saldo_milhas = 80 WHERE id_cliente = 3;
UPDATE cliente SET saldo_milhas = 100 WHERE id_cliente = 4;
UPDATE cliente SET saldo_milhas = 190 WHERE id_cliente = 5;
UPDATE cliente SET saldo_milhas = 500 WHERE id_cliente = 7;
UPDATE cliente SET saldo_milhas = 100 WHERE id_cliente = 9;
UPDATE cliente SET saldo_milhas = 200 WHERE id_cliente = 10;
