package com.androidsafe.mail;

import android.util.Log;

public class MailSender {
	public MailSender() {

	}

	public void sendGpsMail(final String mailContent, final String mailAddress) {

		new Thread(new Runnable() {

			public void run() {
				Log.i("my", "start to send mail 2");
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.qq.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName("ant_safe@qq.com");
				mailInfo.setPassword("zh2529323");// 您的邮箱密码
				mailInfo.setFromAddress("ant_safe@qq.com");
				mailInfo.setToAddress(mailAddress);
				mailInfo.setSubject("来自【蚂蚁防盗系统】");
				mailInfo.setContent(mailContent);
				// 这个类主要来发送邮件
				SimpleMailSender sms = new SimpleMailSender();
				sms.sendTextMail(mailInfo);// 发送文体格式

				// sms.sendHtmlMail(mailInfo);// 发送html格式

			}
		}).start();

	}

	public void sendContactsMail(final String mailContent, final String mailAddress) {

		new Thread(new Runnable() {

			public void run() {
				Log.i("my", "start to send mail 2");
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.qq.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName("ant_safe@qq.com");
				mailInfo.setPassword("zh2529323");// 您的邮箱密码
				mailInfo.setFromAddress("ant_safe@qq.com");
				mailInfo.setToAddress(mailAddress);
				mailInfo.setSubject("您手机获取到的位置信息【安卓防盗系统】");
				mailInfo.setContent(mailContent);
				// 这个类主要来发送邮件
				SimpleMailSender sms = new SimpleMailSender();
				sms.sendTextMail(mailInfo);// 发送文体格式

				// sms.sendHtmlMail(mailInfo);// 发送html格式

			}
		}).start();

	}

}
