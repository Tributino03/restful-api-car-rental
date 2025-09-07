import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Landlords } from '../../../models/landlord';
import { LandlordService } from '../../../services/landlord.service';
import { CommonModule } from '@angular/common';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-landlordslist',
  standalone: true,
  imports: [CommonModule, MdbRippleModule],
  templateUrl: './landlordslist.component.html',
  styleUrls: ['./landlordslist.component.scss']
})
export class LandlordslistComponent implements OnInit {

  landlordList: Landlords[] = [];

  private router = inject(Router);
  private landlordService = inject(LandlordService);
  loginService = inject(LoginService);

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.landlordService.findAll().subscribe({
      next: list => { this.landlordList = list; },
      error: () => { Swal.fire('Erro!', 'Ocorreu um erro ao carregar a lista de locadores.', 'error'); }
    });
  }

  new() {
    this.router.navigate(['/admin/landlords/new']);
  }

  edit(landlord: Landlords) {
    this.router.navigate(['/admin/landlords/edit', landlord.id]);
  }

}