package com.androidsafe.mail;

import javax.mail.*;

public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		PasswordAuthentication mPasswordAuthentication = new PasswordAuthentication(
				userName, password);
		return mPasswordAuthentication;
	}
}
