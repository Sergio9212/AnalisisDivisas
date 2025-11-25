package monedas.api.core.servicios;

import java.time.LocalDate;
import java.util.List;

import monedas.api.dominio.entidades.CambioMoneda;

public interface ICambioMonedaServicio {

    public List<CambioMoneda> listar();

    public CambioMoneda obtener(Long id);

    public List<CambioMoneda> listarPorMoneda(Long idMoneda);

    public List<CambioMoneda> listarPorPeriodo(Long idMoneda, LocalDate fechaInicio, LocalDate fechaFin);

    public CambioMoneda buscarPorMonedaYFecha(Long idMoneda, LocalDate fecha);

    public CambioMoneda agregar(CambioMoneda cambioMoneda);

    public CambioMoneda modificar(CambioMoneda cambioMoneda);

    public boolean eliminar(Long id);
}
