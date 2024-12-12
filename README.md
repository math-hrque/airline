# **Empresa Aérea - DS152 N5 2/2024** 

## **Equipe 6 - Integrantes**
- JULIO MITSUJI YAMAGUCHI LIMEIRA
- LUIZ FELIPE TOZATI
- MATHEUS HENRIQUE MIRANDA
- RODRIGO BARBOSA DA SILVA
- VINICIUS SAVITRAZ
- YASMIN ALLANNY CALDERON SILVA

---

## **Como Executar o Projeto**

1. Através do shell script automatizado:

   1.1. Na raiz do projeto, execute o comando que fornece permissão ao script:
     
    ```bash
    chmod +x start.sh
    ```
    
   1.2. Na sequência, execute o comando de construir as imagens e subir os serviços:
     
    ```bash
    ./start.sh
    ```
    
2. Através do Docker Compose diretamente:

    2.1. Na raiz do projeto, execute o comando para construir as imagens e subir os serviços:
     
    ```bash
    docker-compose up --build -d
    ```

---

## **Requisitos Funcionais**

1. Link do Vídeo:

    1.1. [YouTube](https://youtu.be/XA6vPZW9oj0) (Velocidade acelerada)

    1.2. [Goole Drive](https://drive.google.com/drive/folders/19QShZeNdSQuWYuxDJ5Bc_1zptIMcxHq9?usp=drive_link) (Velocidade normal)

---

## **Scripts**

1. Inicialização do banco de dados:
   - Todos os scripts de banco de dados estão nas pastas **resources\db\migration** das APIs;
  
2. Construção das imagens e execução do projeto
   - Todos os scripts para construção das imagens e execução do projeto estão no **docker-compose.yaml** presente na pasta raiz + os **Dockerfile** presentes nas sub-pastas de cada projeto.

---

## **Código Fonte**

1. Todo código-fonte da aplicação está presente.
