package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * <input players="2" control="joy8way" buttons="2" coins="2"/>
 * @author Pierre Smeyers
 */
public class Driver {
	private DriverStatus status;

	private DriverStatus emulation;

	private DriverStatus color;

	private DriverStatus sound;

	private DriverStatus graphic;

	private DriverStatus cocktail;

	private DriverStatus protection;

	private SaveState savestate;

	private int palettesize;
	
	@Override
	public String toString() {
		return "    <driver status='"+status+"' emulation='"+emulation+"' color='"+color+"' sound='"+sound+"' graphic='"+graphic+"' savestate='"+savestate+"' palettesize='"+palettesize+"'/>";
	}

	@XmlAttribute(required=true)
	public DriverStatus getStatus() {
		return status;
	}

	@XmlAttribute(required=true)
	public DriverStatus getEmulation() {
		return emulation;
	}

	@XmlAttribute(required=true)
	public DriverStatus getColor() {
		return color;
	}

	@XmlAttribute(required=true)
	public DriverStatus getSound() {
		return sound;
	}

	@XmlAttribute(required=true)
	public DriverStatus getGraphic() {
		return graphic;
	}

	@XmlAttribute
	public SaveState getSavestate() {
		return savestate;
	}

	@XmlAttribute(required=true)
	public int getPalettesize() {
		return palettesize;
	}

	@XmlAttribute
	public DriverStatus getCocktail() {
		return cocktail;
	}

	@XmlAttribute
	public DriverStatus getProtection() {
		return protection;
	}

	public void setCocktail(DriverStatus cocktail) {
		this.cocktail = cocktail;
	}
	
	public void setProtection(DriverStatus protection) {
		this.protection = protection;
	}

	public void setStatus(DriverStatus status) {
		this.status = status;
	}

	public void setEmulation(DriverStatus emulation) {
		this.emulation = emulation;
	}

	public void setColor(DriverStatus color) {
		this.color = color;
	}

	public void setSound(DriverStatus sound) {
		this.sound = sound;
	}

	public void setGraphic(DriverStatus graphic) {
		this.graphic = graphic;
	}

	public void setSavestate(SaveState savestate) {
		this.savestate = savestate;
	}

	public void setPalettesize(int palettesize) {
		this.palettesize = palettesize;
	}
	
	
}
