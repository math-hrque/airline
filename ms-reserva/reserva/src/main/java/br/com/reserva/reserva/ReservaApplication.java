package br.com.reserva.reserva;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservaApplication implements CommandLineRunner {

	private final Flyway flywayCud;
	private final Flyway flywayR;

	public ReservaApplication(Flyway flywayCud, Flyway flywayR) {
		this.flywayCud = flywayCud;
		this.flywayR = flywayR;
	}

	public static void main(String[] args) {
		SpringApplication.run(ReservaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		flywayCud.migrate();
		flywayR.migrate();
	}
}
