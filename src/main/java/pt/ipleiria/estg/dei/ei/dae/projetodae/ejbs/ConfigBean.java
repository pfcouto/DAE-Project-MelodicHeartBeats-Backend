package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
@Startup
public class ConfigBean {
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");
    @EJB
    private DoctorBean doctorBean;
    @EJB
    private PatientBean patientBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private PrescriptionBean prescriptionBean;

    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Hello Java EE!");

            doctorBean.create("dJoao","1234","Joao","1997-06-23","djoao@mail.pt","919000222","Of.1");

            patientBean.create("pLeandro","1234","Leandro","1997-06-23","pleandro@mail.pt","919000111");

            prescriptionBean.create("dJoao","pLeandro","TEXTO DA DESCRICAO","2020-06-23","2020-07-23");

            administratorBean.create("admin", "1234", "Administrator", "1987-02-19" ,"admin@mail.com", "963123123");

//            doctorBean.create("Lacerda", "1234", "Jorge Lacerda", "lacerga@mail.com", "967733870", "A");
//            doctorBean.create("Silva", "1234", "Eduardo Silva", "silva@mail.com", "123123123", "B");
//
//            patientBean.create("patient", "1234", "Patient1",  "patient@mail.com", "963321321" );

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
