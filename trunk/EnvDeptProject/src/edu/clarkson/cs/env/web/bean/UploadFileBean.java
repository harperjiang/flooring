package edu.clarkson.cs.env.web.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.LoggerFactory;

import com.kooobao.common.web.bean.AbstractBean;
import com.kooobao.common.web.fileupload.FileBean;
import com.kooobao.common.web.fileupload.MultipartRequestWrapper;

import edu.clarkson.cs.env.service.FileService;
import edu.clarkson.cs.env.service.SummaryService;

@ManagedBean(name = "uploadFileBean")
@SessionScoped
public class UploadFileBean extends AbstractBean {

	@ManagedProperty("#{fileService}")
	FileService fileService;

	@ManagedProperty("#{summaryService}")
	SummaryService summaryService;

	public String upload() {
		if (!(FacesContext.getCurrentInstance().getExternalContext()
				.getRequest() instanceof MultipartRequestWrapper)) {
			addMessage(FacesMessage.SEVERITY_ERROR, "No File Chosen");
			return "failed";
		}
		MultipartRequestWrapper request = (MultipartRequestWrapper) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		FileBean fb = request.getFile("fileupload");
		if (null == fb) {
			addMessage(FacesMessage.SEVERITY_ERROR, "No File Chosen");
			return "failed";
		}
		try {
			getFileService().uploadFile(fb);
			getSummaryService().updateSummary();
			addMessage(FacesMessage.SEVERITY_INFO, "File Uploaded");
			return "success";
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Exception in the service", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
			return "failed";
		}
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public SummaryService getSummaryService() {
		return summaryService;
	}

	public void setSummaryService(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

}
