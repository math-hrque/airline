import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StateService {
  private updateFuncionariosSubject = new Subject<void>();
  private updateVoosFuncionariosSubject = new Subject<void>();

  updateFuncionarios$ = this.updateFuncionariosSubject.asObservable();
  updateVoosFuncionarios$ = this.updateVoosFuncionariosSubject.asObservable();

  triggerUpdateListagemFuncionarios() {
    this.updateFuncionariosSubject.next();
  }

  triggerUpdateListagemVoosFuncionarios() {
    this.updateVoosFuncionariosSubject.next();
  }
}
