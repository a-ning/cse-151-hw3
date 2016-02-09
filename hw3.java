/* ----------------------------------------------------------------------------
 * Alicia Ning A10796746
 * Gabriel Gaddi A10851046
 * CSE 151 - Chaudhuri
 * 11 February 2016
 * --------------------------------------------------------------------------*/

import java.io.*;
import java.util.*;
import java.text.*;

public class hw3 {
	/* function to read files */
	public static LinkedList<Integer[]> read (File file) {
		LinkedList<Integer[]> res = new LinkedList<Integer[]>();
		String line = null;

		try {
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);

			/* while there are still lines to read */
			while((line = bReader.readLine()) != null) {
				/* split each line by spaces */
				String[] vals = line.split(" ");
				/* array to store values */
				Integer[] intVals = new Integer[vals.length];

				/* store values as integers */
				for(int i = 0; i < intVals.length; i++) {
					intVals[i] = Integer.parseInt(vals[i]);
				}

				/* add to LL */
				res.add(intVals);
			}

			/* close file */
			bReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Failed to open file");
		} catch(IOException ex) {
			System.out.println("Error reading file");
		}

		return res;
	}

	public static void main (String[] args) {

	}
}