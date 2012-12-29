/**
 * 
 */
package edu.clarkson.env;

/**
 * @author Fighter
 *
 */
public class Scene {
	private String scheme;

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	@Override
	public String toString() {
		return "Scene [scheme=" + scheme + ",]";
	}

}
