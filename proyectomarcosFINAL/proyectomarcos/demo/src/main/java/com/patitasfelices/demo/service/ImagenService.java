package com.patitasfelices.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Guarda las fotos de las mascotas en la carpeta static/Img del proyecto y
 * expone la ruta para que WebConfig las sirva en vivo desde el disco.
 */
@Service
public class ImagenService {

    private final Path directorio;

    public ImagenService() {
        Path ruta = Paths.get("src", "main", "resources", "static", "Img").toAbsolutePath();
        if (!Files.exists(ruta)) {
            // Cuando VS Code ejecuta desde la carpeta padre del proyecto
            Path alternativa = Paths.get("demo", "src", "main", "resources", "static", "Img").toAbsolutePath();
            if (Files.exists(alternativa)) {
                ruta = alternativa;
            }
        }
        try {
            Files.createDirectories(ruta);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear la carpeta de imágenes: " + ruta, e);
        }
        this.directorio = ruta;
    }

    public Path getDirectorio() {
        return directorio;
    }

    /** Guarda el archivo y devuelve la ruta web (ej: /Img/1700000000_max.jpg). */
    public String guardar(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename() == null ? "foto.jpg" : file.getOriginalFilename();
        String limpio = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String nombreArchivo = System.currentTimeMillis() + "_" + limpio;

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, directorio.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
        }
        return "/Img/" + nombreArchivo;
    }
}
