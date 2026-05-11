"use client";

import { useState, useEffect } from "react";
import Image from "next/image";
import { ProductDetail } from "@/types/product";

interface ProductDisplayProps {
  productId: number;
}

export default function ProductDisplay({ productId }: ProductDisplayProps) {
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [cantidad, setCantidad] = useState(1);

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        setLoading(true);
        const response = await fetch(`${API_URL}/api/products/${productId}`);
        
        if (!response.ok) {
          throw new Error("Producto no encontrado");
        }
        
        const data = await response.json();
        setProduct(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error al cargar producto");
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [productId, API_URL]);

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-5 py-12">
        <div className="text-center">Cargando producto...</div>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="mx-auto max-w-7xl px-5 py-12">
        <div className="text-center text-red-600">
          {error || "Producto no disponible"}
        </div>
      </div>
    );
  }

  const aumentar = () => setCantidad(prev => (prev < product.stock ? prev + 1 : prev));
  const disminuir = () => setCantidad(prev => (prev > 1 ? prev - 1 : 1));

  return (
    <div className="mx-auto max-w-7xl px-5 py-12 md:px-8 lg:py-16">
      <div className="grid gap-10 lg:grid-cols-2 lg:gap-16">
        {/* Imagen */}
        <div className="image-card overflow-hidden rounded-[2rem]">
          <div className="relative aspect-square w-full">
            <Image
              src={product.imageUrl}
              alt={product.name}
              fill
              className="object-cover"
              sizes="(max-width: 768px) 100vw, 50vw"
              priority
            />
          </div>
        </div>

        {/* Información */}
        <div className="space-y-6">
          <h1 className="font-display text-4xl text-[--foreground] md:text-5xl">
            {product.name}
          </h1>

          <div className="flex items-center gap-3">
            <span className="text-3xl font-bold text-[--foreground]">
              S/{product.price}
            </span>
            {product.stock > 0 ? (
              <span className="rounded-full bg-green-100 px-3 py-1 text-xs text-green-700">
                En stock ({product.stock})
              </span>
            ) : (
              <span className="rounded-full bg-red-100 px-3 py-1 text-xs text-red-700">
                Agotado
              </span>
            )}
          </div>

          {/* Detalles técnicos */}
          <div className="space-y-2 rounded-2xl bg-[--surface] p-4">
            <div className="flex justify-between border-b border-[--border-soft] pb-2">
              <span className="text-sm text-[--muted]">Categoría</span>
              <span className="text-sm font-medium">{product.categoryName}</span>
            </div>
            <div className="flex justify-between border-b border-[--border-soft] pb-2">
              <span className="text-sm text-[--muted]">Tamaño</span>
              <span className="text-sm font-medium">{product.sizeName}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-sm text-[--muted]">Dimensiones</span>
              <span className="text-sm font-medium">{product.sizeDimension}</span>
            </div>
          </div>

          {/* Descripción */}
          <div className="space-y-2">
            <h3 className="font-semibold text-[--foreground]">Descripción</h3>
            <p className="text-base leading-7 text-[--muted]">{product.description}</p>
          </div>

          {/* Cantidad */}
          <div className="space-y-3">
            <h2 className="text-sm font-semibold uppercase tracking-[0.2em] text-[--muted]">
              Cantidad
            </h2>
            <div className="flex items-center gap-3">
              <button
                onClick={disminuir}
                disabled={cantidad <= 1}
                className="flex h-10 w-10 items-center justify-center rounded-full border border-[--border-soft] bg-white text-lg font-semibold transition hover:border-[--accent] disabled:opacity-50"
              >
                -
              </button>
              <input
                type="number"
                value={cantidad}
                onChange={(e) => {
                  const val = parseInt(e.target.value);
                  if (!isNaN(val) && val > 0 && val <= product.stock) {
                    setCantidad(val);
                  }
                }}
                min="1"
                max={product.stock}
                className="h-10 w-16 rounded-xl border border-[--border-soft] bg-[#fffaf7] text-center outline-none focus:border-[--accent]"
              />
              <button
                onClick={aumentar}
                disabled={cantidad >= product.stock}
                className="flex h-10 w-10 items-center justify-center rounded-full border border-[--border-soft] bg-white text-lg font-semibold transition hover:border-[--accent] disabled:opacity-50"
              >
                +
              </button>
            </div>
          </div>

          {/* Botón comprar */}
          <button
            disabled={product.stock === 0}
            className="button-primary inline-flex w-full cursor-pointer items-center justify-center rounded-full px-5 py-3 text-sm font-semibold text-black disabled:opacity-50"
          >
            {product.stock === 0 ? "Agotado" : "Agregar al carrito"}
          </button>

          {/* Productos relacionados */}
          {product.relatedProducts.length > 0 && (
            <div className="mt-8">
              <h3 className="mb-4 font-semibold">También te puede interesar</h3>
              <div className="grid grid-cols-2 gap-3">
                {product.relatedProducts.map((related) => (
                  <div key={related.id} className="rounded-xl border border-[--border-soft] p-2">
                    <div className="relative aspect-square w-full">
                      <Image
                        src={related.imageUrl}
                        alt={related.name}
                        fill
                        className="rounded-lg object-cover"
                      />
                    </div>
                    <p className="mt-2 text-sm font-medium">{related.name}</p>
                    <p className="text-sm text-[--accent]">S/{related.price}</p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}