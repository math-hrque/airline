#!/bin/bash

# Habilita a interrupção em caso de erro
set -e

# Função para construir imagens Docker individualmente
build_images() {
  echo "🔨🚧 Iniciando o processo de construção das imagens Docker..."

  echo "🚀💻 Construindo imagens dos microsserviços..."
  docker build -t ms-auth:latest ./ms-auth/auth
  echo "✅📦 Imagem do ms-auth construída com sucesso!"
  
  docker build -t ms-cliente:latest ./ms-cliente/cliente
  echo "✅📦 Imagem do ms-cliente construída com sucesso!"

  docker build -t ms-funcionario:latest ./ms-funcionario/funcionario
  echo "✅📦 Imagem do ms-funcionario construída com sucesso!"

  docker build -t ms-reserva:latest ./ms-reserva/reserva
  echo "✅📦 Imagem do ms-reserva construída com sucesso!"

  docker build -t ms-voos:latest ./ms-voos/voos
  echo "✅📦 Imagem do ms-voos construída com sucesso!"

  docker build -t saga:latest ./saga/saga
  echo "✅📦 Imagem do saga construída com sucesso!"

  echo "🌉✨ Construindo imagem do API Gateway..."
  docker build -t api-gateway:latest ./gateway
  echo "✅🌐 Imagem do API Gateway construída com sucesso!"

  echo "🎨🖌️ Construindo imagem do Frontend Angular..."
  docker build -t angular-frontend:latest ./angular
  echo "✅🌟 Imagem do Frontend Angular construída com sucesso!"

  echo "🎉🚀 Todas as imagens foram construídas com sucesso!"
}

# Função para iniciar os containers
start_containers() {
  echo "🔧⚙️ Subindo serviços com Docker Compose..."
  docker-compose up -d
  echo "✅🎯 Todos os serviços estão em execução! Confira os logs usando 'docker-compose logs'."
}

# Executar funções
build_images
start_containers
