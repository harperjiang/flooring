package edu.clarkson.cs.env.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.persistence.NoResultException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.web.fileupload.FileBean;

import edu.clarkson.cs.env.dao.CriteriaDao;
import edu.clarkson.cs.env.dao.RecordDao;
import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.entity.Record;

public class DefaultFileService implements FileService {

	@Override
	public void uploadFile(FileBean file) {
		// Remove all records
		getCriteriaDao().removeAll();
		getRecordDao().removeAll();

		// Open Files
		Workbook workbook = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file.getPath());
			if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
					.equals(file.getContentType())) {
				workbook = new XSSFWorkbook(fis);
			} else if ("application/xls".equals(file.getContentType())) {
				workbook = new HSSFWorkbook(fis);
			} else {
				throw new IllegalArgumentException("Unrecognized File:"
						+ file.getContentType());
			}
			Sheet sheet = workbook.getSheetAt(0);

			Criteria[] items = new Criteria[18];
			int[] index = new int[] { 1, 3, 4, 5, 7, 8, 9, 11, 12, 13, 15, 16,
					17, 19, 20, 21, 23, 24 };
			initCriteria(items);
			for (int i = 4; i < 494; i++) {
				Row row = sheet.getRow(i);
				for (int j = 0; j < 18; j++) {
					Criteria criteria = items[j];
					Cell cell = row.getCell(index[j]);
					Record record = new Record();
					record.setCriteria(criteria);
					if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						record.setData(new BigDecimal(cell
								.getNumericCellValue()).setScale(5,
								BigDecimal.ROUND_HALF_UP));
					} else {
						record.setData(new BigDecimal(cell.getStringCellValue()));
					}
					getRecordDao().store(record);
				}
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			throw new RuntimeException(e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
	}

	protected void initCriteria(Criteria[] items) {
		for (int i = 0; i < items.length; i++) {
			items[i] = new Criteria();
			items[i].setProbabilitySuspension(2);
			items[i].setHouseType(1);
			items[i].setVentScheme(1);
			// Where to get the floor loading
			items[i].setFloorLoading(1);
		}
		for (int i = 0; i < items.length; i += 3) {
			items[i].setVentLevel(1);
			items[i + 1].setVentLevel(2);
			items[i + 2].setVentLevel(3);
		}
		for (int i = 0; i < 9; i++) {
			items[i].setPartGran(1);
		}
		for (int i = 9; i < 18; i++) {
			items[i].setPartGran(2);
		}
		for (int i = 0; i < 3; i++) {
			items[i].setFloorType(3);
			items[i + 12].setFloorType(3);
		}
		for (int i = 3; i < 6; i++) {
			items[i].setFloorType(2);
			items[i + 6].setFloorType(2);
		}
		for (int i = 6; i < 9; i++) {
			items[i].setFloorType(1);
			items[i + 9].setFloorType(1);
		}
		for (int i = 0; i < items.length; i++) {
			try {
				Criteria save = getCriteriaDao().find(items[i]);
				items[i] = save;
			} catch (NoResultException e) {
				items[i] = getCriteriaDao().store(items[i]);
			}
		}
	}

	static final String[] ventSchemeText = { "Natural", "Air Conditioned" };
	static final String[] houseTypeText = { "Single Detached",
			"Apartment Building" };
	static final String[] floorTypeText = { "Hardwood", "Carpeting",
			"HD Carpeting" };
	static final String[] floorLoadingText = { "Low", "Medium", "High" };
	static final String[] ventLevelText = { "Low", "Medium", "High" };
	static final String[] partGranText = { "Fine", "Coarse" };
	static final String[] probSusText = { "CR=0", "CR≠0", "CR=∅" };

	static final String formatCriteria(Criteria c) {
		StringBuilder result = new StringBuilder();
		result.append("Ventialization Scheme:")
				.append(ventSchemeText[c.getVentScheme() - 1]).append(",");
		result.append("House Type:")
				.append(houseTypeText[c.getHouseType() - 1]).append(",");
		result.append("Floor Type:")
				.append(floorTypeText[c.getFloorType() - 1]).append(",");
		result.append("Floor Loading:")
				.append(floorLoadingText[c.getFloorLoading() - 1]).append(",");
		result.append("Ventialization Level:")
				.append(ventLevelText[c.getVentLevel() - 1]).append(",");
		result.append("Particle Granularity:")
				.append(partGranText[c.getPartGran() - 1]).append(",");
		result.append("Probability of Suspension:").append(
				probSusText[c.getProbabilitySuspension() - 1]);
		return result.toString();
	}

	@Override
	public void downloadRecords(String[] cids, OutputStream output) {

		// Create XLS files
		Workbook wb = new HSSFWorkbook();
		int sheetCounter = 0;
		for (String cid : cids) {
			Sheet sheet = wb.createSheet("Data Set " + ((sheetCounter++) + 1));
			Criteria criteria = getCriteriaDao().find(Integer.valueOf(cid));
			if (criteria == null)
				continue;
			Cursor<Record> records = getRecordDao().findAll(criteria);

			// The first row displays condition
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
			cell.setCellValue(formatCriteria(criteria));
			int counter = 1;
			while (records.hasNext()) {
				Record record = records.next();
				row = sheet.createRow(counter++);
				cell = row.createCell(0, Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(record.getData().doubleValue());
			}
		}
		try {
			wb.write(output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RecordDao recordDao;

	private CriteriaDao criteriaDao;

	public RecordDao getRecordDao() {
		return recordDao;
	}

	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

	public CriteriaDao getCriteriaDao() {
		return criteriaDao;
	}

	public void setCriteriaDao(CriteriaDao criteriaDao) {
		this.criteriaDao = criteriaDao;
	}

}
