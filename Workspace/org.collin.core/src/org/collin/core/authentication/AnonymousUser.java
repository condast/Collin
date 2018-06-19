package org.collin.core.authentication;

import java.util.Calendar;
import java.util.Date;

import org.condast.commons.ui.date.DateUtils;

public class AnonymousUser implements ILoginUser {

	public static final String S_ANONYMOUS ="Anonymous";
	public static final String S_EMAIL ="info@waterdiertjes.nl";
	
	private long id;
	
	private String userName;
	
	private String password;
	
	private String email;

	private Date createDate;
	
	private Date updateDate;

	/**
	 * token for communication
	 */
	private transient int lsbtoken;

	public AnonymousUser() {
		this.id =0;
		this.userName = S_ANONYMOUS;
		this.password = S_ANONYMOUS;
		this.email = S_EMAIL;
		
		Date now = Calendar.getInstance().getTime();
		this.lsbtoken = now.hashCode();
		this.createDate = now;
		this.updateDate = now;
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
		token = token<<31 + this.lsbtoken;
		return token;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
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
	public boolean isAdmin(String userName, long token) {
		return false;
	}
}