package it.polimi.db2.projectdb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "User", schema = "db_telco")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class User {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String username;
	
	private String email;
	
	private String password;
	
	private String status;
	
	@OneToMany(mappedBy="user_ID", fetch = FetchType.LAZY)
	private List<ActivationSchedule> scheduleList;
	
	@OneToMany(mappedBy="user_ID", fetch = FetchType.LAZY)
	private List<Payment> paymentList;
	
	@OneToMany(mappedBy="user_ID", fetch = FetchType.EAGER)
	private List<Order> orderList;

	
	public User() {}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.status = "REGULAR";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		if(status)
			this.status="REGULAR";
		else
			this.status="INSOLVENT";
	}

	public List<ActivationSchedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<ActivationSchedule> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	
	public void addOrder(Order order) {
		orderList.add(order);
	}
	
	public Boolean isInsolvent() {
		if(status.equals("INSOLVENT"))
			return true;
		else
			return false;
	}
}
