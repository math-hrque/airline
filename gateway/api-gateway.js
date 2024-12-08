require("dotenv").config();
const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors"); // Importar o middleware CORS

const app = express();
const port = process.env.PORT || 3000;

// URLs dos microserviços do arquivo .env
const funcionarioServiceUrl = process.env.MS_FUNCIONARIO_URL;
const funcionarioServiceSagaUrl = process.env.MS_FUNCIONARIO_SAGA_URL;
const voosServiceUrl = process.env.MS_VOOS_URL;

// Middleware para configurar CORS globalmente
app.use(
  cors({
    origin: ["http://localhost:4200"], // Permitir apenas o frontend Angular
    methods: ["GET", "POST", "PUT", "DELETE"], // Métodos permitidos
    allowedHeaders: ["Content-Type", "Authorization"], // Cabeçalhos permitidos
    credentials: true,
  })
);

// Middleware para registrar as requisições
app.use((req, res, next) => {
  console.log(`Recebido pedido para ${req.url}`);
  next();
});

// Rota para listar funcionários
app.get(
  "/api/funcionarios",
  createProxyMiddleware({
    target: funcionarioServiceUrl,
    changeOrigin: true,
    pathRewrite: {
      "^/api/funcionarios": "/ms-funcionario/listar-funcionario", // Mapeia a rota local para a rota do serviço
    },
  })
);

// Rota para consultar funcionários por email
app.get(
  "/api/funcionarios/email/:email",
  createProxyMiddleware({
    target: funcionarioServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios/email",
        "/ms-funcionario/consultar-email"
      ),
  })
);

// Rota para consultar funcionários por ID
app.get(
  "/api/funcionarios/id/:idFuncionario",
  createProxyMiddleware({
    target: funcionarioServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios/id",
        "/ms-funcionario/consultar-idfuncionario"
      ),
  })
);

// Rota para inativar funcionário por email
app.put(
  "/api/funcionarios/inativar/:email",
  createProxyMiddleware({
    target: funcionarioServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios/inativar",
        "/saga/ms-funcionario/inativar-funcionario"
      ),
  })
);

// Rota para listar voos nas próximas 48 horas
app.get(
  "/api/voos",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: {
      "^/api/voos": "/ms-voos/listar-voos-48h", // Mapeia a rota local para a rota do serviço
    },
  })
);

// MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS
app.post(
  "/api/voos/cadastrar",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos", "/ms-voos/cadastrar-voo"),
  })
);

app.get(
  "/api/voos/listar",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: {
      "^/api/voos": "/ms-voos/listar",
    },
  })
);
// MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS

// Iniciar o API Gateway
app.listen(port, () => {
  console.log(`API Gateway rodando em http://localhost:${port}`);
});
