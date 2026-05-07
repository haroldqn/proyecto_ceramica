"use client";

import { useState } from "react";
import Image from "next/image";

const ProductDisplay = ({ product }) => {
    // estado para la cantidad del producto
    const [cantidad, setCantidad] = useState(1);

    const aumentar = () => setCantidad(prev => prev + 1);
    const disminuir = () => setCantidad(prev => (prev > 1 ? prev - 1 : 1));
    const manejarCambio = (e) => {
        const valor = parseInt(e.target.value);
        if (!isNaN(valor) && valor > 0) {
            setCantidad(valor);
        }
    };

    // estos son los tamaños disponibles para cada producto
    const sizes = ["Pulga", "Dije", "Mini", "Small", "Super small"];
    const [selectedSize, setSelectedSize] = useState("Mediano");

    return (
        <div className="mx-auto max-w-7xl px-5 py-12 md:px-8 lg:py-16">
            <div className="grid gap-10 lg:grid-cols-2 lg:gap-16">

                {/* Columna izquierda - Galería */}
                <div className="flex gap-4">
                    
                    {/* slider del producto con 4 imagenes */}
                    <div className="flex flex-col gap-3">
                        {[1, 2, 3, 4].map((i) => (
                            <div
                                key={i}
                                className="h-20 w-20 cursor-pointer overflow-hidden rounded-xl border border-[--border-soft] bg-white"
                            >
                                <img
                                    src={product.image}
                                    alt=""
                                    className="h-full w-full object-cover"
                                />
                            </div>
                        ))}
                    </div>

                    {/* imagen principal del producto */}
                    <div className="image-card flex-1 overflow-hidden rounded-[2rem]">
                        <div
                            className="relative aspect-square w-full"
                            style={{
                                background: product.tonos
                                    ? `linear-gradient(140deg, ${product.tonos[0]} 0%, ${product.tonos[1]} 100%)`
                                    : "linear-gradient(140deg, #ead7ca 0%, #cfad95 100%)",
                            }}
                        >
                            {product.etiqueta && (
                                <span className="absolute left-4 top-4 z-10 inline-flex rounded-full border border-white/35 bg-[rgba(37,23,15,0.48)] px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.24em] text-white backdrop-blur-sm">
                                    {product.etiqueta}
                                </span>
                            )}
                            <img
                                src={product.image}
                                alt={product.nombre}
                                className="h-full w-full object-cover"
                            />
                        </div>
                    </div>
                </div>

                {/* Columna derecha - Información del producto */}
                <div className="space-y-6">
                    <h1 className="font-display text-4xl text-[--foreground] md:text-5xl">
                        {product.nombre}
                    </h1>

                    {/* Precio */}
                    <div className="flex items-center gap-3">
                        <span className="text-3xl font-bold text-[--foreground]">
                            S/{product.precio}
                        </span>
                    </div>

                    {/* descripcion del producto */}
                    <div className="space-y-2">
                        <p className="text-base leading-7 text-[--muted]">
                            {product.descripcion}
                        </p>
                    </div>

                    {/* esto selecciona el tamanaño  */}
                    <div className="space-y-3">
                        <h3 className="font-semibold text-[--foreground]">
                            Selecciona el tamaño:
                        </h3>
                        <div className="flex gap-3">
                            {sizes.map((size) => (
                                <button
                                    key={size}
                                    onClick={() => setSelectedSize(size)}
                                    className={`rounded-full border px-6 py-2 text-sm transition ${selectedSize === size
                                            ? "border-[--accent] bg-[rgba(37,23,15,0.52)] text-white"
                                            : "border-[--border-soft] text-[--foreground] hover:border-[--accent]"
                                        }`}
                                >
                                    {size}
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* selector de cantidad */}
                    <div className="space-y-3">
                        <h2 className="text-sm font-semibold uppercase tracking-[0.2em] text-[--muted]">
                            Cantidad
                        </h2>
                        <div className="flex items-center gap-3">
                            <button
                                onClick={disminuir}
                                className="flex h-10 w-10 items-center justify-center rounded-full border border-[--border-soft] bg-white text-lg font-semibold text-[--foreground] transition hover:border-[--accent] hover:bg-[--accent]/5"
                            >
                                -
                            </button>
                            <input
                                type="number"
                                value={cantidad}
                                onChange={manejarCambio}
                                min="1"
                                className="h-10 w-16 rounded-xl border border-[--border-soft] bg-[#fffaf7] text-center text-[--foreground] outline-none focus:border-[--accent] focus:bg-white"
                            />
                            <button
                                onClick={aumentar}
                                className="flex h-10 w-10 items-center justify-center rounded-full border border-[--border-soft] bg-white text-lg font-semibold text-[--foreground] transition hover:border-[--accent] hover:bg-[--accent]/5"
                            >
                                +
                            </button>
                        </div>
                    </div>

                    {/* boton para agregar al carrito */}
                    <button className="button-primary inline-flex w-full cursor-pointer items-center justify-center rounded-full px-5 py-3 text-sm font-semibold text-black">
                        Agregar al carrito
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ProductDisplay;