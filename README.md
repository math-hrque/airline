# COMPANY SERVERS

## Guia para rodar o projeto

### **STEP 1.** Instalar Java 21 e Configurar Variável de Ambiente

#### 1.1. Se houver outras versões do Java instaladas no computador, **EXCLUA TODAS ANTES**, pois pode dar erro no projeto posteriormente;

#### 1.2. Deve ser instalado **EXATAMENTE** a versão [21.0.4+7-LTS Temurin](https://adoptium.net/temurin/releases/);

#### 1.3. Após instalado, veja o vídeo de tutorial de [Configuração da Variável de Ambiente do Java](https://www.youtube.com/watch?v=LcgpVCnnYQM&ab_channel=JuanPetrik);
  
#### 1.4. Após configurado, verifique a versão do Java no cmd:
    java -version

#### 1.5. Resposta esperada no terminal:
    openjdk version "21.0.4" 2024-07-16 LTS
    OpenJDK Runtime Environment Temurin-21.0.4+7 (build 21.0.4+7-LTS)
    OpenJDK 64-Bit Server VM Temurin-21.0.4+7 (build 21.0.4+7-LTS, mixed mode, sharing)

<br>

### **STEP 2.** Instalar Apache Maven e Configurar Variável de Ambiente

#### 2.1. Se houver outras versões do Apache Maven instaladas no computador, **EXCLUA TODAS ANTES**, pois pode dar erro no projeto posteriormente;

#### 2.2. Deve ser instalado **EXATAMENTE** a versão [Apache Maven 3.9.9](https://maven.apache.org/download.cgi);

#### 2.3. Após instalado, veja o vídeo de tutorial de [Configuração da Variável de Ambiente do Apache Maven](https://www.youtube.com/watch?v=rfhTnfbBQcY&ab_channel=Descompila);
  
#### 2.4. Após configurado, verifique a versão do Java no cmd:
    mvn -version

#### 2.5. Resposta esperada no terminal:
    Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)                                                           
    Maven home: C:\Program Files\apache-maven-3.9.9-bin\apache-maven-3.9.9                                                  
    Java version: 21.0.4, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.4.7-hotspot         
    Default locale: pt_BR, platform encoding: UTF-8                                                                         
    OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

<br>

### **STEP 3.** Instalar e Configurar o Docker:

#### 3.1. O Docker necessita de alguns pré-requisitos antes de ser instalado caso seja no Windows, que caso contrário, dará erro já na instalação, sendo elas:
- Windows 10 ou 11 (Versão 22H2 ou superior) - Melhor o 10, pois o Windows 11 há possibilidade de alguns bugs por ser mais recente;
- Instalar WLS 2;
- Habilitar Hyper-V/Hypervisor e Windowns Subsystem for Linus no Windows Features;
- Habilitar virtualização na BIOS/UEFI da placa mãe.

#### 3.2. Instale a versão **MAIS RECENTE** que não seja a **Beta** do [Docker](https://docs.docker.com/desktop/install/windows-install/);
	
#### 3.3. Se precisar, veja o tutorial de [instalação dos pré-requisitos e Docker](https://www.youtube.com/watch?v=5RQbdMn04Oc&t=159s&ab_channel=DavidBombal);
  
#### 3.4. Após instalado, execute o Docker como **ADMINISTRADOR** sempre.

<br>

### **STEP 4.** Clonar e Abrir o Projeto Back-end no Visual Studio Code

#### 4.1. Clonar projeto no diretório desejável:
	git clone https://github.com/math-hrque/company-servers.git

#### 4.2. Entrar na pasta do projeto:
	cd company-servers

#### 4.3. Abrir o projeto no Visual Studio Code:
	code .

<br>

### **STEP 5.** Extensões Necessárias no Visual Studio Code para o projeto

#### 5.1. Instale as seguintes extensões no Visual Studio Code:
- Extension Pack for Java;
- Spring Boot Extension Pack;
- Spring Initializr Java Support;
- Spring Boot Dashboard.

<br>

### **STEP 6.** Inicialize os containers Docker:
#### 6.1. Conceito e comando da conteinização:
- Com o docker aberto como administrador, agora na pasta raiz do projeto Java do Visual Studio Code, encontramos o arquivo docker-compose.yaml, tudo que está nele é criado como container com um simples comando, basta digitar no terminal do Visual Studio Code do projeto:
	```
	docker-compose up -d
	```

#### 6.2. Resposta esperada no terminal:
    [+] Running 5/5
    Network company-servers_company-network  Created
    Container redis-container                Started
    Container postgres-db                    Started
    Container mongo-db                       Started
    Container rabbitmq                       Started 

#### 6.3. Contexto:
- Foi iniciado 4 containers, na primeira vez ele irá demorar mais, porque além de inicializar os containers, ele terá que instalar as imagens de cada item que estamos declarando. Cada container fica disponibilizado em uma porta diferente conforme está no docker-compose, lembrando que mongo-db usamos para MS Auth e Postgres-db para os demais MS. Além disso não é necessário instalar localmente mongo ou postgres, pois o container já sobe com a imagem do banco de dados que fica no próprio servidor e é acessado pela sua porta configurada. O que precisaremos instalar são os workbenchs para acessar esses bancos de dados criados e operantes. O start e a pausa dos containers podem ser feitas pela interface do próprio Docker.

#### 6.4. Comando para parar os containers:
	```
	docker-compose down
	```

<br>

### **STEP 7.** Execute o Projeto Java:

#### 7.1. No Visual Studio Code, Abra a Paleta de Comandos: **CTRL + SHIFT + P**;

#### 7.2. Digite: **dashboard**;

#### 7.3. Escolha: **Exibir: Mostrar Spring Boot Dashboard**;

#### 7.4. Canto esquerdo-acima do Visual Studio Code, aonde tem uma tab "APPS", clique no **Run** dos MS do projeto que deseja utilizar, exemplo: se vai trabalhar no MS Voos, basta executar apenas esse;

#### 7.5. Resposta final esperada no terminal após execução:
	Started Application in 7.934 seconds (process running for 8.606)

#### 7.6. Lembrando que o projeto não irá funcionar se não tiver o Docker rodando, pois ele tentará se conectar ao banco de dados e lançará erro de sql conection.

<br>

### **STEP 8.** Workbenchs e configuração de acesso para os banco de dados:

#### 8.1. Com o projeto e Docker rodando, instale os Workbenchs para acessar os bancos de dados:
- pgAdmin 4 - Versão 8.11 (released Aug. 22, 2024), para o [Postgres](https://www.pgadmin.org/download/pgadmin-4-windows/)
- MongoDB Compass - Versão 1.44.3 (Stbale), para o [Mongo](https://www.mongodb.com/try/download/compass)

#### 8.2. Após instalados, abram uma nova conexão neles:
- Para pgAdmin 4, a configuração é:
    ```
    address: localhost
    port: 5433
    maintenance: postgres
    username: postgres
    password: postgres
    ```

- Para MongoDB, a configuração é:
    ```
    URI: mongodb://mongo:mongo@localhost:27018/
    ```

#### 8.3. Lembrando que a conexão não irá funcionar se não tiver o Docker rodando, pois ele tentará se conectar ao banco de dados e o servidor não estará ativo.

<br>

### **STEP 9.** Instalar Postman para testar os end-points que serão criados:

#### 9.1. Instale a última versão do [Postman](https://www.youtube.com/watch?v=RbT_stw02C4&ab_channel=Prof.Rog%C3%A9rioNapole%C3%A3oJr.)

<br>

### **STEP 10.** INFORMAÇÕES PARA USO DO PROJETO:

#### 10.1. Por padrão o projeto já cria no banco de dados em sua inicialização todas as tabelas e dados de teste necessários, verifique se ao rodar o projeto possui as tabelas e dados nas tabelas;

#### 10.2. Os requisitos não-funcionais solicitados pelo professor já estão implementados;

#### 10.3. Para resetar os dados do banco, pare de rodar o projeto, pare de rodar o Docker, exclua o container do Docker e faça o **STEP 6.1** novamente e rode o projeto novamente;

#### 10.4. Para quem for mexer no projeto MS Reserva, precisa entender que está usando CQRS, tudo que for para listar usará somente arquivos que terminam com **R** e tudo que for alterar usará apenas arquivos que terminam com **CUD**, pois as listagens só usarão do banco ContaR que está DESNORMALIZADO e as alterações usarão do banco ContaCUD que está NORMALIZADO e depois atualizará no banco ContaR;

#### 10.5. Api SAGA só é utilizado se o requisito utiliza mensageria, bem como arquivos da pasta **consumers** das APIs MS;

#### 10.6. Se é necessário a consulta de vários dados que estão espalhados por diversas APIs MS, faz-se um end-point que pega cada informação em cada MS e na API Gateway utiliza-se API Composite para fazer essas diversas chamadas em diversas APIs MS que no final montará esse objeto completo;

#### 10.7. Há exemplo de códigos de implementação nos MS Auth, MS Cliente e MS Funcionário;

#### 10.8. Toda a estrutura já está montada, só será necessário implementar nos arquivos da pasta **controllers**, **services** e se precisar de alguma consulta diferente das que já existem o **repositories**;

#### 10.9. Todo o resto já foi implementado, como: 
- Mensagens de erros personalizados nos arquivos da pasta **exceptions** que devem ser utilizados para gerar respostas HTTPs consistentes;
- Dtos que serão utilizados para retornar para o front-end nos arquivos da pasta **dtos** que devem ser utilizados para gerar respostas HTTPs consistentes;
- Enums que tanto o front-end utilizará quando já está formalizado no banco de dados, nos arquivos da pasta **enums**;
- Models que mapeiam o banco de dados pra buscar e alterar objetos, está na pasta **models**.

#### 10.10. Noções básicas do projeto:
- [Modelo lógico do Projeto](https://drive.google.com/drive/folders/1dPCeFZOM-aIzSE9JUP48XEaB17qoqNc4?usp=sharing);
- \configs: Contém classes de configuração do Spring, como configuração de beans, segurança, etc;
- \consumers: Contém os consumidores dos microsserviços;
- \controllers: Contém os controladores REST que lidam com as requisições HTTP, no caso os end-points;
- \dtos: Contém os objetos de transferência de dados no intuito de omitir dados sensíveis como senhas presentes nas classes de padrão ORM;
- \enums: contém enums da regra de negócio do projeto;
- \exeptions: Contém classes para tratamento de exceções customizadas;
- \models: Contém as classes de modelo ORM (entidades) mapeadas no banco de dados;
- \repositories: Contém as interfaces que acessam a camada de dados;
- \security: Contém classes relacionadas à segurança, como configuração de autenticação e autorização;
- \services: Contém a lógica de negócios da aplicação;
- \utils: Contém classes utilitárias que são usadas em todo o projeto, como manipular strings, datas, horas e fuso-horários e converter tipos diferentes entre classes;
- \Application.java: inicia a execução da aplicação;
- \ServletInitializer: permite a inicialização do Spring Boot a partir de um servlet tradicional, como o do Tomcat embutido em um servidor externo;
- \resources\db\migration: contém arquivos .sql que fazem a manipulação pré-definida no banco de dados assim que o projeto é iniciado, por meio da ferramenta flyway;
- \resources\application.properties: contém as configurações para se conectar a diversos serviços como no banco de dados e envio de emails;
- Dockerfile: contém instruções para construir uma imagem Docker do projeto;
- pom.xml: contém todas as dependências do projeto;
- docker-compose.yaml: contém definições para construir imagens Docker, sejam elas provenientes de vários Dockerfile para subir todas as aplicações de uma vez e/ou sejam elas provenientes de aplicações que são configuradas no próprio docker-compose como servidores de banco de dados;
- init-db.sh: está vinculado com o docker-compose.yaml, assim que iniciamos o docker-compose, ele ao conectar-se aos banco de dados executa a query de criação das databases necessárias.
