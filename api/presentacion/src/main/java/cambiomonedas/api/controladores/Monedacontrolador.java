package cambiomonedas.api.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import monedas.api.core.servicios.IMonedaServicio;
import monedas.api.dominio.entidades.Moneda;

@RestController
@RequestMapping("/api/monedas")
@CrossOrigin(origins = "*") // Permitir CORS para desarrollo
public class Monedacontrolador {

    @Autowired
    private IMonedaServicio monedaServicio;

    /**
     * GET /api/monedas/listar
     * Lista todas las monedas
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Moneda>> listar() {
        List<Moneda> monedas = monedaServicio.listar();
        return ResponseEntity.ok(monedas);
    }

    /**
     * GET /api/monedas/obtener/{id}
     * Obtiene una moneda por su ID
     */
    @GetMapping("/obtener/{id}")
    public ResponseEntity<Moneda> obtener(@PathVariable Long id) {
        Moneda moneda = monedaServicio.obtener(id);
        if (moneda != null) {
            return ResponseEntity.ok(moneda);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/monedas/buscar?nombre=xxx
     * Busca monedas por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Moneda>> buscar(@RequestParam String nombre) {
        List<Moneda> monedas = monedaServicio.buscar(nombre);
        return ResponseEntity.ok(monedas);
    }

    /**
     * GET /api/monedas/buscarPorPais?nombre=xxx
     * Busca la moneda de un país específico
     */
    @GetMapping("/buscarPorPais")
    public ResponseEntity<Moneda> buscarPorPais(@RequestParam String nombre) {
        Moneda moneda = monedaServicio.buscarPorPais(nombre);
        if (moneda != null) {
            return ResponseEntity.ok(moneda);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/monedas/crear
     * Crea una nueva moneda
     */
    @PostMapping("/crear")
    public ResponseEntity<Moneda> crear(@RequestBody Moneda moneda) {
        try {
            Moneda nuevaMoneda = monedaServicio.agregar(moneda);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMoneda);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /api/monedas/actualizar/{id}
     * Actualiza una moneda existente
     */
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Moneda> actualizar(@PathVariable Long id, @RequestBody Moneda moneda) {
        moneda.setId(id);
        Moneda monedaActualizada = monedaServicio.modificar(moneda);
        if (monedaActualizada != null) {
            return ResponseEntity.ok(monedaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/monedas/eliminar/{id}
     * Elimina una moneda por su ID
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = monedaServicio.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/monedas/health
     * Endpoint de salud para verificar que el servicio está funcionando
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de Monedas funcionando correctamente");
    }
}
