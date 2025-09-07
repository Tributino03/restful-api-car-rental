import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './login.service';


export const loginGuard: CanActivateFn = (route, state) => {

  const loginService = inject(LoginService);
  const router = inject(Router);

  const restrictedRoutesForUser = [
    '/admin/cars/new',
    '/admin/cars/edit', 
    '/admin/brand/new',
    '/admin/brand/edit',
    '/admin/landlords/new',
    '/admin/landlords/edit',
    '/admin/rentals/new'
  ];

  if (loginService.hasRole('USER') && restrictedRoutesForUser.some(r => state.url.includes(r))) {
    alert('Você não tem permissão de acesso a essa rota!');
    router.navigate(['/admin/cars']); 
    return false;
  }

  return true;
};