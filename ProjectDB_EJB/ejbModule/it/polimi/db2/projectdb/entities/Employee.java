package it.polimi.db2.projectdb.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Employee", schema = "db_telco")
@NamedQuery(name = "Employee.checkCredentials", query = "SELECT e FROM Employee e  WHERE e.ID = ?1 and e.Password = ?2")
public class Employee {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	
	private String Password;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}	
	
}
