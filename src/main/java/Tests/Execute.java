package Tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import Converter.Promelizer;
import Converter.Sequentializer;
import globaltype.G;
import picalculus.P;
import sequentialglobalprocess.S;
import sequentialglobalprocess.STermination;
import util.FileExport;
import util.Tuple;

/**
 * @author Tim Pollandt
 *
 */
public class Execute {

	enum Example {
		Auctioneer, Needham
	};

	// Set false for time measuring.
	public static boolean DebugInfo = true;
	static int counter = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int startSize = 1;
		int stopSize = 1;
		int stepSize = 1;
		int maxAttempts = 1;
		// Choose example.
		final Example currentExample = Example.valueOf(args[0]);
		if (currentExample == Example.Needham) {
			// Choose debug output vs. time measure.
			DebugInfo = Boolean.parseBoolean(args[1]);
			startSize = Integer.parseInt(args[2]);
			if (!DebugInfo) {
				// Loop properties for time measuring.
				stopSize = Integer.parseInt(args[3]);
				stepSize = Integer.parseInt(args[4]);
				maxAttempts = Integer.parseInt(args[5]);
			}
		}

		Sequentializer converter = new Sequentializer();

		S output = new STermination();
		switch (currentExample) {
		// Auctioneer example. Simple execution including debug and final outputs (console + files).
		case Auctioneer:
			Auctioneer auctioneer = new Auctioneer();
			output = converter.convert(new HashSet<P>() {
				private static final long serialVersionUID = 1L;
				{
					add(auctioneer.AuctioneerPi);
				}
			}, auctioneer.AuctioneerGlobal);
			String tex = auctioneer.AuctioneerGlobal.toLatex() + "\n\n" + auctioneer.AuctioneerPi.toLatex() + "\n\n"
					+ output.toLatex();
			try {
				FileExport.writeNextFile(false, "data/Auctioneer", "tex", tex);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		// Needham-Schroeder public-key example.
		case Needham:
			Needham nh = new Needham();
			if (DebugInfo) {
				Tuple<G, Set<P>> input = nh.needhamFactory(startSize);
				output = converter.convert(input.second(), input.first());
			} else {
				String timeLog = "";// "process number;generation;sequentialization;promela;steps;lines\n";
				Long[] times = new Long[3];
				long time;
				String code = "";
				nh.needhamFactory(1); // Generating data once as first time is slower.
				for (int i = startSize; i <= stopSize; i += stepSize) {
					for (int attempts = 0; attempts < maxAttempts; attempts++) {
						counter = 0;

						// Generating test data.
						time = System.nanoTime();
						Tuple<G, Set<P>> input = nh.needhamFactory(i);
						times[0] = System.nanoTime() - time;

						// Converting to SGP.
						time = System.nanoTime();
						output = converter.convert(input.second(), input.first());
						times[1] = System.nanoTime() - time;

						// Converting to Promela.
						time = System.nanoTime();
						code = new Promelizer().convert(output);
						times[2] = System.nanoTime() - time;

						timeLog += Integer.toString(i) + ";" + Long.toString(times[0]) + ";" + Long.toString(times[1])
								+ ";" + Long.toString(times[2]) + ";" + Integer.toString(counter) + ";"
								+ Integer.toString(code.split("\r\n|\r|\n").length) + "\n";
					}
					System.out.println(timeLog);
					try {
						FileExport.writeNextFile(false, String.format("results/Needham_%05d", i), "pml", code);
						FileExport.appendToFile(false, "results/time", "csv", timeLog);
					} catch (IOException e) {
						e.printStackTrace();
					}
					timeLog = "";
				}
				System.out.println("Process finished.");
			}
			break;
		}
		if (DebugInfo) {
			System.out.println(output);
			System.out.println(output.toLatex());

			String code = new Promelizer().convert(output);

			System.out.println("\n\nResulting code:\n\n" + code);
		}
	}

	public static void increaseCounter() {
		counter++;
	}

}
