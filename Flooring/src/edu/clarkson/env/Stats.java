/**
 * 
 */
package edu.clarkson.env;

/**
 * @author Fighter
 *
 */
public class Stats {
	private double min;
	private double p25;
	private double medium;
	private double p75;
	private double max;
	
	
	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}
	/**
	 * @return the p25
	 */
	public double getP25() {
		return p25;
	}
	/**
	 * @param p25 the p25 to set
	 */
	public void setP25(double p25) {
		this.p25 = p25;
	}
	/**
	 * @return the medium
	 */
	public double getMedium() {
		return medium;
	}
	/**
	 * @param medium the medium to set
	 */
	public void setMedium(double medium) {
		this.medium = medium;
	}
	/**
	 * @return the p75
	 */
	public double getP75() {
		return p75;
	}
	/**
	 * @param p75 the p75 to set
	 */
	public void setP75(double p75) {
		this.p75 = p75;
	}
	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "Statsinfo [min=" + min + ", 25%=" + p25 
									   + ", medium=" + medium + "," 
				                       + ", 75%=" + p75 + "," 
									   + ", max=" + max + "," 
				       + "]";
	}
}
