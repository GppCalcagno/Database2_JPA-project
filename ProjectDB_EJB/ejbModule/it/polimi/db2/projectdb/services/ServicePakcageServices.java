package it.polimi.db2.projectdb.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.projectdb.entities.FixedInternet;
import it.polimi.db2.projectdb.entities.FixedPhone;
import it.polimi.db2.projectdb.entities.MobileInternet;
import it.polimi.db2.projectdb.entities.MobilePhone;
import it.polimi.db2.projectdb.entities.OptionalProduct;
import it.polimi.db2.projectdb.entities.ServicePackage;
import it.polimi.db2.projectdb.entities.ValidityPeriod;
import it.polimi.db2.projectdb.exceptions.ServicePackageException;

@Stateless
public class ServicePakcageServices {
	@PersistenceContext(unitName = "ProjectDB_EJB")
	
	private EntityManager em;
	
	public ServicePakcageServices() {}
	
	public List<ServicePackage> findAllServPackages() throws ServicePackageException{
		List<ServicePackage> servPackList = null;
		
		try {
			servPackList = em.createNamedQuery("ServicePackage.findAll", ServicePackage.class).getResultList();
		}
		catch(PersistenceException e) {
			throw new ServicePackageException("Service Packages not found.");
		}
		if (servPackList.isEmpty())
			return null;
		else 
			return servPackList; 
	}
	
	public ServicePackage findServPackByID(int servPackID) {
		ServicePackage servPack = em.find(ServicePackage.class, servPackID);
		return servPack;
	}
	
	public void createServPack (String name, int fP_ID, int mP_ID, int fI_ID, int mI_ID, 
			List<Integer> optProdList_ID, List<Integer> valPeriodList_ID) {
		ServicePackage servPack = new ServicePackage(name);
		
		FixedPhone fP = em.find(FixedPhone.class, fP_ID);
		if(fP!=null)
			servPack.setFP_ID(fP);
		
		MobilePhone mP = em.find(MobilePhone.class, mP_ID);
		if(mP!=null)
			servPack.setMP_ID(mP);
		
		FixedInternet fI = em.find(FixedInternet.class, fI_ID);
		if(fI!=null)
			servPack.setFI_ID(fI);
		
		MobileInternet mI = em.find(MobileInternet.class, mI_ID);
		if(mI!=null)
			servPack.setMI_ID(mI);
		
		for(int optProd_ID : optProdList_ID) {
			OptionalProduct optProd = em.find(OptionalProduct.class, optProd_ID);
			if(optProd != null)
				servPack.addOptProd(optProd);
		}
		
		for(int valPer_ID : valPeriodList_ID) {
			ValidityPeriod valPer = em.find(ValidityPeriod.class, valPer_ID);
			if(valPer != null)
				servPack.addValPeriod(valPer);
		}
		
		// for debugging: let's check if mission is managed
		System.out.println("Method createServPack before em.persist(servPack)");
		System.out.println("Is servPack object managed?  " + em.contains(servPack));
		
		em.persist(servPack);
		
		System.out.println("Method createServPack AFTER em.persist(servPack)");
		System.out.println("Is servPack object managed?  " + em.contains(servPack));
		
		em.flush();
	}
	
	public void addOptProd(ServicePackage servPack, OptionalProduct optProd) {
		servPack.addOptProd(optProd);
		
		System.out.println("Method addOptProd before em.persist(servPack)");
		System.out.println("Is servPack object managed?  " + em.contains(servPack));
		
		em.persist(servPack);
		
		System.out.println("Method addOptProd AFTER em.persist(servPack)");
		System.out.println("Is servPack object managed?  " + em.contains(servPack));
		
		em.flush();
	}
}
