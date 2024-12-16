import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, NgForm } from '@angular/forms';
import { ReservaGatewayService } from '../../services/api-gateway/reserva-gateway.service';
import { VooGateway } from '../../shared/models/api-gateway/voo-gateway';
import { ReservaGateway } from '../../shared/models/api-gateway/reserva-gateway.model';

@Component({
  selector: 'app-r12-confirmacao-embarque',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './r12-confirmacao-embarque.component.html',
  styleUrl: './r12-confirmacao-embarque.component.css',
})
export class R12ConfirmacaoEmbarqueComponent implements OnInit {
  @Input() vooRecebido!: VooGateway;
  @ViewChild('formCodigoreserva') formCodigoReserva!: NgForm;

  ngOnInit() {}

  private reservas: ReservaGateway[] = [];
  codigoReserva: string = '';
  reservaNaoEncontrada: boolean = false;
  reservaEncontrada: boolean = false;
  reserva!: ReservaGateway;

  constructor(
    private reservaGatewayService: ReservaGatewayService,
    public activeModal: NgbActiveModal
  ) {}

  consultarReserva() {
    this.reservaGatewayService.consultarReserva(this.codigoReserva).subscribe({
      next: (data: ReservaGateway | null) => {
        if (data == null) {
          this.reservaNaoEncontrada = true;
          this.reservaEncontrada = false;
        } else {
          this.reserva = data;
          if (this.reserva.voo.codigoVoo === this.vooRecebido.codigoVoo) {
            this.reservaEncontrada = true;
            this.reservaNaoEncontrada = false;
          } else {
            this.reservaNaoEncontrada = true;
            this.reservaEncontrada = false;
          }
        }
      },
      error: (err) => {
        this.reservaNaoEncontrada = true;
        this.reservaEncontrada = false;
      },
    });
  }

  confirmaReserva() {
    this.reservaGatewayService
      .confirmarEmbarque(this.reserva.voo.codigoVoo, this.reserva.codigoReserva)
      .subscribe({
        next: (response) => {
          if (response) {
            alert(
              'Reserva alterada com sucesso! Status da Reserva: ' +
                this.reserva.tipoEstadoReserva
            );
            this.activeModal.close();
          } else {
            alert(
              'Reserva alterada com sucesso! Status da Reserva: ' +
                this.reserva.tipoEstadoReserva
            );
          }
        },
        error: (err) => {
          console.error('Erro ao confirmar a reserva:', err);
          alert(
            'Reserva alterada com sucesso! Status da Reserva: ' +
              this.reserva.tipoEstadoReserva
          );
        },
      });
  }
}
