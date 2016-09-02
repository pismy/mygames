package pismy.mygames.dat.wrapper;

import java.util.List;

import pismy.mygames.dat.IGame;
import pismy.mygames.dat.IRom;
import pismy.mygames.utils.GameComparator;

public class GameWrapper extends IGame<IRom> {
	final IGame<IRom> main, parent;

	public GameWrapper(IGame main, IGame parent) {
		this.main = main;
		this.parent = parent;
	}

	@Override
	public String getName() {
		return main.getName();
	}

	@Override
	public String getDescription() {
		return parent != null ? parent.getDescription() : main.getDescription();
	}

	@Override
	public String getCategory() {
		return parent != null ? parent.getCategory() : main.getCategory();
	}

	@Override
	public String getManufacturer() {
		return parent != null ? parent.getManufacturer() : main.getManufacturer();
	}

	@Override
	public String getCloneOf() {
		return parent != null ? parent.getCloneOf() : main.getCloneOf();
	}

	@Override
	public int getYear() {
		return parent != null ? parent.getYear() : main.getYear();
	}

	@Override
	public List<IRom> getRoms() {
		return main.getRoms();
	}

	public IRom getRom(String name) {
		return main.byName(name);
	}
}
