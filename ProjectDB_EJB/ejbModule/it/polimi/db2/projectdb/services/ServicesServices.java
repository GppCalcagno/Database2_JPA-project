package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.*;
import it.polimi.db2.projectdb.exceptions.ServicePackageException;

@Stateless
public class ServicesServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	
	private EntityManager em;
	
	public ServicesServices() {}
	
	public List<FixedInternet> findAllFixInt() throws ServicePackageException{
		List<FixedInternet> fixIntList = null;
		
		try {
			fixIntList = em.createNamedQuery("FixedInternet.findAll", FixedInternet.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (fixIntList.isEmpty())
			return null;
		else 
			return fixIntList;
	}
	
	public List<FixedPhone> findAllFixPho() throws ServicePackageException{
		List<FixedPhone> fixPhoList = null;
		
		try {
			fixPhoList = em.createNamedQuery("FixedPhone.findAll", FixedPhone.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (fixPhoList.isEmpty())
			return null;
		else 
			return fixPhoList; 
	}
	
	public List<MobilePhone> findAllMobPho() throws ServicePackageException{
		List<MobilePhone> mobPhoList = null;
		
		try {
			mobPhoList = em.createNamedQuery("MobilePhone.findAll", MobilePhone.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (mobPhoList.isEmpty())
			return null;
		else 
			return mobPhoList; 
	}
	
	public List<MobileInternet> findAllMobInt() throws ServicePackageException{
		List<MobileInternet> mobIntList = null;
		
		try {
			mobIntList = em.createNamedQuery("MobileInternet.findAll", MobileInternet.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (mobIntList.isEmpty())
			return null;
		else 
			return mobIntList; 
	}
	
	public FixedPhone findFPByID(int fP_ID) {
		FixedPhone fixPhone = em.find(FixedPhone.class, fP_ID);
		return fixPhone;
	}
	
	public FixedInternet findFIByID(int fI_ID) {
		FixedInternet fixInternet = em.find(FixedInternet.class, fI_ID);
		return fixInternet;
	}
	
	public MobileInternet findMIByID(int mI_ID) {
		MobileInternet mobInternet = em.find(MobileInternet.class, mI_ID);
		return mobInternet;
	}
	
	public MobilePhone findMPByID(int mP_ID) {
		MobilePhone mobPhone = em.find(MobilePhone.class, mP_ID);
		return mobPhone;
	}
}
