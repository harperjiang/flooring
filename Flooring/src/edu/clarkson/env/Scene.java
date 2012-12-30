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
	private String house;
	private String floor;
	private String loading;
	private String ventilation;
	private String particle;
	private String resuspension;

	
	@Override
	public String toString() {
		return "Scene [scheme=" + scheme + ", house=" + house + ", floor=" + floor + ", loading=" + loading 
				+ ", ventilation=" + ventilation + ", particle=" + particle + ", resuspension=" + resuspension +",]";
	}	
	
	public String toFile() {
		return scheme + "_" + house + "_" + floor + "_" + loading 
				+ "_" + ventilation + "_" + particle + "_" + resuspension +".csv";
	}
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
	

	/**
	 * @return the house
	 */
	public String getHouse() {
		return house;
	}

	/**
	 * @param house the house to set
	 */
	public void setHouse(String house) {
		this.house = house;
	}

	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the loading
	 */
	public String getLoading() {
		return loading;
	}

	/**
	 * @param loading the loading to set
	 */
	public void setLoading(String loading) {
		this.loading = loading;
	}

	/**
	 * @return the ventilation
	 */
	public String getVentilation() {
		return ventilation;
	}

	/**
	 * @param ventilation the ventilation to set
	 */
	public void setVentilation(String ventilation) {
		this.ventilation = ventilation;
	}

	/**
	 * @return the particle
	 */
	public String getParticle() {
		return particle;
	}

	/**
	 * @param particle the particle to set
	 */
	public void setParticle(String particle) {
		this.particle = particle;
	}

	/**
	 * @return the resuspension
	 */
	public String getResuspension() {
		return resuspension;
	}

	/**
	 * @param resuspension the resuspension to set
	 */
	public void setResuspension(String resuspension) {
		this.resuspension = resuspension;
	}

}
