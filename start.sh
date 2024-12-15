#!/bin/bash
set -e

build_images() {
  echo "🔨🚧 Starting the Docker images build process..."

  echo "🚀💻 Building microservices images..."
  docker build -t ms-auth:latest ./ms-auth/auth
  echo "✅📦 ms-auth image built successfully!"
  
  docker build -t ms-cliente:latest ./ms-cliente/cliente
  echo "✅📦 ms-customer image built successfully!"

  docker build -t ms-funcionario:latest ./ms-funcionario/funcionario
  echo "✅📦 ms-employee image built successfully!"

  docker build -t ms-reserva:latest ./ms-reserva/reserva
  echo "✅📦 ms-booking image built successfully!"

  docker build -t ms-voos:latest ./ms-voos/voos
  echo "✅📦 ms-flight image built successfully!"

  echo "🌟🌀 Building the Saga orchestrator image..."
  docker build -t saga:latest ./saga/saga
  echo "✅📦 Saga orchestrator image built successfully!"

  echo "🌉✨ Building the API Gateway image..."
  docker build -t api-gateway:latest ./api-gateway
  echo "✅🌐 API Gateway image built successfully!"

  echo "🎨🖌️ Building the Angular Frontend image..."
  docker build -t angular-frontend:latest ./angular-frontend
  echo "✅🌟 Angular Frontend image built successfully!"

  echo "🎉🚀 All images have been built successfully!"
}

start_containers() {
  echo "🔧⚙️ Starting services with Docker Compose..."
  docker-compose up -d
  echo "✅🎯 All services are up and running! Check logs using 'docker-compose logs'."
}

build_images
start_containers
