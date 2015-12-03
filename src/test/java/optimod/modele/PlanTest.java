package optimod.modele;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


public class PlanTest {

    private Plan plan;

    @Before
    public void setUp() throws Exception {
        plan = new Plan();
    }


    @Test
    public void testChargerPlan() throws Exception {

    }

    @Test
    public void testTrouverIntersection() throws Exception {
        Intersection intersection = new Intersection(0, 0, 0);
        List<Intersection> intersections = new ArrayList<>();
        intersections.add(intersection);
        plan.setIntersections(intersections);
        assertEquals(intersection, plan.trouverIntersection(0));

    }

    @Test
    public void testReinitialiser() throws Exception {
        Intersection intersection = new Intersection(0, 0, 0);
        List<Intersection> intersections = new ArrayList<>();
        intersections.add(intersection);
        plan.setIntersections(intersections);
        assertEquals(intersection, plan.getIntersections().get(0));
        plan.reinitialiser();
        assertTrue(plan.getIntersections().isEmpty());
    }

    @Test
    public void testTrouverIntersection1() throws Exception {


    }
}