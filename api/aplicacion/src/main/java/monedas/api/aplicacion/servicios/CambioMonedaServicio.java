package monedas.api.aplicacion.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import monedas.api.dominio.entidades.CambioMoneda;
import monedas.api.core.servicios.ICambioMonedaServicio;
import monedas.api.infraestructura.repositorios.ICambioMonedaRepositorio;

@Service
public class CambioMonedaServicio implements ICambioMonedaServicio {

    @Autowired
    private ICambioMonedaRepositorio repositorio;

    @Override
    public List<CambioMoneda> listar() {
        return repositorio.findAll();
    }

    @Override
    @SuppressWarnings("null")
    public CambioMoneda obtener(Long id) {
        var cambio = repositorio.findById(id);
        return cambio.isEmpty() ? null : cambio.get();
    }

    @Override
    public List<CambioMoneda> listarPorMoneda(Long idMoneda) {
        return repositorio.listarPorMoneda(idMoneda);
    }

    @Override
    public List<CambioMoneda> listarPorPeriodo(Long idMoneda, LocalDate fechaInicio, LocalDate fechaFin) {
        return repositorio.listarPorPeriodo(idMoneda, fechaInicio, fechaFin);
    }

    @Override
    public CambioMoneda buscarPorMonedaYFecha(Long idMoneda, LocalDate fecha) {
        return repositorio.buscarPorMonedaYFecha(idMoneda, fecha);
    }

    @Override
    public CambioMoneda agregar(CambioMoneda cambioMoneda) {
        cambioMoneda.setId(0);
        return repositorio.save(cambioMoneda);
    }

    @Override
    @SuppressWarnings("null")
    public CambioMoneda modificar(CambioMoneda cambioMoneda) {
        Optional<CambioMoneda> cambioEncontrado = repositorio.findById(cambioMoneda.getId());
        if (!cambioEncontrado.isEmpty()) {
            return repositorio.save(cambioMoneda);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("null")
    public boolean eliminar(Long id) {
        try {
            repositorio.deleteById(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
