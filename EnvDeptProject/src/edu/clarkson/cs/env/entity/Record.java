package edu.clarkson.cs.env.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kooobao.common.domain.entity.SimpleEntity;

@Entity
@Table(name = "env_record")
public class Record extends SimpleEntity {

	@ManyToOne
	@JoinColumn(name = "criteria_id", referencedColumnName = "obj_id")
	private Criteria criteria;

	@Column(name = "data")
	private BigDecimal data;

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

}
