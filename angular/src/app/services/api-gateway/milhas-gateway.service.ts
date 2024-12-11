import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { MilhaDetalhesGateway } from '../../shared/models/api-gateway/milha-detalhes-gateway.model';

@Injectable({
  providedIn: 'root',
})
export class MilhasGatewayService {
  constructor(private _http: HttpClient) {}

  private readonly NEW_URL = 'http://localhost:3015/api';

  private readonly httpOptions = {
    observe: 'response' as 'response',
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  comprarMilhas(idCliente: number, quantidadeMilhas: number): Observable<any> {
    const url = `${this.NEW_URL}/comprar-milhas?idCliente=${idCliente}&quantidadeMilhas=${quantidadeMilhas}`;

    return this._http.post<any>(url, null, this.httpOptions).pipe(
      map((resp) => {
        if (resp.status === 200) {
          return resp.body;
        } else {
          return null;
        }
      }),
      catchError((err) => {
        console.error('Erro ao comprar milhas', err);
        throw err;
      })
    );
  }

  consultarExtrato(idCliente: number): Observable<MilhaDetalhesGateway | null> {
    const url = `${this.NEW_URL}/extrato-milhas/${idCliente}`;

    return this._http
      .get<MilhaDetalhesGateway>(url, { observe: 'response' })
      .pipe(
        map((resp: HttpResponse<MilhaDetalhesGateway>) => {
          if (resp.status === 200) {
            return resp.body;
          } else {
            return new MilhaDetalhesGateway();
          }
        }),
        catchError((err) => {
          console.error('Erro ao consultar extrato de milhas', err);
          return throwError(err);
        })
      );
  }
}
