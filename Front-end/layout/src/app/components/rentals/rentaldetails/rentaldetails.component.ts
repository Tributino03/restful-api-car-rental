import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';

import { Rental } from '../../../models/rental';
import { Car } from '../../../models/car';
import { Landlords } from '../../../models/landlord';

import { RentalService } from '../../../services/rental.service';
import { CarService } from '../../../services/car.service';
import { LandlordService } from '../../../services/landlord.service';

@Component({
  selector: 'app-rentaldetails',
  standalone: true,
  imports: [CommonModule, FormsModule, MdbFormsModule],
  templateUrl: './rentaldetails.component.html',
  styleUrls: ['./rentaldetails.component.scss']
})
export class RentaldetailsComponent implements OnInit {
  
  rental: Rental = new Rental();
  carList: Car[] = [];
  landlordList: Landlords[] = [];
  
  private router = inject(Router);
  private rentalService = inject(RentalService);
  private carService = inject(CarService);
  private landlordService = inject(LandlordService);

  ngOnInit(): void {
    this.loadCarList();
    this.loadLandlordList();
  }

  loadCarList() {
    this.carService.listAll().subscribe({
      next: list => { this.carList = list; },
      error: () => { Swal.fire('Erro', 'Não foi possível carregar a lista de carros.', 'error'); }
    });
  }

  loadLandlordList() {
    this.landlordService.findAll().subscribe({
      next: list => { this.landlordList = list; },
      error: () => { Swal.fire('Erro', 'Não foi possível carregar a lista de locadores.', 'error'); }
    });
  }

  save() {
    this.rentalService.create(this.rental).subscribe({
      next: newRental => {
        Swal.fire({
          title: 'Aluguel Registrado!',
          html: `O aluguel foi registrado com sucesso. <br> Valor Total: <strong>R$ ${newRental.totalValue.toFixed(2)}</strong>`,
          icon: 'success'
        });
        this.router.navigate(['/admin/rentals']);
      },
      error: erro => {
        Swal.fire({
          title: 'Erro!',
          text: `Ocorreu um erro ao registrar o aluguel: ${erro.error}`,
          icon: 'error'
        });
      }
    });
  }

  compareById(item1: any, item2: any): boolean {
    return item1 && item2 ? item1.id === item2.id : item1 === item2;
  }
}