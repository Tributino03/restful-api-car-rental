import { Component, inject, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- IMPORTANTE
import { Brand } from '../../../models/brand';
import Swal from 'sweetalert2';
import { MdbModalModule, MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { BranddetailsComponent } from "../branddetails/branddetails.component";
import { BrandService } from '../../../services/brand.service';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';

@Component({
  selector: 'app-brandlist',
  standalone: true, // <-- IMPORTANTE: Declare como standalone
  imports: [
    CommonModule, // <-- IMPORTANTE: Necessário para o @for funcionar
    MdbModalModule,
    BranddetailsComponent,
    MdbRippleModule
  ],
  templateUrl: './brandlist.component.html',
  styleUrls: ['./brandlist.component.scss']
})
export class BrandlistComponent {

  lista: Brand[] = [];
  brandEdit: Brand = new Brand();

  modalService = inject(MdbModalService);
  // Garanta que o nome da referência no HTML é "modalBrandDetails"
  @ViewChild("modalBrandDetails") modalBrandDetails!: TemplateRef<any>; 
  modalRef!: MdbModalRef<any>;

  brandService = inject(BrandService);

  constructor() {
    this.listAll();
  }

  listAll() {
    this.brandService.listAll().subscribe({
      next: lista => {
        this.lista = lista;
      },
      error: err => {
        Swal.fire({
          title: 'Erro!',
          text: 'Ocorreu um erro ao carregar a lista de marcas.',
          icon: 'error'
        });
      },
    });
  }

  deleteById(brand: Brand) {
    Swal.fire({
      title: 'Você tem certeza?',
      text: "Esta ação não pode ser revertida!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, excluir!',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.brandService.delete(brand.id).subscribe({
          next: () => {
            Swal.fire('Deletado!', 'A marca foi deletada com sucesso.', 'success');
            this.listAll();
          },
          error: (err) => {
            Swal.fire('Erro!', `Não foi possível deletar: ${err.error}`, 'error');
          }
        });
      }
    });
  }

  new() {
    this.brandEdit = new Brand();
    this.modalRef = this.modalService.open(this.modalBrandDetails);
  }

  edit(brand: Brand) {
    // A tela de edição de marca não foi implementada ainda, então este botão pode ser adicionado depois
    this.brandEdit = Object.assign({}, brand);
    this.modalRef = this.modalService.open(this.modalBrandDetails);
  }

 retornoDetails(brand: Brand) { 
  this.listAll();
  this.modalRef.close();
}
}