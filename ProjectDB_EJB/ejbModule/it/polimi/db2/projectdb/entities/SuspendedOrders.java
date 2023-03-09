package it.polimi.db2.projectdb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SuspendedOrders", schema = "db_telco")
@NamedQuery(name="SuspendedOrders.findAll", query="SELECT s FROM SuspendedOrders s")
public class SuspendedOrders {
	private static final long serialVersionUID = 1L;
	
	@Id
	private int ID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
