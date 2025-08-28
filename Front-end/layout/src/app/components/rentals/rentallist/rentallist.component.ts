import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Rental } from '../../../models/rental';
import { RentalService } from '../../../services/rental.service';
import { CommonModule } from '@angular/common';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';

@Component({
  selector: 'app-rentallist',
  standalone: true,
  imports: [CommonModule, MdbRippleModule],
  templateUrl: './rentallist.component.html',
  styleUrls: ['./rentallist.component.scss']
})
export class RentallistComponent implements OnInit {
  
  rentalList: Rental[] = [];

  private router = inject(Router);
  private rentalService = inject(RentalService);

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.rentalService.findAll().subscribe({
      next: list => { this.rentalList = list; },
      error: () => { Swal.fire('Erro', 'Não foi possível carregar a lista de aluguéis.', 'error'); }
    });
  }

  new() {
    this.router.navigate(['/admin/rentals/new']);
  }
}