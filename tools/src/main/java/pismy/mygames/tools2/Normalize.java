package pismy.mygames.tools2;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pismy.mygames.Files;
import pismy.mygames.dat.IGame;
import pismy.mygames.dat.lst.LstGame;
import pismy.mygames.utils.Tree;

import com.google.common.base.Objects;

/**
 * <ul>
 * <li>filtrer les ROMs BIOS
 * <li>télécharger toutes les ROM parent
 * <li>le nom title et scan est-il toujours le même ?
 * <li>le nom title et scan d'un clone est-il toujours le nom du parent ?
 * <li>infos systématiquement redondantes d'un clone et du master ? (year, category, manuf)
 * </ul>
 */
public class Normalize {
	public static void main(String[] args) throws IOException {
		List<LstGame> games = LstGame.load(Files.getMameDBLst());
		countClones(games);
		normBios(games);
		normImages(games);
		normCloneImages(games);
		normCategories(games);
		normManufacturers(games);
	}
	public static void normBios(List<? extends IGame> games) {
		System.out.println("[BIOS] games:");
		int count = 0;
		for(IGame g : games) {
			if("BIOS".equals(g.getCategory())) {
				System.out.println(" ... "+g);
				count++;
			}
		}
		System.out.println(" ---> "+count+" games");
	}
	public static void normImages(List<LstGame> games) {
		System.out.println("[title != snapshot] games:");
		int count = 0;
		for(LstGame g : games) {
			if(!Objects.equal(g.getTitleImage(), g.getSnapshotImage())) {
				System.out.println(" ... "+g);
				count++;
			}
		}
		System.out.println(" ---> "+count+" games");
	}
	public static void normCloneImages(List<LstGame> games) {
		System.out.println("[clone image != parent image] games:");
		int count = 0;
		for(LstGame g : games) {
			if(g.getCloneOf() != null && g.getTitleImage() != null && !Objects.equal(g.getTitleImage(), g.getCloneOf())) {
				System.out.println(" ... "+g);
				count++;
			}
		}
		System.out.println(" ---> "+count+" games");
	}
	private static void countClones(List<? extends IGame> games) {
		int clones = 0;
		for (IGame g : games) {
			if(g.getCloneOf() != null) {
				clones++;
			}
		}
		System.out.println(" ---> "+clones+" clones/"+games.size()+" games");
	}
	private static void normManufacturers(List<? extends IGame> games) {
		Map<String, Integer> manuf = new HashMap<String, Integer>();
		Map<String, Integer> simpleManuf = new HashMap<String, Integer>();
		for (IGame g : games) {
			if(g.getManufacturer() == null) {
				continue;
			}
			Integer count = manuf.get(g.getManufacturer());
			manuf.put(g.getManufacturer(), count == null ? 1 : count+1);
			
			String simple = g.getManufacturer();
			int idxSlash = simple.indexOf('/');
			if(idxSlash > 0) {
				simple = simple.substring(0, idxSlash);
			}
			int idxParen = simple.indexOf('(');
			if(idxParen > 0) {
				simple = simple.substring(0, idxParen);
			}
			simple = simple.trim();
			count = simpleManuf.get(simple);
			simpleManuf.put(simple, count == null ? 1 : count+1);
		}
		System.out.println("All Manufacturers ("+manuf.size()+"):");
		System.out.println(manuf);
		
		System.out.println("Simplified Manufacturers ("+simpleManuf.size()+"):");
		System.out.println(simpleManuf);
	}

	private static void normCategories(List<? extends IGame> games) {
		int[] categories = new int[10];
		Tree<Integer> catTree = new Tree<Integer>();
		catTree.set(0);
		for (IGame g : games) {
			if (g.getCloneOf() == null) {
				if (g.getCategory() != null) {
					String[] catPath = g.getCategory().split("/");
					for (int i = 0; i < catPath.length; i++)
						catPath[i] = catPath[i].trim();
					int nb = catPath.length;
					categories[nb]++;

					Tree<Integer> tree = catTree.getSubTree(catPath, true);
					tree.set(tree.get() == null ? 1 : tree.get() + 1);
				} else {
					categories[0]++;
					catTree.set(catTree.get() + 1);
				}
			}
		}
		System.out.println("Categories ("+categories.length+"):");
		for (int i = 0; i < categories.length; i++) {
			if (categories[i] != 0)
				System.out.println(i + ": " + categories[i]);
		}
		System.out.println(catTree);

		// mame.save(new File("data/mamedb2.xml"));
	}
}
