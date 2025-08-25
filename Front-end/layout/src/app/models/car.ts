import { Brand } from "./brand";

export class Car {
    id!: number;
    name!: string;
    year!: number;
    vehicleValue!: number;
    brand!: Brand;
    rentals!: any[];
}