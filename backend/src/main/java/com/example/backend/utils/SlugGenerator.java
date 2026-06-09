package com.example.backend.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilidad para generar slugs (URLs amigables) a partir de textos.
 * Los slugs son versiones normalizadas de cadenas de texto que son
 * seguras para usar en URLs.
 */
public final class SlugGenerator {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9-]");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    private SlugGenerator() {
        // Clase utilitaria, no instanciable
    }

    /**
     * Genera un slug a partir de un texto dado.
     * El proceso incluye:
     * 1. Convertir a minúsculas
     * 2. Normalizar caracteres (eliminar acentos)
     * 3. Reemplazar espacios y caracteres especiales con guiones
     * 4. Eliminar guiones múltiples consecutivos
     * 5. Eliminar guiones al inicio y final
     *
     * @param text Texto original para convertir a slug
     * @return Slug generado
     */
    public static String generateSlug(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Convertir a minúsculas y normalizar (eliminar acentos)
        String normalized = Normalizer.normalize(text.toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Reemplazar espacios y caracteres especiales con guiones
        String slug = normalized.replaceAll("[\\s_]", "-");
        slug = slug.replaceAll("[^a-z0-9\\-]", "");

        // Eliminar guiones múltiples
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");

        // Eliminar guiones al inicio y final
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }

    /**
     * Genera un slug único añadiendo un sufijo numérico si es necesario.
     *
     * @param baseSlug Slug base
     * @param existingSlugs Conjunto de slugs existentes
     * @return Slug único
     */
    public static String generateUniqueSlug(String baseSlug, java.util.Set<String> existingSlugs) {
        if (!existingSlugs.contains(baseSlug)) {
            return baseSlug;
        }

        int counter = 2;
        String uniqueSlug;
        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (existingSlugs.contains(uniqueSlug));

        return uniqueSlug;
    }

    /**
     * Valida si un slug tiene el formato correcto.
     *
     * @param slug Slug a validar
     * @return true si el slug es válido, false en caso contrario
     */
    public static boolean isValidSlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            return false;
        }
        return NON_ALPHANUMERIC.matcher(slug).find() == false 
               && !slug.startsWith("-") 
               && !slug.endsWith("-")
               && !slug.contains("--");
    }
}