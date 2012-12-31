package edu.clarkson.cs.env.dao.file;

import java.math.BigDecimal;

import javax.persistence.NoResultException;

import com.kooobao.common.domain.entity.SimpleEntity;

import edu.clarkson.cs.env.dao.SummaryDao;
import edu.clarkson.cs.env.dao.file.FileSummaryDao.SummaryStub;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Summary;
import edu.clarkson.cs.frm.dao.AbstractFileDao;

public class FileSummaryDao extends AbstractFileDao<Summary, SummaryStub>
		implements SummaryDao {

	public static class SummaryStub extends SimpleEntity {
		private BigDecimal max;
		private BigDecimal min;
		private BigDecimal val75;
		private BigDecimal val25;
		private BigDecimal median;
		private long criteriaOid;

		public BigDecimal getMax() {
			return max;
		}

		public void setMax(BigDecimal max) {
			this.max = max;
		}

		public BigDecimal getMin() {
			return min;
		}

		public void setMin(BigDecimal min) {
			this.min = min;
		}

		public BigDecimal getVal75() {
			return val75;
		}

		public void setVal75(BigDecimal val75) {
			this.val75 = val75;
		}

		public BigDecimal getVal25() {
			return val25;
		}

		public void setVal25(BigDecimal val25) {
			this.val25 = val25;
		}

		public BigDecimal getMedian() {
			return median;
		}

		public void setMedian(BigDecimal median) {
			this.median = median;
		}

		public long getCriteriaOid() {
			return criteriaOid;
		}

		public void setCriteriaOid(long criteriaOid) {
			this.criteriaOid = criteriaOid;
		}

	}

	@Override
	protected SummaryStub wrap(Summary e) {
		SummaryStub ss = new SummaryStub();
		ss.setMax(e.getMax());
		ss.setMin(e.getMin());
		ss.setMedian(e.getMedian());
		ss.setVal25(e.getVal25());
		ss.setVal75(e.getVal75());
		ss.setCriteriaOid(e.getCriteria().getOid());
		ss.setCreateTime(e.getCreateTime());
		return ss;
	}

	@Override
	protected Summary unwrap(SummaryStub o) {
		Summary su = new Summary();
		su.setCreateTime(o.getCreateTime());
		su.setMax(o.getMax());
		su.setMin(o.getMin());
		su.setMedian(o.getMedian());
		su.setVal25(o.getVal25());
		su.setVal75(o.getVal75());
		su.setCreateTime(o.getCreateTime());
		su.setCriteria((Criteria) daos.get(Criteria.class).find(
				o.getCriteriaOid()));
		return su;
	}

	@Override
	public Summary findByCriteria(Criteria copy) {
		for(SummaryStub ss: storage.values()) {
			if(ss.getCriteriaOid() == copy.getOid())
				return unwrap(ss);
		}
		throw new NoResultException();
	}

}
