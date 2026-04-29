package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:no-reply@dakshinvihar.local}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(Booking booking) {
        if (booking == null || booking.getEmail() == null || booking.getEmail().isBlank()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(booking.getEmail());
        message.setSubject("Dakshin Vihar - Room Booking Confirmation");
        message.setText(buildBookingEmailBody(booking));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            // Keep booking flow successful even if SMTP is not configured or temporarily unavailable.
            System.err.println("Booking email could not be sent: " + ex.getMessage());
        }
    }

    private String buildBookingEmailBody(Booking booking) {
        return "Dear " + booking.getCustomerName() + ",\n\n"
            + "Thank you for booking with Dakshin Vihar.\n\n"
            + "Your booking details:\n"
            + "- Customer Name: " + booking.getCustomerName() + "\n"
            + "- Email: " + booking.getEmail() + "\n"
            + "- Room Type: " + booking.getRoomType() + "\n"
            + "- Check-in: " + booking.getCheckIn() + "\n"
            + "- Check-out: " + booking.getCheckOut() + "\n\n"
            + "We look forward to hosting you.\n\n"
            + "Regards,\n"
            + "Dakshin Vihar";
    }
}

