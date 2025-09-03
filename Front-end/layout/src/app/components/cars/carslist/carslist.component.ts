import { Component, inject, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Car } from '../../../models/car';
import Swal from 'sweetalert2';
import { MdbModalModule, MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { CarsdetailsComponent } from "../carsdetails/carsdetails.component";
import { CarService } from '../../../services/car.service';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';

@Component({
  selector: 'app-carslist',
  standalone: true,
  imports: [CommonModule, MdbModalModule, CarsdetailsComponent, MdbRippleModule],
  templateUrl: './carslist.component.html',
  styleUrls: ['./carslist.component.scss']
})
export class CarslistComponent {

  lista: Car[] = [];
  carEdit: Car = new Car();

  modalService = inject(MdbModalService);
  @ViewChild("modalCarDetails") modalCarDetails!: TemplateRef<any>;
  modalRef!: MdbModalRef<any>;

  carService = inject(CarService);

  constructor() {
    this.listAll();
  }

  listAll() {
    this.carService.listAll().subscribe({
      next: lista => { this.lista = lista; },
      error: () => { Swal.fire('Erro!', 'Ocorreu um erro ao carregar a lista de carros.', 'error'); }
    });
  }

  deleteById(car: Car) {
    Swal.fire({
      title: 'Você tem certeza?',
      text: "Esta ação não pode ser revertida!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sim, excluir!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.carService.delete(car.id).subscribe({
          next: () => {
            Swal.fire('Deletado!', 'O carro foi deletado com sucesso.', 'success');
            this.listAll();
          },
          error: erro => { Swal.fire('Erro!', `Ocorreu um erro ao deletar: ${erro.error}`, 'error'); }
        });
      }
    });
  }

  new() {
    this.carEdit = new Car();
    this.modalRef = this.modalService.open(this.modalCarDetails);
  }

  edit(car: Car) {
    this.carEdit = JSON.parse(JSON.stringify(car));
    this.modalRef = this.modalService.open(this.modalCarDetails);
  }

  retornoDetails(car: Car) {
    this.listAll();
    this.modalRef.close();
  }
}