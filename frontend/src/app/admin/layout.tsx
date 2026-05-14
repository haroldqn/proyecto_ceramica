"use client";

import { usePathname } from "next/navigation";
import Link from "next/link";
// import { useAuth } from "@/features/auth/hooks/useAuth"; // Comentado temporalmente
// import { useEffect } from "react"; // Comentado temporalmente
// import { useRouter } from "next/navigation"; // Comentado temporalmente

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  // const { user } = useAuth();
  // const router = useRouter();
  const pathname = usePathname();

  /*
  useEffect(() => {
    if (!user) {
      router.push("/");
    }
  }, [user, router]);

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <p className="text-gray-500 font-medium">Verificando credenciales...</p>
      </div>
    );
  }
  */

  // Ajusta estos enlaces según los nombres de tus carpetas dentro de app/admin
  const navItems = [
    { name: "Dashboard", href: "/admin/dashboard" },
    { name: "Usuarios", href: "/admin/users" },
    { name: "Productos", href: "/admin/products" },
  ];

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col shadow-md">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800">Admin Panel</h2>
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
                }`}
              >
                {item.name}
              </Link>
            );
          })}
        </nav>
        <div className="p-4 border-t border-gray-200">
          <Link href="/" className="text-sm text-gray-500 hover:text-gray-700 font-medium">
            ← Volver a la tienda
          </Link>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-y-auto">
        <div className="p-8">
          {children}
        </div>
      </main>
    </div>
  );
}