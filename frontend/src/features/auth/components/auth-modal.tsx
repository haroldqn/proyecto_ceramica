"use client";

import { useState } from "react";
import LoginForm from "./login-form";
import RegisterForm from "./register-form";

export default function AuthModal({ onClose }: any) {
  const [mode, setMode] = useState<"login" | "register">("login");

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-xl w-96">

        {/* Píldora */}
        <div className="flex mb-4 bg-gray-200 rounded-full">
          <button
            onClick={() => setMode("login")}
            className={`flex-1 p-2 rounded-full ${
              mode === "login" ? "bg-blue-600 text-white" : ""
            }`}
          >
            Login
          </button>

          <button
            onClick={() => setMode("register")}
            className={`flex-1 p-2 rounded-full ${
              mode === "register" ? "bg-green-600 text-white" : ""
            }`}
          >
            Register
          </button>
        </div>

        {/* Form dinámico */}
        {mode === "login" ? <LoginForm /> : <RegisterForm />}

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