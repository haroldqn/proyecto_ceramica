"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import AuthModal from "@/features/auth/components/auth-modal";
import type { AuthUser } from "@/features/auth/types";

export default function Navbar() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);

  useEffect(() => {
    const syncUserFromStorage = () => {
      const storedUser = window.localStorage.getItem("user");
      setUser(storedUser ? (JSON.parse(storedUser) as AuthUser) : null);
    };

    syncUserFromStorage();
    window.addEventListener("storage", syncUserFromStorage);

    return () => {
      window.removeEventListener("storage", syncUserFromStorage);
    };
  }, []);

  const handleSectionScroll = (sectionId: string) => {
    const section = document.getElementById(sectionId);
    if (!section) {
      return;
    }

    const headerOffset = 96;
    const sectionPosition = section.getBoundingClientRect().top + window.scrollY;

    window.scrollTo({
      top: sectionPosition - headerOffset,
      behavior: "smooth",
    });
  };

  const handleLogin = (userData: AuthUser) => {
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
    <>
      <header className="sticky top-0 z-40 border-b border-[rgba(120,87,66,0.1)] bg-[rgba(251,246,241,0.82)] backdrop-blur-xl">
        <div className="mx-auto flex max-w-7xl items-center justify-between gap-6 px-5 py-4 md:px-8">
          <Link href="/" className="space-y-1">
            <p className="font-display text-3xl leading-none text-[--foreground]">
              El mundo de Mery
            </p>
            <p className="text-[11px] uppercase tracking-[0.28em] text-[--muted]">
              Cerámica decorativa
            </p>
          </Link>

          <nav className="hidden items-center gap-8 text-sm text-[--muted] md:flex">
            <button
              type="button"
              onClick={() => handleSectionScroll("colecciones")}
              className="cursor-pointer transition hover:text-[--foreground]"
            >
              Colecciones
            </button>
            <button
              type="button"
              onClick={() => handleSectionScroll("destacados")}
              className="cursor-pointer transition hover:text-[--foreground]"
            >
              Destacados
            </button>
            <button
              type="button"
              onClick={() => handleSectionScroll("proceso")}
              className="cursor-pointer transition hover:text-[--foreground]"
            >
              Proceso
            </button>
          </nav>

          <div className="flex items-center gap-3">
            <button className="hidden rounded-full border border-[--border-soft] px-4 py-2 text-sm text-[--foreground] md:inline-flex">
              Carrito (0)
            </button>

            {!user ? (
              <button
                onClick={() => setShowModal(true)}
                className="button-primary cursor-pointer rounded-full px-5 py-2.5 text-sm font-semibold text-black"
              >
                Iniciar sesión
              </button>
            ) : (
              <div className="relative">
                <button
                  onClick={() => setShowDropdown(!showDropdown)}
                  className="cursor-pointer rounded-full border border-[--border-soft] bg-white px-4 py-2 text-sm font-semibold text-[--foreground]"
                >
                  {user.name}
                </button>

                {showDropdown && (
                  <div className="absolute right-0 mt-3 min-w-44 rounded-2xl border border-[--border-soft] bg-white p-2 shadow-[0_20px_50px_rgba(51,31,21,0.14)]">
                    <button
                      onClick={handleLogout}
                      className="w-full cursor-pointer rounded-xl px-3 py-2 text-left text-sm text-red-600 transition hover:bg-red-50"
                    >
                      Cerrar sesión
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </header>

      {showModal && <AuthModal onClose={() => setShowModal(false)} onLogin={handleLogin} />}
    </>
  );
}
