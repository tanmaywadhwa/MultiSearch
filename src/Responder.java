

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Servlet implementation class for Servlet: Servlet1
 *
 */
public class Responder extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	String Q="",qURL="",qFrwd="",WikiResult="",BooksResult="",QuotesResult="",NewsResult="",embedReturn="",disp="";;
	String[] WikiText=new String[20],BooksText=new String[20],QuotesText=new String[20],NewsText=new String[20];
	String[] WikiTitle=new String[20],BooksTitle=new String[20],QuotesTitle=new String[20],NewsTitle=new String[20];
	String[] WikiId=new String[20],BooksId=new String[20],QuotesId=new String[20],NewsId=new String[20];
	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Responder() {
		super();

	}   	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}  	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		for(int i=0;i<15;i++){
			WikiTitle[i]="";
		}
		Q= request.getParameter("q");
		qURL= request.getParameter("q");
		qURL=qURL.replace(" ","%20");
		//System.out.println("Q="+Q);
		//System.out.println("qURL="+qURL);
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

		//Wikipedia

		try{
			URL query = new URL("http://localhost:8983/solr/Wikipedia/select?q="+qURL);
			URLConnection conn = query.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputline = null;
			while ((inputline = in.readLine()) != null) 
			{
				if(!inputline.contains("<str name=")&&!inputline.contains("</lst>")&&!inputline.contains("doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
					inputline=inputline.replaceAll("<[^>]*>", "");
				WikiResult=WikiResult+inputline+"\n";

			}

			WikiResult=WikiResult.replaceAll("&[^\\s]*;","");
			WikiResult=WikiResult.replace("&","");
			//System.out.println(WikiResult);
			//System.out.println("____________________ ________________");
			in.close();
		}catch(Exception ex){
			System.out.println("Wiki exception: ");
			ex.printStackTrace();
		}

		//Wiki parser

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {


				boolean flag_str=false;
				boolean flag_doc=false;
				private StringBuilder characters = new StringBuilder(100);




				int count=0;
				int count_str=0;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {
					WikiId[0]="abc";
					if(qName.equalsIgnoreCase("doc"))
					{
						count++;
						count_str=0;
						flag_doc= true;
						//System.out.println("countdoc"+count);
					}

					if(qName.equalsIgnoreCase("str")&&flag_doc==true)
					{ 
						count_str++;
						flag_str= true;
					}


				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					if(qName.equalsIgnoreCase("str")&&flag_doc==true){

						if(count_str==1)
							WikiId[count]=characters.toString();
						else if(count_str==2)
							WikiTitle[count]=characters.toString();
						else if(count_str==3)
							WikiText[count]=characters.toString();

						characters.setLength(0);
						flag_str = false;
					}

					if(qName.equalsIgnoreCase("doc"))
						flag_doc=false;

				}

				public void characters(char ch[], int start, int length) throws SAXException {


					if(flag_str)
						characters.append(new String(ch,start,length));

				}

			};


			saxParser.parse(new InputSource(new StringReader(WikiResult)), handler);
		} catch (Exception e) {
			System.out.println("exception found: ");
			e.printStackTrace();
		}
		//for(int i=1;i<15;i++){
		//	System.out.println(WikiTitle[i]);
		//}
		qFrwd=WikiTitle[1];

		
		//HTML 

		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"jquery-ui.css\">");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
        out.println("<script src=\"jquery-1.10.2.js\"></script>"); 
        out.println("<script src=\"jquery-ui.js\"></script>");
        //out.println("<script>");
        //out.println("$(function() {$(\"#autosearch\").autocomplete({source: function( request, response ) {var str=request.term;$.ajax({url: \"http://localhost:8983/solr/Wikipedia/select?facet=true&facet.field=title&facet.prefix=\"+str,data: { facet:'true',wt: 'json',}, dataType: \"jsonp\",jsonp: 'json.wrf',success: function( data ) {var tr=data.facet_counts.facet_fields.title;var results = [];var tr1=tr.toString().split(\",\");var count=10;  // MAXIMUM NO. OF SUGGESTIONSfor ( var i = 0; i < tr1.length; i++ ) {if(tr1[i]!=0 && count>0){results.push(tr1[i]);count--;}}response(results);}}); },minLength: 1});});");
        out.println("<script>");
        out.println("$(function() { alert('running');");
        out.println("$(\".autosearch\").autocomplete({);");
        		out.println("source: function( request, response ) {");
        		out.println("var str=request.term");
        			
        		out.println("$.ajax({");
        			
        			
        		out.println(" url: \"http://localhost:8983/solr/Wikipedia/query?facet=true&facet.field=title&facet.prefix=\"+str,");
        		out.println("data: {"); 
                        
        		out.println("facet:'true',");
        		out.println("wt: 'json',");
        		out.println("},"); 
        		out.println("dataType: \"jsonp\",");
        				out.println("jsonp: 'json.wrf',");
                     
        				out.println("success: function( data ) {");
        			     //alert(facet_counts);
        				out.println("var tr=data.facet_counts.facet_fields.title;");
        				 
        				out.println("var results = [];");
        				out.println("var tr1=tr.toString().split(\",\");");
        				out.println("var count=10;  // MAXIMUM NO. OF SUGGESTIONS");
        				out.println("for ( var i = 0; i < tr1.length; i++ ) {");
        				out.println("if(tr1[i]!=0 && count>0)");
        				out.println("{");
        				out.println("results.push(tr1[i]);");
        		
        					  out.println("count--;");
        				out.println("}");
        					
         
        				out.println("}");
        				out.println("alert('Script is running');");
        				out.println("response(results);");
        				
        				
        				out.println("}");
        				out.println("});"); 
        				out.println("},");
        				out.println("minLength: 1");
        				out.println("});");
        				out.println("});");
        				out.println("</script>");
        				//out.println("</script>");
        out.println("<link href='http://fonts.googleapis.com/css?family=Roboto+Slab:400,300' rel='stylesheet' type='text/css'>");
        out.println("<link href='http://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>");
        out.println("<link href='http://fonts.googleapis.com/css?family=Varela+Round' rel='stylesheet' type='text/css'>");
		out.println("<style>");
		out.println("#frame { zoom: 0.95; -moz-transform: scale(0.95); -moz-transform-origin: 0 0; }");
		out.println("</style>");
//Spellcheck
		out.println("<script>");
		out.println("$(function() {");
		out.println("$(document).ready(function() {");
			    //alert('IN submit');
			    //var url = "path/to/your/script.php"; // the script where you handle the form input.
		out.println("var str=\""+Q+"\";");
		out.println("$.ajax({");
						
						
		out.println("url: \"http://localhost:8983/solr/Wikipedia/select?wt=json&indent=true&spellcheck=true&spellcheck.build=true&spellcheck.onlyMorePopular=true&spellcheck.collate=true&spellcheck.accuracy=0.7&spellcheck.q=\"+str,");
		out.println("data: {"); 
			                
		out.println("facet:'true',");
		out.println("wt: 'json',");
		out.println("},"); 
		out.println("dataType: \"jsonp\",");
		out.println("jsonp: 'json.wrf',");
			             
		out.println("success: function( data ) {");
						     //alert(facet_counts);
		out.println("var tr=data.spellcheck.suggestions;");
							 
		out.println("var qu=str;");
		out.println("var len=tr.length;");
		out.println("var res=tr[len-1];");
							 
		out.println("if(res!=null)");
		out.println("{");
							 //alert('Did you mean "'+res+'" instead of  '+'"'+qu+'"');
		out.println("$( \"p\" ).show( \"slow\" );");
		out.println("$( \"p\" ).text( 'Did you mean \"'+res+'\" instead of  '+'\"'+qu+'\"' );");
		out.println("res="+Q+"");
		//out.println("$('a').prop('href','');");
		out.println("response(res);");
		out.println("}");
							
		out.println("}");
		out.println("});");

		out.println("return false; // avoid to execute the actual submit of the form.");
		out.println("});");
		out.println("});"); 

		out.println("</script>");
//end of spellcheck 		
		//out.println("<script src=\"spellcheck.js\"></script>");
		
		out.println("</head>");
		out.println("<body>");
		out.println("<center><img src=\"logo.jpg\" height=\50\" width=\"200\">");
		
		out.println("<form id=\"sub\" action=\"Responder\" method=\"post\"><input type=\"text\" name=\"q\" id=\"quer1\" class=\"quer tftextinput autosearch\" value=\""+Q+"\" onclick=\"this.value='';\" onfocus=\"this.select()\" onblur=\"this.value=!this.value?'Enter Query':this.value;\">  <input  id=\"sub\" type=\"submit\" value=\"Search\" class=\"tfbutton\" onclick=\"f1()\"/><br>");
		//out.println("<input type=\"radio\" name=\"hidden\" id=\"quer\" value=\""+Q+"\">");
		//out.println("<input type=\"text\" size=\"80\" name=\"q\" id=\"autosearch\"  value=\"Enter Query\" onclick=\"this.value='';\" onfocus=\"this.select()\" onblur=\"this.value=!this.value?'Enter Query':this.value;\"><br>");
		out.println("</form></center>");
		out.println("<font size=\"1\">");
		//out.println("The query that has reached is: "+Q+". It is being processed now.<br>");
		if(WikiTitle[1]!="")
		{
		out.println("<table style=\"table-layout:fixed; overflow: scroll; \">");
		out.println("<tr>");
		out.println("</tr><font size=\"1\">");
		
		if(!WikiTitle[1].equalsIgnoreCase(Q))
			disp="No exact results found,showing most likely results";	
			else
				disp=" ";
		out.println("<font size=2 color=blue><center>"+disp+"</center></font>");
		
		out.println("<tr><th><font size=\"1\">Similar Wiki Results</font></th><th><font size=\"1\"> Results from multiple sources</font></th>");
		// "<th>Wiki Page</th><th>Books About \""+Q+"\"</th><th>Quotes About \""+Q+"\"</th></tr>");
		out.println("<tr>");
		out.println("<td align=left valign=top>");
		for(int i=2;i<=10;i++){
			if(WikiTitle[i]!="")
			{
				out.println("<font size=\"2\"><a href=\"embeded.jsp?query="+WikiTitle[i]+"\" target=\"embed\" onclick=\"embed("+qFrwd+")\">"+WikiTitle[i]+"</a></font><br><br><br>");
			} 
			else 
				out.println(" ");
		}
		out.println("</td>");
		out.println(embed(qFrwd));
		//out.println("<td width=\"1100\" height=\"800\"><iframe width=\"1100\" height=\"800\" src=\"embeded.jsp?query="+qFrwd+"\"></iframe></td>");
		//out.println("<td width=\"800\" height=\"700\"><iframe id=\"frame\" width=\"800\" height=\"700\" src=\"http://www.wikipedia.com/wiki/"+Q+"\"></iframe></td>");
		//out.println("<td style=\" overflow:scroll; width:50px; height:100px;\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result2+"</frame></td>");
		out.println("</tr>");
		//out.println("</tr>");
		out.println("</table>");
	}
		else{
			out.println("<h1><center>OOPS.. we couldn't find anything similar to you search in our dwarf index. Please try QWERTYing again</center></h1>");
			out.println("<center><h1><p></p></h1></center>");
			//out.println("<a href=\"\">CLICK HERE</a>");
		}
		
		out.println("</font>");
		out.println("</body>");
		out.println("</html>");


		//System.out.println(""+BooksResult);
		//System.out.println("\n\n\n ____________________ \n\n\n");
		//System.out.println(""+Result2);
		for(int i=1;i<15;i++){
			WikiTitle[i]="";
		}
		WikiResult="";
		BooksResult="";
		QuotesResult="";
		NewsResult="";
		qURL="";
		qFrwd="";
		Q="";
		disp="";

	}
	public String embed(String qfrwd)
	{
		String Frame="<td width=\"1200\" height=\"750\"><iframe id=\"embed\" width=\"1200\" height=\"750\" src=\"embeded.jsp?query="+qfrwd+"\"></iframe></td>";
		return(Frame);
	}
}
