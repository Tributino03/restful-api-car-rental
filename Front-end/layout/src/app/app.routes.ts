import { Routes } from '@angular/router';
import { LoginComponent } from './components/layout/login/login.component';
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { CarslistComponent } from './components/cars/carslist/carslist.component';
import { CarsdetailsComponent } from './components/cars/carsdetails/carsdetails.component';
import { BrandlistComponent } from './components/brand/brandlist/brandlist.component';
import { BranddetailsComponent } from './components/brand/branddetails/branddetails.component';
import { LandlordslistComponent } from './components/landlords/landlordslist/landlordslist.component';
import { LandlordsdetailsComponent } from './components/landlords/landlordsdetails/landlordsdetails.component';

export const routes: Routes = [
    {path : "", redirectTo: "login", pathMatch: "full"},
    {path: "login", component: LoginComponent},
    {path: "admin", component: PrincipalComponent, children: [
        {path: "cars", component: CarslistComponent},
        {path: "cars/new", component: CarsdetailsComponent},
        {path: "cars/edit/:id", component: CarsdetailsComponent},
        {path: "brand", component: BrandlistComponent},
        {path: "brand/new", component: BranddetailsComponent},
        {path: "brand/edit/:id", component: BranddetailsComponent},
        {path: "landlords", component: LandlordslistComponent},
        {path: "landlords/new", component: LandlordsdetailsComponent},
        {path: "landlords/edit/:id", component: LandlordsdetailsComponent},
        
    ]}
];
