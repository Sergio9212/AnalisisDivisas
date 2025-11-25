package monedas.api.core.servicios;

import java.util.List;

import monedas.api.dominio.entidades.Pais;

public interface IPaisServicio {

    public List<Pais> listar();

    public Pais obtener(Long id);

    public List<Pais> buscar(String nombre);

    public Pais buscarPorCodigo(String codigo);

    public Pais agregar(Pais pais);

    public Pais modificar(Pais pais);

    public boolean eliminar(Long id);
}
