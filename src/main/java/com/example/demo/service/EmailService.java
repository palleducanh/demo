package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
@PropertySource("classpath:application.properties")
public class EmailService {

	private Log log = LogFactory.getLog(EmailService.class);

	public boolean sendEmailToAll(List<User> recipientList, String emailBodyText) {
		if (recipientList != null) {
			try {
				for (User user : recipientList) {
					sendEmail(user, emailBodyText);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	private void sendEmail(User recipient, String emailBodyText) {
		try {
			Mail mail = prepareMail(recipient, emailBodyText);

			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));

			Response response = sg.api(request);

		} catch (IOException e) {
			log.error("error sending email to " + recipient.getFirstname() + " " + recipient.getLastname());
			e.printStackTrace();
		}
		log.info("mail has sent to " + recipient.getFirstname() + " " + recipient.getLastname());
	}

	private Mail prepareMail(User recipient, String emailBodyText) {
		// customized email content with user first name, last name
		final String signature = "Best Regards, \n My Bank Application.";
		StringBuilder finalText = new StringBuilder();
		String subject = "test sendGrid";
		finalText.append("Dear " + recipient.getFirstname() + " " + recipient.getLastname() + "," + "\n");
		finalText.append(emailBodyText + "\n");
		finalText.append(signature);

		Email fromEmail = new Email("moneydontsleep8888@gmail.com");
		Email toEmail = new Email(recipient.getUsername());
		Content content = new Content("text/plain", finalText.toString());
		Mail mail = new Mail(fromEmail, subject, toEmail, content);
		return mail;
	}

}
