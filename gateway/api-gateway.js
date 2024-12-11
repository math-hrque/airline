require("dotenv").config();
const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors"); // Importar o middleware CORS
const axios = require("axios");

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
const reservaServiceSagaUrl = process.env.MS_RESERVA_SAGA_URL;

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

// ==============================================[MILHAS]==============================================

// Seu código para configurar os endpoints
app.post(
  "/api/comprar-milhas",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/comprar-milhas", "/ms-cliente/comprar-milhas"), // Reescreve o caminho para o correto no microserviço
  })
);

// Endpoint para consultar o extrato de milhas
app.get(
  "/api/extrato-milhas/:idCliente",
  createProxyMiddleware({
    target: clienteServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/extrato-milhas", "/ms-cliente/listar-milhas"), // Reescreve o caminho para o correto no microserviço
  })
);

// ==============================================[MILHAS]==============================================

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

// Nova rota para listar aeroportos
app.get(
  "/api/listar-aeroporto", // O endpoint que seu frontend vai acessar
  createProxyMiddleware({
    target: voosServiceUrl, // URL do serviço de voos
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/listar-aeroporto", "/ms-voos/listar-aeroporto"), // Faz o proxy para o endpoint real do microserviço
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

// Rota para obter voo por código do voo
app.get(
  "/api/voos/:codigoVoo", // Recebe o código do voo como parâmetro na URL
  createProxyMiddleware({
    target: voosServiceUrl, // URL do microserviço de voos
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos", "/ms-voos/visualizar-voo"), // Substitui o caminho da URL para o do serviço real
  })
);

// Nova rota para listar voos atuais
app.get(
  "/api/voos-atuais",
  createProxyMiddleware({
    target: voosServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace("/api/voos-atuais", "/ms-voos/listar-voos-atuais"),
  })
);

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


// AQUI
app.get("/api/voos-realizados-cancelados/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    // 1. Consultar os voos realizados e cancelados
    const voosResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/listar-voos-realizados-cancelados`
    );
    const voosData = voosResponse.data;

    if (!voosData || voosData.length === 0) {
      return res
        .status(404)
        .json({ message: "Nenhum voo encontrado" });
    }

    // 2. Enviar a lista de voos para o endpoint de reservas
    const reservasResponse = await axios({
      method: "get",
      url: `${reservaServiceUrl}/ms-reserva/listar-reservas-voos-realizados-cancelados/${idUsuario}`,
      headers: { "Content-Type": "application/json" },
      data: voosData, // Enviar os dados dos voos no corpo da requisição GET
    });

    const reservasData = reservasResponse.data;

    // 3. Preencher os dados de cada voo
    for (let i = 0; i < reservasData.length; i++) {
      const voo = reservasData[i];

      // Consultar os dados detalhados de cada voo utilizando o código do voo
      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${voo.voo.codigoVoo}`
        );
        const vooDetails = vooDetailsResponse.data;

        // Verificar se os dados do voo foram encontrados
        if (vooDetails && Object.keys(vooDetails).length > 0) {
          // Adicionar os dados detalhados ao voo
          reservasData[i].voo = { ...vooDetails };
        } else {
          // Caso não encontre os detalhes do voo, podemos continuar sem preencher
          console.log(`Detalhes não encontrados para o voo ${voo.codigoVoo}`);
          reservasData[i].voo = { ...voo, detalhes: null };
        }
      } catch (error) {
        console.error(
          `Erro ao consultar detalhes do voo ${voo.codigoVoo}:`,
          error
        );
        reservasData[i].voo = { ...voo, detalhes: null }; // Caso de erro, manter os dados sem detalhes
      }
    }

    // 4. Retornar a resposta com os dados detalhados dos voos
    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar voos ou reservas:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar voos ou reservas" });
  }
});


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

// Endpoint para realizar check-in
app.put(
  "/api/reservas/fazer-checkin/:codigoReserva",
  createProxyMiddleware({
    target: reservaServiceUrl, // Serviço de reservas
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
    target: reservaServiceSagaUrl, // URL do microserviço de reservas
    changeOrigin: true,
    pathRewrite: (path, req) => {
      // Reescreve a URL da requisição para o microserviço
      return path.replace(
        "/api/reservas/cancelar-reserva",
        `/saga/ms-reserva/cancelar-reserva`
      );
    },
  })
);

// Rota para cadastrar reserva
// aqui
app.post(
  "/api/reservas/cadastrar-reserva",
  createProxyMiddleware({
    target: reservaServiceSagaUrl, // URL do microserviço de reserva
    changeOrigin: true,
    pathRewrite: (path, req) =>
      path.replace(
        "/api/reservas/cadastrar-reserva",
        "/saga/ms-reserva/cadastrar-reserva"
      ), // Reescreve a URL
  })
);

// // Rota para consultar reserva sem dados do voo
// app.get(
//   "/api/reservas/consultar-reserva/:codigoReserva",
//   createProxyMiddleware({
//     target: reservaServiceUrl, // URL do microserviço de reserva
//     changeOrigin: true,
//     pathRewrite: (path, req) =>
//       path.replace(
//         "/api/reservas/consultar-reserva",
//         "/ms-reserva/consultar-reserva"
//       ),
//   })
// );

app.get("/api/listar-reservas-cliente/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    // 1. Consultar as reservas do usuário no serviço ms-reserva
    const reservasResponse = await axios.get(
      `${reservaServiceUrl}/ms-reserva/listar-reservas-cliente/${idUsuario}`
    );
    const reservasData = reservasResponse.data;

    if (!reservasData || reservasData.length === 0) {
      return res.status(404).json({ message: "Nenhuma reserva encontrada" });
    }

    // 2. Preencher os dados de cada voo
    for (let i = 0; i < reservasData.length; i++) {
      const reserva = reservasData[i];
      const vooCodigo = reserva.voo.codigoVoo; // Assumindo que o código do voo está aqui

      // Consultar os dados detalhados de cada voo utilizando o código do voo
      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${vooCodigo}`
        );
        const vooDetails = vooDetailsResponse.data;

        // Verificar se os dados do voo foram encontrados
        if (vooDetails && Object.keys(vooDetails).length > 0) {
          // Adicionar os dados detalhados ao voo
          reservasData[i].voo = { ...vooDetails };
        } else {
          // Caso não encontre os detalhes do voo, continuar sem preencher
          console.log(`Detalhes não encontrados para o voo ${vooCodigo}`);
          reservasData[i].voo = { ...reserva.voo, detalhes: null };
        }
      } catch (error) {
        console.error(`Erro ao consultar detalhes do voo ${vooCodigo}:`, error);
        reservasData[i].voo = { ...reserva.voo, detalhes: null }; // Caso de erro, manter os dados sem detalhes
      }
    }

    // 3. Retornar a resposta com os dados detalhados dos voos
    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar reservas ou voos:", error);
    return res.status(500).json({ message: "Erro interno ao consultar reservas ou voos" });
  }
});



// Rota para consultar reserva e voo
app.get("/api/reservas/consultar-reserva/:codigoReserva", async (req, res) => {
  const { codigoReserva } = req.params;

  try {
    // 1. Consultar a reserva utilizando a URL completa
    const reservaResponse = await axios.get(
      `${reservaServiceUrl}/ms-reserva/consultar-reserva/${codigoReserva}` // Usando a URL completa
    );
    const reservaData = reservaResponse.data;

    if (!reservaData) {
      return res.status(404).json({ message: "Reserva não encontrada" });
    }

    // 2. Consultar os dados do voo utilizando o códigoVoo da reserva
    const codigoVoo = reservaData.voo.codigoVoo;
    if (!codigoVoo) {
      return res
        .status(404)
        .json({ message: "Código de voo não encontrado na reserva" });
    }

    // 3. Consultar o voo utilizando a URL completa
    const vooResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/visualizar-voo/${codigoVoo}`
    ); // Usando a URL completa
    const vooData = vooResponse.data;

    if (!vooData) {
      return res.status(404).json({ message: "Voo não encontrado" });
    }

    // 4. Combinar as informações de reserva e voo
    reservaData.voo = vooData;

    // 5. Retornar a resposta combinada
    return res.json(reservaData);
  } catch (error) {
    console.error("Erro ao consultar reserva ou voo:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar reserva ou voo" });
  }
});

// OUTRO
// Rota para listar voos das próximas 48 horas e enviar o resultado para o endpoint de reservas
app.get("/api/reservas-voos-48h/:idUsuario", async (req, res) => {
  const { idUsuario } = req.params;

  try {
    // 1. Consultar os voos das próximas 48 horas
    const voosResponse = await axios.get(
      `${voosServiceUrl}/ms-voos/listar-voos-48h`
    );
    const voosData = voosResponse.data;

    if (!voosData || voosData.length === 0) {
      return res
        .status(404)
        .json({ message: "Nenhum voo encontrado nas próximas 48 horas" });
    }

    // 3. Enviar a lista de voos com os dados detalhados para o endpoint de reservas
    console.log("Dados dos voos com detalhes:", voosData); // Log para verificar se os voos foram preenchidos corretamente

    const reservasResponse = await axios({
      method: "get",
      url: `${reservaServiceUrl}/ms-reserva/listar-reservas-voos-48h/${idUsuario}`,
      data: voosData, // Enviar os dados dos voos no corpo da requisição GET
      headers: { "Content-Type": "application/json" },
    });
    const reservasData = reservasResponse.data;

    // 2. Preencher os dados de cada voo
    for (let i = 0; i < reservasData.length; i++) {
      const voo = reservasData[i];

      // Consultar os dados detalhados de cada voo utilizando o código do voo
      try {
        const vooDetailsResponse = await axios.get(
          `${voosServiceUrl}/ms-voos/visualizar-voo/${voo.voo.codigoVoo}`
        );
        const vooDetails = vooDetailsResponse.data;

        // Log para verificar a resposta da API de voos
        // console.log('Resposta do voo:', vooDetails);

        // Verificar se os dados do voo foram encontrados
        if (vooDetails && Object.keys(vooDetails).length > 0) {
          // Adicionar os dados detalhados ao voo
          reservasData[i].voo = { ...vooDetails };
        } else {
          // Caso não encontre os detalhes do voo, podemos continuar sem preencher
          console.log(`Detalhes não encontrados para o voo ${voo.codigoVoo}`);
          reservasData[i].voo = { ...voo, detalhes: null };
        }
      } catch (error) {
        console.error(
          `Erro ao consultar detalhes do voo ${voo.codigoVoo}:`,
          error
        );
        reservasData[i].voo = { ...voo, detalhes: null }; // Caso de erro, manter os dados sem detalhes
      }
    }

    // 4. Retornar a resposta combinada
    return res.json(reservasData);
  } catch (error) {
    console.error("Erro ao consultar voos ou reservas:", error);
    return res
      .status(500)
      .json({ message: "Erro interno ao consultar voos ou reservas" });
  }
});

// ==============================================[RESERVA]==============================================

// Iniciar o API Gateway
app.listen(port, () => {
  console.log(`API Gateway rodando em http://localhost:${port}`);
});
