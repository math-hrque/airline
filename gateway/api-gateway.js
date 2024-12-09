require("dotenv").config();
const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors"); // Importar o middleware CORS

const app = express();
const port = process.env.PORT || 3000;

// COMUM
const funcionarioServiceUrl = process.env.MS_FUNCIONARIO_URL;
const reservaServiceUrl = process.env.MS_RESERVA_URL; // URL do microserviço de reserva
const voosServiceUrl = process.env.MS_VOOS_URL;
const clienteServiceUrl = process.env.MS_CLIENTE_URL;
const loginServiceUrl = process.env.MS_AUTH_URL;

// SAGA
const funcionarioServiceSagaUrl = process.env.MS_FUNCIONARIO_SAGA_URL;
const voosServiceSagaUrl = process.env.MS_VOOS_SAGA_URL;
const clienteServiceSagaUrl = process.env.MS_CLIENTE_SAGA_URL;

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

// ==============================================[LOGIN]==============================================

// Rota para login
app.post(
  "/api/auth/login", // Rota na API Gateway
  createProxyMiddleware({
    target: loginServiceUrl, // URL do microserviço de autenticação
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/auth/login", // Rota local
        "/ms-auth/login" // Rota real do microserviço de autenticação
      ),
  })
);

// ==============================================[LOGIN]==============================================

// ==============================================[VOO]==============================================

// Adiciona a rota para cadastrar um voo
app.post(
  "/api/voos/cadastrar-voo", // Rota da API Gateway
  createProxyMiddleware({
    target: voosServiceUrl, // URL do serviço de voos
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/cadastrar-voo", "/ms-voos/cadastrar-voo"), // URL do serviço real
  })
);

// ==============================================[VOO]==============================================

// ==============================================[CLIENTE]==============================================

// Rota para consultar endereço por CEP
app.get(
  "/api/clientes/consultar-endereco/:cep",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/clientes/consultar-endereco",
        `/ms-cliente/consultar-endereco`
      ),
  })
);

// Rota para cadastrar um cliente
app.post(
  "/api/clientes/cadastrar-cliente", // Rota da API Gateway
  createProxyMiddleware({
    target: clienteServiceSagaUrl, // URL do serviço de cliente real
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/clientes/cadastrar-cliente", // URL que será acessada pela API Gateway
        "/saga/ms-cliente/cadastrar-cliente" // URL do serviço real
      ),
  })
);

// Rota para consultar cliente por email
app.get(
  "/api/clientes/email/:email", // Rota da API Gateway
  createProxyMiddleware({
    target: clienteServiceUrl, // URL do microserviço de cliente
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/clientes/email", // URL na API Gateway
        "/ms-cliente/consultar-email" // URL real do microserviço
      ),
  })
);

// ==============================================[CLIENTE]==============================================

// ==============================================[FUNCIONARIOS]==============================================

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

// Rota para atualizar funcionário (com proxy para o Saga)
app.put(
  "/api/funcionarios/atualizar-funcionario",
  createProxyMiddleware({
    target: funcionarioServiceSagaUrl, // URL do microserviço Saga
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios/atualizar-funcionario",
        "/saga/ms-funcionario/atualizar-funcionario"
      ), // Reescreve a URL
  })
);

// Rota para criar um funcionário (com proxy para o Saga)
app.post(
  "/api/funcionarios",
  createProxyMiddleware({
    target: funcionarioServiceSagaUrl, // URL do microserviço Saga
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios",
        "/saga/ms-funcionario/cadastrar-funcionario"
      ), // Reescreve a URL
  })
);

// ==============================================[FUNCIONARIOS]==============================================

// ==============================================[VOOS]==============================================

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

// Rota para cancelar voo
app.put(
  "/api/voos/cancelar-voo/:codigoVoo", // Recebe o código do voo como parâmetro na URL
  createProxyMiddleware({
    target: voosServiceSagaUrl, // URL do microserviço de voos
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/cancelar-voo", "/saga/ms-voos/cancelar-voo"),
  })
);

app.put(
  "/api/voos/realizar-voo/:codigoVoo", // Recebe o código do voo como parâmetro na URL
  createProxyMiddleware({
    target: voosServiceSagaUrl, // URL do microserviço de voos
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/realizar-voo", "/saga/ms-voos/realizar-voo"), // Reescreve para o novo endpoint
  })
);

// MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS // MATHEUS
http: app.post(
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

// ==============================================[VOOS]==============================================

// ==============================================[RESERVA]==============================================

// Rota para confirmar embarque
app.put(
  "/api/reservas/confirmar-embarque",
  createProxyMiddleware({
    target: reservaServiceUrl, // URL do microserviço de reserva
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/reservas/confirmar-embarque",
        "/ms-reserva/confirmar-embarque"
      ), // Reescreve a URL
  })
);

// Rota para consultar reserva
app.get(
  "/api/reservas/consultar-reserva/:codigoReserva",
  createProxyMiddleware({
    target: reservaServiceUrl, // URL do microserviço de reserva
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/reservas/consultar-reserva",
        "/ms-reserva/consultar-reserva"
      ),
  })
);

// ==============================================[RESERVA]==============================================

// Iniciar o API Gateway
app.listen(port, () => {
  console.log(`API Gateway rodando em http://localhost:${port}`);
});
