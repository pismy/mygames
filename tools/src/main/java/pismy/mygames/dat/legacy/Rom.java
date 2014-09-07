package pismy.mygames.dat.legacy;

import pismy.mygames.dat.IRom;
import pismy.mygames.utils.parse.ParseError;
import pismy.mygames.utils.parse.ParseHelper;


/**
 * @author Pierre Smeyers
 */
public class Rom implements IRom {
	
	private String name;

	private long size;

	private String crc;

	@Override
	public String toString() {
		return "    <rom name='"+name+"' size='"+size+"' crc='"+crc+"'/>";
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public String getCrc() {
		return crc;
	}
	@Override
	public String getMd5() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSha1() {
		// TODO Auto-generated method stub
		return null;
	}
	protected static Rom parse(ParseHelper parser) throws ParseError {
		if(!parser.consumeString("rom ("))
			parser.error(ParseError.RC_APPLICATION_ERROR, "rom block expected");
		
		Rom rom = new Rom();
		
		parser.skipChars(DatConsts.WHITE);
		
		if(!parser.consumeString("name"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "name field expected");
		parser.skipChars(DatConsts.WHITE);
		rom.name = parser.readUntil(DatConsts.WHITE);
		
		parser.skipChars(DatConsts.WHITE);
		
		if(!parser.consumeString("size"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "size field expected");
		parser.skipChars(DatConsts.WHITE);
		rom.size = Integer.parseInt(parser.readUntil(DatConsts.WHITE));
		
		parser.skipChars(DatConsts.WHITE);
		
		if(!parser.consumeString("crc"))
			parser.error(ParseError.RC_APPLICATION_ERROR, "crc field expected");
		parser.skipChars(DatConsts.WHITE);
		rom.crc = parser.readUntil(DatConsts.WHITE);
		
		// skip rest
		parser.readUntil(")");
		parser.consumeChar(')', null);
		
		return rom;
	}

}
