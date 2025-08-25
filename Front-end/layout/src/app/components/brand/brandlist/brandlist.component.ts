import { Component, EventEmitter, inject, Input, TemplateRef, ViewChild } from '@angular/core';
import { MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { BrandService } from '../../../services/brand.service';
import { Brand } from '../../../models/brand';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Observable } from 'rxjs';
import { BranddetailsComponent } from "../branddetails/branddetails.component";

@Component({
  selector: 'app-brandlist',
  imports: [BranddetailsComponent],
  templateUrl: './brandlist.component.html',
  styleUrl: './brandlist.component.scss'
})
export class BrandlistComponent {

  
  lista: Brand[] = [];
  brandEdit = new Brand(0,"");

  modalService = inject(MdbModalService);
  @ViewChild("modalBrandDetails") modalCarDetails!: TemplateRef<any>;
  modalRef!: MdbModalRef<any>;

  brandService = inject(BrandService);

  constructor(){

    this.listAll();
   
    
  }

  listAll(){

    this.brandService.listAll().subscribe({
      next: lista => {
        this.lista = lista;
        
      },error: err => {
        Swal.fire({
        title: 'Erro!',
        text: 'Ocorreu um erro',
        icon: 'error',
        confirmButtonText: 'ok'
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
      next: (mensagem) => {
        Swal.fire({
          title: mensagem,
          icon: 'success',
          confirmButtonText: 'Ok',
        });
        this.listAll();
      },
      error: (err) => {
        Swal.fire({
          title: 'Erro!',
          icon: 'error',
          confirmButtonText: 'ok'
        });
      }
    });
      
}
      
    });
}

new(){
  this.brandEdit = new Brand(0,"");
  this.modalRef = this.modalService.open(this.modalCarDetails);
}
edit(brand: Brand){
  this.brandEdit = Object.assign({}, brand);
  this.modalRef = this.modalService.open(this.modalCarDetails);
}

retornoDetails(brand: Brand){
  this.listAll();
  this.modalRef.close();
}

  

}
