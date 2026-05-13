"use client";

import { useEffect, useRef, useState } from "react";
import Script from "next/script";
import toast from "react-hot-toast";
import { loginWithEmail, loginWithGoogle } from "@/features/auth/services/auth-service";

type Props = {
  onLogin: (user: { name: string; role: string }) => void;
};

export default function LoginForm({ onLogin }: Props) {
  const [loading, setLoading] = useState(false);
  const [scriptLoaded, setScriptLoaded] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const buttonRef = useRef<HTMLDivElement | null>(null);

  const clientId = process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID;

  useEffect(() => {
    if (window.google) {
      setScriptLoaded(true);
    }
  }, []);

  useEffect(() => {
    if (!scriptLoaded || !clientId || !buttonRef.current || !window.google) {
      return;
    }

    buttonRef.current.innerHTML = "";

    if (!window.__googleIdentityInitialized) {
      window.google.accounts.id.initialize({
        client_id: clientId,
        callback: async ({ credential }) => {
          setLoading(true);
          setError("");

          try {
            const data = await loginWithGoogle(credential);
            localStorage.setItem("token", data.token);
            localStorage.setItem("user", JSON.stringify({ name: data.name, role: data.role }));
            toast.success(`¡Bienvenido ${data.name}!`);
            onLogin({ name: data.name, role: data.role });
          } catch (err: unknown) {
            const errorMsg = err instanceof Error ? err.message : "Error al iniciar con Google";
            setError(errorMsg);
            toast.error(errorMsg);
          } finally {
            setLoading(false);
          }
        },
        auto_select: false,
        cancel_on_tap_outside: true,
      });

      window.__googleIdentityInitialized = true;
    }

    window.google.accounts.id.renderButton(buttonRef.current, {
      theme: "outline",
      size: "large",
      shape: "pill",
      text: "continue_with",
      width: 320,
      logo_alignment: "left",
    });
  }, [clientId, onLogin, scriptLoaded]);

  const handleEmailLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (!email || !password) {
      setError("Completa tu correo y contraseña.");
      setLoading(false);
      return;
    }

    try {
      const data = await loginWithEmail({ email, password });
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify({ name: data.name, role: data.role }));
      toast.success(`¡Bienvenido ${data.name}!`);
      onLogin({ name: data.name, role: data.role });
    } catch (err: unknown) {
      const errorMsg = err instanceof Error ? err.message : "Error al iniciar sesión";
      setError(errorMsg);
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Script
        src="https://accounts.google.com/gsi/client"
        strategy="afterInteractive"
        onLoad={() => setScriptLoaded(true)}
        onReady={() => setScriptLoaded(true)}
      />

      <div className="space-y-4">
        <div className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] p-4 text-sm leading-6 text-[--muted]">
          Usa tu cuenta de Google para entrar. Si es tu primera vez, crearemos tu acceso automáticamente.
        </div>

        <div className="grid gap-4 rounded-2xl border border-[rgba(78,54,39,0.14)] bg-white p-4">
          {clientId ? (
            <div className="flex flex-col items-center gap-3">
              <div ref={buttonRef} className="flex justify-center" />
              {loading && <p className="text-sm text-[--muted]">Validando cuenta de Google...</p>}
            </div>
          ) : (
            <p className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
              Falta configurar NEXT_PUBLIC_GOOGLE_CLIENT_ID en el frontend.
            </p>
          )}

          <div className="relative py-2 text-center text-sm text-[--muted]">
            <span className="relative bg-white px-3">o utiliza tu correo</span>
            <div className="absolute left-0 top-1/2 h-px w-full bg-[rgba(78,54,39,0.14)]" />
          </div>

          <form onSubmit={handleEmailLogin} className="grid gap-3">
            <input
              type="email"
              placeholder="Correo electrónico"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] px-4 py-3 text-[--foreground] outline-none transition placeholder:text-[#8b7667] focus:border-[--accent] focus:bg-white"
              required
            />
            <input
              type="password"
              placeholder="Contraseña"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="rounded-2xl border border-[rgba(78,54,39,0.14)] bg-[#fffaf7] px-4 py-3 text-[--foreground] outline-none transition placeholder:text-[#8b7667] focus:border-[--accent] focus:bg-white"
              required
            />
            <button
              type="submit"
              className="mt-1 cursor-pointer rounded-full bg-[--foreground] p-3 font-semibold text-black transition hover:bg-[--accent]"
              disabled={loading}
            >
              {loading ? "Iniciando sesión..." : "Entrar con correo"}
            </button>
            {error && <p className="text-sm text-red-500">{error}</p>}
          </form>
        </div>
      </div>
    </>
  );
}
