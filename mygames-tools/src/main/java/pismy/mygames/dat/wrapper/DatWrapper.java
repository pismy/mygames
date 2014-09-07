package pismy.mygames.dat.wrapper;

import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IDat;
import pismy.mygames.dat.IGame;
import pismy.mygames.utils.GameUtils;

public class DatWrapper implements IDat {
	final IDat main, parent;
	List<GameWrapper> games;
	public DatWrapper(IDat main, IDat parent) {
		this.main = main;
		this.parent = parent;
	}
	public List<? extends IGame> getGames() {
		if(games == null) {
			games = new ArrayList<GameWrapper>();
			for(IGame mainGame : main.getGames()) {
				IGame parentGame = parent == null ? null : GameUtils.getGameByName(parent, mainGame.getName());
				games.add(new GameWrapper(mainGame, parentGame));
			}
		}
		return games;
	};
	public GameWrapper getGame(String name) {
		return GameUtils.getGameByName(this, name);
	}

}
