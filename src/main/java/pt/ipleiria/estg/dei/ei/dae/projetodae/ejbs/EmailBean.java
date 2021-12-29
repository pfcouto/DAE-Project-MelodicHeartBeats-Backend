package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB" )
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP" )
    private Session mailSession;
    private static final Logger logger = Logger.getLogger("EmailBean.logger" );
    @PersistenceContext
    EntityManager em;

    public void sendPrescription(String email, int prescriptionId) {
        Prescription prescription = em.find(Prescription.class, prescriptionId);
        String path = "C:\\prescription_" + prescription.getId() + ".pdf";

        Document doc = new Document();
        try {
            //generate a PDF at the specified location
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path));
            System.out.println("PDF created." );
            //opens the PDF
            doc.open();
            //adds paragraph to the PDF file
            doc.add(new Paragraph("ID #" + prescription.getId()));
            doc.add(new Paragraph("Doctor: " + prescription.getDoctor().getName()));
            doc.add(new Paragraph("Patient: " + prescription.getPatient().getName()));
            doc.add(new Paragraph("Since: " + prescription.getStartDate()));
            doc.add(new Paragraph("To: " + prescription.getEndDate()));
            doc.add(new Paragraph(" " ));
            doc.add(new Paragraph("Description:" ));
            doc.add(new Paragraph(prescription.getDescription()));
            //close the PDF file
            doc.close();
            //closes the writer
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            try {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email, false));
                message.setSubject("Prescription #" + prescriptionId);
                message.setText(prescription.getDescription());

                MimeBodyPart mbp = new MimeBodyPart();

                DataSource fds = new FileDataSource(path);
                mbp.setDisposition(Part.ATTACHMENT);
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setFileName(fds.getName());
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp);
                message.setContent(mp);


                message.setSentDate(timestamp);
                Transport.send(message);
                System.out.println("Email sent." );
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }
}
