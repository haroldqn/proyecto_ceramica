import type { Category } from "@/features/home/data/catalog";

type CategoryCardProps = {
  category: Category;
};

export default function CategoryCard({ category }: CategoryCardProps) {
  return (
    <article className="group rounded-[2rem] border border-[--border-soft] bg-[--surface] p-6 shadow-[0_18px_45px_rgba(81,53,38,0.08)] transition-transform duration-300 hover:-translate-y-1">
      <div
        className={`mb-6 h-40 rounded-[1.5rem] bg-gradient-to-br ${category.accent} p-5 text-white shadow-inner`}
      >
        <div className="flex h-full flex-col justify-between">
          <span className="w-fit rounded-full border border-white/35 px-3 py-1 text-xs uppercase tracking-[0.24em] text-white/90">
            {category.countLabel}
          </span>
          <div className="space-y-2">
            <div className="h-6 w-24 rounded-full bg-white/20" />
            <div className="h-6 w-16 rounded-full bg-white/15" />
          </div>
        </div>
      </div>

      <div className="space-y-3">
        <h3 className="font-display text-3xl text-[--foreground]">{category.title}</h3>
        <p className="text-sm leading-6 text-[--muted]">{category.description}</p>
      </div>
    </article>
  );
}
