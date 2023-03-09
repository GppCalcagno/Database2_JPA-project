package it.polimi.db2.projectdb.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.entities.User;

@WebServlet("/GoToPay")
public class GoToPay extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	
	public GoToPay() {};
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException { doGet(request, response);}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "/WEB-INF/PaymentPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		User user;
		Boolean existUser = false;
		Order order;
		Boolean existsOPT=false;
		
		try {
			user = (User) request.getSession().getAttribute("user");
			order = (Order)request.getSession().getAttribute("order");
			if(user != null)	existUser = true;
			if(order.getOptProdList().size()!=0) existsOPT=true;
			
		}
		catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT Retrive Data in GoToPay");
			return;
		}
		ctx.setVariable("isLogged", existUser);
		ctx.setVariable("order", order);
		ctx.setVariable("esistsOPT", existsOPT);	
		
		
	
		templateEngine.process(path, ctx, response.getWriter());
	}
}
