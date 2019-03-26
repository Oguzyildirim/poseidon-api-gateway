import { IUser } from 'app/shared/model/user.model';
import { IProductOrder } from 'app/shared/model/product-order.model';
import { ICompany } from 'app/shared/model/company.model';

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
  user?: IUser;
  orders?: IProductOrder[];
  company?: ICompany;
}

export const defaultValue: Readonly<IShip> = {};
