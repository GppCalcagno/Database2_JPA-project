package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.services.OptionalProductServices;
import it.polimi.db2.projectdb.services.ServicePakcageServices;

@WebServlet("/CreateOptionalProduct")
public class CreateOptionalProduct extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/OptionalProductServices")
	private OptionalProductServices oService;
	@EJB(name = "it.polimi.db2.projectdb.services/ServicePackageServices")
	private ServicePakcageServices sService;
	
	public CreateOptionalProduct() {
		super();
	}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("employee") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		
		String name_prod = null;
		Float monFee_int = null;
		BigDecimal monFee = null;
		List<Integer> servPacksList = new ArrayList<>();
		String[] servPacks_str = null;
		try {
			name_prod = StringEscapeUtils.escapeJava(request.getParameter("name_prod"));
			monFee_int = Float.parseFloat(request.getParameter("monFee"));
			servPacks_str = request.getParameterValues("servPacks");
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		if(servPacks_str != null) {
			for(String s : servPacks_str) {
				try{
					servPacksList.add(Integer.parseInt(s));
				}catch(Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "2CANT TAKE DATA from HTLM in CreateServicePackage");
					return;
				}
			}
		}
		try {
			monFee = BigDecimal.valueOf(monFee_int);
			oService.createOptionalProduct(name_prod, monFee, servPacksList);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create optprod");
			return;
		}
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/HomeEmployee";
		response.sendRedirect(path);
	}
	
	public void destroy() {}
}
