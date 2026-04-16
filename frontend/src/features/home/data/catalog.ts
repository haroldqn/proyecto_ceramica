export type Category = {
  id: number;
  title: string;
  description: string;
  accent: string;
  countLabel: string;
  image: string;
  alt: string;
};

export type FeaturedProduct = {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  etiqueta: string;
  formato: string;
  tonos: [string, string];
  image: string;
  alt: string;
};

export type BrandValue = {
  id: number;
  title: string;
  description: string;
};

export const categories: Category[] = [
  {
    id: 1,
    title: "Frutas en cerámica",
    description: "Piezas pequeñas, brillantes y decorativas para cocinas, mesas auxiliares y rincones con color.",
    accent: "from-[#d8b49a] to-[#a9694f]",
    countLabel: "Colección frutal",
    image: "/categorias/frutas.png",
    alt: "Miniaturas de frutas en cerámica sobre una superficie neutra",
  },
  {
    id: 2,
    title: "Figuras artesanales",
    description: "Animales y piezas figurativas con presencia cálida para repisas, escritorios y detalles especiales.",
    accent: "from-[#c9b8a7] to-[#8d6b5a]",
    countLabel: "Edición decorativa",
    image: "/categorias/jarrones.png",
    alt: "Varias figuras de cerámica decorativa alineadas sobre una mesa",
  },
  {
    id: 3,
    title: "Piezas temáticas",
    description: "Objetos con carácter, ideales para regalos, vitrinas pequeñas y colecciones con personalidad.",
    accent: "from-[#e8d8c8] to-[#bc8b67]",
    countLabel: "Serie especial",
    image: "/categorias/brujas.png",
    alt: "Búho de cerámica decorativa en primer plano",
  },
];

export const featuredProducts: FeaturedProduct[] = [
  {
    id: 1,
    nombre: "Set Frutal",
    precio: 49,
    descripcion: "Miniaturas de frutas esmaltadas con acabado brillante para decorar cocinas y bandejas.",
    etiqueta: "Nueva colección",
    formato: "Set decorativo",
    tonos: ["#d9bfae", "#b77d59"],
    image: "/categorias/set_frutal.png",
    alt: "Colección de frutas de cerámica esmaltada",
  },
  {
    id: 2,
    nombre: "Figura Guacamayo",
    precio: 38,
    descripcion: "Pieza pequeña de ave colorida hecha a mano para repisas, escritorios o regalos.",
    etiqueta: "Más vendido",
    formato: "Figura artesanal",
    tonos: ["#cab2a1", "#8d6958"],
    image: "/categorias/loro.png",
    alt: "Figura de guacamayo de cerámica pintada a mano",
  },
  {
    id: 3,
    nombre: "Búho Decorativo",
    precio: 56,
    descripcion: "Figura protagonista con lectura ornamental, ideal para vitrinas y mesas auxiliares.",
    etiqueta: "Hecho a mano",
    formato: "Pieza especial",
    tonos: ["#eadbcc", "#c08963"],
    image: "/categorias/buho.png",
    alt: "Búho de cerámica decorativa de tonos crema y verde",
  },
  {
    id: 4,
    nombre: "Figura Canina",
    precio: 44,
    descripcion: "Figura simpática de inspiración artesanal para espacios cálidos y colecciones decorativas.",
    etiqueta: "Curaduría especial",
    formato: "Serie atelier",
    tonos: ["#dbc8bc", "#9d725d"],
    image: "/categorias/perro.png",
    alt: "Figura de perro de cerámica pintada a mano",
  },
];

export const brandValues: BrandValue[] = [
  {
    id: 1,
    title: "Diseño con intención",
    description: "Cada pieza es pensada para destacar, combinando estética y funcionalidad.",
  },
  {
    id: 2,
    title: "Trabajo artesanal",
    description: "Nada es en serie: cada producto tiene detalles únicos que lo hacen especial.",
  },
  {
    id: 3,
    title: "Hecho para tu espacio",
    description: "Creamos piezas que se adaptan a tu estilo y aportan personalidad a tu entorno.",
  },
];
