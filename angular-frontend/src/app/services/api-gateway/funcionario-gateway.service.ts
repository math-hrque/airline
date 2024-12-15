import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { FuncionarioGateway } from '../../shared/models/api-gateway/funcionario-gateway.model';

@Injectable({
  providedIn: 'root',
})
export class FuncionarioGatewayService {
  constructor(private _http: HttpClient) {}

  NEW_URL = 'http://localhost:3015/api';

  httpOptions = {
    observe: 'response' as 'response',
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  atualizarFuncionario(
    funcionario: FuncionarioGateway
  ): Observable<FuncionarioGateway | null> {
    return this._http
      .put<FuncionarioGateway>(
        `${this.NEW_URL}/funcionarios/atualizar-funcionario`,
        JSON.stringify(funcionario),
        this.httpOptions
      )
      .pipe(
        map((resp: HttpResponse<FuncionarioGateway>) => {
          if (resp.status === 202) {
            return resp.body;
          } else {
            return null;
          }
        }),
        catchError((err) => {
          console.error('Erro ao atualizar funcionário', err);
          return throwError(() => err);
        })
      );
  }

  inativarFuncionario(email: string): Observable<FuncionarioGateway | null> {
    return this._http
      .put<FuncionarioGateway>(
        `${this.NEW_URL}/funcionarios/inativar/${email}`,
        null,
        this.httpOptions
      )
      .pipe(
        map((resp: HttpResponse<FuncionarioGateway>) => {
          if (resp.status === 202) {
            return resp.body;
          } else {
            return null;
          }
        }),
        catchError((err) => {
          console.error('Erro ao excluir funcionário:', err);
          return throwError(() => err);
        })
      );
  }

  getAllFuncionarios(): Observable<FuncionarioGateway[] | null> {
    return this._http
      .get<FuncionarioGateway[]>(
        `${this.NEW_URL}/funcionarios`,
        this.httpOptions
      )
      .pipe(
        map((resp: HttpResponse<FuncionarioGateway[]>) => {
          if (resp.status === 200) {
            return resp.body;
          } else {
            return [];
          }
        }),
        catchError((err) => {
          if (err.status === 404) {
            return of([]);
          } else {
            return throwError(() => err);
          }
        })
      );
  }

  getFuncionarioById(id: number): Observable<FuncionarioGateway | null> {
    return this._http
      .get<FuncionarioGateway>(
        `${this.NEW_URL}/funcionarios/id/${id}`,
        this.httpOptions
      )
      .pipe(
        map((resp: HttpResponse<FuncionarioGateway>) => {
          if (resp.status === 200) {
            return resp.body;
          } else {
            return null;
          }
        }),
        catchError((err) => {
          if (err.status === 404) {
            return of(null);
          } else {
            return throwError(() => err);
          }
        })
      );
  }

  postFuncionario(
    funcionario: FuncionarioGateway
  ): Observable<FuncionarioGateway | null> {
    return this._http
      .post<FuncionarioGateway>(
        `${this.NEW_URL}/funcionarios`,
        JSON.stringify(funcionario),
        this.httpOptions
      )
      .pipe(
        map((resp: HttpResponse<FuncionarioGateway>) => {
          if (resp.status === 202) {
            return resp.body;
          } else {
            return null;
          }
        }),
        catchError((err) => {
          console.error('Erro ao criar funcionário', err);
          return throwError(() => err);
        })
      );
  }
}
