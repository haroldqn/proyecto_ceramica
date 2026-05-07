"use client";

import { useEffect, useRef, useState } from "react";
import Script from "next/script";
import toast from "react-hot-toast";

type Props = {
  onLogin: (user: { name: string; role: string }) => void;
};

export default function LoginForm({ onLogin }: Props) {
  const [loading, setLoading] = useState(false);
  const [scriptLoaded, setScriptLoaded] = useState(false);
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

    window.google.accounts.id.initialize({
      client_id: clientId,
      callback: async ({ credential }) => {
        setLoading(true);

        try {
          const response = await fetch("http://localhost:8080/api/auth/google", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ credential }),
          });

          if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "No se pudo iniciar sesión con Google");
          }

          const data = await response.json();
          localStorage.setItem("token", data.token);
          localStorage.setItem("user", JSON.stringify({ name: data.name, role: data.role }));
          toast.success(`¡Bienvenido ${data.name}!`);
          onLogin({ name: data.name, role: data.role });
        } catch (err: unknown) {
          const errorMsg = err instanceof Error ? err.message : "Error al iniciar con Google";
          toast.error(errorMsg);
        } finally {
          setLoading(false);
        }
      },
      auto_select: false,
      cancel_on_tap_outside: true,
    });

    window.google.accounts.id.renderButton(buttonRef.current, {
      theme: "outline",
      size: "large",
      shape: "pill",
      text: "continue_with",
      width: 320,
      logo_alignment: "left",
    });
  }, [clientId, onLogin, scriptLoaded]);

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

        {!clientId ? (
          <p className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
            Falta configurar NEXT_PUBLIC_GOOGLE_CLIENT_ID en el frontend.
          </p>
        ) : (
          <div className="flex flex-col items-center gap-3">
            <div ref={buttonRef} className="flex justify-center" />
            {loading && <p className="text-sm text-[--muted]">Validando cuenta de Google...</p>}
          </div>
        )}
      </div>
    </>
  );
}
