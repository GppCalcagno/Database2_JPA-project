package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.Employee;
import it.polimi.db2.projectdb.entities.User;
import it.polimi.db2.projectdb.exceptions.CredentialsException;

@Stateless
public class EmployeeServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public EmployeeServices() {
		
	}
	
	public Employee checkCredentials(int usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<Employee> eList = null;
		try {
			eList = em.createNamedQuery("Employee.checkCredentials", Employee.class).setParameter(1, usrn).setParameter(2, pwd).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (eList.isEmpty()) return null;
		else if (eList.size() == 1)	return eList.get(0);
		throw new NonUniqueResultException("More than one employee registered with same credentials");
	}
}
