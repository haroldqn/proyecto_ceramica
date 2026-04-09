"use client";

type Props = {
  producto: {
    id: number;
    nombre: string;
    precio: number;
  };
  user: any;
  onRequireAuth: () => void;
};

export default function ProductCard({ producto, user, onRequireAuth }: Props) {
  const handleComprar = () => {
    if (!user) {
      onRequireAuth(); // abre modal
    } else {
      console.log("comprar producto:", producto.id);
    }
  };

  return (
    <div className="border rounded-lg p-4 shadow hover:shadow-lg">
      <h2 className="text-xl font-semibold">{producto.nombre}</h2>
      <p className="text-gray-600">S/ {producto.precio}</p>

      <button
        onClick={handleComprar}
        className="mt-3 bg-blue-600 text-white px-3 py-1 rounded"
      >
        Comprar
      </button>
    </div>
  );
}