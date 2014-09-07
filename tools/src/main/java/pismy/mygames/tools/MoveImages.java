package pismy.mygames.tools;

import java.io.File;

public class MoveImages {
	public static void main(String[] args) {
		move(new File("C:/CRHX7117/dev/Workspaces/Tools/gamz/browse/images/mamedb/snaps"), new File("C:/CRHX7117/dev/Workspaces/Tools/gamz/browse/images/mamedb8bits/snaps"));
		move(new File("C:/CRHX7117/dev/Workspaces/Tools/gamz/browse/images/mamedb/titles"), new File("C:/CRHX7117/dev/Workspaces/Tools/gamz/browse/images/mamedb8bits/titles"));
	}

	private static void move(File fromDir, File toDir) {
		for(File f : fromDir.listFiles()) {
			String name = f.getName();
			if(name.endsWith("-fs8.png")) {
				File to = new File(toDir, name.substring(0, name.length()-8)+".png");
				f.renameTo(to);
			}
		}
	}
}
