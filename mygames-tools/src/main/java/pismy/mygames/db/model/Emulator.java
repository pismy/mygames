package pismy.mygames.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Emulator {
	/**
	 * the Emulator ID
	 */
	@Id
	String id;
	/**
	 * the Emulator title
	 */
	@Column(nullable = false)
	String title;
	@Column(nullable = false)
	String iconImg;
	@Column(nullable = false)
	String runCommand;
	@Column(nullable = false)
	String romsDir;
	@OneToMany(mappedBy = "emulator", fetch = FetchType.LAZY)
	List<PlayableRom> games;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIconImg() {
		return iconImg;
	}

	public void setIconImg(String iconImg) {
		this.iconImg = iconImg;
	}

	public String getRunCommand() {
		return runCommand;
	}

	public void setRunCommand(String runCommand) {
		this.runCommand = runCommand;
	}

	public String getRomsDir() {
		return romsDir;
	}

	public void setRomsDir(String romsDir) {
		this.romsDir = romsDir;
	}

	public List<PlayableRom> getGames() {
		return games;
	}

	public void setGames(List<PlayableRom> games) {
		this.games = games;
	}

}
