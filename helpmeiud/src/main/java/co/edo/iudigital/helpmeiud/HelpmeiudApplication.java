package co.edo.iudigital.helpmeiud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HelpmeiudApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HelpmeiudApplication.class, args);
	}

	// para ejemplo, generamos contraseñas cifradas
	@Override
	public void run(String ...args) throws Exception{
		String password = "123456";
		for(int i = 0; i < 2; i++) {
			String encoded = passwordEncoder.encode(password);
			System.out.println("CONTRASEÑA: " + encoded);
		}
	}

}
