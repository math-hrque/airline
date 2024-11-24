require('dotenv').config();
const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors'); // Importar o middleware CORS

const app = express();
const port = process.env.PORT || 3000;

// URL do microserviço de funcionários do arquivo .env
const funcionarioServiceUrl = process.env.MS_FUNCIONARIO_URL;

// Middleware para configurar CORS globalmente
app.use(cors({
  origin: ['http://localhost:4200'], // Permitir apenas o frontend Angular
  methods: ['GET', 'POST', 'PUT', 'DELETE'], // Métodos permitidos
  allowedHeaders: ['Content-Type', 'Authorization'], // Cabeçalhos permitidos
  credentials: true
}));

// Middleware para registrar as requisições
app.use((req, res, next) => {
  console.log(`Recebido pedido para ${req.url}`);
  next();
});

// Rota para listar funcionários
app.get('/api/funcionarios', createProxyMiddleware({
  target: funcionarioServiceUrl,
  changeOrigin: true,
  pathRewrite: {
    '^/api/funcionarios': '/ms-funcionario/listar-funcionario' // mapeia a rota local para a rota do serviço
  }
}));

// Rota para consultar funcionários por email
app.get('/api/funcionarios/email/:email', createProxyMiddleware({
  target: funcionarioServiceUrl,
  changeOrigin: true,
  pathRewrite: (path, req) => path.replace('/api/funcionarios/email', '/ms-funcionario/consultar-email')
}));

// Rota para consultar funcionários por ID
app.get('/api/funcionarios/id/:idFuncionario', createProxyMiddleware({
  target: funcionarioServiceUrl,
  changeOrigin: true,
  pathRewrite: (path, req) => path.replace('/api/funcionarios/id', '/ms-funcionario/consultar-idfuncionario')
}));

// Iniciar o API Gateway
app.listen(port, () => {
  console.log(`API Gateway rodando em http://localhost:${port}`);
});
