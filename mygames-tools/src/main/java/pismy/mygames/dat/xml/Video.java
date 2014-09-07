package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class Video {
	@XmlAttribute(required=true)
	private VideoScreen screen;

	@XmlAttribute(required=true)
	private VideoOrientation orientation;

	@XmlAttribute
	private int width;

	@XmlAttribute
	private int height;

	@XmlAttribute
	private int aspectx;

	@XmlAttribute
	private int aspecty;

	@XmlAttribute
	private double refresh;

	@Override
	public String toString() {
		return "    <video screen='"+screen+"' orientation='"+orientation+"' width='"+width+"' height='"+height+"' aspectx='"+aspectx+"' aspecty='"+aspecty+"' refresh='"+refresh+"'/>";
	}

	public VideoScreen getScreen() {
		return screen;
	}

	public VideoOrientation getOrientation() {
		return orientation;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getAspectx() {
		return aspectx;
	}

	public int getAspecty() {
		return aspecty;
	}

	public double getRefresh() {
		return refresh;
	}

}
