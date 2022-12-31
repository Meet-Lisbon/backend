package pt.meetlisbon.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendingMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper=new MimeMessageHelper(message,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        javaMailSender.send(message);
    }
}


//@Component
//public class EmailSender {
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendEmail(String msgTo, String subject, String body) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(msgTo);
//        msg.setSubject(subject);
//        msg.setText(body);
//        javaMailSender.send(msg);
//
//    }
//}