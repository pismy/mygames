package pismy.mygames.emulationstation.gamelist;

import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
	/**
	 * the ROM path
	 */
	@XmlElement
	public String path;
	/**
	 * the displayed name for the game.
	 */
	@XmlElement
	public String name;
	/**
	 * a description of the game. Longer descriptions will automatically scroll, so don't worry about size.
	 */
	@XmlElement
	public String desc;
	/**
	 * the path to an image to display for the game (like box art or a screenshot).
	 */
	@XmlElement
	public String image;
	/**
	 * the path to a smaller image, displayed in image lists like the grid view. Should be small to ensure quick loading. Currently not used.
	 */
	@XmlElement
	public String thumbnail;
	/**
	 * the rating for the game, expressed as a floating point number between 0 and 1. Arbitrary values are fine (ES can display half-stars, quarter-stars, etc).
	 */
	public Float rating;
	/**
	 * the date the game was released. Displayed as date only, time is ignored.
	 */
	public String releasedate;
	/**
	 * the developer for the game.
	 */
	public String developer;
	/**
	 * the publisher for the game.
	 */
	public String publisher;
	/**
	 * 
	 */
	public String genre;
	/**
	 * the number of players the game supports.
	 */
	public Integer players;
	/**
	 * the number of times this game has been played
	 */
	public Integer playcount;
	/**
	 * the last date and time this game was played.
	 */
	public String lastplayed;
}
