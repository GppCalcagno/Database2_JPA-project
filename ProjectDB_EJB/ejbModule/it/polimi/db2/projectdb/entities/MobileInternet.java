package it.polimi.db2.projectdb.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "MobileInternet", schema = "db_telco")
@NamedQuery(name="MobileInternet.findAll", query="SELECT mi FROM MobileInternet mi")
public class MobileInternet {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private int NumGiga;
	
	private int FeeGiga;
	
	@OneToMany(mappedBy="MI_ID", fetch=FetchType.EAGER)
	private List<ServicePackage> servPackList;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNumGiga() {
		return NumGiga;
	}

	public void setNumGiga(int numGiga) {
		NumGiga = numGiga;
	}

	public int getFeeGiga() {
		return FeeGiga;
	}

	public void setFeeGiga(int feeGiga) {
		FeeGiga = feeGiga;
	}

	public List<ServicePackage> getServPackList() {
		return servPackList;
	}

	public void setServPackList(List<ServicePackage> servPackList) {
		this.servPackList = servPackList;
	}
	
	public void addServPack(ServicePackage servPack) {
		getServPackList().add(servPack);
	}
	
	public void removeServPack(ServicePackage servPack) {
		getServPackList().remove(servPack);
	}
}
