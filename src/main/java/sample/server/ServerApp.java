package sample.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class ServerApp {
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApp.class, args);
	}
}