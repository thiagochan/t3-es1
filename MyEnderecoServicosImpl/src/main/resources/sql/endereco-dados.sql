-- Dados iniciais. Executado logo depois de endereco-schema.sql.

-- As 27 Unidades da Federação
INSERT INTO UnidadeFederativa (siglaUF, nomeUF) VALUES
    ('AC', 'Acre'), ('AL', 'Alagoas'), ('AP', 'Amapá'), ('AM', 'Amazonas'),
    ('BA', 'Bahia'), ('CE', 'Ceará'), ('DF', 'Distrito Federal'), ('ES', 'Espírito Santo'),
    ('GO', 'Goiás'), ('MA', 'Maranhão'), ('MT', 'Mato Grosso'), ('MS', 'Mato Grosso do Sul'),
    ('MG', 'Minas Gerais'), ('PA', 'Pará'), ('PB', 'Paraíba'), ('PR', 'Paraná'),
    ('PE', 'Pernambuco'), ('PI', 'Piauí'), ('RJ', 'Rio de Janeiro'), ('RN', 'Rio Grande do Norte'),
    ('RS', 'Rio Grande do Sul'), ('RO', 'Rondônia'), ('RR', 'Roraima'), ('SC', 'Santa Catarina'),
    ('SP', 'São Paulo'), ('SE', 'Sergipe'), ('TO', 'Tocantins');

-- Tipos de logradouro (a sigla é a chave)
INSERT INTO TipoLogradouro (siglaLogradouro, nomeLogradouro) VALUES
    ('R', 'Rua'), ('AV', 'Avenida'), ('AL', 'Alameda'), ('TV', 'Travessa'), ('ROD', 'Rodovia'),
    ('PC', 'Praça'), ('EST', 'Estrada'), ('LG', 'Largo'), ('VL', 'Viela');

-- Algumas cidades. A primeira recebe o id 1, a segunda o id 2, e assim por diante.
INSERT INTO Cidade (nome, siglaUF) VALUES
    ('Foz do Iguaçu', 'PR'), ('Medianeira', 'PR'), ('Cascavel', 'PR'), ('Curitiba', 'PR'),
    ('São Paulo', 'SP'), ('Rio de Janeiro', 'RJ'), ('Florianópolis', 'SC'), ('Porto Alegre', 'RS');

-- Endereços de exemplo, só para demonstração
INSERT INTO Bairro (nome) VALUES ('Centro');

INSERT INTO Logradouro (nome, siglaLogradouro) VALUES
    ('Brasil', 'AV'), ('Almirante Barroso', 'R'), ('Brasília', 'AV');

INSERT INTO Endereco (cep, idCidade, idBairro, idLogradouro) VALUES
    ('85851000',
     (SELECT idCidade FROM Cidade WHERE nome = 'Foz do Iguaçu' AND siglaUF = 'PR'),
     (SELECT idBairro FROM Bairro WHERE nome = 'Centro'),
     (SELECT idLogradouro FROM Logradouro WHERE nome = 'Brasil' AND siglaLogradouro = 'AV')),
    ('85851000',
     (SELECT idCidade FROM Cidade WHERE nome = 'Foz do Iguaçu' AND siglaUF = 'PR'),
     (SELECT idBairro FROM Bairro WHERE nome = 'Centro'),
     (SELECT idLogradouro FROM Logradouro WHERE nome = 'Almirante Barroso' AND siglaLogradouro = 'R')),
    ('85884000',
     (SELECT idCidade FROM Cidade WHERE nome = 'Medianeira' AND siglaUF = 'PR'),
     (SELECT idBairro FROM Bairro WHERE nome = 'Centro'),
     (SELECT idLogradouro FROM Logradouro WHERE nome = 'Brasília' AND siglaLogradouro = 'AV'));
