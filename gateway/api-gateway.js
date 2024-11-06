require('dotenv').config();
const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();
const port = process.env.PORT || 3000;

// Microservice URLs from environment variables
const services = {
    'auth': process.env.MS_AUTH_URL,
    'cliente': process.env.MS_CLIENTE_URL,
    'funcionario': process.env.MS_FUNCIONARIO_URL,
    'reserva': process.env.MS_RESERVA_URL,
    'voos': process.env.MS_VOOS_URL
};

// Middleware to log requests
app.use((req, res, next) => {
    console.log(`Received request for ${req.url}`);
    next();
});

// Adiciona uma rota específica para listar funcionários
app.get('/api/funcionarios', createProxyMiddleware({
    target: services['funcionario'],
    changeOrigin: true,
    pathRewrite: {
        '^/api/funcionarios': '/listar-funcionario' // mapeia a rota local para a rota do serviço
    }
}));

// Adiciona uma rota específica para consultar funcionários por email
app.get('/api/funcionarios/email/:email', createProxyMiddleware({
    target: services['funcionario'],
    changeOrigin: true,
    pathRewrite: (path, req) => path.replace('/api/funcionarios/email', '/consultar-email')
}));

// Adiciona uma rota específica para consultar funcionários por ID
app.get('/api/funcionarios/id/:idFuncionario', createProxyMiddleware({
    target: services['funcionario'],
    changeOrigin: true,
    pathRewrite: (path, req) => path.replace('/api/funcionarios/id', '/consultar-idfuncionario')
}));

// Set up proxy for each microservice
app.use('/auth', createProxyMiddleware({ target: services['auth'], changeOrigin: true }));
app.use('/cliente', createProxyMiddleware({ target: services['cliente'], changeOrigin: true }));
app.use('/funcionario', createProxyMiddleware({ target: services['funcionario'], changeOrigin: true }));
app.use('/reserva', createProxyMiddleware({ target: services['reserva'], changeOrigin: true }));
app.use('/voos', createProxyMiddleware({ target: services['voos'], changeOrigin: true }));

// Start the API Gateway
app.listen(port, () => {
    console.log(`API Gateway running at http://localhost:${port}`);
});
