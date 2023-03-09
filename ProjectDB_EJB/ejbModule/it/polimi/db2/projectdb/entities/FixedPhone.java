package it.polimi.db2.projectdb.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "FixedPhone", schema = "db_telco")
@NamedQuery(name="FixedPhone.findAll", query="SELECT fp FROM FixedPhone fp")
public class FixedPhone{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	@OneToMany(mappedBy="FP_ID", fetch=FetchType.EAGER)
	private List<ServicePackage> servPackList;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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
