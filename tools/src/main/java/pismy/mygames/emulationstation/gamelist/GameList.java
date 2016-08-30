package pismy.mygames.emulationstation.gamelist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gameList")
public class GameList {
	@XmlElement(name="game")
	public List<Game> games = new ArrayList<Game>();
}
