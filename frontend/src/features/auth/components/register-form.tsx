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

  const isValidName = (value: string) => /^[a-záéíóúñ\s]+$/i.test(value);
  const isValidEmail = (value: string) => value.endsWith("@gmail.com");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (!isValidName(name)) {
      const message = "El nombre debe contener solo letras y espacios";
      setError(message);
      setLoading(false);
      toast.error(message);
      return;
    }

    if (!isValidEmail(email)) {
      const message = "El correo debe ser de Gmail (@gmail.com)";
      setError(message);
      setLoading(false);
      toast.error(message);
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
    } catch (err: unknown) {
      const errorMessage = err instanceof Error ? err.message : "Error al registrar";
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
        onChange={(e) => setName(e.target.value)}
        className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] px-4 py-3 text-[--foreground] outline-none transition placeholder:text-[#8b7667] focus:border-[--accent] focus:bg-white"
        required
      />
      <input
        type="email"
        placeholder="Correo (solo Gmail)"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] px-4 py-3 text-[--foreground] outline-none transition placeholder:text-[#8b7667] focus:border-[--accent] focus:bg-white"
        required
      />
      <input
        type="password"
        placeholder="Contraseña"
        autoComplete="off"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] px-4 py-3 text-[--foreground] outline-none transition placeholder:text-[#8b7667] focus:border-[--accent] focus:bg-white"
        required
      />
      <button
        type="submit"
        className="mt-2 cursor-pointer rounded-full bg-[--foreground] p-3 font-semibold text-white transition hover:bg-[--accent]"
        disabled={loading}
      >
        {loading ? "Cargando..." : "Crear cuenta"}
      </button>
      {error && <p className="text-sm text-red-500">{error}</p>}
    </form>
  );
}
