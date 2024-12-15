require("dotenv").config();
const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors");
const axios = require("axios");

const app = express();
const port = process.env.PORT || 3000;

const funcionarioServiceUrl = process.env.MS_FUNCIONARIO_URL;
const reservaServiceUrl = process.env.MS_RESERVA_URL;
const voosServiceUrl = process.env.MS_VOOS_URL;
const clienteServiceUrl = process.env.MS_CLIENTE_URL;
const loginServiceUrl = process.env.MS_AUTH_URL;

const funcionarioServiceSagaUrl = process.env.MS_FUNCIONARIO_SAGA_URL;
const voosServiceSagaUrl = process.env.MS_VOOS_SAGA_URL;
const clienteServiceSagaUrl = process.env.MS_CLIENTE_SAGA_URL;
const reservaServiceSagaUrl = process.env.MS_RESERVA_SAGA_URL;

app.use(
  cors({
    origin: ["http://localhost:4200"],
    methods: ["GET", "POST", "PUT", "DELETE"],
    allowedHeaders: ["Content-Type", "Authorization"],
    credentials: true,
  })
);

app.use((req, res, next) => {
  next();
});

// ==============================================[LOGIN]================================================

app.post(
  "/api/auth/login",
  createProxyMiddleware({
    target: loginServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/auth/login",
        "/ms-auth/login"
      ),
  })
);

// ==============================================[LOGIN]================================================

// ==============================================[CLIENTE]==============================================

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

app.post(
  "/api/clientes/cadastrar-cliente",
  createProxyMiddleware({
    target: clienteServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/clientes/cadastrar-cliente",
        "/saga/ms-cliente/cadastrar-cliente"
      ),
  })
);

app.get(
  "/api/clientes/email/:email",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/clientes/email",
        "/ms-cliente/consultar-email"
      ),
  })
);

// ==============================================[CLIENTE]==============================================

// ==============================================[MILHAS]===============================================

app.post(
  "/api/comprar-milhas",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/comprar-milhas", "/ms-cliente/comprar-milhas"),
  })
);

app.get(
  "/api/extrato-milhas/:idCliente",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/extrato-milhas", "/ms-cliente/listar-milhas"),
  })
);

// ==============================================[MILHAS]================================================

// ==============================================[FUNCIONARIOS]==========================================

app.get(
  "/api/funcionarios",
  createProxyMiddleware({
    target: funcionarioServiceUrl,
    changeOrigin: true,
    pathRewrite: {
      "^/api/funcionarios": "/ms-funcionario/listar-funcionario",
    },
  })
);

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

app.put(
  "/api/funcionarios/atualizar-funcionario",
  createProxyMiddleware({
    target: funcionarioServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios/atualizar-funcionario",
        "/saga/ms-funcionario/atualizar-funcionario"
      ),
  })
);

app.post(
  "/api/funcionarios",
  createProxyMiddleware({
    target: funcionarioServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/funcionarios",
        "/saga/ms-funcionario/cadastrar-funcionario"
      ),
  })
);

// ==============================================[FUNCIONARIOS]==========================================

// ==============================================[VOOS]==================================================

app.get(
  "/api/listar-aeroporto",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/listar-aeroporto", "/ms-voos/listar-aeroporto"),
  })
);

app.get(
  "/api/voos",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: {
      "^/api/voos": "/ms-voos/listar-voos-48h",
    },
  })
);

app.get(
  "/api/voos/:codigoVoo",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos", "/ms-voos/visualizar-voo"),
  })
);

app.get(
  "/api/voos-atuais",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos-atuais", "/ms-voos/listar-voos-atuais"),
  })
);

app.post(
  "/api/voos/cadastrar-voo",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/cadastrar-voo", "/ms-voos/cadastrar-voo"),
  })
);

app.put(
  "/api/voos/cancelar-voo/:codigoVoo",
  createProxyMiddleware({
    target: voosServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/cancelar-voo", "/saga/ms-voos/cancelar-voo"),
  })
);

app.put(
  "/api/voos/realizar-voo/:codigoVoo",
  createProxyMiddleware({
    target: voosServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos/realizar-voo", "/saga/ms-voos/realizar-voo"),
  })
);

app.get("/api/voos-realizados-cancelados/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    const voosResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/listar-voos-realizados-cancelados`
    );
    const voosData = voosResponse.data;

    if (!voosData || voosData.length === 0) {
      return res
        .status(404)
        .json({ message: "Nenhum voo encontrado" });
    }

    const reservasResponse = await axios({
      method: "get",
      url: `${reservaServiceUrl}/ms-reserva/listar-reservas-voos-realizados-cancelados/${idUsuario}`,
      headers: { "Content-Type": "application/json" },
      data: voosData,
    });

    const reservasData = reservasResponse.data;

    for (let i = 0; i < reservasData.length; i++) {
      const voo = reservasData[i];

      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${voo.voo.codigoVoo}`
        );
        const vooDetails = vooDetailsResponse.data;

        if (vooDetails && Object.keys(vooDetails).length > 0) {
          reservasData[i].voo = { ...vooDetails };
        } else {
          reservasData[i].voo = { ...voo, detalhes: null };
        }
      } catch (error) {
        console.error(
          `Erro ao consultar detalhes do voo ${voo.codigoVoo}:`,
          error
        );
        reservasData[i].voo = { ...voo, detalhes: null };
      }
    }

    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar voos ou reservas:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar voos ou reservas" });
  }
});

// ==============================================[VOOS]================================================

// ==============================================[RESERVA]=============================================

app.put(
  "/api/reservas/confirmar-embarque",
  createProxyMiddleware({
    target: reservaServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/reservas/confirmar-embarque",
        "/ms-reserva/confirmar-embarque"
      ),
  })
);

app.put(
  "/api/reservas/fazer-checkin/:codigoReserva",
  createProxyMiddleware({
    target: reservaServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) => {
      console.log(
        `Redirecionando para: ${reservaServiceUrl}/ms-reserva/fazer-checkin/${req.params.codigoReserva}`
      );
      return path.replace(
        "/api/reservas/fazer-checkin",
        "/ms-reserva/fazer-checkin"
      );
    },
  })
);

app.put(
  "/api/reservas/cancelar-reserva/:codigoReserva",
  createProxyMiddleware({
    target: reservaServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) => {
      return path.replace(
        "/api/reservas/cancelar-reserva",
        `/saga/ms-reserva/cancelar-reserva`
      );
    },
  })
);

app.post(
  "/api/reservas/cadastrar-reserva",
  createProxyMiddleware({
    target: reservaServiceSagaUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/reservas/cadastrar-reserva",
        "/saga/ms-reserva/cadastrar-reserva"
      ),
  })
);

app.get("/api/listar-reservas-cliente/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    const reservasResponse = await axios.get(
      `${reservaServiceUrl}/ms-reserva/listar-reservas-cliente/${idUsuario}`
    );
    const reservasData = reservasResponse.data;

    if (!reservasData || reservasData.length === 0) {
      return res.status(404).json({ message: "Nenhuma reserva encontrada" });
    }

    for (let i = 0; i < reservasData.length; i++) {
      const reserva = reservasData[i];
      const vooCodigo = reserva.voo.codigoVoo;

      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${vooCodigo}`
        );
        const vooDetails = vooDetailsResponse.data;

        if (vooDetails && Object.keys(vooDetails).length > 0) {
          reservasData[i].voo = { ...vooDetails };
        } else {
          reservasData[i].voo = { ...reserva.voo, detalhes: null };
        }
      } catch (error) {
        console.error(`Erro ao consultar detalhes do voo ${vooCodigo}:`, error);
        reservasData[i].voo = { ...reserva.voo, detalhes: null };
      }
    }

    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar reservas ou voos:", error);
    return res.status(500).json({ message: "Erro interno ao consultar reservas ou voos" });
  }
});

app.get("/api/reservas/consultar-reserva/:codigoReserva", async (req, res) => {
  const { codigoReserva } = req.params;

  try {
    const reservaResponse = await axios.get(
      `${reservaServiceUrl}/ms-reserva/consultar-reserva/${codigoReserva}`
    );
    const reservaData = reservaResponse.data;

    if (!reservaData) {
      return res.status(404).json({ message: "Reserva não encontrada" });
    }

    const codigoVoo = reservaData.voo.codigoVoo;
    if (!codigoVoo) {
      return res
        .status(404)
        .json({ message: "Código de voo não encontrado na reserva" });
    }

    const vooResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/visualizar-voo/${codigoVoo}`
    );
    const vooData = vooResponse.data;

    if (!vooData) {
      return res.status(404).json({ message: "Voo não encontrado" });
    }

    reservaData.voo = vooData;

    return res.json(reservaData);
  } catch (error) {
    console.error("Erro ao consultar reserva ou voo:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar reserva ou voo" });
  }
});

app.get("/api/reservas-voos-48h/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    const voosResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/listar-voos-48h`
    );
    const voosData = voosResponse.data;

    if (!voosData || voosData.length === 0) {
      return res
        .status(404)
        .json({ message: "Nenhum voo encontrado nas próximas 48 horas" });
    }

    const reservasResponse = await axios({
      method: "get",
      url: `${reservaServiceUrl}/ms-reserva/listar-reservas-voos-48h/${idUsuario}`,
      data: voosData,
      headers: { "Content-Type": "application/json" },
    });
    const reservasData = reservasResponse.data;

    for (let i = 0; i < reservasData.length; i++) {
      const voo = reservasData[i];

      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${voo.voo.codigoVoo}`
        );
        const vooDetails = vooDetailsResponse.data;

        if (vooDetails && Object.keys(vooDetails).length > 0) {
          reservasData[i].voo = { ...vooDetails };
        } else {
          reservasData[i].voo = { ...voo, detalhes: null };
        }
      } catch (error) {
        console.error(
          `Erro ao consultar detalhes do voo ${voo.codigoVoo}:`,
          error
        );
        reservasData[i].voo = { ...voo, detalhes: null };
      }
    }

    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar voos ou reservas:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar voos ou reservas" });
  }
});

// ==============================================[RESERVA]=============================================

app.listen(port, () => {
  console.log(`API Gateway rodando em http://localhost:${port}`);
});
