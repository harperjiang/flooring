package edu.clarkson.cs.env.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kooobao.common.spring.ApplicationContextHolder;

import edu.clarkson.cs.env.service.FileService;

public class DownloadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -995093867971967858L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String criteria = request.getParameter("criteria");
		if (StringUtils.isEmpty(criteria)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String[] carray = criteria.split("_");
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ java.net.URLEncoder.encode("data.xls", "utf8"));

		FileService fileService = ApplicationContextHolder.getInstance()
				.getApplicationContext().getBean(FileService.class);
		try {
			fileService.downloadRecords(carray, response.getOutputStream());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		response.getOutputStream().flush();
	}

}
