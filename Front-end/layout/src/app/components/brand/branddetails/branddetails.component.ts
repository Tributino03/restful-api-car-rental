import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Brand } from '../../../models/brand';
import { BrandService } from '../../../services/brand.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-branddetails',
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './branddetails.component.html',
  styleUrl: './branddetails.component.scss'
})
export class BranddetailsComponent {

  @Input("brand") brand: Brand = new Brand();
  @Output("retorno") retorno = new EventEmitter<any>();

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private brandService = inject(BrandService);

  constructor() {
    const id = this.route.snapshot.params['id'];
    if (id > 0) {
      this.findById(id);
    }
  }

  findById(id: number) {
    this.brandService.findById(id).subscribe({
      next: brand => {
        this.brand = brand;
      },
      error: erro => {
        Swal.fire({
          title: 'Não encontrado!',
          text: `Ocorreu um erro ao tentar localizar o carro com ID ${id}.`,
          icon: 'error',
          confirmButtonText: 'Ok'
        });
      }
    });
  }

  save() {
  this.brandService.create(this.brand).subscribe({
    next: brandSalva => {
      Swal.fire({
        title: 'Salvo com sucesso!',
        icon: 'success',
        confirmButtonText: 'Ok'
      }).then(() => {
        this.retorno.emit(brandSalva); 
      });
    },
    error: erro => {
      Swal.fire({
        title: 'Erro!',
        text: 'Ocorreu um erro ao salvar: ' + erro.error,
        icon: 'error',
        confirmButtonText: 'Ok'
      });
    }
  });
}

}
