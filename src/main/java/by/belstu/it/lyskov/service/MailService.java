package by.belstu.it.lyskov.service;

public interface MailService {

    void sendSimpleMessage(String to, String subject, String text);
}
