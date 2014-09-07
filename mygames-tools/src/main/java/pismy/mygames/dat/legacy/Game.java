package pismy.mygames.dat.legacy;

import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IGame;
import pismy.mygames.utils.parse.ParseError;
import pismy.mygames.utils.parse.ParseHelper;

/**
 * @author Pierre Smeyers
 */
public class Game implements IGame {
	private String name;
	private String description;
	private List<Rom> roms = new ArrayList<Rom>();

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getManufacturer() {
		return null;
	}

	@Override
	public String getCloneOf() {
		return null;
	}

	@Override
	public int getYear() {
		return 0;
	}

	public List<Rom> getRoms() {
		return roms;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("  <game");

		if (name != null) {
			sb.append(" name='" + name + "'");
		}
		sb.append(">");

		if (description != null) {
			sb.append("\n    <description>" + description + "</description>");
		}

		for (Rom r : roms) {
			sb.append("\n");
			sb.append(r);
		}

		sb.append("\n  </game>");

		return sb.toString();
	}

	protected static Game parse(ParseHelper parser) throws ParseError {
		if (!parser.consumeString("game ("))
			parser.error(ParseError.RC_APPLICATION_ERROR, "game block expected");
		parser.skipChars(DatConsts.WHITE);

		Game game = new Game();

		if (!parser.consumeString("name"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "name field expected");
		parser.skipChars(DatConsts.WHITE);
		game.name = parser.readUntil(DatConsts.WHITE);

		parser.skipChars(DatConsts.WHITE);

		if (!parser.consumeString("description"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "description field expected");
		parser.skipChars(DatConsts.WHITE);
		game.description = parser.readUntil(DatConsts.WHITE);

		while (true) {
			parser.skipChars(DatConsts.WHITE);
			if (parser.consumeChar(')', null))
				// end of block
				return game;

			game.roms.add(Rom.parse(parser));
		}
	}
	// @Override
	// public boolean equals(Object obj) {
	// return obj != null && obj instanceof IGame && GameUtils.compare(this,
	// (IGame)obj) == GameComp.equals;
	// }
	// @Override
	// public int hashCode() {
	// return name.hashCode();
	// }
}
