import { Moment } from 'moment';
import { IOrderItem } from 'app/shared/model/order-item.model';

export const enum OrderStatus {
  COMPLETED = 'COMPLETED',
  PENDING = 'PENDING',
  CANCELLED = 'CANCELLED'
}

export interface IProductOrder {
  id?: number;
  placedDate?: Moment;
  status?: OrderStatus;
  code?: string;
  orderItems?: IOrderItem[];
  shipEmail?: string;
  shipId?: number;
}

export const defaultValue: Readonly<IProductOrder> = {};
