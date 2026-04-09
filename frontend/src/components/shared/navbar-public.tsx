"use client";

import { useState } from "react";
import AuthModal from "@/features/auth/components/auth-modal";

export default function Navbar() {
  const [user, setUser] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <header className="flex justify-between p-4 bg-white shadow">
      <h1 className="font-bold">Mi Tienda</h1>

      {!user ? (
        <button
          onClick={() => setShowModal(true)}
          className="bg-blue-600 text-white px-4 py-1 rounded"
        >
          Iniciar sesión
        </button>
      ) : (
        <div className="relative">
          <button
            onClick={() => setShowDropdown(!showDropdown)}
            className="bg-gray-200 px-3 py-1 rounded"
          >
            {user.name}
          </button>

          {showDropdown && (
            <div className="absolute right-0 mt-2 bg-white shadow rounded p-2">
              <button
                onClick={() => setUser(null)}
                className="text-red-500"
              >
                Cerrar sesión
              </button>
            </div>
          )}
        </div>
      )}

      {showModal && <AuthModal onClose={() => setShowModal(false)} />}
    </header>
  );
}