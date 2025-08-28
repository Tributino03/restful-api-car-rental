import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Brand } from '../../../models/brand';
import { BrandService } from '../../../services/brand.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-branddetails',
  standalone: true,
  imports: [CommonModule, MdbFormsModule, FormsModule], 
  templateUrl: './branddetails.component.html',
  styleUrls: ['./branddetails.component.scss']
})
export class BranddetailsComponent {

  @Input() brand: Brand = new Brand();
  @Output() retorno = new EventEmitter<Brand>();

  private brandService = inject(BrandService);
  constructor() {}

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
        const errorMessage = (typeof erro.error === 'string') ? erro.error : 'Ocorreu um erro desconhecido. Verifique o console.';
        
        Swal.fire({
          title: 'Erro!',
          text: `Ocorreu um erro ao salvar: ${errorMessage}`,
          icon: 'error',
          confirmButtonText: 'Ok'
        });
      }
    });
  }
}