import { Car } from "./car";
import { Landlords } from "./landlord";

export class Rental {
    id!: number;
    startDate!: string; 
    returnDate!: string;
    totalValue!: number;
    status!: string;

    car: Car = new Car();
    landlord: Landlords = new Landlords();
}