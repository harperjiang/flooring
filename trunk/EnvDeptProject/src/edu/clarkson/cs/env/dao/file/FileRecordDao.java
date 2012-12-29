package edu.clarkson.cs.env.dao.file;

import java.util.ArrayList;
import java.util.List;

import com.kooobao.common.domain.dao.Cursor;

import edu.clarkson.cs.env.dao.RecordDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;
import edu.clarkson.cs.frm.dao.AbstractFileDao;

public class FileRecordDao extends AbstractFileDao<Record> implements RecordDao {

	@Override
	public Cursor<Record> findAll(Criteria criteria) {
		List<Record> records = new ArrayList<Record>();
		lock.readLock().lock();
		try {
			for (Record record : storage.values()) {
				if (criteria.equals(record.getCriteria()))
					records.add(record);
			}
		} finally {
			lock.readLock().unlock();
		}
		return new ListCursor(records);
	}

}
