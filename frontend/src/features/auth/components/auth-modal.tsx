"use client";

import LoginForm from "./login-form";
import type { AuthModalProps, AuthUser } from "@/features/auth/types";

export default function AuthModal({ onClose, onLogin }: AuthModalProps) {
  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const handleLoginSuccess = (user: AuthUser) => {
    onLogin(user);
    onClose();
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-[rgba(22,14,9,0.62)] px-4 backdrop-blur-sm"
      onClick={handleOverlayClick}
    >
      <div className="w-full max-w-md rounded-[2rem] border border-[rgba(72,49,35,0.12)] bg-[#fffdfb] p-7 shadow-[0_30px_80px_rgba(50,29,18,0.24)]">
        <div className="mb-6 space-y-2">
          <p className="text-xs font-semibold uppercase tracking-[0.3em] text-[--accent]">
            El mundo de Mery
          </p>
          <h2 className="font-display text-4xl leading-none text-[--foreground]">
            Accede con Google
          </h2>
          <p className="text-sm leading-6 text-[--muted]">
            Inicia sesión con tu cuenta de Google para continuar tu compra y guardar tus favoritos.
          </p>
        </div>

        <LoginForm onLogin={handleLoginSuccess} />

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
