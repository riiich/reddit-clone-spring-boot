package com.example.springredditclone.service;

import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender emailSender;
    private final MailContentBuilder mailContentBuilder;

    public void sendEmail(NotificationEmail notificationEmail) throws SpringRedditException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springbootreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try {
            emailSender.send(messagePreparator);
            log.info("Activation link is sent to your email!");
        } catch(MailException e) {
            throw new SpringRedditException("There was an error sending the activation link to your email, " + notificationEmail.getRecipient() + " ;(");
        }
    }
}
