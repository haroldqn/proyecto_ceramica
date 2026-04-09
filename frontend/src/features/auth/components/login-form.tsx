"use client";

import { useState } from "react";
import toast from "react-hot-toast";

type Props = {
  onLogin: (user: { name: string; role: string }) => void;
};

export default function LoginForm({ onLogin }: Props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

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
    } catch (err: any) {
      const errorMsg = err.message || "Error en el login";
      setError(errorMsg);
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
        {loading ? "Cargando..." : "Iniciar sesión"}
      </button>
    </form>
  );
}