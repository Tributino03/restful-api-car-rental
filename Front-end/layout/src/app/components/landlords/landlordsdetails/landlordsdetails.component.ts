import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Landlords } from '../../../models/landlord';
import { LandlordService } from '../../../services/landlord.service';
import { CommonModule } from '@angular/common';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-landlordsdetails',
  standalone: true,
  imports: [CommonModule, MdbFormsModule, FormsModule],
  templateUrl: './landlordsdetails.component.html',
  styleUrls: ['./landlordsdetails.component.scss']
})
export class LandlordsdetailsComponent implements OnInit {
  
  landlord: Landlords = new Landlords();
  isEditing: boolean = false;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private landlordService = inject(LandlordService);

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id > 0) {
      this.isEditing = true;
      this.findById(id);
    }
  }

  findById(id: number) {
    this.landlordService.findById(id).subscribe({
      next: data => { this.landlord = data; },
      error: erro => { Swal.fire('Erro!', `Ocorreu um erro ao localizar o locador.`, 'error'); }
    });
  }

  findAddressByCep() {
    if (this.landlord.address.cep && this.landlord.address.cep.length === 8) {
      this.landlordService.findByCep(this.landlord.address.cep).subscribe({
        next: viaCepDto => {
          this.landlord.address.street = viaCepDto.logradouro;
          this.landlord.address.neighborhood = viaCepDto.bairro;
          this.landlord.address.city = viaCepDto.localidade;
          this.landlord.address.state = viaCepDto.uf;
        },
        error: () => { Swal.fire('CEP não encontrado', 'Não foi possível encontrar o endereço para o CEP informado.', 'warning'); }
      });
    }
  }

  save() {
    if (this.isEditing) {
      this.landlordService.updateAddress(this.landlord.id, this.landlord.address).subscribe({
        next: () => {
          Swal.fire('Sucesso!', 'Endereço atualizado com sucesso.', 'success');
          this.router.navigate(['/admin/landlords']);
        },
        error: erro => { Swal.fire('Erro!', `Ocorreu um erro ao atualizar: ${erro.error}`, 'error'); }
      });
    } else {
      this.landlordService.create(this.landlord).subscribe({
        next: () => {
          Swal.fire('Sucesso!', 'Locador cadastrado com sucesso.', 'success');
          this.router.navigate(['/admin/landlords']);
        },
        error: erro => { Swal.fire('Erro!', `Ocorreu um erro ao cadastrar: ${erro.error}`, 'error'); }
      });
    }
  }
}