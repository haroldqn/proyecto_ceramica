"use client";

import ProductDisplay from "@/features/products/components/ProductDisplay";

// Esto es solo un ejemplo, por el momento...
const mockProduct = {
    id: 2,
    nombre: "Figura Guacamayo",
    precio: 38,
    descripcion: "Pieza pequeña de ave colorida hecha a mano para repisas, escritorios o regalos.",
    image: "/categorias/loro.png",
    alt: "Lorito azul"
};

export default function ProductPage() {
    return (
        <div className="container mx-auto py-10">
            <ProductDisplay product={mockProduct} />
        </div>
    );
}