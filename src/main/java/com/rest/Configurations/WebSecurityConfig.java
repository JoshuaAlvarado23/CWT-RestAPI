package com.rest.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final static String[] PUBLIC_LIST = {"/customer/all","/customer/id/{id}"};
	private final static String USERNAME = "admin";
	private final static String PW = "1234";
	private enum ROLES {
		ADMIN, USER
	}
	
	@Autowired
	public void adminConfig(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser(USERNAME).password(encoder().encode(PW)).roles(ROLES.ADMIN.name());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.httpBasic().authenticationEntryPoint(new BasicAuthEntry())
		.and()
		.authorizeHttpRequests()
		.antMatchers("/customer/secured/**").hasRole("ADMIN")
		.antMatchers(PUBLIC_LIST).permitAll()
		.anyRequest().authenticated();
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	

	public PasswordEncoder encoder() {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		DelegatingPasswordEncoder dp = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		dp.setDefaultPasswordEncoderForMatches(bc);
		return dp;
	}
	

}
