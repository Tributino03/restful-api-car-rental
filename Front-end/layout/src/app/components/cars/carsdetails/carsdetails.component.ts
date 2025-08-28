import { Component, OnInit, inject, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';

import { Car } from '../../../models/car';
import { Brand } from '../../../models/brand';
import { CarService } from '../../../services/car.service';
import { BrandService } from '../../../services/brand.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-carsdetails',
  standalone: true,
  imports: [CommonModule, MdbFormsModule, FormsModule],
  templateUrl: './carsdetails.component.html',
  styleUrls: ['./carsdetails.component.scss']
})
export class CarsdetailsComponent implements OnInit {

  @Input() car: Car = new Car();
  @Output() retorno = new EventEmitter<Car>();

  brandList: Brand[] = [];
  isEditing: boolean = false;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private carService = inject(CarService);
  private brandService = inject(BrandService);

  ngOnInit(): void {
    this.loadBrandList();
    const id = this.route.snapshot.params['id'];
    if (id > 0) {
      this.isEditing = true;
      this.findById(id);
    }
  }

  loadBrandList() {
    this.brandService.listAll().subscribe({
      next: brands => { this.brandList = brands; },
      error: () => { Swal.fire('Erro!', 'Ocorreu um erro ao carregar a lista de marcas.', 'error'); }
    });
  }

  findById(id: number) {
    this.carService.findById(id).subscribe({
      next: car => { this.car = car; },
      error: () => { Swal.fire('Não encontrado!', `Ocorreu um erro ao localizar o carro com ID ${id}.`, 'error'); }
    });
  }

  save() {
    let apiCall$: Observable<Car>;

    if (this.isEditing) {
      apiCall$ = this.carService.update(this.car);
    } else {
      apiCall$ = this.carService.create(this.car);
    }

    apiCall$.subscribe({
      next: carSalvo => {
        Swal.fire({
          title: 'Salvo com sucesso!',
          icon: 'success',
          confirmButtonText: 'Ok'
        }).then(() => {
          this.retorno.emit(carSalvo);
        });
      },
      error: erro => {
        const errorMessage = (typeof erro.error === 'string') ? erro.error : 'Ocorreu um erro desconhecido.';
        Swal.fire({
          title: 'Erro!',
          text: `Ocorreu um erro ao salvar: ${errorMessage}`,
          icon: 'error',
          confirmButtonText: 'Ok'
        });
      }
    });
  }

  compareBrands(b1: Brand, b2: Brand): boolean {
    return b1 && b2 ? b1.id === b2.id : b1 === b2;
  }
}