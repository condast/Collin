package org.collin.authentication.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.condast.commons.authentication.user.ILoginUser;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.ui.date.DateUtils;

@Entity
public class Login implements ILoginUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Basic(optional = false)
	@Column( nullable=false)
	private String userName;
	
	@Basic(optional = true)
	@Column( nullable=false)
	private String password;
	
	@Basic(optional = true)
	@Column( nullable=true)
	private String email;

	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Basic(optional = true)
	@OneToOne
	private Person person; 
	
	/**
	 * token for communication
	 */
	private transient int lsbtoken;

	public Login() {
		Date now = Calendar.getInstance().getTime();
		this.lsbtoken = now.hashCode();
	}

	@Override
	public long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date create) {
		this.createDate = create;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date update) {
		this.updateDate = update;
	}
	
	@Override
	public long getToken() {
		long token = toString().hashCode();
		token<<=31;
		token += this.lsbtoken;
		return token;
	}

	/**
	 * returns true if the user has administrative privileges
	 * @param userName
	 * @param token
	 * @return
	 */
	public boolean isAdmin( String userName, long token ) {
		return ( ILoginUser.S_ADMIN.equals(userName)) && ILoginUser.AdminToken == token;
	}

	@Override
	public String toString() {
		String date = DateUtils.getFormatted(this.createDate );
		return this.userName + "{" + date + "}";
	}

	@Override
	public int compareTo(ILoginUser arg0) {
		long diff = id - arg0.getId();
		return ( diff < 0)?-1: (diff>0)?1:0;
	}

	@Override
	public boolean isCorrect(long userId, String token) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setToken(long token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LatLng getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(double latitude, double longitude) {
		// TODO Auto-generated method stub
		
	}
}
