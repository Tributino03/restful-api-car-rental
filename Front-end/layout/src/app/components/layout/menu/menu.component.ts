import { Component, inject } from '@angular/core';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { Router, RouterLink } from '@angular/router'; 
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
  router = inject(Router);
  usuario: Usuario | null = null;

  constructor() {
    this.usuario = this.loginService.getUsuarioLogado();
  }

  logout() {
    this.loginService.removerToken();
    this.router.navigate(['/login']);
  }


}