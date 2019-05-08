export interface IOrderItem {
  id?: number;
  quantity?: number;
  productPartNo?: string;
  productId?: number;
  orderCode?: string;
  orderId?: number;
}

export const defaultValue: Readonly<IOrderItem> = {};
