import { Component } from '@angular/core';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { RouterLink } from '@angular/router'; 

@Component({
  selector: 'app-menu',
  standalone: true, 
  imports: [MdbCollapseModule, RouterLink],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

}