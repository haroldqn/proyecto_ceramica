"use client";

import { useState } from "react";
import LoginForm from "./login-form";
import RegisterForm from "./register-form";

export default function AuthModal({ onClose, onLogin }: any) {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [successMessage, setSuccessMessage] = useState("");

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const handleRegisterSuccess = () => {
    setSuccessMessage("Cuenta creada. Puedes iniciar sesión ahora.");
    setMode("login");
  };

  const handleLoginSuccess = (user: any) => {
    onLogin(user);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center" onClick={handleOverlayClick}>
      <div className="bg-white p-6 rounded-xl w-96">

        {/* Píldora */}
        <div className="flex mb-4 bg-gray-200 rounded-full">
          <button
            onClick={() => {
              setMode("login");
              setSuccessMessage("");
            }}
            className={`flex-1 p-2 rounded-full ${
              mode === "login" ? "bg-[#c08576] text-white" : ""
            }`}
          >
            Iniciar Sesión
          </button>

          <button
            onClick={() => {
              setMode("register");
              setSuccessMessage("");
            }}
            className={`flex-1 p-2 rounded-full ${
              mode === "register" ? "bg-[#c08576] text-white" : ""
            }`}
          >
            Crear Cuenta
          </button>
        </div>

        {successMessage && <p className="mb-3 text-green-600">{successMessage}</p>}

        {/* Form dinámico */}
        {mode === "login" ? (
          <LoginForm onLogin={handleLoginSuccess} />
        ) : (
          <RegisterForm onRegister={handleRegisterSuccess} />
        )}

        <button
          onClick={onClose}
          className="mt-4 text-sm text-gray-500"
        >
          Cerrar
        </button>
      </div>
    </div>
  );
}