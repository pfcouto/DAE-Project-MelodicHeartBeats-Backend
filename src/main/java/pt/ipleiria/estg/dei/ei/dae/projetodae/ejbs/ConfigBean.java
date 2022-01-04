package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.QualitativeValuesDTO;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.LinkedList;
import java.util.List;
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
    @EJB
    BiometricsTypeBean biometricsTypeBean;
    @EJB
    ObservationBean observationBean;
    @EJB
    RuleBean ruleBean;
    @EJB
    PRCBean prcBean;


    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Hello Java EE!");
            doctorBean.create("dJoao", "1234", "Joao", "1997-06-23", "djoao@mail.pt", "919000222", "Of.1");
            doctorBean.create("dLuis", "1234", "Luis", "1986-03-25", "dLuis@mail.pt", "919000333", "Of.2");
            patientBean.create("pLeandro", "1234", "Leandro", "1997-06-23", "pleandro@mail.pt", "919000111");
            patientBean.create("pManuel", "1234", "Manuel", "1972-08-11", "pManuel@mail.pt", "919000777");
            administratorBean.create("admin", "1234", "Administrator", "1987-02-19", "admin@mail.com", "963123123");
            administratorBean.create("admin2", "1234", "Administrator2", "2000-02-19", "admin2@mail.com", "963123124");

            QualitativeValuesDTO qualitativeValuesDTO = new QualitativeValuesDTO(35, "Baixo");
            QualitativeValuesDTO qualitativeValuesDTO2 = new QualitativeValuesDTO(37, "Medio");
            QualitativeValuesDTO qualitativeValuesDTO3 = new QualitativeValuesDTO(39, "Alto");
            List<QualitativeValuesDTO> qualitativeValuesDTOList = new LinkedList<>();
            qualitativeValuesDTOList.add(qualitativeValuesDTO);
            qualitativeValuesDTOList.add(qualitativeValuesDTO2);
            qualitativeValuesDTOList.add(qualitativeValuesDTO3);
            List<QualitativeValuesDTO> qualitativeValuesDTOListVazia = new LinkedList<>();
            biometricsTypeBean.create("Temperatura corpural","Temperatura medida em baixo do braco",45,30,"C","admin",qualitativeValuesDTOList);
            biometricsTypeBean.create("Pulsacao","Pulsacao cardiaca medida em BMP",250,40,"BMP","admin2",qualitativeValuesDTOListVazia);
            observationBean.create("2021-08-20","pManuel",1,36,"temperatura medida no Braco","Lisboa",null);
            observationBean.create("2021-09-20","pManuel",1,35,"temperatura medida no Braco","Leiria","dJoao");
            observationBean.create("2021-10-20","pManuel",1,40,"temperatura medida no Braco","Coimbra",null);
            observationBean.create("2021-08-20","pManuel",2,72,"pulsacao medida no Braco","Lisboa","dLuis");
            ruleBean.create(1, ">", 37, "Tomar banho de agua morna, nao muito quente, nao muito fria", 1);
            ruleBean.create(2, ">", 70, "exercicios fisico leve 2x por dia", 45);
            prcBean.create("pManuel","2021-08-05","2022-08-05");
            prescriptionBean.create("dJoao","pManuel","Comprimido Y 1x ao almoço","2021-08-05","2021-09-05");

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
