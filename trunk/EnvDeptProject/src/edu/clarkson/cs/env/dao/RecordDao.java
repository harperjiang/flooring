package edu.clarkson.cs.env.dao;

import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.domain.dao.Dao;

import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;

public interface RecordDao extends Dao<Record> {

	Cursor<Record> findAll(Criteria criteria);

}
