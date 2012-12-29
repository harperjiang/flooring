package edu.clarkson.cs.env.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kooobao.common.domain.entity.SimpleEntity;

@Entity
@Table(name = "env_summary")
public class Summary extends SimpleEntity {

	@OneToOne
	@JoinColumn(name = "criteria_id", referencedColumnName = "obj_id")
	private Criteria criteria;

	@Column(name = "data_max")
	private BigDecimal max;

	@Column(name = "data_val75")
	private BigDecimal val75;

	@Column(name = "data_median")
	private BigDecimal median;

	@Column(name = "data_val25")
	private BigDecimal val25;

	@Column(name = "data_min")
	private BigDecimal min;

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getVal75() {
		return val75;
	}

	public void setVal75(BigDecimal val75) {
		this.val75 = val75;
	}

	public BigDecimal getMedian() {
		return median;
	}

	public void setMedian(BigDecimal median) {
		this.median = median;
	}

	public BigDecimal getVal25() {
		return val25;
	}

	public void setVal25(BigDecimal val25) {
		this.val25 = val25;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

}
