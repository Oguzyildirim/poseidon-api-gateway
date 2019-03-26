import { IProductOrder } from 'app/shared/model/product-order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  order?: IProductOrder;
}

export const defaultValue: Readonly<IOrderItem> = {};
