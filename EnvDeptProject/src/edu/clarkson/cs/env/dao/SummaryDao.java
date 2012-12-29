package edu.clarkson.cs.env.dao;

import com.kooobao.common.domain.dao.Dao;

import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Summary;

public interface SummaryDao extends Dao<Summary> {

	Summary findByCriteria(Criteria copy);

}
