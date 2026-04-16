export type Category = {
  id: number;
  title: string;
  description: string;
  accent: string;
  countLabel: string;
};

export type FeaturedProduct = {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  etiqueta: string;
  formato: string;
  tonos: [string, string];
};

export type BrandValue = {
  id: number;
  title: string;
  description: string;
};

export const categories: Category[] = [
  {
    id: 1,
    title: "Vasijas esculturales",
    description: "Piezas de volumen suave para salas, recibidores y repisas protagonistas.",
    accent: "from-[#d8b49a] to-[#a9694f]",
    countLabel: "18 diseños",
  },
  {
    id: 2,
    title: "Figuras artesanales",
    description: "Siluetas orgánicas con acabados mate que aportan identidad sin recargar.",
    accent: "from-[#c9b8a7] to-[#8d6b5a]",
    countLabel: "12 colecciones",
  },
  {
    id: 3,
    title: "Sobremesa y detalles",
    description: "Objetos decorativos pequeños para mesas de centro, consolas y rincones cálidos.",
    accent: "from-[#e8d8c8] to-[#bc8b67]",
    countLabel: "24 piezas",
  },
];

export const featuredProducts: FeaturedProduct[] = [
  {
    id: 1,
    nombre: "Vasija Aura",
    precio: 149,
    descripcion: "Forma cilíndrica estilizada con acabado arena y presencia serena.",
    etiqueta: "Nueva colección",
    formato: "Altura 28 cm",
    tonos: ["#d9bfae", "#b77d59"],
  },
  {
    id: 2,
    nombre: "Figura Nido",
    precio: 96,
    descripcion: "Figura curva para repisas y escritorios con textura mineral mate.",
    etiqueta: "Más vendido",
    formato: "Edición limitada",
    tonos: ["#cab2a1", "#8d6958"],
  },
  {
    id: 3,
    nombre: "Centro Calma",
    precio: 124,
    descripcion: "Pieza horizontal pensada para mesas de comedor o consolas minimalistas.",
    etiqueta: "Hecho a mano",
    formato: "Ancho 34 cm",
    tonos: ["#eadbcc", "#c08963"],
  },
  {
    id: 4,
    nombre: "Totem Brisa",
    precio: 172,
    descripcion: "Composición vertical de líneas limpias para rincones con luz natural.",
    etiqueta: "Curaduría especial",
    formato: "Serie atelier",
    tonos: ["#dbc8bc", "#9d725d"],
  },
];

export const brandValues: BrandValue[] = [
  {
    id: 1,
    title: "Diseño honesto",
    description: "Cada silueta nace para convivir con espacios sobrios, luz cálida y materiales nobles.",
  },
  {
    id: 2,
    title: "Acabado artesanal",
    description: "Trabajamos texturas mate, bordes suaves y esmaltes neutros que resisten tendencias pasajeras.",
  },
  {
    id: 3,
    title: "Colecciones pequeñas",
    description: "Producimos por series cortas para cuidar el detalle y mantener piezas con carácter propio.",
  },
];
