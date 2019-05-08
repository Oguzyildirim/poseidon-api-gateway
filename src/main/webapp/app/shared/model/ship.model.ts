import { IProductOrder } from 'app/shared/model/product-order.model';

export const enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export interface IShip {
  id?: number;
  shipId?: number;
  firstName?: string;
  lastName?: string;
  gender?: Gender;
  email?: string;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  userLogin?: string;
  userId?: number;
  orders?: IProductOrder[];
  companyEmail?: string;
  companyId?: number;
}

export const defaultValue: Readonly<IShip> = {};
