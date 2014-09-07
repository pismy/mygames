package pismy.mygames.db.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Represents a Rom, independently of an emulator
 */
@Entity
public class Rom {
	/**
	 * the Rom ID (filename)
	 */
	@Id
	String id;
	@Column
	String screenShot;
	@Column
	String titleShot;
	/**
	 * the parent Rom
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	Rom cloneof;
	/**
	 * the Game title
	 */
	@Column(nullable = false)
	String title;
	/**
	 * the Game manufacturer
	 */
	@Column
	String manufacturer;
	/**
	 * the Game category
	 */
	@Column
	String category;
	/**
	 * the Game production year
	 */
	@Column
	int year;

	@OneToMany(mappedBy = "rom", fetch = FetchType.LAZY)
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Rom getCloneof() {
		return cloneof;
	}

	public void setCloneof(Rom cloneof) {
		this.cloneof = cloneof;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<PlayableRom> getGames() {
		return games;
	}

	public void setGames(List<PlayableRom> games) {
		this.games = games;
	}

}
