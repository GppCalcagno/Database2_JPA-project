package it.polimi.db2.projectdb.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.services.OrderServices;

@WebServlet("/getRejectedOrder")
public class getRejectedOrder extends HttpServlet {
	
	@EJB(name = "it.polimi.db2.projectdb.services/OrderServices")
	private OrderServices orderService;
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		//GET DATA
		Order order=null;
		Integer ordID = null;
		
		try {
			ordID = Integer.parseInt(request.getParameter("ordID"));
		} catch (Exception e) {
			// for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cant retrive data from HTLM in get RejectedOrder ");
			return;
		}
		
		try {
			order = orderService.getFromID(ordID);
			request.getSession().setAttribute("order", order);
		} catch (Exception e) {
			// for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cant retrive data from HTLM in get RejectedOrder ");
			return;
		}
		
		String path = getServletContext().getContextPath() + "/GoToPay";
		response.sendRedirect(path);		
	}
}
