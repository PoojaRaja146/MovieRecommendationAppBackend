package sample.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ServerApp {
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApp.class, args);
	}
}