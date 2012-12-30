package edu.clarkson.env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Download
 */
@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final int BUFSIZE = 4096;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Download() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    BufferedReader reader = request.getReader();
	    StringBuilder inputStringBuilder = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	            inputStringBuilder.append(line);
	    }

	    /*
	     * 
	     * Convert application/json format to java objects
	     * 
	     */
	    Gson gson = new Gson();
	    Scene scene = gson.fromJson(inputStringBuilder.toString(), Scene.class);
	    System.out.println(scene.toString());

	    String path = new String();
    	/*
    	 * path build to the right scene
    	 */
   		path += "/DATA";
   		path += "/"+scene.toFile();
   		System.out.println(scene.toFile());
   		
   		ServletContext context = request.getSession().getServletContext();
	    InputStream is = context.getResourceAsStream(path);	
		
		response.setContentType("application/ocet-stream");
		response.setHeader("Content-Disposition",
				"attachment;filename=scenerecord.csv");

		int read=0;
		byte[] buffer = new byte[BUFSIZE];
		OutputStream os = response.getOutputStream();

		buffer = scene.toString().getBytes();
		os.write(buffer, 0, scene.toString().length());
		while((read = is.read(buffer))!= -1){
			os.write(buffer, 0, read);
		}
		os.flush();
		os.close();	
	}

}


