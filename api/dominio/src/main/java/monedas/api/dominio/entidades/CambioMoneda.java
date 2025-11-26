package monedas.api.dominio.entidades;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "cambiomoneda")
public class CambioMoneda {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "idmoneda", referencedColumnName = "id", nullable = false)
    private Moneda moneda;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "cambio", nullable = false)
    private double cambio;

    // Constructores
    public CambioMoneda() {
    }

    public CambioMoneda(long id, Moneda moneda, LocalDate fecha, double cambio) {
        this.id = id;
        this.moneda = moneda;
        this.fecha = fecha;
        this.cambio = cambio;
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }
}
