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
				mailInfo.setPassword("zh2529323");// ������������
				mailInfo.setFromAddress("ant_safe@qq.com");
				mailInfo.setToAddress(mailAddress);
				mailInfo.setSubject("���ԡ����Ϸ���ϵͳ��");
				mailInfo.setContent(mailContent);
				// �������Ҫ�������ʼ�
				SimpleMailSender sms = new SimpleMailSender();
				sms.sendTextMail(mailInfo);// ���������ʽ

				// sms.sendHtmlMail(mailInfo);// ����html��ʽ

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
				mailInfo.setPassword("zh2529323");// ������������
				mailInfo.setFromAddress("ant_safe@qq.com");
				mailInfo.setToAddress(mailAddress);
				mailInfo.setSubject("���ֻ���ȡ����λ����Ϣ����׿����ϵͳ��");
				mailInfo.setContent(mailContent);
				// �������Ҫ�������ʼ�
				SimpleMailSender sms = new SimpleMailSender();
				sms.sendTextMail(mailInfo);// ���������ʽ

				// sms.sendHtmlMail(mailInfo);// ����html��ʽ

			}
		}).start();

	}

}
