import { Component, inject, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Brand } from '../../../models/brand';
import Swal from 'sweetalert2';
import { MdbModalModule, MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { BranddetailsComponent } from "../branddetails/branddetails.component";
import { BrandService } from '../../../services/brand.service';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';

@Component({
  selector: 'app-brandlist',
  standalone: true,
  imports: [CommonModule, MdbModalModule, BranddetailsComponent, MdbRippleModule],
  templateUrl: './brandlist.component.html',
  styleUrls: ['./brandlist.component.scss']
})
export class BrandlistComponent {

  lista: Brand[] = [];
  brandEdit: Brand = new Brand();

  modalService = inject(MdbModalService);
  @ViewChild("modalBrandDetails") modalBrandDetails!: TemplateRef<any>;
  modalRef!: MdbModalRef<any>;

  brandService = inject(BrandService);

  constructor() {
    this.listAll();
  }

  listAll() {
    this.brandService.listAll().subscribe({
      next: lista => { this.lista = lista; },
      error: () => { Swal.fire('Erro!', 'Ocorreu um erro ao carregar a lista de marcas.', 'error'); }
    });
  }

  deleteById(brand: Brand) {
    Swal.fire({
      title: 'Você tem certeza?',
      text: "Esta ação não pode ser revertida!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sim, excluir!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.brandService.delete(brand.id).subscribe({
          next: () => {
            Swal.fire('Deletado!', 'A marca foi deletada com sucesso.', 'success');
            this.listAll();
          },
          error: (err) => { Swal.fire('Erro!', `Não foi possível deletar: ${err.error}`, 'error'); }
        });
      }
    });
  }

  new() {
    this.brandEdit = new Brand();
    this.modalRef = this.modalService.open(this.modalBrandDetails);
  }

  // O retorno do modal (sucesso ou erro) aciona este método
  retornoDetails(brand: Brand) {
    this.listAll(); // ATUALIZA A LISTA
    this.modalRef.close(); // FECHA O MODAL
  }
}