package edu.clarkson.cs.env.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kooobao.common.spring.ApplicationContextHolder;

import edu.clarkson.cs.env.entity.Criteria;
import edu.clarkson.cs.env.service.FileService;

public class DownloadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -995093867971967858L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Criteria criteria = new Criteria();
		// private int ventScheme;// {Natural,AirConditioned};
		// private int houseType;// {SingleDetached,ApartmentBuilding};
		// private int floorType;// {Hardwood,Carpeting,HD Carpeting};
		// private int ventLevel;// {low,medium,high};
		// private int partGran;// {fine,corase};
		// private int probabilitySuspension;// {zero,nonZero,inf};
		criteria.setVentScheme(Integer.parseInt(request
				.getParameter("vent_scheme")));
		criteria.setHouseType(Integer.parseInt(request
				.getParameter("house_type")));
		criteria.setFloorType(Integer.parseInt(request
				.getParameter("floor_type")));
		criteria.setVentLevel(Integer.parseInt(request
				.getParameter("vent_level")));
		criteria.setPartGran(Integer.parseInt(request.getParameter("part_gran")));
		criteria.setProbabilitySuspension(Integer.parseInt(request
				.getParameter("probability_suspension")));

		response.setContentType("application/xls");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ java.net.URLEncoder.encode("data.xls", "utf8"));

		FileService fileService = ApplicationContextHolder.getInstance()
				.getApplicationContext().getBean(FileService.class);
		try {
			fileService.downloadRecords(criteria, response.getOutputStream());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		response.getOutputStream().flush();
	}

}
