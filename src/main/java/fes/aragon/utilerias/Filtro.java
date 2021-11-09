package fes.aragon.utilerias;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fes.aragon.modelo.Usuario;

public class Filtro implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		System.out.println("Peticion de: " + req.getRequestURL());
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession sesion = req.getSession();
		Usuario registro = (Usuario) sesion.getAttribute("usuario");
		if (registro == null) {
			res.sendRedirect("/Exam1Drive/");
		} else {
			chain.doFilter(request, response);
		}

		
	}

}
