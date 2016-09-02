package pismy.mygames.dat;

import java.util.List;

public abstract class IDat<G extends IGame<?>> {
	public G byName(String name) {
		for(G g : getGames()) {
			if(name.equals(g.getName())) {
				return g;
			}
		}
		return null;
	}
	public abstract List<G> getGames();
}
