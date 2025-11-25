package cambiomonedas.api.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import monedas.api.core.servicios.IPaisServicio;
import monedas.api.dominio.entidades.Pais;

@RestController
@RequestMapping("/api/paises")
@CrossOrigin(origins = "*")
public class PaisControlador {

    @Autowired
    private IPaisServicio paisServicio;

    /**
     * GET /api/paises/listar
     * Lista todos los países
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Pais>> listar() {
        List<Pais> paises = paisServicio.listar();
        return ResponseEntity.ok(paises);
    }

    /**
     * GET /api/paises/obtener/{id}
     * Obtiene un país por su ID
     */
    @GetMapping("/obtener/{id}")
    public ResponseEntity<Pais> obtener(@PathVariable Long id) {
        Pais pais = paisServicio.obtener(id);
        if (pais != null) {
            return ResponseEntity.ok(pais);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/paises/buscar?nombre=xxx
     * Busca países por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Pais>> buscar(@RequestParam String nombre) {
        List<Pais> paises = paisServicio.buscar(nombre);
        return ResponseEntity.ok(paises);
    }

    /**
     * GET /api/paises/buscarPorCodigo?codigo=xxx
     * Busca un país por código alfa2 o alfa3
     */
    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Pais> buscarPorCodigo(@RequestParam String codigo) {
        Pais pais = paisServicio.buscarPorCodigo(codigo);
        if (pais != null) {
            return ResponseEntity.ok(pais);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/paises/crear
     * Crea un nuevo país
     */
    @PostMapping("/crear")
    public ResponseEntity<Pais> crear(@RequestBody Pais pais) {
        try {
            Pais nuevoPais = paisServicio.agregar(pais);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPais);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /api/paises/actualizar/{id}
     * Actualiza un país existente
     */
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Pais> actualizar(@PathVariable Long id, @RequestBody Pais pais) {
        pais.setId(id);
        Pais paisActualizado = paisServicio.modificar(pais);
        if (paisActualizado != null) {
            return ResponseEntity.ok(paisActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/paises/eliminar/{id}
     * Elimina un país por su ID
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = paisServicio.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/paises/health
     * Endpoint de salud
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de Países funcionando correctamente");
    }
}
