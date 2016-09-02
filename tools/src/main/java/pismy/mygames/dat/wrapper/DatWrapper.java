package pismy.mygames.dat.wrapper;

import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IDat;
import pismy.mygames.dat.IGame;
import pismy.mygames.utils.GameComparator;

public class DatWrapper extends IDat<GameWrapper> {
	final IDat<?> main, parent;
	List<GameWrapper> games;
	public DatWrapper(IDat<?> main, IDat<?> parent) {
		this.main = main;
		this.parent = parent;
	}
	public List<GameWrapper> getGames() {
		if(games == null) {
			games = new ArrayList<GameWrapper>();
			for(IGame mainGame : main.getGames()) {
				IGame parentGame = parent == null ? null : parent.byName(mainGame.getName());
				games.add(new GameWrapper(mainGame, parentGame));
			}
		}
		return games;
	};
	public GameWrapper getGame(String name) {
		return this.byName(name);
	}

}
