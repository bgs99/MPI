package ru.itmo.hungergames.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.response.NewsResponse;

import java.util.List;

@Component
public class EmailSender {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNews(User to, List<NewsResponse> newsResponses) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to.getSettings().getEmail());
        message.setSubject("New news");

        StringBuilder stringBuilder = new StringBuilder("There are last news:\n");
        for (NewsResponse newsResponse : newsResponses) {
            stringBuilder.append(String.format("%s\n%s\n", newsResponse.getName(), newsResponse.getContent()));
            stringBuilder.append("----------\n");
        }

        message.setText(stringBuilder.toString());
        javaMailSender.send(message);
    }
}
