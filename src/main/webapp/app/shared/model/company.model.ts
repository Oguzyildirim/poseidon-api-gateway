import { IShip } from 'app/shared/model/ship.model';

export interface ICompany {
  id?: number;
  name?: string;
  email?: string;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  userLogin?: string;
  userId?: number;
  ships?: IShip[];
}

export const defaultValue: Readonly<ICompany> = {};
