export default function HeroShowcase() {
  return (
    <div className="relative overflow-hidden rounded-[2.5rem] border border-white/60 bg-[radial-gradient(circle_at_top,#f6ece3_0%,#ead8ca_43%,#dbc0ab_100%)] p-6 shadow-[0_30px_80px_rgba(84,54,38,0.18)]">
      <div className="absolute -right-8 top-12 h-44 w-44 rounded-full bg-white/25 blur-3xl" />
      <div className="absolute bottom-0 left-0 h-32 w-32 rounded-tr-[3rem] bg-[#ab7757]/20" />

      <div className="grid min-h-[420px] grid-cols-2 gap-4">
        <div className="flex items-end">
          <div className="ceramic-shape ceramic-shape-tall w-full max-w-[200px]" />
        </div>
        <div className="flex flex-col justify-between pt-6">
          <div className="ml-auto w-28 rounded-full border border-white/45 bg-white/20 px-4 py-2 text-center text-xs uppercase tracking-[0.24em] text-white/85">
            Taller
          </div>
          <div className="flex items-center justify-end gap-4">
            <div className="ceramic-shape ceramic-shape-small" />
            <div className="ceramic-shape ceramic-shape-arch" />
          </div>
        </div>
      </div>

      <div className="mt-6 flex items-center justify-between gap-4 rounded-[1.5rem] border border-white/40 bg-white/30 px-5 py-4 backdrop-blur-sm">
        <div>
          <p className="text-xs uppercase tracking-[0.28em] text-white/80">
            Selección curada
          </p>
          <p className="mt-2 font-display text-3xl text-white">Cerámica para habitar con calma</p>
        </div>
        <div className="hidden h-14 w-px bg-white/35 md:block" />
        <p className="max-w-48 text-sm leading-6 text-white/85">
          Formas limpias, tonos tierra y piezas pensadas para espacios sobrios.
        </p>
      </div>
    </div>
  );
}
