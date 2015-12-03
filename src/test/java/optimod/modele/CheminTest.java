package optimod.modele;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class CheminTest {


	public static void comparerChemins(Chemin cheminATester, Chemin cheminType) {
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


	public static void comparerCheminsPrimitives(Chemin cheminATester, Chemin cheminType) {
		assertNotNull(cheminATester);
		assertNotNull(cheminType);

		assertEquals(cheminATester.getDuree(), cheminType.getDuree());
	}

	public static boolean comparerCheminsBool(Chemin cheminATester, Chemin cheminType) {
		if (!comparerCheminsPrimitivesBool(cheminATester, cheminType)){
			return false;
		}

		List<Troncon> tronconsATester = cheminATester.getTroncons();
		List<Troncon> tronconsType = cheminType.getTroncons();

		if(tronconsATester.size()!= tronconsType.size()){
			return false;
		}

		for(int i = 0; i < tronconsATester.size(); i++) {
			Troncon tronconATester = tronconsATester.get(i);
			Troncon tronconType = tronconsType.get(i);

			if(!TronconTest.comparerTronconsBool(tronconATester, tronconType)){
				return false;
			}
		}

		return !(!LivraisonTest.comparerLivraisonsPrimitivesBool(cheminATester.getDepart(), cheminType.getDepart()) ||
				!LivraisonTest.comparerLivraisonsPrimitivesBool(cheminATester.getArrivee(), cheminType.getArrivee()));

	}


	public static boolean comparerCheminsPrimitivesBool(Chemin cheminATester, Chemin cheminType) {
		return (cheminATester!=null && cheminType!=null && cheminATester.getDuree() == cheminType.getDuree());
	}




}