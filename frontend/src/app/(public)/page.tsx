"use client";

import { useState, useEffect } from "react";
import ProductCard from "@/features/products/components/product-card";
import AuthModal from "@/features/auth/components/auth-modal";

export default function Home() {
  const [user, setUser] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/products")
      .then(res => res.json())
      .then(data => setProductos(data));
  }, []);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Productos</h1>

      <div className="grid grid-cols-3 gap-6">
        {productos.map((p: any) => (
          <ProductCard
            key={p.id}
            producto={{ id: p.id, nombre: p.name, precio: p.price }}
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