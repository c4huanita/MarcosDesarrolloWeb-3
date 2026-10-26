package com.patitasfelices.demo.config;

import com.patitasfelices.demo.model.Categoria;
import com.patitasfelices.demo.model.Rol;
import com.patitasfelices.demo.model.Usuario;
import com.patitasfelices.demo.repository.CategoriaRepository;
import com.patitasfelices.demo.repository.RolRepository;
import com.patitasfelices.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carga los datos mínimos la primera vez que se arranca la aplicación:
 * roles, un administrador y las categorías base.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Rol admin = rolRepository.findByNombreIgnoreCase("Administrador")
                .orElseGet(() -> rolRepository.save(new Rol("Administrador")));
        rolRepository.findByNombreIgnoreCase("Adoptante")
                .orElseGet(() -> rolRepository.save(new Rol("Adoptante")));

        if (usuarioRepository.findByEmailIgnoreCase("admin@patitas.com").isEmpty()) {
            Usuario u = new Usuario();
            u.setNombre("Administrador General");
            u.setEmail("admin@patitas.com");
            u.setPassword("123456");
            u.setRol(admin);
            usuarioRepository.save(u);
        }

        if (categoriaRepository.count() == 0) {
            categoriaRepository.save(new Categoria(null, "Cachorros", "Mascotas menores a 6 meses"));
            categoriaRepository.save(new Categoria(null, "Adolescente", "Mascotas entre los 6 meses a 2 años"));
            categoriaRepository.save(new Categoria(null, "Adulto", "Mascotas entre los 2 a 7 años"));
        }
    }
}
