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

// SAGA
const funcionarioServiceSagaUrl = process.env.MS_FUNCIONARIO_SAGA_URL;
const voosServiceSagaUrl = process.env.MS_VOOS_SAGA_URL;

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

//localhost:8085/saga/ms-voos/cancelar-voo/TADS0081

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
