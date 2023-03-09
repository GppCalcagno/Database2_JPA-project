package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
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

import it.polimi.db2.projectdb.entities.Alert;
import it.polimi.db2.projectdb.entities.Employee;
import it.polimi.db2.projectdb.entities.InsolventUsers;
import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.SalesReport1;
import it.polimi.db2.projectdb.entities.SalesReport2;
import it.polimi.db2.projectdb.entities.SalesReport3;
import it.polimi.db2.projectdb.entities.SuspendedOrders;
import it.polimi.db2.projectdb.exceptions.SalesReportException;
import it.polimi.db2.projectdb.services.SalesReportServices;

@WebServlet("/SalesReport")
public class GoToSalesReport  extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.projectdb.services/SalesReportServices")
	private SalesReportServices SRserv;
	
	public GoToSalesReport(){};
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {doGet(request, response);}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee employee;
		Boolean existEmp = false;
		try {
			employee = (Employee) request.getSession().getAttribute("employee");
			if(employee != null)
				existEmp = true;
		}
		catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Where is my gold?1");
			return;
		}
		
		List<SalesReport1> sr1List;
		List<SalesReport2> sr2List;
		List<SalesReport3> sr3List;
		List<InsolventUsers> insUsList;
		List<SuspendedOrders> suspOrList;
		List<Alert> alertsList;
		
		try {
			sr1List=SRserv.getSalesReport1();
			sr2List=SRserv.getSalesReport2();
			sr3List=SRserv.getSalesReport3();
			insUsList=SRserv.getInsolventUsers();
			suspOrList=SRserv.getSuspendedOrders();
			alertsList=SRserv.getAlerts();
		} catch (SalesReportException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		String path = "/WEB-INF/SalesReport.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("employee", employee);
		ctx.setVariable("existEmp", existEmp);
		
		ctx.setVariable("sr1List", sr1List);
		ctx.setVariable("Existsr1List", !sr1List.isEmpty());
		ctx.setVariable("sr2List", sr2List);
		ctx.setVariable("Existsr2List", !sr2List.isEmpty());
		ctx.setVariable("sr3List", sr3List);
		ctx.setVariable("Existsr3List", !sr3List.isEmpty());
		ctx.setVariable("insUsList", insUsList);
		ctx.setVariable("suspOrList", suspOrList);
		ctx.setVariable("alertsList", alertsList);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
}
