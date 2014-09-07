package pismy.mygames.db.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Represents a Rom playable on an emulator
 */
@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "rom",
// "emulator" }))
public class PlayableRom implements Serializable {
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	Rom rom;

	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	Emulator emulator;

	@Column
	int playCount;

	@Column
	long playElapseTime;

	@Column
	long lastPlayed;
	
	/**
	 * false by default
	 */
	@Column
	boolean notWorking;
	/**
	 * true if emulator AND rom are present
	 */
	@Column
	boolean available;

	public Rom getRom() {
		return rom;
	}

	public void setRom(Rom rom) {
		this.rom = rom;
	}

	public Emulator getEmulator() {
		return emulator;
	}

	public void setEmulator(Emulator emulator) {
		this.emulator = emulator;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public long getPlayElapseTime() {
		return playElapseTime;
	}

	public void setPlayElapseTime(long playElapseTime) {
		this.playElapseTime = playElapseTime;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isNotWorking() {
		return notWorking;
	}
	
	public void setNotWorking(boolean notWorking) {
		this.notWorking = notWorking;
	}

	public long getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(long lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

}
