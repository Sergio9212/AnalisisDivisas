package monedas.api.infraestructura.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import monedas.api.dominio.entidades.Pais;

@Repository
public interface IPaisRepositorio extends JpaRepository<Pais, Long> {

    @Query("SELECT p FROM Pais p WHERE p.nombre LIKE '%' || ?1 || '%'")
    List<Pais> buscar(String nombre);

    @Query("SELECT p FROM Pais p WHERE p.codigoAlfa2 = ?1 OR p.codigoAlfa3 = ?1")
    Pais buscarPorCodigo(String codigo);
}
