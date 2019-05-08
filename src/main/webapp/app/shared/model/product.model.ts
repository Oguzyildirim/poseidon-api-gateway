export interface IProduct {
  id?: number;
  levelOne?: string;
  levelTwo?: string;
  levelThree?: string;
  partNo?: string;
  description?: string;
  uom?: string;
  mtmlUom?: string;
  explanation?: string;
  picture?: string;
  information?: string;
  productCategoryLevelOne?: string;
  productCategoryId?: number;
}

export const defaultValue: Readonly<IProduct> = {};
