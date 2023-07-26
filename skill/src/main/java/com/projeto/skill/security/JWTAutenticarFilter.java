package com.projeto.skill.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.skill.data.DetalheLoginData;
import com.projeto.skill.model.Login;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter{
	//Tempo de expiracao do Token, 600_000 e igual mais ou menos 10 minutos.
	public static final int TOKEN_EXPIRACAO = 600_000;
	
	public static final String TOKEN_SENHA = "390aa29c-45ba-4d8d-812c-b8104fd110ed";
	
	private final AuthenticationManager authenticationManager;

	public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
		try {
			Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
			
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					login.getLogin(),
					login.getSenha(),
					new ArrayList<>()
			));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Falha ao autenticar usuario", e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		DetalheLoginData loginData = (DetalheLoginData) authResult.getPrincipal();
		
		String token =JWT.create()
				.withSubject(loginData.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
				.sign(Algorithm.HMAC512(TOKEN_SENHA));
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}
	
	
}
