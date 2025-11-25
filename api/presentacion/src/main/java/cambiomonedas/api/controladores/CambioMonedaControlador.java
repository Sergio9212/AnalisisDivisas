package cambiomonedas.api.controladores;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import monedas.api.core.servicios.ICambioMonedaServicio;
import monedas.api.dominio.entidades.CambioMoneda;

@RestController
@RequestMapping("/api/cambios")
@CrossOrigin(origins = "*")
public class CambioMonedaControlador {

    @Autowired
    private ICambioMonedaServicio cambioMonedaServicio;

    /**
     * GET /api/cambios/listar
     * Lista todos los cambios de moneda
     */
    @GetMapping("/listar")
    public ResponseEntity<List<CambioMoneda>> listar() {
        List<CambioMoneda> cambios = cambioMonedaServicio.listar();
        return ResponseEntity.ok(cambios);
    }

    /**
     * GET /api/cambios/obtener/{id}
     * Obtiene un cambio de moneda por su ID
     */
    @GetMapping("/obtener/{id}")
    public ResponseEntity<CambioMoneda> obtener(@PathVariable Long id) {
        CambioMoneda cambio = cambioMonedaServicio.obtener(id);
        if (cambio != null) {
            return ResponseEntity.ok(cambio);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/cambios/listarPorMoneda/{idMoneda}
     * Lista todos los cambios de una moneda específica
     */
    @GetMapping("/listarPorMoneda/{idMoneda}")
    public ResponseEntity<List<CambioMoneda>> listarPorMoneda(@PathVariable Long idMoneda) {
        List<CambioMoneda> cambios = cambioMonedaServicio.listarPorMoneda(idMoneda);
        return ResponseEntity.ok(cambios);
    }

    /**
     * GET
     * /api/cambios/listarPorPeriodo?idMoneda=1&fechaInicio=2024-01-01&fechaFin=2024-12-31
     * Lista cambios de una moneda en un período específico
     */
    @GetMapping("/listarPorPeriodo")
    public ResponseEntity<List<CambioMoneda>> listarPorPeriodo(
            @RequestParam Long idMoneda,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<CambioMoneda> cambios = cambioMonedaServicio.listarPorPeriodo(idMoneda, fechaInicio, fechaFin);
        return ResponseEntity.ok(cambios);
    }

    /**
     * GET /api/cambios/buscarPorFecha?idMoneda=1&fecha=2024-01-01
     * Busca el cambio de una moneda en una fecha específica
     */
    @GetMapping("/buscarPorFecha")
    public ResponseEntity<CambioMoneda> buscarPorFecha(
            @RequestParam Long idMoneda,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        CambioMoneda cambio = cambioMonedaServicio.buscarPorMonedaYFecha(idMoneda, fecha);
        if (cambio != null) {
            return ResponseEntity.ok(cambio);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/cambios/crear
     * Crea un nuevo registro de cambio de moneda
     */
    @PostMapping("/crear")
    public ResponseEntity<CambioMoneda> crear(@RequestBody CambioMoneda cambioMoneda) {
        try {
            CambioMoneda nuevoCambio = cambioMonedaServicio.agregar(cambioMoneda);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCambio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /api/cambios/actualizar/{id}
     * Actualiza un registro de cambio de moneda existente
     */
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CambioMoneda> actualizar(@PathVariable Long id, @RequestBody CambioMoneda cambioMoneda) {
        cambioMoneda.setId(id);
        CambioMoneda cambioActualizado = cambioMonedaServicio.modificar(cambioMoneda);
        if (cambioActualizado != null) {
            return ResponseEntity.ok(cambioActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/cambios/eliminar/{id}
     * Elimina un registro de cambio de moneda por su ID
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = cambioMonedaServicio.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/cambios/health
     * Endpoint de salud
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de Cambios de Moneda funcionando correctamente");
    }
}
