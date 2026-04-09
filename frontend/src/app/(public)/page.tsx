"use client";

import { useState } from "react";
import ProductCard from "@/features/products/components/product-card";
import AuthModal from "@/features/auth/components/auth-modal";

export default function Home() {
  const [user, setUser] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);

  const productos = [
    { id: 1, nombre: "Producto 1", precio: 100 },
    { id: 2, nombre: "Producto 2", precio: 200 },
  ];

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Productos 🛒</h1>

      <div className="grid grid-cols-3 gap-6">
        {productos.map((p) => (
          <ProductCard
            key={p.id}
            producto={p}
            user={user}
            onRequireAuth={() => setShowModal(true)}
          />
        ))}
      </div>

      {showModal && (
        <AuthModal onClose={() => setShowModal(false)} />
      )}
    </div>
  );
}