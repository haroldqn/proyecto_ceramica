"use client";

import { useState } from "react";
import toast from "react-hot-toast";

type Props = {
  onLogin: (user: { name: string; role: string }) => void;
};

export default function LoginForm({ onLogin }: Props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Credenciales inválidas");
      }

      const data = await response.json();
      localStorage.setItem("token", data.token);
      toast.success(`¡Bienvenido ${data.name}!`);
      onLogin({ name: data.name, role: data.role });
    } catch (err: unknown) {
      const errorMsg = err instanceof Error ? err.message : "Error en el login";
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3">
      <input
        type="email"
        placeholder="Correo"
        autoComplete="off"
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
        className="mt-2 cursor-pointer rounded-full bg-[--foreground] p-3 font-semibold text-black transition hover:bg-[--accent]"
        disabled={loading}
      >
        {loading ? "Cargando..." : "Iniciar sesión"}
      </button>
    </form>
  );
}
