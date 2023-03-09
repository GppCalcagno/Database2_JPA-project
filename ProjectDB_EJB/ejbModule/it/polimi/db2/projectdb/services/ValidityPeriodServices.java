package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.exceptions.ServicePackageException;
import it.polimi.db2.projectdb.exceptions.ValidityPeriodException;

@Stateless
public class ValidityPeriodServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	private EntityManager em;
	
	public ValidityPeriodServices() {}

	public List<ValidityPeriod> findAllValPeriod() throws ServicePackageException {
		List<ValidityPeriod> valPeriodList = null;

		try {
			valPeriodList = em.createNamedQuery("ValidityPeriod.findAll", ValidityPeriod.class).getResultList();
		} catch (PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		
		if (valPeriodList.isEmpty())
			return null;
		else
			return valPeriodList;
	}

	public ValidityPeriod getFromID(int ID) throws ValidityPeriodException {
		try {
			return em.find(ValidityPeriod.class, ID);
		} catch (PersistenceException e) {
			throw new ValidityPeriodException("Can't Access DB: ValidityPerdio.getFromID");
		}
	}
}
