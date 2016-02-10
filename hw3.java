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

	/* function to sort the data based on feature */
	public static LinkedList<float[]> sort (LinkedList<float[]> data, 
		int feature) {
		int halfway;											/* mid pt */
		float[] halfpt, currpt;
		LinkedList<float[]> left = new LinkedList<float[]>();	/* < list */
		LinkedList<float[]> right = new LinkedList<float[]>();	/* > list */
		LinkedList<float[]> combined = new LinkedList<float[]>();

		if (data.size() <= 1) { return data; }

		/* get pivot pt */
		halfway = data.size() / 2;
		halfpt = data.get(halfway);
		data.remove(halfway);

		/* loop through the data and sort by comparing to mid pt */
		for (int i = 0; i < data.size(); i++) {
			currpt = data.get(i);
			if (currpt[feature] <= halfpt[feature]) {
				left.push(currpt);
			} else {
				right.push(currpt);
			}
		}

		/* proceed to sort recursively */
		left = sort (left, feature);
		right = sort (right, feature);

		/* put the lists together */
		for (int i = 0; i < left.size(); i++) {
			combined.push(left.get(i));
		}

		combined.push(halfpt);

		for (int i = 0; i < right.size(); i++) {
			combined.push(right.get(i));
		}

		return combined;
	}

	/* function to calculate entropy */
	public static double calcEnt (int amt1A, int amt2A, int amt3A, 
		int amt1B, int amt2B, int amt3B, double size) {
		double pr1, pr2, pr3;
		double log1, log2, log3;
		double ent;

		pr1 = (amt1A + amt1B) / size;
		pr2 = (amt2A + amt2B) / size;
		pr3 = (amt3A + amt3B) / size;

		log1 = 0;
		log2 = 0;
		log3 = 0;

		if (pr1 != 0) { log1 = Math.log(pr1); }
		if (pr2 != 0) { log2 = Math.log(pr2); }
		if (pr3 != 0) { log3 = Math.log(pr3); }

		ent = -(pr1 * log1) - (pr2 * log2) - (pr3 * log3);

		return ent;
	}

	/* function to calculate conditional entropy */
	public static double calcEntCond (int amt1A, int amt2A, int amt3A, 
		int amt1B, int amt2B, int amt3B, double size) {
		double totalPrA, totalPrB;
		double pr1A, pr2A, pr3A,
			   pr1B, pr2B, pr3B;
		double log1A, log2A, log3A,	/* logs */ 
			   log1B, log2B, log3B;
		double entA, entB, entCond;	/* entropy */

		/* total probabilities */
		totalPrA = (amt1A + amt2A + amt3A) / size;
		totalPrB = (amt1B + amt2B + amt3B) / size;

		/* conditional probabilities */
		pr1A = (amt1A / size) / totalPrA;
		pr2A = (amt2A / size) / totalPrA;
		pr3A = (amt3A / size) / totalPrA;
		pr1B = (amt1B / size) / totalPrB;
		pr2B = (amt2B / size) / totalPrB;
		pr3B = (amt3B / size) / totalPrB;

		/* calculate logs */
		log1A = 0;
		log2A = 0;
		log3A = 0;
		log1B = 0;
		log2B = 0;
		log3B = 0;

		if (pr1A != 0) { log1A = Math.log(pr1A); }
		if (pr2A != 0) { log2A = Math.log(pr2A); }
		if (pr3A != 0) { log3A = Math.log(pr3A); }
		if (pr1B != 0) { log1B = Math.log(pr1B); }
		if (pr2B != 0) { log2B = Math.log(pr2B); }
		if (pr3B != 0) { log3B = Math.log(pr3B); }

		entA = -(pr1A * log1A) - (pr2A * log2A) - (pr3A * log3A);
		entB = -(pr1B * log1B) - (pr2B * log2B) - (pr3B * log3B);

		entCond = (totalPrA * entA) + (totalPrB * entB);

		return entCond;
	}

	/* function to find best information gap */
	public static float[] findIG (LinkedList<float[]> data, int feature) {
		float[] res;				/* result: best ig and its index */
		int currLabel;
		int amt1A, amt2A, amt3A,	/* counts */
			amt1B, amt2B, amt3B;
		double ent, entCond;		/* entropy */
		double ig, bestIG;			/* information gain */
		int ind = 0;				/* index of best ig */

		/* set bestIG to negative for now */
		bestIG = -1;

		/* take pairs of data points */
		for (int i = 0; i < data.size() - 2; i++) {
			amt1A = 0;
			amt2A = 0;
			amt3A = 0;
			for (int j = 0; j <= i; j++) {
				currLabel = (int)data.get(j)[4];
				if (currLabel == 1) { amt1A++; }
				else if (currLabel == 2) { amt2A++; }
				else if (currLabel == 3) { amt3A++; }
			}

			amt1B = 0;
			amt2B = 0;
			amt3B = 0;
			for (int j = 0; j < data.size() - 1; j++) {
				currLabel = (int)data.get(j)[4];
				if (currLabel == 1) { amt1B++; }
				else if (currLabel == 2) { amt2B++; }
				else if (currLabel == 3) { amt3B++; }
			}

			double size = (double)data.size();

			/* calculate entropy and conditional entropy */
			ent = calcEnt (amt1A, amt2A, amt3A, 
						   amt1B, amt2B, amt3B, size);
			entCond = calcEntCond (amt1A, amt2A, amt3A, 
								   amt1B, amt2B, amt3B, size);

			/* calculate information gain */
			ig = ent - entCond;

			if (ig > bestIG) {
				bestIG = ig;
				ind = i;
			}
		}

		res = new float[] {(float)bestIG, (float)ind};
		return res;
	}

	/* function to build the tree*/
	public static Node build (Node r, LinkedList<float[]> data) {
		boolean isPure = true;						/* flag - check if pure */
		float[] currNode, nextNode;					/* node variables */
		Iterator<float[]> dataIt = data.iterator();	/* iterator thru data */

		LinkedList<float[]> attr0, attr1, attr2, attr3;
		float[] ig0, ig1, ig2, ig3;

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

		/* sort by features */
		attr0 = sort (data, 0);
		attr1 = sort (data, 1);
		attr2 = sort (data, 2);
		attr3 = sort (data, 3);

		/* calc best IG for each x */
		ig0 = findIG (attr0, 0);
		ig1 = findIG (attr1, 1);
		ig2 = findIG (attr2, 2);
		ig3 = findIG (attr3, 3);

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