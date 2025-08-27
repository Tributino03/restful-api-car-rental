import { Address } from "./address";

export class Landlords {
    id!: number;
    name!: string;
    cpf!: string;
    dateOfBirth!: string;
    address: Address = new Address();
    active!: boolean;
    // rentals!: any[];
}