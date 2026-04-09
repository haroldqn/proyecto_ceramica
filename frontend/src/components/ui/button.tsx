type Props = {
  children: React.ReactNode;
  onClick?: () => void;
};

export default function Button({ children, onClick }: Props) {
  return (
    <button
      onClick={onClick}
      className="px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700"
    >
      {children}
    </button>
  );
}