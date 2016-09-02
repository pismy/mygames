package pismy.mygames.dat.lst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IGame;
import pismy.mygames.dat.IRom;

import com.google.common.base.Strings;

public class LstGame extends IGame<IRom> {
	private String name;

	private String cloneOf;

	private String category;

	private String description;

	private int year;

	private String manufacturer;
	
	private String imgTitle;
	
	private String imgSnapshot;
	
	private String imgOrientation = "n";
	
	public LstGame() {
	}
	
	public LstGame(IGame game) {
		this.category = game.getCategory();
		this.cloneOf = game.getCloneOf();
		this.description = game.getDescription();
		this.manufacturer = game.getManufacturer();
		this.name = game.getName();
		this.year = game.getYear();
		if(game instanceof LstGame) {
			this.imgTitle = ((LstGame) game).getTitleImage();
			this.imgSnapshot = ((LstGame) game).getSnapshotImage();
			this.imgOrientation = ((LstGame) game).getImgOrientation();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloneOf() {
		return cloneOf;
	}

	public void setCloneOf(String cloneof) {
		this.cloneOf = cloneof;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getTitleImage() {
		return imgTitle;
	}

	public void setTitleImage(String titleImage) {
		this.imgTitle = titleImage;
	}

	public String getSnapshotImage() {
		return imgSnapshot;
	}

	public void setSnapshotImage(String snapshotImage) {
		this.imgSnapshot = snapshotImage;
	}
	
	public String getImgOrientation() {
		return imgOrientation;
	}
	public void setImgOrientation(String imgOrientation) {
		this.imgOrientation = imgOrientation;
	}

	@Override
	public List<IRom> getRoms() {
		return null;
	}
	
	private static String orBlank(String s) {
		return s == null ? "" : s;
	}
	private static String orNull(String s) {
		return Strings.isNullOrEmpty(s) ? null : s;
	}
	
	@Override
	public String toString() {
		return toLine();
	}

	protected String toLine() {
		StringBuilder line = new StringBuilder();
		line.append(getName());
		line.append('|');
		line.append(orBlank(getCloneOf()));
		line.append('|');
		line.append(orBlank(getDescription()));
		line.append('|');
		line.append(orBlank(getCategory()));
		line.append('|');
		line.append(orBlank(getManufacturer()));
		line.append('|');
		line.append(getYear());
		line.append('|');
		line.append(orBlank(getImgOrientation()));
		line.append('|');
		line.append(orBlank(getSnapshotImage()));
		line.append('|');
		line.append(orBlank(getTitleImage()));
		return line.toString();
	}
//	public static void main(String[] args) {
//		System.out.println(Arrays.asList("005||005|Maze / Shooter Small|Sega|1981|005|005".split("\\|")));
//		System.out.println(Arrays.asList("11beat||Eleven Beat|Unplayable|Hudson|1998|| ".split("\\|")));
//	}
	protected static LstGame fromLine(String line) throws IOException {
		Tokenizer tok = new Tokenizer(line, '|');
//		String[] tokens = line.split("\\|");
//		if(tok.ens.length < 8) {
//			throw new IOException("not a parseable game: "+line);
//		}
		LstGame g = new LstGame();
		g.setName(orNull(tok.nextToken()));
		g.setCloneOf(orNull(tok.nextToken()));
		g.setDescription(orNull(tok.nextToken()));
		g.setCategory(orNull(tok.nextToken()));
		g.setManufacturer(orNull(tok.nextToken()));
		g.setYear(Integer.parseInt(tok.nextToken()));
//		g.setImgOrientation(orNull(tok.nextToken()));
		g.setSnapshotImage(orNull(tok.nextToken()));
		g.setTitleImage(orNull(tok.nextToken()));
		
		return g;
	}
	private static class Tokenizer {
		final String str;
		final char sep;
		int idx = -1;
		int count = 0;
		public Tokenizer(String str, char sep) {
			this.str = str;
			this.sep = sep;
		}
		public String nextToken() {
			if(idx >= str.length()) {
				throw new RuntimeException("no more token in '"+str+"' (found "+count+")");
			}
			int prevIdx = idx;
			idx = str.indexOf(sep, idx+1);
			if(idx < 0) {
				idx = str.length();
			}
			count++;
			return str.substring(prevIdx+1, idx);
		}
		
	}
	
	public static void save(List<LstGame> games, File to) throws IOException {
		Writer w = new FileWriter(to);
		for(LstGame g : games) {
			w.write(g.toLine());
			w.write("\n");
		}
		w.flush();
		w.close();
	}
	
	public static List<LstGame> load(File from) throws IOException {
		List<LstGame> set = new ArrayList<LstGame>();
		BufferedReader r = new BufferedReader(new FileReader(from));
		String line = null;
		while((line = r.readLine()) != null) {
			if(line.length() == 0 || line.startsWith("#"))
				// skip line
				continue;
			set.add(fromLine(line));
		}
		r.close();
		return set;

	}

	
}
