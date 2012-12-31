package edu.clarkson.cs.env.service;

import java.io.OutputStream;

import com.kooobao.common.web.fileupload.FileBean;

public interface FileService {

	public void uploadFile(FileBean file);

	public void downloadRecords(String[] criteriaIds, OutputStream output);

}
