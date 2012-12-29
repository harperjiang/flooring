package edu.clarkson.cs.env.service;

import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Summary;

public interface SummaryService {

	public Summary read(Criteria criteria);
	
	public void updateSummary();

}
