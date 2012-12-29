package edu.clarkson.cs.env.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooobao.common.domain.entity.SimpleEntity;

@Entity
@Table(name = "env_criteria")
public class Criteria extends SimpleEntity {

	@Column(name = "vent_scheme")
	private int ventScheme;// {Natural,AirConditioned};

	@Column(name = "house_type")
	private int houseType;// {SingleDetached,ApartmentBuilding};

	@Column(name = "floor_type")
	private int floorType;// {Hardwood,Carpeting,HD Carpeting};

	@Column(name = "floor_loading")
	private int floorLoading; // {low,medium,high}

	@Column(name = "vent_level")
	private int ventLevel;// {low,medium,high};

	@Column(name = "part_gran")
	private int partGran;// {fine,corrase};

	@Column(name = "prob_sus")
	private int probabilitySuspension;// {zero,nonZero,inf};

	public boolean notEmpty() {
		return ventScheme * houseType * floorType * ventLevel * partGran
				* floorLoading * probabilitySuspension != 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Criteria)) {
			return super.equals(obj);
		}
		Criteria another = (Criteria) obj;
		return equals(this.ventLevel, another.ventLevel)
				&& equals(this.houseType, another.houseType)
				&& equals(this.floorType, another.floorType)
				&& equals(ventScheme, another.ventScheme)
				&& equals(this.partGran, another.partGran)
				&& equals(this.probabilitySuspension,
						another.probabilitySuspension);
	}

	public int getVentScheme() {
		return ventScheme;
	}

	public void setVentScheme(int ventScheme) {
		this.ventScheme = ventScheme;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public int getFloorType() {
		return floorType;
	}

	public void setFloorType(int floorType) {
		this.floorType = floorType;
	}

	public int getVentLevel() {
		return ventLevel;
	}

	public void setVentLevel(int ventLevel) {
		this.ventLevel = ventLevel;
	}

	public int getPartGran() {
		return partGran;
	}

	public void setPartGran(int partGran) {
		this.partGran = partGran;
	}

	public int getProbabilitySuspension() {
		return probabilitySuspension;
	}

	public void setProbabilitySuspension(int probabilitySuspension) {
		this.probabilitySuspension = probabilitySuspension;
	}

	public int getFloorLoading() {
		return floorLoading;
	}

	public void setFloorLoading(int floorLoading) {
		this.floorLoading = floorLoading;
	}

}
