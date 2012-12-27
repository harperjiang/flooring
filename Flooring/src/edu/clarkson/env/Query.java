package edu.clarkson.env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Query
 */
@WebServlet("/Query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
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
		PrintWriter responseOutput = response.getWriter();
		
		/*
		try {
			JSONObject query = new JSONObject(request.getParameter("json"));
			//query.put("scheme", "natural");
			responseOutput.println(query);
			responseOutput.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//get all the post body due to application/json
	    BufferedReader reader = request.getReader();
	    StringBuilder inputStringBuilder = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	            inputStringBuilder.append(line);
	    }


	    //JSONArray query = new JSONArray(inputStringBuilder.toString());
	    Gson gson = new Gson();
	    Scene[] scenes = gson.fromJson(inputStringBuilder.toString(), Scene[].class);
	    //System.out.print(scenes);
	    responseOutput.println(gson.toJson(scenes));
	    //responseOutput.println(query.getJSONArray(0));
	    responseOutput.close();



	    /*
	     * do the query to database
	     * 
	     * */
	}

}
