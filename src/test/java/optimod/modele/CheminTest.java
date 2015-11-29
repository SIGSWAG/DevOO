package optimod.modele;

import org.junit.Test;

import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by jonathan on 18/11/15.
 */
public class CheminTest {

	@Test
	public  void comparerChemins(Chemin cheminATester, Chemin cheminType) {
		comparerCheminsPrimitives(cheminATester, cheminType);

		List<Troncon> tronconsATester = cheminATester.getTroncons();
		List<Troncon> tronconsType = cheminType.getTroncons();

		assertEquals(tronconsATester.size(), tronconsType.size());

		for(int i = 0; i < tronconsATester.size(); i++) {
			Troncon tronconATester = tronconsATester.get(i);
			Troncon tronconType = tronconsType.get(i);

			TronconTest.comparerTroncons(tronconATester, tronconType);
		}

		LivraisonTest.comparerLivraisonsPrimitives(cheminATester.getDepart(), cheminType.getDepart());
		LivraisonTest.comparerLivraisonsPrimitives(cheminATester.getArrivee(), cheminType.getArrivee());
	}

	@Test
	public void comparerCheminsPrimitives(Chemin cheminATester, Chemin cheminType) {
		assertNotNull(cheminATester);
		assertNotNull(cheminType);

		assertEquals(cheminATester.getDuree(), cheminType.getDuree());
	}

}