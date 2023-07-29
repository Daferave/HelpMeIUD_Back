package co.edo.iudigital.helpmeiud.service.impl;

import co.edo.iudigital.helpmeiud.service.iface.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements IEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender sender;

    @Override
    public boolean sendEmail(String mensaje, String email, String asunto) {
        LOGGER.info("Mensaje: {}", mensaje);
        return sendEmailTool(mensaje, email, asunto);
    }

    private boolean sendEmailTool(String mensaje, String email,String asunto) {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("noreply@iudigital.edu.co");
            helper.setTo(email);
            helper.setText(mensaje, true);
            helper.setSubject(asunto);
            sender.send(message);
            send = true;
            LOGGER.info("Email enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        }
        return send;
    }
}
