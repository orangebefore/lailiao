package com.quark.mail;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.quark.common.config;

public class SendMail {

	public static final String HOST = config.email_smtp;
	public static final String PROTOCOL = "smtp";
	public static final int PORT = 25;
	public static final String FROM = config.email_username;
	public static final String PWD = config.email_password;

	private static Session getSession() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.store.protocol", "");
		properties.put("mail.smtp.auth", true);
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM, PWD);
			}
		};
		Session session = Session.getDefaultInstance(properties, authenticator);

		return session;
	}

	public static void SendHtmlEmail(String subject, String sendHtml, String to,
			File attachment) {
		Session session = getSession();
		Transport transport = null;
		try {
			Message message = new MimeMessage(session);
			// 发件人
			InternetAddress from = new InternetAddress(FROM);
			message.setFrom(from);

			// 收件人
			InternetAddress toEmail = new InternetAddress(to);
			message.setRecipient(Message.RecipientType.TO, toEmail);

			// 邮件主题
			message.setSubject(subject);

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 添加邮件正文
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);

			// 添加附件的内容
			if (attachment != null) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				attachmentBodyPart.setDataHandler(new DataHandler(source));

				// 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
				// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
				// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
				// messageBodyPart.setFileName("=?GBK?B?" +
				// enc.encode(attachment.getName().getBytes()) + "?=");

				// MimeUtility.encodeWord可以避免文件名乱码
				attachmentBodyPart.setFileName(MimeUtility
						.encodeWord(attachment.getName()));
				multipart.addBodyPart(attachmentBodyPart);
			}

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();

			transport = session.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(HOST, FROM, PWD);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void sendText(String to, String subject, String content) {
		Session session = getSession();
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			InternetAddress[] addresses = { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, addresses);
			message.setSubject(subject);
			message.setSentDate(new Date());
			// 设置邮件显示格式
			message.setContent(content, "text/html;charset=utf-8");
			// send
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void send(String title,String to, String content) {
		Session session = getSession();
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			InternetAddress[] addresses = { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, addresses);
			message.setSubject(title);
			message.setSentDate(new Date());
			// 设置邮件显示格式
			message.setContent("您的悠译验证码为："+content, "text/html;charset=utf-8");
			// send
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		
	}
}
