package monedas.api.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import monedas.api.dominio.entidades.Pais;
import monedas.api.core.servicios.IPaisServicio;
import monedas.api.infraestructura.repositorios.IPaisRepositorio;

@Service
public class PaisServicio implements IPaisServicio {

    @Autowired
    private IPaisRepositorio repositorio;

    @Override
    public List<Pais> listar() {
        return repositorio.findAll();
    }

    @Override
    @SuppressWarnings("null")
    public Pais obtener(Long id) {
        var pais = repositorio.findById(id);
        return pais.isEmpty() ? null : pais.get();
    }

    @Override
    public List<Pais> buscar(String nombre) {
        return repositorio.buscar(nombre);
    }

    @Override
    public Pais buscarPorCodigo(String codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    @Override
    public Pais agregar(Pais pais) {
        pais.setId(0);
        return repositorio.save(pais);
    }

    @Override
    @SuppressWarnings("null")
    public Pais modificar(Pais pais) {
        Optional<Pais> paisEncontrado = repositorio.findById(pais.getId());
        if (!paisEncontrado.isEmpty()) {
            return repositorio.save(pais);
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
