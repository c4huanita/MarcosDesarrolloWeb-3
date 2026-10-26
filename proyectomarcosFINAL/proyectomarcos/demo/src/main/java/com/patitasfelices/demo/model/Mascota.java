package com.patitasfelices.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La raza es obligatoria")
    @Size(max = 60, message = "La raza no puede superar los 60 caracteres")
    @Column(length = 60)
    private String raza;

    @NotBlank(message = "La edad es obligatoria")
    @Size(max = 30, message = "La edad no puede superar los 30 caracteres")
    @Column(length = 30)
    private String edad;

    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "Macho|Hembra", message = "El sexo debe ser Macho o Hembra")
    @Column(length = 10)
    private String sexo;

    @NotBlank(message = "El tamaño es obligatorio")
    @Size(max = 30, message = "El tamaño no puede superar los 30 caracteres")
    @Column(length = 30)
    private String tamano;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0 kg")
    @DecimalMax(value = "120.0", message = "El peso no puede superar los 120 kg")
    private Double peso;

    @Size(max = 50, message = "La personalidad no puede superar los 50 caracteres")
    @Column(length = 50)
    private String personalidad;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagen;

    // Muchas mascotas pertenecen a una categoría
    @NotNull(message = "Debes seleccionar una categoría")
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Al borrar la mascota se borran sus solicitudes automáticamente
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudAdopcion> solicitudes;

    public Mascota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getPersonalidad() { return personalidad; }
    public void setPersonalidad(String personalidad) { this.personalidad = personalidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public List<SolicitudAdopcion> getSolicitudes() { return solicitudes; }
    public void setSolicitudes(List<SolicitudAdopcion> solicitudes) { this.solicitudes = solicitudes; }
}
