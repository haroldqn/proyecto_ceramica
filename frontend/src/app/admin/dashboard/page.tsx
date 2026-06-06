"use client";

import { useEffect, useState } from "react";
import { adminService } from "@/features/admin/services/admin-service";
import DashboardCard from "../components/DashboardCard";

export default function AdminDashboardPage() {
    const [productCount, setProductCount] = useState<number>(0);
    const [userCount, setUserCount] = useState<number>(3); // ESte dato es temporal....
    const [salesCount, setSalesCount] = useState<number>(7); // Lo mismo con este :v
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                setLoading(true);

                const [products, users] = await Promise.all([
                    adminService.getProducts(),
                    adminService.getUsers()
                ]);

                // ESTO ES LA SUMA DEL STOCK TOTAL PARA QUE SE VEA EN INVENTARIO
                const totalStock = products.reduce((accumulator, product) => {
                    return accumulator + (product.stock || 0);
                }, 0);

                setProductCount(totalStock); 
                setUserCount(users.length);   
            } catch (error) {
                console.error("Error al cargar métricas del dashboard", error);
            } finally {
                setLoading(false);
            }
        };

        void fetchDashboardData();
    }, []);

    if (loading) {
        return <div className="text-slate-500 font-medium">Cargando resumen del sistema...</div>;
    }

    return (
        <div className="space-y-6">
            {/* Encabezado*/}
            <div>
                <h1 className="text-2xl font-bold text-slate-950">Resumen del Sistema</h1>
                <p className="text-sm text-[#736357] font-medium mt-1">
                    Estado en tiempo real de las ventas de este mes
                </p>
            </div>

            {/* Esto es el contenedor de las 3 Cards*/}
            <div className="flex flex-col sm:flex-row gap-5 w-full max-w-5xl">
                <DashboardCard
                    title="N° de Ventas"
                    value={salesCount}
                    iconType="sales"
                />
                <DashboardCard
                    title="Clientes recientes"
                    value={userCount}
                    iconType="users"
                />
                <DashboardCard
                    title="Inventario"
                    value={productCount}
                    iconType="inventory"
                />
            </div>
        </div>
    );
}