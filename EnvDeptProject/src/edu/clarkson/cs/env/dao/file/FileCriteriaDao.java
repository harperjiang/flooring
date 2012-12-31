package edu.clarkson.cs.env.dao.file;

import edu.clarkson.cs.env.dao.CriteriaDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.frm.dao.AbstractFileDao;

public class FileCriteriaDao extends AbstractFileDao<Criteria,Criteria> implements
		CriteriaDao {

	@Override
	protected Criteria unwrap(Criteria o) {
		return o;
	}
	
	@Override
	protected Criteria wrap(Criteria e) {
		return e;
	}

}
