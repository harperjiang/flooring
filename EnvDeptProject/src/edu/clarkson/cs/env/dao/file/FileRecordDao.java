package edu.clarkson.cs.env.dao.file;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.domain.entity.SimpleEntity;

import edu.clarkson.cs.env.dao.RecordDao;
import edu.clarkson.cs.env.dao.file.FileRecordDao.RecordStub;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;
import edu.clarkson.cs.frm.dao.AbstractFileDao;

public class FileRecordDao extends AbstractFileDao<Record, RecordStub>
		implements RecordDao {

	public static final class RecordStub extends SimpleEntity {
		private BigDecimal data;
		private long criteriaOid;

		public BigDecimal getData() {
			return data;
		}

		public void setData(BigDecimal data) {
			this.data = data;
		}

		public long getCriteriaOid() {
			return criteriaOid;
		}

		public void setCriteriaOid(long criteriaOid) {
			this.criteriaOid = criteriaOid;
		}
	}

	@Override
	protected RecordStub wrap(Record e) {
		RecordStub rs = new RecordStub();
		rs.setOid(e.getOid());
		rs.setCriteriaOid(e.getCriteria().getOid());
		rs.setCreateTime(e.getCreateTime());
		rs.setData(e.getData());
		return rs;
	}

	@Override
	protected Record unwrap(RecordStub o) {
		Record r = new Record();
		r.setCreateTime(o.getCreateTime());
		r.setOid(o.getOid());
		r.setData(o.getData());
		r.setCriteria((Criteria) daos.get(Criteria.class).find(
				o.getCriteriaOid()));
		return r;
	}

	@Override
	public Cursor<Record> findAll(Criteria criteria) {
		List<Record> records = new ArrayList<Record>();
		lock.readLock().lock();
		try {
			for (RecordStub record : storage.values()) {
				if (criteria.getOid() == record.getCriteriaOid())
					records.add(unwrap(record));
			}
		} finally {
			lock.readLock().unlock();
		}
		return new ListCursor(records);
	}

}
