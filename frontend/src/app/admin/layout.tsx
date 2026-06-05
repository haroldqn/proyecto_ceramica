"use client";


import { usePathname } from "next/navigation";
import Link from "next/link";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const { user, isLoading, isAdmin } = useAuth();
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    if (!isLoading && (!user || !isAdmin)) {
      router.push("/");
    }
  }, [user, isAdmin, isLoading, router]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <p className="text-gray-500 font-medium">Verificando credenciales...</p>

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";
import { useAuth } from "@/features/auth/hooks/useAuth";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const { user, isLoading, isAdmin, logout } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const isAdminLogin = pathname === "/admin";

  useEffect(() => {
    if (isLoading) {
      return;
    }

    if (isAdminLogin && user && isAdmin) {
      router.replace("/admin/productos");
      return;
    }

    if (!isAdminLogin && (!user || !isAdmin)) {
      router.replace("/admin");
    }
  }, [user, isAdmin, isLoading, isAdminLogin, router]);

  if (isAdminLogin) {
    return <>{children}</>;
  }

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <p className="text-slate-500 font-medium">Verificando credenciales...</p>

      </div>
    );
  }

  if (!user || !isAdmin) {
    return (

      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-2">Acceso Denegado</h2>
          <p className="text-gray-600">No tienes permisos para acceder a esta sección.</p>

      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-2">Acceso denegado</h2>
          <p className="text-slate-600">No tienes permisos para acceder a esta seccion.</p>

        </div>
      </div>
    );
  }

  const navItems = [

    { name: "Dashboard", href: "/admin/dashboard" },
    { name: "Usuarios", href: "/admin/users" },
    { name: "Productos", href: "/admin/products" },
  ];

  return (
    <div className="flex h-screen bg-gray-100">
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col shadow-md">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800">Admin Panel</h2>
          <p className="text-sm text-gray-500 mt-1">Hola, {user.name}</p>

    { name: "Usuarios", href: "/admin/users" },
    { name: "Productos", href: "/admin/productos" },
  ];

  return (
    <div className="flex h-screen bg-slate-100">
      <aside className="w-64 bg-white border-r border-slate-200 flex flex-col shadow-md">
        <div className="p-6 border-b border-slate-200">
          <h2 className="text-2xl font-bold text-slate-900">Admin Panel</h2>
          <p className="text-sm text-slate-500 mt-1">Hola, {user.name}</p>
        </div>
        <nav className="flex-1 p-4 space-y-2">
          {navItems.map((item) => {
            const isActive = pathname?.startsWith(item.href);
            return (
              <Link
                key={item.name}
                href={item.href}
                className={`block px-4 py-2 rounded-lg transition-colors ${
                  isActive
                    ? "bg-blue-50 text-blue-700 font-medium"

                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"

                    : "text-slate-600 hover:bg-slate-50 hover:text-slate-900"

                }`}
              >
                {item.name}
              </Link>
            );
          })}
        </nav>

        <div className="p-4 border-t border-gray-200 space-y-2">
          <Link href="/" className="block text-sm text-gray-500 hover:text-gray-700 font-medium">
            ← Volver a la tienda
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

        <div className="p-4 border-t border-slate-200 space-y-2">
          <Link href="/" className="block text-sm text-slate-500 hover:text-slate-700 font-medium">
            Volver a la tienda
          </Link>
          <button
            onClick={() => {
              logout();
              router.replace("/admin");
            }}
            className="block text-sm text-red-500 hover:text-red-700 font-medium"
          >
            Cerrar sesion

          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-y-auto">

        <div className="p-8">
          {children}
        </div>
        <div className="p-8">{children}</div>
      </main>
    </div>
  );
}
