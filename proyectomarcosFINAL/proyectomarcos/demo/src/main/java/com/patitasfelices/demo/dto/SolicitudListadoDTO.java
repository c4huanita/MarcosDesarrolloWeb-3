package com.patitasfelices.demo.dto;

import java.time.LocalDate;

public class SolicitudListadoDTO {
    private Long id;
    private String nombreCompleto;
    private String telefono;
    private String motivo;
    private String estado;
    private LocalDate fechaSolicitud;
    private Long mascotaId;
    private String nombreMascota;      // Viene de tabla mascotas
    private String categoriaMascota;   // Viene de tabla categorias
    private String emailUsuario;       // Viene de tabla usuarios

    public SolicitudListadoDTO(Long id, String nombreCompleto, String telefono, String motivo,
                               String estado, LocalDate fechaSolicitud, Long mascotaId,
                               String nombreMascota, String categoriaMascota, String emailUsuario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.mascotaId = mascotaId;
        this.nombreMascota = nombreMascota;
        this.categoriaMascota = categoriaMascota;
        this.emailUsuario = emailUsuario;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public Long getMascotaId() { return mascotaId; }
    public String getNombreMascota() { return nombreMascota; }
    public String getCategoriaMascota() { return categoriaMascota; }
    public String getEmailUsuario() { return emailUsuario; }
}