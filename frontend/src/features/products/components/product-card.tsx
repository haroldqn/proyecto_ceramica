"use client";

import type { AuthUser } from "@/features/auth/types";

type Props = {
  producto: {
    id: number;
    nombre: string;
    precio: number;
    descripcion: string;
    etiqueta: string;
    formato: string;
    tonos: [string, string];
  };
  user: AuthUser | null;
  onRequireAuth: () => void;
};

export default function ProductCard({ producto, user, onRequireAuth }: Props) {
  const handleComprar = () => {
    if (!user) {
      onRequireAuth();
      return;
    }

    console.log("comprar producto:", producto.id);
  };

  return (
    <article className="group rounded-[2rem] border border-[--border-soft] bg-[--surface] p-5 shadow-[0_18px_45px_rgba(77,50,36,0.08)] transition duration-300 hover:-translate-y-1 hover:shadow-[0_24px_55px_rgba(77,50,36,0.14)]">
      <div
        className="relative overflow-hidden rounded-[1.6rem] p-5"
        style={{
          background: `linear-gradient(140deg, ${producto.tonos[0]} 0%, ${producto.tonos[1]} 100%)`,
        }}
      >
        <span className="inline-flex rounded-full border border-white/35 bg-white/18 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.24em] text-white">
          {producto.etiqueta}
        </span>

        <div className="flex min-h-56 items-end justify-between gap-4">
          <div className="ceramic-shape ceramic-shape-small bg-white/75" />
          <div className="ceramic-shape ceramic-shape-tall bg-white/88" />
        </div>
      </div>

      <div className="space-y-4 px-1 pt-5">
        <div className="flex items-start justify-between gap-3">
          <div>
            <h3 className="font-display text-3xl text-[--foreground]">{producto.nombre}</h3>
            <p className="mt-1 text-sm text-[--muted]">{producto.formato}</p>
          </div>
          <p className="text-base font-semibold text-[--foreground]">S/ {producto.precio}</p>
        </div>

        <p className="text-sm leading-6 text-[--muted]">{producto.descripcion}</p>

        <button
          onClick={handleComprar}
          className="inline-flex w-full items-center justify-center rounded-full bg-[--foreground] px-5 py-3 text-sm font-semibold text-white transition hover:bg-[--accent]"
        >
          Comprar
        </button>
      </div>
    </article>
  );
}
