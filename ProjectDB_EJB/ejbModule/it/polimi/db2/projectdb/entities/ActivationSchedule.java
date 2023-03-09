package it.polimi.db2.projectdb.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "ActivationSchedule", schema = "db_telco")
public class ActivationSchedule {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;

	@Temporal(TemporalType.DATE)
	private Date ActivationDate;
	
	@Temporal(TemporalType.DATE)
	private Date DeactivationDate;
		
	@ManyToOne
	@JoinColumn(name="user_ID")
	private User user_ID;
	
	
	@OneToOne @JoinColumn(name="Order_ID")
	private Order Order_ID;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Date getActivationDate() {
		return ActivationDate;
	}

	public void setActivationDate(Date activationDate) {
		ActivationDate = activationDate;
	}

	public Date getDeactivationDate() {
		return DeactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		DeactivationDate = deactivationDate;
	}



	public User getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(User user_ID) {
		this.user_ID = user_ID;
	}

	public Order getOrder_ID() {
		return Order_ID;
	}

	public void setOrder_ID(Order order_ID) {
		Order_ID = order_ID;
	}
	
	
}
