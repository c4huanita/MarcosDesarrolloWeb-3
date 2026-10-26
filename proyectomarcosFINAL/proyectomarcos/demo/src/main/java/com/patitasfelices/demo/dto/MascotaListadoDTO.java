package com.patitasfelices.demo.dto;

public class MascotaListadoDTO {
    private Long id;
    private String nombre;
    private String raza;
    private String edad;
    private String sexo;
    private String tamano;
    private String peso;
    private String personalidad;
    private String descripcion;
    private String imagen;
    private Long categoriaId;
    private String nombreCategoria;  // Viene de la tabla categorias
    private Long totalSolicitudes;   // Calculado contando la tabla solicitudes

    public MascotaListadoDTO(Long id, String nombre, String raza, String edad, String sexo,
                             String tamano, String peso, String personalidad, String descripcion,
                             String imagen, Long categoriaId, String nombreCategoria, Long totalSolicitudes) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.tamano = tamano;
        this.peso = peso;
        this.personalidad = personalidad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.totalSolicitudes = totalSolicitudes;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRaza() { return raza; }
    public String getEdad() { return edad; }
    public String getSexo() { return sexo; }
    public String getTamano() { return tamano; }
    public String getPeso() { return peso; }
    public String getPersonalidad() { return personalidad; }
    public String getDescripcion() { return descripcion; }
    public String getImagen() { return imagen; }
    public Long getCategoriaId() { return categoriaId; }
    public String getNombreCategoria() { return nombreCategoria; }
    public Long getTotalSolicitudes() { return totalSolicitudes; }
}