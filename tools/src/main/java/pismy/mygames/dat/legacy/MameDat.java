package pismy.mygames.dat.legacy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import pismy.mygames.dat.IDat;
import pismy.mygames.utils.parse.ParseError;
import pismy.mygames.utils.parse.ParseHelper;

/**
 * @author Pierre Smeyers
 */
@XmlRootElement(name = "mame")
public class MameDat extends IDat<Game> {
	
	private String name;
	private String description;
	private String category;
	private String version;
	
	private List<Game> games = new ArrayList<Game>();
	
	public List<Game> getGames() {
		return games;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<mame name='"+name+"'>");
		for(Game g : games) {
			sb.append("\n");
			sb.append(g);
		}
		sb.append("\n</mame>");
		return sb.toString();
	}
	
//	public Game getByRom(String romName) {
//		for(Game g : games) {
//			if(romName.equals(g.getName()))
//				return g;
//		}
//		return null;
//	}
	
//	public static void main(String[] args) throws IOException, ParseError {
//		MameDat mame = load(new File("E:/perso/MAME/DAT/mame4all (0.37b5)/clrmame.dat"));
//		System.out.println(mame);
//		System.out.println("games: "+mame.getGames().size());
//		
//		System.out.println("game (baddudes): "+mame.getByRom("baddudes"));
//	}
	
	public static MameDat load(File datFile) throws IOException, ParseError {
		Reader rd = new FileReader(datFile);
		ParseHelper parser = new ParseHelper(rd);
		parser.curChar();
		MameDat m = parse(parser);
		rd.close();
		return m;
	}
	
	protected static MameDat parse(ParseHelper parser) throws ParseError {
		parser.skipChars(DatConsts.WHITE);
		MameDat mame = new MameDat();
		if(!parser.consumeString("clrmamepro ("))
			parser.error(ParseError.RC_APPLICATION_ERROR, "clrmamepro block expected");
		
		parser.skipChars(DatConsts.WHITE);
		
		if(!parser.consumeString("name"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "name field expected");
		parser.skipChars(DatConsts.WHITE);
		mame.name = parser.readUntil(DatConsts.WHITE);
		
		parser.skipChars(DatConsts.WHITE);
		
		if(!parser.consumeString("description"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "description field expected");
		parser.skipChars(DatConsts.WHITE);
		mame.description = parser.readUntil(DatConsts.WHITE);
		
		// skip rest
		parser.readUntil(")");
		parser.consumeChar(')', null);
		
		while(true) {
			parser.skipChars(DatConsts.WHITE);
			if(parser.curChar() <= 0)
				// EOF
				return mame;
			
			mame.games.add(Game.parse(parser));
		}
	}
}
