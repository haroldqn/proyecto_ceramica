import { apiRequest } from "@/lib/api-client";

export interface AdminUserResponse {
  id: number;
  name: string;
  email: string;
  role: string;
}

export interface AdminProductResponse {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
}

export const adminService = {
  getUsers: () => apiRequest<AdminUserResponse[]>("/api/admin/users"),

  getProducts: () => apiRequest<AdminProductResponse[]>("/api/admin/products"),
  
  createProduct: (data: Partial<AdminProductResponse>) => 
    apiRequest<AdminProductResponse>("/api/admin/products", { 
      method: "POST", 
      body: data 
    }),
    
  updateProduct: (id: number, data: Partial<AdminProductResponse>) => 
    apiRequest<AdminProductResponse>(`/api/admin/products/${id}`, { 
      method: "PUT", 
      body: data 
    }),
    
  deleteProduct: (id: number) => 
    apiRequest<void>(`/api/admin/products/${id}`, { 
      method: "DELETE" 
    }),
};