package pismy.mygames.dat.mamedb;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import pismy.mygames.dat.xml.Driver;
import pismy.mygames.dat.xml.DriverStatus;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Rom;
import pismy.mygames.dat.xml.RomStatus;
import pismy.mygames.dat.xml.YesNo;

/**
 * Tools to grab ROMs info from http://www.mamedb.com
 * 
 * @author crhx7117
 * 
 */
public class MameDB {
	
	private static Pattern CLONE_OF = Pattern.compile("\\(clone of:\\s*(\\w+)\\)");
	private static Pattern SAMPLE_OF = Pattern.compile("\\(sample of:\\s*(\\w+)\\)");
	
	private static XPath XPATH = XPathFactory.newInstance().newXPath();
	
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, JAXBException {
		System.out.println(get("toobin"));
		System.out.println(get("revenger"));
	}
	
	public static class MameDbGame extends Game {
		private String titleImage;
		private String snapshotImage;
		@XmlAttribute
		public String getTitleImage() {
			return titleImage;
		}
		public void setTitleImage(String titleImage) {
			this.titleImage = titleImage;
		}
		@XmlAttribute
		public String getSnapshotImage() {
			return snapshotImage;
		}
		public void setSnapshotImage(String snapshotImage) {
			this.snapshotImage = snapshotImage;
		}
		
		@Override
		public String toString() {
			return super.toString()+" title: "+titleImage+", snap: "+snapshotImage;
		}
	}

	public static MameDbGame get(String name) throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, XPathExpressionException {
		URL url = new URL("http://www.mamedb.com/game/"+name);
		Reader reader = new InputStreamReader(url.openStream());
		
		Document doc = parseAsXml(reader);
		reader.close();
		Element html = doc.getDocumentElement();
		
		MameDbGame game = new MameDbGame();
		
		// get the images root table
		Element image_display = (Element)XPATH.evaluate(".//table[@class='image_display']", html, XPathConstants.NODE);
		// then get all images
		if(image_display != null) {
			NodeList images = (NodeList) XPATH.evaluate(".//img", image_display, XPathConstants.NODESET);
			for(int i=0; i<images.getLength(); i++) {
				Element img = (Element)images.item(i);
				String src = img.getAttribute("src");
				if(src.startsWith("/titles/")) {
					game.setTitleImage(src.substring(8, src.lastIndexOf('.')));
				} else if(src.startsWith("/snap/")) {
					game.setSnapshotImage(src.substring(6, src.lastIndexOf('.')));
				}
			}
		}
		
//		// get all sections
//		NodeList titles = (NodeList) XPATH.evaluate(".//h1", html, XPathConstants.NODESET);
//		for(int i=0; i<titles.getLength(); i++) {
//			System.out.println("section["+i+"]: "+titles.item(i).getTextContent());
//		}
		
		game.setName(name);
		
		// --- 1: get game details section
		Element detailsHeader = (Element)XPATH.evaluate(".//h1[text()='Game Details']", html, XPathConstants.NODE);
//		System.out.println("game details: "+details.getTextContent());
		FormData details = parseFormSection(detailsHeader);
		String desc = details.getValue("name");
		if(desc != null) {
			// extract clone and sample
//			System.out.println("desc: "+desc);
			Matcher co = CLONE_OF.matcher(desc);
			if(co.find()) {
				String clone = co.group(1).trim();
				game.setCloneOf(clone);
				desc = co.replaceAll("");
//				System.out.println("cloneof '"+clone+"'; desc: "+desc);
			}
			Matcher so = SAMPLE_OF.matcher(desc);
			if(so.find()) {
				String sample = so.group(1).trim();
				game.setSampleOf(sample);
				desc = so.replaceAll("");
//				System.out.println("sampleof '"+sample+"'; desc: "+desc);
			}
			desc = desc.trim();
			game.setDescription(desc);
		}
		
		String year = details.getValue("year");
		if(year != null) {
			try {
				game.setYear(Integer.parseInt(year));
			} catch(NumberFormatException e) {
				System.err.println("Not a year: "+year);
			}
		}
		game.setCategory(details.getValue("category"));
		
		game.setManufacturer(details.getValue("manufacturer"));
		
		Driver driver = new Driver();
		driver.setStatus(DriverStatus.valueOf(details.getValue("status")));
		driver.setEmulation(DriverStatus.valueOf(details.getValue("emulation")));
		driver.setColor(DriverStatus.valueOf(details.getValue("color")));
		driver.setSound(DriverStatus.valueOf(details.getValue("sound")));
		driver.setGraphic(DriverStatus.valueOf(details.getValue("graphic")));
		String paletteSize = details.getValue("palette size");
		if(paletteSize != null) {
			try {
				driver.setPalettesize(Integer.parseInt(paletteSize));
			} catch(NumberFormatException e) {
				System.err.println("Not a palette size: "+paletteSize);
			}
		}
		
		game.setDriver(driver);
		
		// --- 3: get roms
		Element romsHeader = (Element)XPATH.evaluate(".//h1[starts-with(normalize-space(.), 'Rom Info')]", html, XPathConstants.NODE);
		TableData roms = parseTableSection(romsHeader);
		for(List<String> row : roms.rows) {
			Rom rom = new Rom();
			rom.setName(row.get(0));
			rom.setBios(row.get(1));
			rom.setSize(Integer.parseInt(row.get(2)));
			rom.setCrc(row.get(3));
			rom.setMd5(row.get(4));
			rom.setSha1(row.get(5));
			rom.setMerge(row.get(6));
			rom.setRegion(row.get(7));
			rom.setOffset(row.get(8));
			rom.setStatus(RomStatus.valueOf(row.get(9)));
			rom.setDispose(YesNo.valueOf(row.get(10)));
			
			game.getRoms().add(rom);
		}
		
		return game;
	}
	private static FormData parseFormSection(Element sectionTitle) {
		FormData form = new FormData();
		
		Node elt = sectionTitle.getNextSibling();
		if(!"br".equalsIgnoreCase(elt.getNodeName()))
			// we were expecting a <br>
			System.err.println("<br> expected right after a section header: "+sectionTitle.getTextContent());
		else
			elt = elt.getNextSibling();
		
		while(elt != null) {
			// next element shall be a <b>
			if(!"b".equalsIgnoreCase(elt.getNodeName()))
				break;
			String name = elt.getTextContent();
			int idx = name.indexOf(':');
			if(idx > 0)
				name = name.substring(0, idx);
			name = trim(name).toLowerCase();
			
			// then value (either text or hyperlink)
			elt = elt.getNextSibling();
			
			// read until <br> or null
			String value="";
			while(elt != null && !"br".equalsIgnoreCase(elt.getNodeName())) {
				value +=elt.getTextContent();
				elt = elt.getNextSibling();
			}
			value = trim(value);
			if(value.isEmpty())
				value = null;
			
			form.add(name, value);
			
			if(elt == null)
				break;
			elt = elt.getNextSibling();
		}
		return form;
	}
	private static TableData parseTableSection(Element sectionTitle) throws XPathExpressionException {
		
		Element table = getNextSiblingElement(sectionTitle);
		if(!"table".equalsIgnoreCase(table.getNodeName())) {
			System.err.println("<table> expected right after section header: "+sectionTitle.getTextContent());
			return null;
		}
		
		// --- 1: grab headers
		NodeList headers = (NodeList) XPATH.evaluate("./thead/tr[1]/td", table, XPathConstants.NODESET);
		List<String> headerNames = new ArrayList<String>();
		for(int i=0; i<headers.getLength(); i++) {
			String name = trim(headers.item(i).getTextContent()).toLowerCase();
			headerNames.add(name);
//			System.out.println("headers["+i+"]: "+name);
		}
		TableData data = new TableData(headerNames);
		
		// --- 2: grab rows
		NodeList rows = (NodeList) XPATH.evaluate("./tbody/tr", table, XPathConstants.NODESET);
		for(int i=0; i<rows.getLength(); i++) {
			Node row = rows.item(i);
			NodeList cells = (NodeList) XPATH.evaluate("./td", row, XPathConstants.NODESET);
			List<String> cellValues = new ArrayList<String>();
			for(int j=0; j<cells.getLength(); j++) {
				String value = trim(cells.item(j).getTextContent());
				if(value.isEmpty())
					value = null;
				cellValues.add(value);
//				System.out.println("cell["+i+","+j+"]: "+value);
			}
			data.addRow(cellValues);
		}
		return data;
	}
	private static Element getNextSiblingElement(Node elt) {
		Node n = elt.getNextSibling();
		while(n != null && n.getNodeType() != Node.ELEMENT_NODE)
			n = n.getNextSibling();
		return (Element)n;
	}
	private static String trim(String str)
	{
		if(str == null || str.length() == 0)
			return null;
		// --- replace nbsp with space
		str = str.replace((char)160, ' ');
		return str.trim();
	}

	private static Document parseAsXml(Reader html) throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException
	{
		//using cobra
//		UserAgentContext context = new SimpleUserAgentContext();
//		DocumentBuilderImpl dbi = new DocumentBuilderImpl(context);
//		// A document URI and a charset should be provided.
//		Document document = dbi.parse(new InputSource(html));
//		return document;
		
//		// using CyberNeko
//		DOMParser parser = new DOMParser();
//		parser.parse(new InputSource(new FileInputStream(html)));
//		return parser.getDocument();

//		// using TagSoup
		XMLReader reader = new Parser();
		reader.setFeature(Parser.namespacesFeature, false);
//		reader.setFeature(Parser.namespacePrefixesFeature, false);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		
		DOMResult result = new DOMResult();
//		InputStream input = new FileInputStream(html);
		transformer.transform(new SAXSource(reader, new InputSource(html)), result);
//		input.close();
		
		// here we go - an DOM built from abitrary HTML
		return (Document)result.getNode();
	}

	public static class FormData {
		LinkedHashMap<String, String> name2Value = new LinkedHashMap<String, String>();
		
		public void add(String name, String value) {
			name2Value.put(name, value);
		}
		public int length() {
			return name2Value.size();
		}
		public Set<String> getNames() {
			return name2Value.keySet();
		}
		public String getValue(String name) {
			return name2Value.get(name);
		}
		
		@Override
		public String toString() {
			return "Form"+name2Value;
		}
	}
	public static class TableData {
		final List<String> headers;
		List<List<String>> rows = new ArrayList<List<String>>();
		
		public TableData(List<String> headers) {
			this.headers = headers;
		}
		public int countCols() {
			return headers.size();
		}
		public int countRows() {
			return rows.size();
		}
		public List<String> getHeaders() {
			return headers;
		}
		public List<List<String>> getRows() {
			return rows;
		}
		public void addRow(List<String> cells) {
			rows.add(cells);
		}
	}
}
