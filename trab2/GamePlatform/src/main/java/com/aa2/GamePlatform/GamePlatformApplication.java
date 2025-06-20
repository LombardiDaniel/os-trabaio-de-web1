package com.aa2.GamePlatform;

import com.aa2.GamePlatform.models.Admin;
import com.aa2.GamePlatform.repositories.AdminRepository; // IMPORT CORRIGIDO: AGORA É 'repositories'
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GamePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamePlatformApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			if (adminRepository.findByEmail("admin@teste.com").isEmpty()) {
				Admin admin = new Admin();
				admin.setNome("Admin Inicial");
				admin.setEmail("admin@teste.com");
				admin.setSenha(passwordEncoder.encode("senha123"));
				adminRepository.save(admin);
				System.out.println("Admin inicial criado: admin@teste.com / senha123");
			}
		};
	}
}