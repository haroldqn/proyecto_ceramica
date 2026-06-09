package com.example.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Utilidad para manejar la subida de archivos.
 * Proporciona métodos para validar, guardar y eliminar archivos subidos.
 */
public final class FileUploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    
    // Tipos de archivos permitidos para imágenes
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/webp", "image/gif"
    );
    
    // Tamaño máximo de archivo (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private FileUploadUtil() {
        // Clase utilitaria, no instanciable
    }

    /**
     * Guarda un archivo subido en el directorio especificado.
     *
     * @param file Archivo multipart subido
     * @param uploadDir Directorio de destino
     * @return Nombre del archivo guardado
     * @throws IOException Si ocurre un error al guardar
     */
    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Validar tipo de archivo
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido: " + file.getContentType());
        }

        // Validar tamaño
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido de 10MB");
        }

        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;

        // Guardar archivo
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        logger.info("Archivo guardado exitosamente: {}", filePath.toAbsolutePath());
        return fileName;
    }

    /**
     * Elimina un archivo del sistema de archivos.
     *
     * @param uploadDir Directorio donde está el archivo
     * @param fileName Nombre del archivo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public static boolean deleteFile(String uploadDir, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Archivo eliminado: {}", filePath.toAbsolutePath());
                return true;
            }
        } catch (IOException e) {
            logger.error("Error al eliminar archivo: {}", filePath.toAbsolutePath(), e);
        }
        return false;
    }

    /**
     * Verifica si un archivo existe en el directorio especificado.
     *
     * @param uploadDir Directorio donde buscar
     * @param fileName Nombre del archivo
     * @return true si el archivo existe, false en caso contrario
     */
    public static boolean fileExists(String uploadDir, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(uploadDir).resolve(fileName));
    }

    /**
     * Obtiene el tamaño del archivo en formato legible.
     *
     * @param bytes Tamaño en bytes
     * @return Tamaño formateado (KB, MB, etc.)
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * Valida si el tipo de archivo es una imagen.
     *
     * @param contentType Tipo MIME del archivo
     * @return true si es una imagen, false en caso contrario
     */
    public static boolean isImage(String contentType) {
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }
}