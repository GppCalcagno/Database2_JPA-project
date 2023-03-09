package it.polimi.db2.projectdb.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.projectdb.entities.Employee;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.exceptions.CredentialsException;
import it.polimi.db2.projectdb.services.EmployeeServices;
import it.polimi.db2.projectdb.services.UserServices;

@WebServlet("/CheckLoginEmployee")
public class CheckLoginEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/EmployeeServices")
	private EmployeeServices empService;
	
	public CheckLoginEmployee() {
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
		// obtain and escape params
		Integer usrn = null;
		String pwd = null;
		try {
			usrn = Integer.parseInt(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (NumberFormatException e) {
			usrn=-1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Employee employee;
		try {
			// query db to authenticate for user
			employee = empService.checkCredentials(usrn, pwd);
		} catch (CredentialsException | NonUniqueResultException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (employee == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			request.getSession().setAttribute("employee", employee);
			path = getServletContext().getContextPath() + "/HomeEmployee";
			response.sendRedirect(path);
		}
	}
	
	public void destroy() {}
}
