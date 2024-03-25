package it.contrader.service;

import it.contrader.dto.BloodTestDTO;
import it.contrader.dto.RegistryDTO;

import it.contrader.dto.TestEmailDTO;
import it.contrader.dto.UrineTestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PdfService pdf;

    @Autowired
    private RegistryService registryUser;

    @Autowired
    private UserService userService;

    public static final String filePath = System.getProperty("user.dir") + "/src/main/java/pdf/";

    public void sendEmailTest(TestEmailDTO emailDTO, String subject) {
        sendDoctorEmail(emailDTO, subject);
        sendPatientEmail(emailDTO, subject);
    }
    public void sendPatientEmail(TestEmailDTO emailDTO, String subject) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String additionalInfo = getHealthcareInfo();


            helper.setFrom("healthcheckcontrader@gmail.com");
            helper.setTo(emailDTO.getEmailPatient());
            helper.setSubject(subject);

            helper.setText(
                    "Gentile " + emailDTO.getNamePatient() + " " + emailDTO.getSurnamePatient() + ",\n\n" +
                    "Le informiamo che l'inserimento delle sue analisi è avvenuto con successo.\n" +
                    "Il referto le verrà inviato dopo la validazione da parte del suo medico curante.\n" +
                    "Per qualsiasi domanda o ulteriore informazione, non esiti a contattarci.\n\n" +
                    "Grazie per aver scelto HealthCheck!\n\n" +
                    "Cordiali saluti,\n" +
                    "Il team di HealthCheck\n\n\n"
                    + additionalInfo);

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    public void sendDoctorEmail(TestEmailDTO emailDTO, String subject) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String additionalInfo = getHealthcareInfo();

            helper.setFrom("healthcheckcontrader@gmail.com");
            helper.setTo(emailDTO.getEmailDoctor());
            helper.setSubject(subject);


            helper.setText(
                    "Dott. " + emailDTO.getNameDoctor() + " " + emailDTO.getSurnameDoctor() + ",\n\n" +
                    "Il paziente " + emailDTO.getNamePatient() + " " + emailDTO.getSurnamePatient() + " ha inserito un nuovo referto da validare.\n\n\n"
                    + additionalInfo);

            File pdfFile = new File(filePath + emailDTO.getFileName());
            helper.addAttachment(emailDTO.getFileName(), pdfFile);

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    public void sendPatientEmailValidate(TestEmailDTO emailDTO, String subject) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String additionalInfo = getHealthcareInfo();

            helper.setFrom("healthcheckcontrader@gmail.com");
            helper.setTo(emailDTO.getEmailPatient());
            helper.setSubject(subject);

            helper.setText(
                    "Gentile " + emailDTO.getNamePatient() + " " + emailDTO.getSurnamePatient() + ",\n\n" +
                    "Le informiamo che il suo medico " + emailDTO.getSurnameDoctor() + " ha validato le sue analisi.\n" +
                    "In allegato troverà il pdf del suo referto.\n" +
                    "Per qualsiasi domanda o ulteriore informazione, non esiti a contattarci.\n\n" +
                    "Grazie per aver scelto HealthCheck!\n\n" +
                    "Cordiali saluti,\n" +
                    "Il team di HealthCheck\n\n\n" + additionalInfo);

            File pdfFile = new File(filePath + emailDTO.getFileName());
            helper.addAttachment(emailDTO.getFileName(), pdfFile);

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    private String getHealthcareInfo() {
        return  "Healthcare - Laboratorio analisi mediche\n" +
                "Sede: Andria\n" +
                "Indirizzo: Via Enrico Dandolo 51 - 76123 Andria (BT)\n" +
                "Telefono: 0883555602\n" +
                "Email: healthcheckcontrader@gmail.com\n\n";
    }
}
