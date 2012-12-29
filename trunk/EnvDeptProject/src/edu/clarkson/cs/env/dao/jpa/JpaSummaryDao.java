package edu.clarkson.cs.env.dao.jpa;

import javax.persistence.NoResultException;

import com.kooobao.common.domain.dao.AbstractJpaDao;

import edu.clarkson.cs.env.dao.SummaryDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Summary;

public class JpaSummaryDao extends AbstractJpaDao<Summary> implements
		SummaryDao {

	@Override
	public Summary findByCriteria(Criteria copy) {
		try {
			return getEntityManager()
					.createQuery(
							"select s from Summary s where s.criteria = :criteria",
							Summary.class).setParameter("criteria", copy)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
