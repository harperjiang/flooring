package edu.clarkson.cs.env.dao.jpa;

import com.kooobao.common.domain.dao.AbstractJpaDao;
import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.domain.dao.cursor.JpaCursor;

import edu.clarkson.cs.env.dao.RecordDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;

public class JpaRecordDao extends AbstractJpaDao<Record> implements RecordDao {

	@Override
	public Cursor<Record> findAll(Criteria criteria) {
		return new JpaCursor<Record>(getEntityManager().createQuery(
				"select r from Record r where r.criteria = :criteria")
				.setParameter("criteria", criteria));
	}

}
