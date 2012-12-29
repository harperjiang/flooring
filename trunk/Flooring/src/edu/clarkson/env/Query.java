package edu.clarkson.env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	    /*
	     * 
	     * Convert application/json format to java objects
	     * 
	     */
	    Gson gson = new Gson();
	    Scene[] scenes = gson.fromJson(inputStringBuilder.toString(), Scene[].class);

	    /*
	     * 
	     * do the query to database
	     * 
	     */


    	ArrayList<Stats> stats = new ArrayList<Stats>();
    	for(int i=0; i< scenes.length;i++){
    		Stats stat = new Stats();
        	ArrayList<Double> record = new ArrayList<Double>();
        	/*
        	 *  construct the file path by query
        	 */
        	String path = new String();
        	/*
        	 * path build to the right scene
        	 */
        	{
        		path += "/DATA";
        		path += "/"+scenes[i].getScheme();
        		path += "/record.csv";
        	}
        	ServletContext context = request.getSession().getServletContext();
        	//detect stream == null for SC_NOT_FOUND
    	    InputStream stream = context.getResourceAsStream(path);
        	BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
    	    String str = new String();
    	    while((str = buffer.readLine())!=null){
    	    	record.add(Double.parseDouble(str));
    	    }	    	

    	    Collections.sort(record);

    	    /*
    	     * Can be wrapped in one function
    	     */
    	    int total = record.size();
    	    stat.setMin(record.get(0));
    	    stat.setP25(record.get((int)(total*0.25)));
    	    stat.setMedium(record.get((int)(total*0.5)));
    	    stat.setP75(record.get((int)(total*0.75)));
    	    stat.setMax(record.get(total-1));
    	    
    	    stats.add(stat);
    	}
	    /*
	     *
	     *  Response with stats results
	     *  
	     */
	    Type type = new TypeToken<ArrayList<Stats>>(){}.getType();
	    responseOutput.println(gson.toJson(stats, type));
	    //responseOutput.println(gson.toJson(record));
	    //responseOutput.println(query.getJSONArray(0));
	    responseOutput.close();

	}

}
