// package example.security.authentication.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.SecurityFilterChain;
//
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		// ...
// 		return http.build();
// 	}
//
// 	@Bean
// 	public UserDetailsService userDetailsService() {
// 		// Return a UserDetailsService that caches users
// 		// ...
// 	}
//
// 	@Autowired
// 	public void configure(AuthenticationManagerBuilder builder) {
// 		builder.eraseCredentials(false);
// 	}
//
// }