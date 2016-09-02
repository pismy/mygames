package pismy.mygames.dat;

import java.util.List;

public abstract class IGame<R extends IRom> {

	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract int getYear();
	
	public abstract String getCloneOf();

	public abstract List<R> getRoms();

	public abstract String getCategory();

	public abstract String getManufacturer();
	
	public R byName(String name) {
		for(R g : getRoms()) {
			if(name.equals(g.getName())) {
				return g;
			}
		}
		return null;
	}

}
