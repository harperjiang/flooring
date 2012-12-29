package edu.clarkson.cs.env.service;

import java.io.OutputStream;

import com.kooobao.common.web.fileupload.FileBean;

import edu.clarkson.cs.env.entity.Criteria;

public interface FileService {

	public void uploadFile(FileBean file);

	public void downloadRecords(Criteria criteria, OutputStream output);

}
