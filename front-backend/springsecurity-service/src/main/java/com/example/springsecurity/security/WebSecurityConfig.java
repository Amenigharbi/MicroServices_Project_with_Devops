package com.example.springsecurity.security;


import com.example.springsecurity.models.Admin;
import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.repository.AdminRepository;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.security.jwt.AuthEntryPointJwt;
import com.example.springsecurity.security.jwt.AuthTokenFilter;
import com.example.springsecurity.security.services.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

//@EnableAutoConfiguration
@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
	@Autowired
    UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;




	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	 private AdminRepository adminRepository;



	@PostConstruct
	public void init() {

		System.out.println("Checking if admin role exists...");
		if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
			boolean isAdminRoleNull = roleRepository.findByName(ERole.ROLE_ADMIN).isPresent();
			System.out.println("Is admin role null? " + isAdminRoleNull);
			Role adminRole = new Role(ERole.ROLE_ADMIN);
			roleRepository.save(adminRole);
			System.out.println("Admin role created.");
		}



		if (!roleRepository.findByName(ERole.ROLE_TOURIST).isPresent()){
			Role secretaryRole = new Role(ERole.ROLE_TOURIST);
			roleRepository.save(secretaryRole);

		}

		if (!roleRepository.findByName(ERole.ROLE_CHEF).isPresent()){
			Role chef = new Role(ERole.ROLE_CHEF);
			roleRepository.save(chef);

		}
//Ajout Admin par defaut si il n'existe pas
		/*if (!adminRepository.findByUsername("admin").isPresent()){
			System.out.println("checking first");
			Admin admin=new Admin();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
		//	admin.setImage("217020621_576022117112813_4527679340232884622_n10038580.jpg");
			Set<Role> roles = new HashSet<>();
			Role role=roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			admin.setRoles(roles);
			admin.setConfirm(true);
			adminRepository.save(admin);
		}*/




	}


	/*
		@PostConstruct
	public void init() {
		// Check and create roles if they do not exist
		createRoleIfNotFound(ERole.ROLE_ADMIN, roleRepository);
		createRoleIfNotFound(ERole.ROLE_TOURIST, roleRepository);
		createRoleIfNotFound(ERole.ROLE_CHEF, roleRepository);

		// Check and create the default admin user if it does not exist
		if (!adminRepository.findByUsername("admin").isPresent()) {
			System.out.println("Creating default admin user...");
			Admin admin = new Admin();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(new BCryptPasswordEncoder().encode("123456"));

			Set<Role> roles = new HashSet<>();
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(adminRole);

			admin.setRoles(roles);
			admin.setConfirm(true);

			adminRepository.save(admin);
			System.out.println("Default admin user created.");
		}
	}

	private void createRoleIfNotFound(ERole roleName, RoleRepository roleRepository) {
		if (!roleRepository.findByName(roleName).isPresent()) {
			Role role = new Role(roleName);
			roleRepository.save(role);
			System.out.println(roleName + " role created.");
		}
	}
	 */


	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

//	@Override
//	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}
	
	@Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
	
	@Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable()
//			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//			.authorizeRequests().antMatchers("/api/auth/**").permitAll()
//			.antMatchers("/api/test/**").permitAll()
//			.anyRequest().authenticated();
//
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	}
	
	@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable()

        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
			.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/categories/**").permitAll()
			.requestMatchers("/subcategories/**").permitAll()
			.requestMatchers("/products/**").permitAll()

			.requestMatchers("/SECURITY-SERVICE/**").permitAll()
			.requestMatchers("/customers/**").permitAll()
			.requestMatchers("/SECURITY-SERVICE/CATEGORY-SERVICE/categories/**").permitAll()
			.requestMatchers("/**").permitAll()
			.anyRequest().authenticated();
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
