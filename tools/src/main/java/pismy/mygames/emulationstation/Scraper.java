package pismy.mygames.emulationstation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import pismy.mygames.dat.mamedb.MameDB;
import pismy.mygames.dat.mamedb.MameDB.MameDbGame;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.emulationstation.gamelist.Game;
import pismy.mygames.emulationstation.gamelist.GameList;
import pismy.mygames.utils.GifSequenceWriter;
import pismy.mygames.utils.file.FileUtils;

public class Scraper {
	/**
	 * ../data/fba/fba_029671_od_release_10_working_roms.dat.xml
	 * ../front-end/images/gifs ../front-end/images/mamedb/titles
	 * ../front-end/images/mamedb/snaps
	 * 
	 * @param args
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException, JAXBException {
		DataFile fba = DataFile.load(new File(args[0]));
		File imgDir = new File(args[1]);
		File titlesDir = new File(args[2]);
		File snapsDir = new File(args[3]);

		GameList list = new GameList();

		for (pismy.mygames.dat.xml.Game g : fba.getGames()) {
			try {
				String name = g.getName();
				MameDbGame desc = MameDB.get(name);
				// download title and snap images
				File gameImgFile = new File(imgDir, desc.getTitleImage() + ".gif");
				boolean hasImage = false;
				if (!gameImgFile.exists() && (desc.getSnapshotImage() != null || desc.getTitleImage() != null)) {
					// download title
					File titleFile = new File(titlesDir, desc.getTitleImage() + ".png");
					BufferedImage titleImg = null;
					if (!titleFile.exists()) {
						try {
							URL titleUrl = new URL("http://www.mamedb.com/titles/" + desc.getTitleImage() + ".png");
							FileUtils.copy(titleUrl.openStream(), new FileOutputStream(titleFile));
							titleImg = ImageIO.read(titleFile);
						} catch (Exception ioe) {
							System.err.println("Error while getting '" + desc.getTitleImage() + "' title image");
						}
					}

					// download snap
					File snapFile = new File(snapsDir, desc.getSnapshotImage() + ".png");
					BufferedImage snapImg = null;
					if (!snapFile.exists()) {
						try {
							URL snapUrl = new URL("http://www.mamedb.com/snap/" + desc.getSnapshotImage() + ".png");
							FileUtils.copy(snapUrl.openStream(), new FileOutputStream(snapFile));
							snapImg = ImageIO.read(snapFile);
						} catch (Exception ioe) {
							System.err.println("Error while getting '" + desc.getSnapshotImage() + "' snap image");
						}
					}

					if (titleImg != null || snapImg != null) {
						// make animated gif
						FileImageOutputStream gifOS = new FileImageOutputStream(gameImgFile);
						int type = titleImg != null ? titleImg.getType() : snapImg.getType();
						GifSequenceWriter gifSequenceWriter = new GifSequenceWriter(gifOS, type, 3000, true);
						if (titleImg != null) {
							gifSequenceWriter.writeToSequence(titleImg);
						}
						if (snapImg != null) {
							gifSequenceWriter.writeToSequence(snapImg);
						}
						gifSequenceWriter.close();
						gifOS.close();
						hasImage = true;
					}
				}

				String animatedGifImage = hasImage ? gameImgFile.getPath() : null;
				list.games.add(Game.builder().name(desc.getDescription()).publisher(desc.getManufacturer()).genre(desc.getCategory())
						.image(animatedGifImage).releasedate(desc.getYear() == 0 ? null : String.valueOf(desc.getYear())).build());
			} catch (IOException ioe) {
				System.out.println("error while processing " + g.getName());
				ioe.printStackTrace();
			}
		}

		// save gamelist
		File xml = new File("gamelist.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(GameList.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(list, xml);
	}
}
