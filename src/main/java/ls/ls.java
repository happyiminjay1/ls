package ls;

import java.io.IOException;

public class ls {
	
		public static void main(String[] args) throws IOException {
			Lscommand lscommand = new Lscommand();
			lscommand.run(args);
		}

}