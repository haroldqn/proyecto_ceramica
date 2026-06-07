"use client";

import { useEffect, useState, useRef } from "react";
import { adminService } from "@/features/admin/services/admin-service";
import DashboardCard from "../components/DashboardCard";
import DashboardChartCard from "../components/DashboardChartCard";
import { Chart } from "chart.js/auto";

export default function AdminDashboardPage() {
    const [productCount, setProductCount] = useState<number>(0);
    const [userCount, setUserCount] = useState<number>(3); // ESte dato es temporal....
    const [salesCount, setSalesCount] = useState<number>(7); // Lo mismo con este :v
    const [loading, setLoading] = useState(true);

    const [categoryLabels, setCategoryLabels] = useState<string[]>([]);
    const [categoryStocks, setCategoryStocks] = useState<number[]>([]);

    const inventoryChartRef = useRef<HTMLCanvasElement | null>(null);

    const inventoryChartInstance = useRef<Chart | null>(null);

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

                const inventoryMap: { [key: string]: number } = {};
                
                products.forEach(product => {
                    const categoryName = product.categoryName || "Sin Categoría";
                    const stock = product.stock || 0;
                    
                    if (inventoryMap[categoryName]) {
                        inventoryMap[categoryName] += stock;
                    } else {
                        inventoryMap[categoryName] = stock;
                    }
                });

                // Separamos las llaves (nombres) y los valores (totales) para el chart
                setCategoryLabels(Object.keys(inventoryMap));
                setCategoryStocks(Object.values(inventoryMap));

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

    // GRAFICOS (Habrá más pero por el momento solo es este :v)
    useEffect(() => {
        if (loading) return;

        // Gráfico 1: Distribución de Inventario 
        if (inventoryChartRef.current && categoryLabels.length > 0) {
            if (inventoryChartInstance.current) inventoryChartInstance.current.destroy();

            inventoryChartInstance.current = new Chart(inventoryChartRef.current, {
                type: "bar",
                data: {
                    labels: categoryLabels, 
                    datasets: [{
                        label: "Unidades disponibles",
                        data: categoryStocks,
                        backgroundColor: "#e3b792",
                        borderRadius: 8
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } }
                }
            });
        }

        // Limpieza de gráficos al desmontar el componente para evitar duplicados en memoria
        return () => {
            if (inventoryChartInstance.current) inventoryChartInstance.current.destroy();
        };
    }, [loading, categoryLabels, categoryStocks]);

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

            {/* Esto contiene los Gráficos en tarjetas reutilizables */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5 w-full max-w-5xl">
                <DashboardChartCard title="Distribución de Inventario" subtitle="Unidades en stock por tipo de cerámica">
                    <canvas ref={inventoryChartRef} />
                </DashboardChartCard>
            </div>
        </div>
    );
}
