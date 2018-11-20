package hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ImageDatabase {
	public static void main(String[] args) throws Exception {
		// create <Rn> <Gn> <Bn> <URL_FILENAME> <DB_FILENAME>
		// query <Q_URL> <DB_FILENAME> <RESPONSE_FILENAME> <K>
		try {
			if (args.length < 5)
				throw new Exception(" Input command line is wrong \n");

			int Rn, Gn, Bn, k;
			File URL_FILENAME, DB_FILENAME, RESPONSE_FILENAME;

			String Q_URL;
			String result;

			if (args[0].equals("create")) {

				Rn = Integer.valueOf(args[1]);
				Gn = Integer.valueOf(args[2]);
				Bn = Integer.valueOf(args[3]);
				URL_FILENAME = new File(args[4]);
				DB_FILENAME = new File(args[5]);
				create c = new create(URL_FILENAME, DB_FILENAME, Rn, Gn, Bn);
				c.read();
				c.write();
			} else if (args[0].equals("query")) {
				Q_URL = args[1];
				DB_FILENAME = new File(args[2]);
				RESPONSE_FILENAME = new File(args[3]);
				k = Integer.valueOf(args[4]);
				query q = new query(Q_URL, DB_FILENAME, RESPONSE_FILENAME, k);
				q.read();
			} else {
				throw new Exception(" Input command line is wrong \n");
			}

		} catch (Exception e) {

			System.out.println(e);
		}
	}

}
