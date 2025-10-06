package controladores;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import monedas.api.core.servicios.IMonedaServicio;
import monedas.api.dominio.entidades.Moneda;

@RestController
@RequestMapping("/api/monedas")
public class Monedacontrolador {

    @Autowired
    private IMonedaServicio monedaServicio;

    @GetMapping("/listar")
    public List<Moneda> listar() {
        return monedaServicio.listar();
    }
}

 