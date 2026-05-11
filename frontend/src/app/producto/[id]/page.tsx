import ProductDisplay from "@/features/products/components/ProductDisplay";

interface PageProps {
  params: Promise<{
    id: string;
  }>;
}

export default async function ProductPage({ params }: PageProps) {
  const { id } = await params;
  return <ProductDisplay productId={parseInt(id)} />;
}