"use client";

import { useState, useEffect } from "react";
import AuthModal from "@/features/auth/components/auth-modal";

export default function Navbar() {
  const [user, setUser] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogin = (userData: { name: string; role: string }) => {
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setShowDropdown(false);
  };

  return (
    <header className="flex justify-between p-4 bg-[#ededed] shadow">
      <h1 className="font-bold">El mundo de Mery</h1>

    <nav className="flex text-[#460C01] font-medium">
      <button className="px-20 hover:rounded-lg hover:bg-[#CCCCCC] cursor-pointer">
        Inicio
      </button>
      <button className="px-20 hover:rounded-lg hover:bg-[#CCCCCC] cursor-pointer">
        Productos
      </button>
      <button className="px-20 hover:rounded-lg hover:bg-[#CCCCCC] cursor-pointer">
        Contacto
      </button>
    </nav>

      {!user ? (
        <button
          onClick={() => setShowModal(true)}
          className="bg-[#c08576] text-white px-4 py-1 rounded cursor-pointer"
        >
          Iniciar sesión
        </button>
      ) : (
        <div className="relative">
          <button
            onClick={() => setShowDropdown(!showDropdown)}
            className="hover:bg-[#CCCCCC] bg-gray-300 px-3 py-1 rounded cursor-pointer"
          >
            {user.name}
          </button>

          {showDropdown && (
            <div className="absolute right-0 mt-2 bg-white shadow rounded p-2">
              <button
                onClick={handleLogout}
                className="px-3 py-1 hover:bg-[#CCCCCC] hover:text-[#9E665A] text-[#c08576] cursor-pointer whitespace-nowrap"
              >
                Cerrar sesión
              </button>
            </div>
          )}
        </div>
      )}

      {showModal && <AuthModal onClose={() => setShowModal(false)} onLogin={handleLogin} />}
    </header>
  );
}