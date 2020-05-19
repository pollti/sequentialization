package globaltype;

/**
 * @author Tim Pollandt
 * 
 *         (G1, G2) Two composed parts.
 *
 */
public class GParallel extends G {

	/**
	 * First part.
	 */
	private G g1;

	/**
	 * Second part.
	 */
	private G g2;

	/**
	 * Constructor.
	 * 
	 * @param part1 first part
	 * @param part2 second part
	 */
	public GParallel(G part1, G part2) {
		this.g1 = part1;
		this.g2 = part2;
		subtypes.add(this.g1);
		subtypes.add(this.g2);
	}

	/**
	 * @return first type sequence G1
	 */
	public G getFirstG() {
		return g1;
	}

	/**
	 * @return second type sequence G2
	 */
	public G getSecondG() {
		return g2;
	}

	@Override
	public String toString() {
		return g1.toString() + "," + g2.toString();
	}

	@Override
	public String toLatex() {
		return "\\GPar{" + g1.toLatex() + "}{" + g2.toLatex() + "}";
	}
}