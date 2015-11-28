package optimod.tsp;

public class GrapheComplet implements Graphe {
	
	private static final int COUT_MAX = 40;
	private static final int COUT_MIN = 10;
	int nbSommets;
	int[][] cout;
	
	/**
	 * Cree un graphe complet dont les aretes ont un cout compris entre COUT_MIN et COUT_MAX
	 * @param nbSommets
	 */
	public GrapheComplet(int nbSommets){
		this.nbSommets = nbSommets;
		/**int table[]={-1, 1184, 740, 533, 1850, 870, 758, 191, 585, 1415, 1443, 1399, 1470, 1370, 1422, 1419, 1416,
				1174, -1, 641, 1023, 1469, 620, 648, 1337, 582, 447, 510, 591, 669, 760, 812, 866, 916,
				750, 680, -1, 406, 1208, 877, 801, 941, 257, 826, 775, 731, 773, 673, 725, 679, 676,
				535, 1067, 408, -1, 1366, 1142, 1012, 726, 468, 1234, 1183, 1139, 1181, 1081, 1133, 1087, 1084,
				1822, 1513, 1208, 1342, -1, 1884, 1876, 2013, 1465, 1205, 1142, 1042, 964, 864, 819, 762, 570,
				889, 628, 846, 1145, 1829, -1, 217, 1017, 704, 900, 963, 1044, 1122, 1149, 1201, 1255, 1276,
				763, 656, 779, 984, 1828, 206, -1, 891, 543, 928, 991, 1072, 1150, 1153, 1205, 1259, 1275,
				208, 1371, 948, 741, 2058, 1017, 905, -1, 772, 1602, 1630, 1586, 1657, 1557, 1609, 1627, 1624,
				592, 599, 236, 441, 1444, 684, 544, 755, -1, 830, 858, 814, 885, 785, 837, 891, 907,
				1413, 444, 815, 1221, 1170, 887, 915, 1576, 821, -1, 63, 150, 228, 319, 371, 425, 617,
				1429, 507, 752, 1158, 1107, 950, 978, 1592, 837, 63, -1, 87, 165, 256, 308, 362, 554,
				1392, 573, 715, 1121, 1020, 1016, 1044, 1555, 800, 163, 100, -1, 78, 169, 221, 275, 467,
				1470, 651, 765, 1171, 942, 1094, 1122, 1633, 878, 241, 178, 78, -1, 91, 143, 197, 389,
				1391, 751, 674, 1080, 851, 1164, 1168, 1554, 799, 341, 278, 178, 100, -1, 52, 106, 298,
				1436, 796, 719, 1125, 799, 1209, 1213, 1599, 844, 386, 323, 223, 145, 45, -1, 54, 246,
				1471, 853, 721, 1127, 745, 1266, 1270, 1656, 901, 443, 380, 280, 202, 102, 57, -1, 192,
				1468, 943, 718, 1124, 553, 1314, 1310, 1659, 941, 635, 572, 472, 394, 294, 249, 192, -1, };
		cout= new int[17][17];

		System.out.println("long table" +table.length);
		int u=0;
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				cout[i][j]=table[u];
				u++;
			}
		}*/

		int iseed = 1;
		cout = new int[nbSommets][nbSommets];
		for (int i=0; i<nbSommets; i++){
		    for (int j=0; j<nbSommets; j++){
		        if (i == j) cout[i][j] = -1;
		        else {
		            int it = 16807 * (iseed % 127773) - 2836 * (iseed / 127773);
		            if (it > 0)	iseed = it;
		            else iseed = 2147483647 + it;
		            cout[i][j] = COUT_MIN + iseed % (COUT_MAX-COUT_MIN+1);
		        }
		    }
		}
	}


	public int getNbSommets() {
		return nbSommets;
	}


	public int getCout(int i, int j) {
		if (i<0 || i>=nbSommets || j<0 || j>=nbSommets)
			return -1;
		return cout[i][j];
	}


	public boolean estArc(int i, int j) {
		if (i<0 || i>=nbSommets || j<0 || j>=nbSommets)
			return false;
		return i != j;
	}

}
