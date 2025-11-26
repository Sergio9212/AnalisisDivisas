package monedas.api.infraestructura.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import monedas.api.dominio.entidades.CambioMoneda;

@Repository
public interface ICambioMonedaRepositorio extends JpaRepository<CambioMoneda, Long> {

    @Query("SELECT c FROM CambioMoneda c WHERE c.moneda.id = ?1 ORDER BY c.fecha DESC")
    List<CambioMoneda> listarPorMoneda(Long idMoneda);

    @Query("SELECT c FROM CambioMoneda c WHERE c.moneda.id = ?1 AND c.fecha BETWEEN ?2 AND ?3 ORDER BY c.fecha ASC")
    List<CambioMoneda> listarPorPeriodo(Long idMoneda, LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT c FROM CambioMoneda c WHERE c.moneda.id = ?1 AND c.fecha = ?2")
    CambioMoneda buscarPorMonedaYFecha(Long idMoneda, LocalDate fecha);
}
