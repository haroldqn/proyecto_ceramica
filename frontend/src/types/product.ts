export interface ProductDetail {
    id: number;
    name: string;
    price: number;
    stock: number;
    imageUrl: string;
    status: boolean;
    categoryName: string;
    sizeName: string;
    sizeDimension: string;
    description: string;
    relatedProducts: RelatedProduct[];
}

export interface RelatedProduct {
    id: number;
    name: string;
    price: number;
    imageUrl: string;
}