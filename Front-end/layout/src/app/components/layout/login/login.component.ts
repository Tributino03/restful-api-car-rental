import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { LoginService } from '../../../auth/login.service';
import { Login } from '../../../models/login';

@Component({
  selector: 'app-login',
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  login : Login = new Login();

  router = inject(Router);

  loginService = inject(LoginService);

  constructor() {
    this.loginService.removerToken();
  }

  logar(){
    
    this.loginService.logar(this.login).subscribe({
      next: token => {
        if(token){
          this.loginService.addToken(token);
          this.router.navigate(['admin/cars']);
        }else{
          alert('usuário ou senhas incorretos!')
        }
      },
      error: erro => {
        alert('deu erro');
      }
    })

  }

}
