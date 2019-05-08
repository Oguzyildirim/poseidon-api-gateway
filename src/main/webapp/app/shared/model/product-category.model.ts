import { IProduct } from 'app/shared/model/product.model';

export interface IProductCategory {
  id?: number;
  levelOne?: string;
  levelTwo?: string;
  levelThree?: string;
  products?: IProduct[];
}

export const defaultValue: Readonly<IProductCategory> = {};
