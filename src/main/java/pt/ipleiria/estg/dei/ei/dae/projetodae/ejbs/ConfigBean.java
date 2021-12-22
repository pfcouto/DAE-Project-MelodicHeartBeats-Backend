package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    DoctorBean doctorBean;
    @EJB
    PatientBean patientBean;
    @EJB
    AdministratorBean administratorBean;
    @EJB
    BiometricsTypeBean biometricsTypeBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateBD() {

        try {

            administratorBean.create("admin", "1234", "Administrator", "admin@mail.com", "963123123");

            doctorBean.create("Lacerda", "1234", "Jorge Lacerda", "lacerga@mail.com", "967733870", "A");
            doctorBean.create("Silva", "1234", "Eduardo Silva", "silva@mail.com", "123123123", "B");

            patientBean.create("patient", "1234", "Patient1",  "patient@mail.com", "963321321" );
            biometricsTypeBean.create("teste","description",2,1,"g","admin");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
