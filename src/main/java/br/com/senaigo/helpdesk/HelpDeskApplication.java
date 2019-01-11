package br.com.senaigo.helpdesk;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.enumeration.ProfileEnum;
import br.com.senaigo.helpdesk.api.repository.UserRepository;

@SpringBootApplication(scanBasePackages = { "br.com.senaigo.helpdesk" })
public class HelpDeskApplication extends SpringBootServletInitializer {
	
	private static final String E_MAIL = "suleimanmoraes@yahoo.com";
	private static final String PASSWORD = "123456";

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}
	
	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail(E_MAIL);
		admin.setPassword(passwordEncoder.encode(PASSWORD));
		admin.setProfileEnum(ProfileEnum.ROLE_ADMIN);
		
		User find = userRepository.findByEmail(E_MAIL);
		if(find == null) {
			userRepository.save(admin);
			System.out.println(String.format("Criando Usuário Admin, com E-mail/Login == \"%s\" e Senha == \"%s\".", E_MAIL, PASSWORD));
		}
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HelpDeskApplication.class);
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
}