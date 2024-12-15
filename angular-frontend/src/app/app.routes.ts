import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './components/page-not-found';
import { authGuard } from './auth/auth.guard';
import { R16ListagemDeFuncionariosComponent } from './funcionario/r16-listagem-de-funcionarios/r16-listagem-de-funcionarios.component';
import { R03MostrarTelaInicialClienteComponent } from './cliente/r03-mostrar-tela-inicial-cliente/r03-mostrar-tela-inicial-cliente.component';
import { R05ComprarMilhasComponent } from './cliente/r05-comprar-milhas/r05-comprar-milhas.component';
import { R07EfetuarReservaComponent } from './cliente/r07-efetuar-reserva/r07-efetuar-reserva.component';
import { R10FazerCheckInComponent } from './cliente/r10-fazer-check-in/r10-fazer-check-in.component';
import { R06ConsultarExtratoMilhasComponent } from './cliente/r06-consultar-extrato-milhas/r06-consultar-extrato-milhas.component';
import { R09ConsultarReservaComponent } from './cliente/r09-consultar-reserva/r09-consultar-reserva.component';
import { R11TelaInicialFuncionarioComponent } from './funcionario/r11-tela-inicial-funcionario/r11-tela-inicial-funcionario.component';
import { R15CadastroDeVooComponent } from './funcionario/r15-cadastro-de-voo/r15-cadastro-de-voo.component';
import { R02EfetuarLoginLogoutComponent } from './cliente/r02-efetuar-login-logout/r02-efetuar-login-logout.component';
import { R01AutocadastroComponent } from './cliente/r01-autocadastro/r01-autocadastro.component';
import { R07EfetuarReserva02Component } from './cliente/r07-efetuar-reserva-02/r07-efetuar-reserva-02.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', title: 'Login', component: R02EfetuarLoginLogoutComponent },
  { path: 'cadastro', title: 'Cadastro', component: R01AutocadastroComponent },

  {
    path: 'homepage-cliente',
    title: 'homepage-cliente',
    component: R03MostrarTelaInicialClienteComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },
  {
    path: 'comprar-milhas',
    title: 'comprar-milhas',
    component: R05ComprarMilhasComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },
  {
    path: 'efetuar-reserva',
    title: 'efetuar-reserva',
    component: R07EfetuarReservaComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },
  {
    path: 'fazer-check-in',
    title: 'fazer-check-in',
    component: R10FazerCheckInComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },
  {
    path: 'consultar-extrato-milhas',
    title: 'consultar-extrato-milhas',
    component: R06ConsultarExtratoMilhasComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },
  {
    path: 'consultar-reserva',
    title: 'consultar-reserva',
    component: R09ConsultarReservaComponent,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },

  {
    path: 'homepage',
    title: 'Homepage',
    component: R11TelaInicialFuncionarioComponent,
    canActivate: [authGuard],
    data: {
      role: 'FUNCIONARIO',
    },
  },

  {
    path: 'cadastro-voos',
    title: 'cadastro-voos',
    component: R15CadastroDeVooComponent,
    canActivate: [authGuard],
    data: {
      role: 'FUNCIONARIO',
    },
  },
  {
    path: 'gerenciar-funcionarios',
    title: 'GerenciarFuncionarios',
    component: R16ListagemDeFuncionariosComponent,
    canActivate: [authGuard],
    data: {
      role: 'FUNCIONARIO',
    },
  },
  {
    path: 'efetuar-reserva-2',
    title: 'EfetuarReserva2',
    component: R07EfetuarReserva02Component,
    canActivate: [authGuard],
    data: {
      role: 'CLIENTE',
    },
  },

  { path: '**', title: 'Error 404', component: PageNotFoundComponent },
];
