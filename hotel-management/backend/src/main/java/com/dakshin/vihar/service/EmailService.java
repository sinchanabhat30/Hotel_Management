package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Booking;
import com.dakshin.vihar.model.Contact;
import com.dakshin.vihar.model.PartyHallBooking;
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

    public void sendPartyHallBookingConfirmation(PartyHallBooking booking) {
        if (booking == null || booking.getEmail() == null || booking.getEmail().isBlank()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(booking.getEmail());
        message.setSubject("Party Hall Booking Confirmation - Dakshin Vihar");
        message.setText(buildPartyHallBookingEmailBody(booking));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            // Keep booking flow successful even if SMTP is not configured or temporarily unavailable.
            System.err.println("Party hall booking email could not be sent: " + ex.getMessage());
        }
    }

    private String buildPartyHallBookingEmailBody(PartyHallBooking booking) {
        return "Dear " + booking.getFullName() + ",\n\n"
            + "Your Party Hall booking has been successfully confirmed.\n\n"
            + "Details:\n\n"
            + "* Event Type: " + booking.getEventType() + "\n"
            + "* Date: " + booking.getEventDate() + "\n"
            + "* Time Slot: " + booking.getTimeSlot() + "\n"
            + "* Guests: " + booking.getNumberOfGuests() + "\n\n"
            + "We look forward to hosting your event at Dakshin Vihar!\n\n"
            + "Regards,\n"
            + "Dakshin Vihar Team";
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

    public void sendAdminBookingNotification(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("dakshinvihar123@gmail.com");
        message.setSubject("New Booking Received - Dakshin Vihar");
        message.setText(buildAdminBookingEmailBody(booking));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            // Keep booking flow successful even if admin email fails.
            System.err.println("Admin booking notification email could not be sent: " + ex.getMessage());
        }
    }

    public void sendAdminPartyHallBookingNotification(PartyHallBooking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("dakshinvihar123@gmail.com");
        message.setSubject("New Booking Received - Dakshin Vihar");
        message.setText(buildAdminPartyHallBookingEmailBody(booking));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            // Keep booking flow successful even if admin email fails.
            System.err.println("Admin party hall booking notification email could not be sent: " + ex.getMessage());
        }
    }

    public void sendAdminContactNotification(Contact contact) {
        if (contact == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("dakshinvihar123@gmail.com");
        message.setReplyTo(contact.getEmail());
        message.setSubject("New Contact Form Message");
        message.setText(buildAdminContactEmailBody(contact));

        mailSender.send(message);
    }

    private String buildAdminContactEmailBody(Contact contact) {
        return "You have received a new message from your website:\n\n"
            + "Name: " + contact.getName() + "\n"
            + "Email: " + contact.getEmail() + "\n"
            + "Message: " + contact.getMessage() + "\n";
    }

    private String buildAdminBookingEmailBody(Booking booking) {
        return "A new booking has been made.\n\n"
            + "Customer Details:\n\n"
            + "* Name: " + booking.getCustomerName() + "\n"
            + "* Email: " + booking.getEmail() + "\n"
            + "* Phone: N/A\n\n"
            + "Booking Type:\n\n"
            + "* Room\n\n"
            + "* Room Type: " + booking.getRoomType() + "\n"
            + "* Number of Rooms: 1\n"
            + "* Check-in Date: " + booking.getCheckIn() + "\n"
            + "* Check-out Date: " + booking.getCheckOut() + "\n\n"
            + "Please review this booking in the system.";
    }

    private String buildAdminPartyHallBookingEmailBody(PartyHallBooking booking) {
        return "A new booking has been made.\n\n"
            + "Customer Details:\n\n"
            + "* Name: " + booking.getFullName() + "\n"
            + "* Email: " + booking.getEmail() + "\n"
            + "* Phone: " + booking.getPhoneNumber() + "\n\n"
            + "Booking Type:\n\n"
            + "* Party Hall\n\n"
            + "* Event Type: " + booking.getEventType() + "\n"
            + "* Event Date: " + booking.getEventDate() + "\n"
            + "* Time Slot: " + booking.getTimeSlot() + "\n"
            + "* Number of Guests: " + booking.getNumberOfGuests() + "\n\n"
            + "Please review this booking in the system.";
    }
}

