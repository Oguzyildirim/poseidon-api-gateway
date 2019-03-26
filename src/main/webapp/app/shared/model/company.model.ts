import { IUser } from 'app/shared/model/user.model';
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
  user?: IUser;
  ships?: IShip[];
}

export const defaultValue: Readonly<ICompany> = {};
