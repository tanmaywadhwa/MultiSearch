

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: Servlet1
 *
 */
 public class Servlet1 extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   String Q,WikiResult="",Result1="",Result2="";
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Servlet1() {
		super();
		
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Q= request.getParameter("q");
		response.setContentType("text/html;charset=UTF-8");
        
		PrintWriter out = response.getWriter();
		
		//Wikipedia
		try{
			URL query = new URL("http://localhost:8983/solr/Wikipedia/select?q="+Q);
			URLConnection conn = query.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputLine = null;
		    while ((inputLine = in.readLine()) != null)
		    {
		    	WikiResult=WikiResult+inputLine+"<br>";
		    }
		    in.close();
		}catch(Exception ex){}
		
		//Books
		try{
		    URL query = new URL("http://localhost:8983/solr/Wikipedia/select/?q="+Q+"&fl=title");
		    BufferedReader in = new BufferedReader(
		    new InputStreamReader(query.openStream()));

		    String inputLine;
		    while ((inputLine = in.readLine()) != null)
		    {
		    	Result1=Result1+inputLine+"<br>";
		    }
		    in.close();
		}catch(Exception ex){}
		
		//Collection 2
		try{
		    URL query = new URL("http://localhost:8983/solr/collection2/select/?q="+Q+"&fl=title");
		    BufferedReader in = new BufferedReader(
		    new InputStreamReader(query.openStream()));

		    String inputLine;
		    while ((inputLine = in.readLine()) != null)
		    {
		    	Result2=Result2+inputLine+"<br>";
		    }
		    in.close();
		}catch(Exception ex){}
        
		
		out.println("<html>");
		out.println("<head>");
		out.println("<style>");
		out.println("#frame { zoom: 0.95; -moz-transform: scale(0.95); -moz-transform-origin: 0 0; }");
		out.println("</style>");
		out.println("</head>");
		out.println("<body><font size=\"2\">");
		//out.println("The query that has reached is: "+Q+". It is being processed now.<br>");
		out.println("<table border=\"1\" style=\"table-layout:fixed; overflow: hidden; \">");
		out.println("<tr>");
		out.println("<center><i><b>Just QWERTY</b></i>");
		out.println("<form action=\"Servlet1\" method=\"post\"><input type=\"text\" name=\"q\" id=\"q\" value=\"Enter Query\" onclick=\"this.value='';\" onfocus=\"this.select()\" onblur=\"this.value=!this.value?'Enter Query':this.value;\"><br>  <input  type=\"submit\" value=\"Search\" onclick=\"f1()\"/></form></center>");
		out.println("</tr>");
		out.println("<tr><th>Top Results on \""+Q+"\"</th><th>Multisource Results</th>");
				// "<th>Wiki Page</th><th>Books About \""+Q+"\"</th><th>Quotes About \""+Q+"\"</th></tr>");
		out.println("<tr>");
		out.println("<td width=\"250\" height=\"700\">"+WikiResult+"</td>");
		out.println("<td width=\"1100\" height=\"800\"><iframe width=\"1100\" height=\"800\" src=\"embeded.jsp?query="+Q+"\"></iframe></td>");
		//out.println("<td width=\"800\" height=\"700\"><iframe id=\"frame\" width=\"800\" height=\"700\" src=\"http://www.wikipedia.com/wiki/"+Q+"\"></iframe></td>");
		//out.println("<td style=\" overflow:scroll; width:50px; height:100px;\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result2+"</frame></td>");
		out.println("</tr>");
		//out.println("</tr>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
		
		
		System.out.println(""+WikiResult);
		System.out.println("\n\n\n ____________________ \n\n\n");
		//System.out.println(""+Result2);
		
		WikiResult="";
		Result1="";
		Result2="";
		
	}   	  	    
}
 