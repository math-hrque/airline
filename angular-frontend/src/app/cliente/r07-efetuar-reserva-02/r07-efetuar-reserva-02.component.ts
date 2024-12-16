import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PipeDinheiroBRPipe } from '../../shared/pipes/pipe-dinheiro-br.pipe';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { VooGateway } from '../../shared/models/api-gateway/voo-gateway';
import { AuthGatewayService } from '../../services/api-gateway/auth-gateway.service';
import { MilhasGatewayService } from '../../services/api-gateway/milhas-gateway.service';
import { MilhaDetalhesGateway } from '../../shared/models/api-gateway/milha-detalhes-gateway.model';
import { ReservaGatewayService } from '../../services/api-gateway/reserva-gateway.service';
import { ReservaCriacaoGateway } from '../../shared/models/api-gateway/reserva-criacao-gateway.model';

@Component({
  selector: 'app-r07-efetuar-reserva-02',
  standalone: true,
  imports: [CommonModule, FormsModule, PipeDinheiroBRPipe, NgxMaskDirective],
  templateUrl: './r07-efetuar-reserva-02.component.html',
  styleUrl: './r07-efetuar-reserva-02.component.css',
})
export class R07EfetuarReserva02Component implements OnInit {
  vooSelecionado!: VooGateway;
  reserva: ReservaCriacaoGateway = new ReservaCriacaoGateway();

  qntMilhasCliente: number = 0;
  qntPassagens: number = 0;
  valorTotal: number = 0;
  valorMilhas: number = 0;

  idCliente: number = 0;

  qntMilhasPagamento: number = 0;
  qntMilhasSaldoFinal: number = 0;

  mostradados1: boolean = false;

  constructor(
    private router: Router,
    private reservaGatewayService: ReservaGatewayService,
    private authGatewayService: AuthGatewayService,
    private milhaGatewayService: MilhasGatewayService
  ) {}

  ngOnInit(): void {
    this.vooSelecionado = history.state.vooSelecionado;

    if (!this.vooSelecionado) {
      this.router.navigate(['/efetuar-reserva']);
    }

    this.getClienteByUser();
  }

  getClienteByUser(): void {
    const usuario = this.authGatewayService.getUser();
    if (usuario) {
      const role = this.authGatewayService.getRoleFromToken();
      let idUsuario: number | null = null;

      if (role === 'CLIENTE' && 'idCliente' in usuario) {
        idUsuario = Number(usuario.idCliente);
        this.idCliente = idUsuario;
      } else if (role === 'FUNCIONARIO' && 'idFuncionario' in usuario) {
        idUsuario = Number(usuario.idFuncionario);
        this.idCliente = idUsuario;
      }

      if (idUsuario !== null) {
        this.milhaGatewayService.consultarExtrato(idUsuario).subscribe({
          next: (extrato: MilhaDetalhesGateway | null) => {
            if (extrato) {
              this.qntMilhasCliente = extrato.saldoMilhas;
            }
          },
          error: (err) => {
            console.error('Erro ao consultar extrato de milhas', err);
          },
        });
      }
    }
  }

  calculaValorPassagem(): void {
    this.valorTotal = this.reserva.quantidadePoltronas * this.vooSelecionado.valorPassagem;
    this.valorMilhas = this.valorTotal / 5;
  }

  pagarReserva(): void {
    this.qntMilhasSaldoFinal = this.valorTotal - this.qntMilhasPagamento * 5;
    this.mostradados1 = true;
  }

  confirmaPagamento(): void {
    this.reserva.codigoReserva = null;
    this.reserva.dataReserva = null;
    this.reserva.valorReserva = this.qntMilhasSaldoFinal;
    this.reserva.milhasUtilizadas = this.qntMilhasPagamento;
    this.reserva.quantidadePoltronas = this.reserva.quantidadePoltronas;
    this.reserva.idCliente = this.idCliente;
    this.reserva.siglaEstadoReserva = null;
    this.reserva.tipoEstadoReserva = null;
    this.reserva.codigoVoo = this.vooSelecionado.codigoVoo;
    this.reserva.codigoAeroportoOrigem = this.vooSelecionado.aeroportoOrigem.codigoAeroporto;
    this.reserva.codigoAeroportoDestino = this.vooSelecionado.aeroportoDestino.codigoAeroporto;

    this.reservaGatewayService.criarReserva(this.reserva).subscribe({
      next: (response) => {
        if (response) {
          alert(
            'Reserva criada com sucesso! Código da Reserva: ' +
              response.codigoReserva
          );

          this.router.navigate(['/homepage-cliente']);
        } else {
          alert('Falha ao criar a reserva. Tente novamente.');
        }
      },
      error: (err) => {
        console.error('Erro ao criar a reserva:', err);
        alert('Falha ao criar a reserva. Tente novamente.');
      },
    });
  }
}
