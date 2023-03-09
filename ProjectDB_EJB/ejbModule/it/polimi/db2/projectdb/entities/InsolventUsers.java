package it.polimi.db2.projectdb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "InsolventUsers", schema = "db_telco")
@NamedQuery(name = "InsolventUsers.findAll", query = "SELECT i FROM InsolventUsers i")
public class InsolventUsers {
private static final long serialVersionUID = 1L;
	
	@Id
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
