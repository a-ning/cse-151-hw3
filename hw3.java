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

	static Node root;

	/* Node class */
	private class Node {
		public int label, count;
		public float threshold;
		public Node left, right;

		public Node () {
			label = -1;
			threshold = -1;
			left = null;
			right = null;
			count = 0;
		}
	}

	/* function to read files */
	public static LinkedList<float[]> read (File file) {
		LinkedList<float[]> res = new LinkedList<float[]>();
		String line = null;

		try {
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);

			/* while there are still lines to read */
			while((line = bReader.readLine()) != null) {
				/* split each line by spaces */
				String[] vals = line.split(" ");
				/* array to store values */
				float[] fVals = new float[vals.length];

				/* store values as integers */
				for(int i = 0; i < fVals.length; i++) {
					fVals[i] = Float.parseFloat(vals[i]);
				}

				/* add to LL */
				res.add(fVals);
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

	/* function to build the tree*/
	public static Node build (Node r, LinkedList<float[]> data) {
		boolean isPure = true;						/* flag - check if pure */
		float[] currNode, nextNode;					/* node variables */
		Iterator<float[]> dataIt = data.iterator();	/* iterator thru data */

		currNode = data.get(0);

		/* iterate through the data */
		for (int i = 0; i < data.size(); i++) {
			nextNode = data.get(i);
			/* check if labels are different - if so, node is impure */
			if (currNode[4] != nextNode[4]) {
				isPure = false;
			}
			currNode = nextNode;
		}

		/* return if pure */
		if (isPure == true) {
			r.label = (int)currNode[4];
			System.out.println("Label " + r.label);
			return r;
		}

		/* if impure: */
		// write function to sort the data

		return r;
	}

	/* main function */
	public static void main (String[] args) {
		LinkedList<float[]> trainData, testData;

		File trainFile = new File("hw3train.txt");
		trainData = read(trainFile);
		File testFile = new File("hw3test.txt");
		testData = read(testFile);

		root = new hw3().new Node();

		root = build(root, trainData);
	}
}