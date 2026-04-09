"use client";

import { useState } from "react";
import toast from "react-hot-toast";

type Props = {
  onRegister: () => void;
};

export default function RegisterForm({ onRegister }: Props) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  // Validar que el nombre solo contenga letras y espacios
  const isValidName = (value: string) => {
    return /^[a-záéíóúñ\s]+$/i.test(value);
  };

  // Validar que el email sea gmail
  const isValidEmail = (value: string) => {
    return value.endsWith("@gmail.com");
  };

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    // Validaciones
    if (!isValidName(name)) {
      setError("El nombre debe contener solo letras y espacios");
      setLoading(false);
      toast.error("El nombre debe contener solo letras y espacios");
      return;
    }

    if (!isValidEmail(email)) {
      setError("El correo debe ser de Gmail (@gmail.com)");
      setLoading(false);
      toast.error("El correo debe ser de Gmail (@gmail.com)");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password }),
      });

      if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Error al registrar");
      }

      toast.success("¡Cuenta creada correctamente!");
      onRegister();
    } catch (err: any) {
      const errorMessage = err.message || "Error al registrar";
      setError(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3">
      <input
        placeholder="Nombre"
        value={name}
        onChange={handleNameChange}
        className="border p-2 rounded"
        required
      />

      <input
        type="email"
        placeholder="Correo (solo Gmail)"
        value={email}
        onChange={handleEmailChange}
        className="border p-2 rounded"
        required
      />
      <input
        type="password"
        placeholder="Contraseña"
        autoComplete="off"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="border p-2 rounded"
        required
      />
      <button
        type="submit"
        className="bg-[#c08576] text-white p-2 rounded cursor-pointer"
        disabled={loading}
      >
        {loading ? "Cargando..." : "Crear cuenta"}
      </button>
      {error && <p className="text-red-500 text-sm">{error}</p>}
    </form>
  );
}