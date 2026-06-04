"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
<<<<<<< HEAD
import { usePathname, useRouter } from "next/navigation";
import type { AuthUser } from "@/features/auth/types";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [checkingSession, setCheckingSession] = useState(true);
=======
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const { user, isLoading, isAdmin } = useAuth();
  const router = useRouter();
>>>>>>> Jair
  const pathname = usePathname();
  const router = useRouter();

  useEffect(() => {
<<<<<<< HEAD
    const storedUser = window.localStorage.getItem("user");
    const storedToken = window.localStorage.getItem("token");

    if (!storedUser || !storedToken) {
      router.push("/");
      return;
    }

    try {
      setUser(JSON.parse(storedUser) as AuthUser);
    } catch {
      window.localStorage.removeItem("user");
      window.localStorage.removeItem("token");
      router.push("/");
    } finally {
      setCheckingSession(false);
    }
  }, [router]);

  if (checkingSession) {
=======
    if (!isLoading && (!user || !isAdmin)) {
      router.push("/");
    }
  }, [user, isAdmin, isLoading, router]);

  if (isLoading) {
>>>>>>> Jair
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50">
        <p className="font-medium text-gray-500">Verificando credenciales...</p>
      </div>
    );
  }

  if (user?.role !== "ADMIN") {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-100 p-6">
        <div className="max-w-md rounded-2xl border border-red-100 bg-white p-6 text-center shadow-sm">
          <p className="text-sm font-semibold uppercase text-red-500">Acceso restringido</p>
          <h1 className="mt-2 text-2xl font-bold text-gray-900">
            No tienes permisos de administrador
          </h1>
          <p className="mt-3 text-sm leading-6 text-gray-600">
            Esta seccion solo esta disponible para usuarios con rol ADMIN. Inicia sesion con una cuenta administradora.
          </p>
          <Link
            href="/"
            className="mt-5 inline-flex rounded-full bg-gray-900 px-5 py-3 text-sm font-semibold text-white hover:bg-gray-700"
          >
            Volver a la tienda
          </Link>
        </div>
      </div>
    );
  }

<<<<<<< HEAD
=======
  if (!user || !isAdmin) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-2">Acceso Denegado</h2>
          <p className="text-gray-600">No tienes permisos para acceder a esta sección.</p>
        </div>
      </div>
    );
  }

>>>>>>> Jair
  const navItems = [
    { name: "Dashboard", href: "/admin/dashboard" },
    { name: "Usuarios", href: "/admin/users" },
    { name: "Productos", href: "/admin/products" },
  ];

  return (
    <div className="flex h-screen bg-gray-100">
<<<<<<< HEAD
      <aside className="flex w-64 flex-col border-r border-gray-200 bg-white shadow-md">
        <div className="border-b border-gray-200 p-6">
          <h2 className="text-2xl font-bold text-gray-800">Admin Panel</h2>
          <p className="mt-1 text-sm text-gray-500">{user.name}</p>
=======
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col shadow-md">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800">Admin Panel</h2>
          <p className="text-sm text-gray-500 mt-1">Hola, {user.name}</p>
>>>>>>> Jair
        </div>
        <nav className="flex-1 space-y-2 p-4">
          {navItems.map((item) => {
            const isActive = pathname?.startsWith(item.href);
            return (
              <Link
                key={item.name}
                href={item.href}
                className={`block rounded-lg px-4 py-2 transition-colors ${
                  isActive
                    ? "bg-blue-50 font-medium text-blue-700"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`}
              >
                {item.name}
              </Link>
            );
          })}
        </nav>
<<<<<<< HEAD
        <div className="border-t border-gray-200 p-4">
          <Link href="/" className="text-sm font-medium text-gray-500 hover:text-gray-700">
            Volver a la tienda
=======
        <div className="p-4 border-t border-gray-200 space-y-2">
          <Link href="/" className="block text-sm text-gray-500 hover:text-gray-700 font-medium">
            ← Volver a la tienda
>>>>>>> Jair
          </Link>
          <button
            onClick={() => {
              localStorage.removeItem("token");
              localStorage.removeItem("user");
              router.push("/");
              window.location.reload();
            }}
            className="block text-sm text-red-500 hover:text-red-700 font-medium"
          >
            Cerrar sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-y-auto">
        <div className="p-8">{children}</div>
      </main>
    </div>
  );
}
