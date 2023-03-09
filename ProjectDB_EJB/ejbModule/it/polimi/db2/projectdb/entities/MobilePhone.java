package it.polimi.db2.projectdb.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "MobilePhone", schema = "db_telco")
@NamedQuery(name="MobilePhone.findAll", query="SELECT mp FROM MobilePhone mp")
public class MobilePhone {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private int NumMinutes;
	
	private int NumSMS;
	
	private int FeeMinutes;
	
	private int FeeSMS;
	
	@OneToMany(mappedBy="MP_ID", fetch=FetchType.EAGER)
	private List<ServicePackage> servPackList;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNumMinutes() {
		return NumMinutes;
	}

	public void setNumMinutes(int numMinutes) {
		NumMinutes = numMinutes;
	}

	public int getNumSMS() {
		return NumSMS;
	}

	public void setNumSMS(int numSMS) {
		NumSMS = numSMS;
	}

	public int getFeeMinutes() {
		return FeeMinutes;
	}

	public void setFeeMinutes(int feeMinutes) {
		FeeMinutes = feeMinutes;
	}

	public int getFeeSMS() {
		return FeeSMS;
	}

	public void setFeeSMS(int feeSMS) {
		FeeSMS = feeSMS;
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
