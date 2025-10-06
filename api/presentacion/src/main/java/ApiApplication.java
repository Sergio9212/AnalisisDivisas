package cambiomonedas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "controladores",
    "monedas.api.aplicacion",
    "monedas.api.core",
    "monedas.api.infraestructura"
})
@EnableJpaRepositories(basePackages = {
    "monedas.api.infraestructura.repositorios"
})
@EntityScan(basePackages = {
    "monedas.api.dominio.entidades"
})
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
