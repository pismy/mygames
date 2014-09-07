package pismy.mygames.dat;

import java.util.List;

public interface IGame {

	String getName();
	
	String getDescription();
	
	int getYear();
	
	String getCloneOf();

	List<? extends IRom> getRoms();

	String getCategory();

	String getManufacturer();

}
