"use client";

import { useState } from "react";
import LoginForm from "./login-form";
import RegisterForm from "./register-form";
import type { AuthModalProps, AuthUser } from "@/features/auth/types";

export default function AuthModal({ onClose, onLogin }: AuthModalProps) {
  const [mode, setMode] = useState<"login" | "register">("login");

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const handleLoginSuccess = (user: AuthUser) => {
    onLogin(user);
    onClose();
  };

  const handleRegisterSuccess = () => {
    setMode("login");
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-[rgba(22,14,9,0.62)] px-4 backdrop-blur-sm"
      onClick={handleOverlayClick}
    >
      <div className="w-full max-w-md rounded-[2rem] border border-[rgba(72,49,35,0.12)] bg-[#fffdfb] p-7 shadow-[0_30px_80px_rgba(50,29,18,0.24)]">
        <div className="mb-6 space-y-4">
          <div className="flex overflow-hidden rounded-full border border-[rgba(72,49,35,0.12)] bg-white">
            <button
              type="button"
              onClick={() => setMode("login")}
              className={`flex-1 rounded-full px-4 py-2 text-sm font-semibold transition ${
                mode === "login"
                  ? "bg-[--accent] text-black"
                  : "text-[--muted] hover:text-[--foreground]"
              }`}
            >
              Iniciar sesión
            </button>
            <button
              type="button"
              onClick={() => setMode("register")}
              className={`flex-1 rounded-full px-4 py-2 text-sm font-semibold transition ${
                mode === "register"
                  ? "bg-[--accent] text-black"
                  : "text-[--muted] hover:text-[--foreground]"
              }`}
            >
              Registrarse
            </button>
          </div>

          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.3em] text-[--accent]">
              El mundo de Mery
            </p>
            <h2 className="font-display text-4xl leading-none text-[--foreground]">
              {mode === "login" ? "Accede con tu cuenta" : "Crea tu cuenta"}
            </h2>
            <p className="text-sm leading-6 text-[--muted]">
              {mode === "login"
                ? "Elige Google o tu correo y contraseña para entrar al sistema."
                : "Si no tienes cuenta, regístrate con tu DNI, correo y contraseña."}
            </p>
          </div>
        </div>

        {mode === "login" ? (
          <LoginForm onLogin={handleLoginSuccess} />
        ) : (
          <RegisterForm onRegister={handleRegisterSuccess} />
        )}

        <button
          onClick={onClose}
          className="mt-5 text-sm font-medium text-[--muted] transition hover:text-[--foreground]"
        >
          Cerrar
        </button>
      </div>
    </div>
  );
}
