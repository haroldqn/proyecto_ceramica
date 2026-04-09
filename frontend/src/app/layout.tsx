import type { Metadata } from 'next'
import '@/styles/globals.css'
import ToasterProvider from '@/components/providers/toaster-provider'

export const metadata: Metadata = {
  title: 'Proyecto Cerámica',
  description: 'Tienda de cerámica',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es">
      <body className="bg-[#f9f5f2]">
        <ToasterProvider />
        {children}
      </body>
    </html>
  )
}