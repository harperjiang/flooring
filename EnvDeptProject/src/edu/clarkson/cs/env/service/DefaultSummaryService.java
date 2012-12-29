package edu.clarkson.cs.env.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.Validate;

import com.kooobao.common.domain.dao.Cursor;

import edu.clarkson.cs.env.dao.CriteriaDao;
import edu.clarkson.cs.env.dao.RecordDao;
import edu.clarkson.cs.env.dao.SummaryDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;
import edu.clarkson.cs.env.entity.Summary;

public class DefaultSummaryService implements SummaryService {

	@Override
	public Summary read(Criteria criteria) {
		Validate.isTrue(criteria.notEmpty());
		try {
			Criteria copy = getCriteriaDao().find(criteria);
			return getSummaryDao().findByCriteria(copy);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void updateSummary() {
		getSummaryDao().removeAll();

		Cursor<Criteria> criteria = getCriteriaDao().findAll();
		while (criteria.hasNext()) {
			Criteria c = criteria.next();
			Cursor<Record> records = getRecordDao().findAll(c);
			Summary summary = generateSummary(records);
			summary.setCriteria(c);
			getSummaryDao().store(summary);
		}
	}

	private Summary generateSummary(Cursor<Record> records) {
		List<Record> recordList = new ArrayList<Record>();
		while (records.hasNext())
			recordList.add(records.next());
		Record[] array = new Record[recordList.size()];
		recordList.toArray(array);
		Arrays.sort(array, new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				return o1.getData().compareTo(o2.getData());
			}
		});

		Summary summary = new Summary();
		summary.setMax(array[array.length - 1].getData());
		summary.setMin(array[0].getData());
		summary.setMedian(array[array.length / 2].getData());
		summary.setVal75(array[(int) (array.length * 0.75)].getData());
		summary.setVal25(array[(int) (array.length * 0.25)].getData());

		return summary;
	}

	private SummaryDao summaryDao;

	private CriteriaDao criteriaDao;

	private RecordDao recordDao;

	public RecordDao getRecordDao() {
		return recordDao;
	}

	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

	public SummaryDao getSummaryDao() {
		return summaryDao;
	}

	public void setSummaryDao(SummaryDao summaryDao) {
		this.summaryDao = summaryDao;
	}

	public CriteriaDao getCriteriaDao() {
		return criteriaDao;
	}

	public void setCriteriaDao(CriteriaDao criteriaDao) {
		this.criteriaDao = criteriaDao;
	}
}
