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

    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Hello Java EE!");

            doctorBean.create("dJoao", "1234", "Joao", "1997-06-23", "djoao@mail.pt", "919000222", "Of.1");
            doctorBean.create("dLuis", "1234", "Luis", "1986-03-25", "dLuis@mail.pt", "919000333", "Of.2");

            patientBean.create("pLeandro", "1234", "Leandro", "1997-06-23", "pleandro@mail.pt", "919000111");
            patientBean.create("pLeonardo", "1234", "Leonardo", "1997-06-23", "pLeonardo@mail.pt", "919000777");

//            prescriptionBean.create("dJoao","pLeandro","TEXTO DA DESCRICAO","2020-06-23","2020-07-23");
//            prescriptionBean.create("dJoao","pLeandro","TEXTO DA DESCRICAO 1","2020-06-23","2022-07-23");
//            prescriptionBean.create("dLuis","pLeandro","TEXTO DA DESCRICAO 2","2022-06-23","2022-07-23");
//            prescriptionBean.create("dLuis","pLeonardo","TEXTO DA DESCRICAO 3","2020-06-23","2020-07-23");

            administratorBean.create("admin", "1234", "Administrator", "1987-02-19", "admin@mail.com", "963123123");
            QualitativeValuesDTO qualitativeValuesDTO = new QualitativeValuesDTO(2, "Baixo");
            List<QualitativeValuesDTO> teste = new LinkedList<>();
            teste.add(qualitativeValuesDTO);
            biometricsTypeBean.create("teste","description",2,1,"g","admin",teste);
            biometricsTypeBean.create("teste2","description",2,1,"g","admin",teste);
            observationBean.create("2000-03-18","pLeandro",1,2,"perna","Lisboa",null);
            observationBean.create("2000-03-18","pLeandro",1,2,"perna","Lisboa","dJoao");
            observationBean.create("2000-03-18","pLeonardo",1,2,"perna","Lisboa",null);
            System.out.println("DEUUU");
//            doctorBean.create("Lacerda", "1234", "Jorge Lacerda", "lacerga@mail.com", "967733870", "A");
//            doctorBean.create("Silva", "1234", "Eduardo Silva", "silva@mail.com", "123123123", "B");
//
//            patientBean.create("patient", "1234", "Patient1",  "patient@mail.com", "963321321" );

            ruleBean.create(1, "=", 2, "Exercicio Fisico", 45);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
