import { Component, inject } from '@angular/core';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { RouterLink } from '@angular/router'; 
import { LoginService } from '../../../auth/login.service';
import { Usuario } from '../../../auth/usuario';

@Component({
  selector: 'app-menu',
  standalone: true, 
  imports: [MdbCollapseModule, RouterLink],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  loginService = inject(LoginService);
  usuario! : Usuario;

  constructor(){
    this.usuario =  this.loginService.getUsuarioLogado();
  }
}