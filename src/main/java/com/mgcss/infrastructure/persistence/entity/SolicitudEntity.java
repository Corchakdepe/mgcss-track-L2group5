package com.mgcss.infrastructure.persistence.entity;

import com.mgcss.domain.model.EstadoSolicitud;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Solicitud")
public class SolicitudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false)
    private EstadoSolicitud estadoSolicitud;

    @Nullable
    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnicoAsignado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "solicitud_historial", joinColumns = @JoinColumn(name = "solicitud_id"))
    private List<EstadoChangeEntity> historial;

}
