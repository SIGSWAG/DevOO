package optimod.es.xml;

import optimod.modele.Plan;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by (PRO) Loïc Touzard on 25/11/2015.
 */
public class DeserialiseurXMLTest {

    @Test
    public void testChargerPlan() throws Exception {
        File xml = new File("src/test/resources/fail/plan-vide.xml");
        Plan plan = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);


    }

    @Test
    public void testChargerDemandeLivraison() throws Exception {

    }
}