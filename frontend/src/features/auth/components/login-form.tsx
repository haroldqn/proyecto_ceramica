"use client";

export default function LoginForm() {
  return (
    <form className="flex flex-col gap-3">
      <input placeholder="Correo" className="border p-2 rounded" />
      <input type="password" placeholder="Contraseña" className="border p-2 rounded" />

      <button className="bg-blue-600 text-white p-2 rounded">
        Iniciar sesión
      </button>
    </form>
  );
}