package it.polimi.db2.projectdb.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.Order;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.services.OptionalProductServices;
import it.polimi.db2.projectdb.services.OrderServices;
import it.polimi.db2.projectdb.services.ValidityPeriodServices;

@WebServlet("/CreateOrder")
public class CreateOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.projectdb.services/OptionaProductServices")
	private OptionalProductServices optServices;
	@EJB(name = "it.polimi.db2.projectdb.services/ValidityPeriodServices")
	private ValidityPeriodServices VPservices;

	private TemplateEngine templateEngine;

	public CreateOrder() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		ServicePackage SP;

		List<OptionalProduct> OPTList = new ArrayList<>();
		String[] OLTstring;
		Boolean existOPT = false;

		User user;
		Boolean existUser = false;

		ValidityPeriod valPer;
		int valPerID;

		Date ActivationDate = null;

		// RETRIVE DATA FROM HTLM
		try {
			SP = (ServicePackage) request.getSession().getAttribute("choosenSP");
			OLTstring = request.getParameterValues("OPTs"); // null if empty
			user = (User) request.getSession().getAttribute("user");
			valPerID = Integer.parseInt(request.getParameter("ValidityPer"));

			if (user != null)
				existUser = true;
			if (OLTstring != null)
				existOPT = true;
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT TAKE DATA from HTLM in CreateOrder");
			return;
		}

		try {
			ActivationDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("ActivationDate"));
			if (!ActivationDate.after(new Date()))
				throw new Exception();

		} catch (Exception e) {
			// error if invalid data type or past dates
			String error = "ERROR: Inconsistent DATE selected";
			request.getSession().setAttribute("errorMsg", error);
			String path = getServletContext().getContextPath() + "/BuyPage";
			response.sendRedirect(path);
			return;
		}

		// GET OPT list
		if (existOPT) {// check on ID's correctness is useless: insertion are fixed
			for (int i = 0; i < OLTstring.length; i++) {
				try {
					OptionalProduct opt = optServices.getFromID(Integer.parseInt(OLTstring[i]));
					OPTList.add(opt);
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT TAKE OPT from HTLM in CreateOrder");
					return;
				}
			}
		}

		// GET validity Period (inserction if fixed)
		try {
			valPer = VPservices.getFromID(valPerID);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CANT TAKE ValPer from HTLM in CreateOrder");
			return;
		}

		Order order = null;

		// create order to be confirmed (could be without a user)
		if (existUser)
			order = new Order(user, SP, OPTList, valPer, ActivationDate);
		else
			order = new Order(SP, OPTList, valPer, ActivationDate);

		request.getSession().setAttribute("order", order);

		// to redirect user
		ctx.setVariable("isLogged", existUser);
		String path = getServletContext().getContextPath() + "/GoToPay";
		response.sendRedirect(path);
	}
}
