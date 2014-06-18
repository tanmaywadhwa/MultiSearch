

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
public class Servlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	String Q="",qURL="",qFrwd="",WikiResult="",BooksResult="",QuotesResult="",NewsResult="",embedReturn="";
	String[] WikiText=new String[20],BooksText=new String[20],QuotesText=new String[20],NewsText=new String[20];
	String[] WikiTitle=new String[20],BooksTitle=new String[20],QuotesTitle=new String[20],NewsTitle=new String[20];
	String[] WikiId=new String[20],BooksId=new String[20],QuotesId=new String[20],NewsId=new String[20];
	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Servlet() {
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
		Q= request.getParameter("q");
		qURL= request.getParameter("q");
		qURL=qURL.replace(" ","%20");
		System.out.println("Q="+Q);
		System.out.println("qURL="+qURL);
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
		for(int i=1;i<15;i++){
			System.out.println(WikiTitle[i]);
		}
		qFrwd=WikiTitle[1];

		
		//HTML 

		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"jquery-ui.css\">");
        out.println("<script src=\"jquery-1.10.2.js\"></script>"); 
        out.println("<script src=\"jquery-ui.js\"></script>");
        out.println("<script>");
        out.println("$(function() {$(\"#autosearch\").autocomplete({source: function( request, response ) {var str=request.term;$.ajax({url: \"http://localhost:8983/solr/Wikipedia/select?facet=true&facet.field=title&facet.prefix=\"+str,data: { facet:'true',wt: 'json',}, dataType: \"jsonp\",jsonp: 'json.wrf',success: function( data ) {var tr=data.facet_counts.facet_fields.title;var results = [];var tr1=tr.toString().split(\",\");var count=10;  // MAXIMUM NO. OF SUGGESTIONSfor ( var i = 0; i < tr1.length; i++ ) {if(tr1[i]!=0 && count>0){results.push(tr1[i]);count--;}}response(results);}}); },minLength: 1});});");
        out.println("</script>");
		out.println("<style>");
		out.println("#frame { zoom: 0.95; -moz-transform: scale(0.95); -moz-transform-origin: 0 0; }");
		out.println("</style>");
		out.println("</head>");
		out.println("<body><font size=\"1\">");
		//out.println("The query that has reached is: "+Q+". It is being processed now.<br>");
		out.println("<table border=\"1\" style=\"table-layout:fixed; overflow: hidden; \">");
		out.println("<tr>");
		out.println("<center><h1><i><b>Just QWERTY</b></i></h1>");
		out.println("<form action=\"Servlet2\" method=\"post\"><input type=\"text\" name=\"q\" class=\"autosearch\" id=\"autosearch\" value=\"Enter Query\" onclick=\"this.value='';\" onfocus=\"this.select()\" onblur=\"this.value=!this.value?'Enter Query':this.value;\">  <input  type=\"submit\" value=\"Search\" onclick=\"f1()\"/></form></center>");
		out.println("</tr>");
		out.println("<tr><th>Top Results on \""+Q+"\"</th><th>Multisource Results</th>");
		// "<th>Wiki Page</th><th>Books About \""+Q+"\"</th><th>Quotes About \""+Q+"\"</th></tr>");
		out.println("<tr>");
		out.println("<td width:200px;\"  height=\"700\">");for(int i=2;i<=10;i++){out.println("<font size=\"1\">- <a href=\"embeded2.jsp?query="+WikiTitle[i]+"\" target=\"embed\" onclick=\"embed("+qURL+")\">"+WikiTitle[i]+"</a></font><br><br><br><br><br>");}
		out.println("</td>");
		out.println(embed(qURL));
		//out.println("<td width=\"1100\" height=\"800\"><iframe width=\"1100\" height=\"800\" src=\"embeded.jsp?query="+qFrwd+"\"></iframe></td>");
		//out.println("<td width=\"800\" height=\"700\"><iframe id=\"frame\" width=\"800\" height=\"700\" src=\"http://www.wikipedia.com/wiki/"+Q+"\"></iframe></td>");
		//out.println("<td style=\" overflow:scroll; width:50px; height:100px;\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result1+"</frame></td>");
		//out.println("<td style=\" overflow:scroll;\" width=\"500\" height=\"700\"><frame width=\"500\" height=\"1000\">"+Result2+"</frame></td>");
		out.println("</tr>");
		//out.println("</tr>");
		out.println("</table>");
		out.println("</font>");
		out.println("</body>");
		out.println("</html>");


		//System.out.println(""+BooksResult);
		//System.out.println("\n\n\n ____________________ \n\n\n");
		//System.out.println(""+Result2);
		for(int i=1;i<15;i++){
			BooksTitle[i]="";
		}
		WikiResult="";
		BooksResult="";
		QuotesResult="";
		NewsResult="";
		qURL="";
		qFrwd="";
		Q="";

	}
	public String embed(String qfrwd)
	{
		//Books
		try{
			URL query = new URL("http://localhost:8983/solr/WikiBooks/select?q="+qfrwd);
			URLConnection conn = query.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputline = null;
			while ((inputline = in.readLine()) != null) 
			{
				if(!inputline.contains("<str name=")&&!inputline.contains("</lst>")&&!inputline.contains("doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
					inputline=inputline.replaceAll("<[^>]*>", "");
				BooksResult=BooksResult+inputline+"\n";

			}

			BooksResult=BooksResult.replaceAll("&[^\\s]*;","");
			BooksResult=BooksResult.replace("&","");

			//String start="<?xml version=\"1.0\"?>\n"+"<!DOCTYPE sresponse [\n"+"<!ENTITY nbsp \"&#160;\">\n"+"]>\n ";

			//BooksResult=start+BooksResult;
			//System.out.println(BooksResult);
			System.out.println("____________________ ________________");
			in.close();
		}catch(Exception ex){ex.printStackTrace();}


		//Books parser

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
					BooksId[0]="abc";
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
							BooksId[count]=characters.toString();
						else if(count_str==2)
							BooksTitle[count]=characters.toString();
						else if(count_str==3)
							BooksText[count]=characters.toString();

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


			saxParser.parse(new InputSource(new StringReader(BooksResult)), handler);
		} catch (Exception e) {
			System.out.println("exception found: ");
			e.printStackTrace();
		}
		for(int i=1;i<15;i++){
			System.out.println("Books Title#"+i+" :"+ BooksTitle[i]);
		}

		//Quotes

		try{
			URL query = new URL("http://localhost:8983/solr/WikiQuotes/select?q="+qfrwd);
			URLConnection conn = query.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputline = null;
			while ((inputline = in.readLine()) != null) 
			{
				if(!inputline.contains("<str name=")&&!inputline.contains("</lst>")&&!inputline.contains("doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
					inputline=inputline.replaceAll("<[^>]*>", "");
				QuotesResult=QuotesResult+inputline+"\n";

			}

			QuotesResult=QuotesResult.replaceAll("&[^\\s]*;","");
			QuotesResult=QuotesResult.replace("&","");
			//System.out.println(WikiResult);
			//System.out.println("____________________ ________________");
			in.close();
		}catch(Exception ex){
			System.out.println("Quotes exception: ");
			ex.printStackTrace();
		}

		//Quotes parser

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
					QuotesId[0]="abc";
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
							QuotesId[count]=characters.toString();
						else if(count_str==2)
							QuotesTitle[count]=characters.toString();
						else if(count_str==3)
							QuotesText[count]=characters.toString();

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


			saxParser.parse(new InputSource(new StringReader(QuotesResult)), handler);
		} catch (Exception e) {
			System.out.println("exception found: ");
			e.printStackTrace();
		}
		for(int i=1;i<15;i++){
			System.out.println(QuotesTitle[i]);
		}
		String qFrwd=QuotesTitle[1];

		//News
		try{
			URL query = new URL("http://localhost:8983/solr/WikiNews/select?q="+qfrwd);
			URLConnection conn = query.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			String inputline = null;
			while ((inputline = in.readLine()) != null) 
			{
				if(!inputline.contains("<str name=")&&!inputline.contains("</lst>")&&!inputline.contains("doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
					inputline=inputline.replaceAll("<[^>]*>", "");
				NewsResult=NewsResult+inputline+"\n";

			}

			NewsResult=NewsResult.replaceAll("&[^\\s]*;","");
			NewsResult=NewsResult.replace("&","");
			//System.out.println(WikiResult);
			//System.out.println("____________________ ________________");
			in.close();
		}catch(Exception ex){
			System.out.println("News exception: ");
			ex.printStackTrace();
		}

		//News parser

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
					QuotesId[0]="abc";
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
							NewsId[count]=characters.toString();
						else if(count_str==2)
							NewsTitle[count]=characters.toString();
						else if(count_str==3)
							NewsText[count]=characters.toString();

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


			saxParser.parse(new InputSource(new StringReader(NewsResult)), handler);
		} catch (Exception e) {
			System.out.println("exception found: ");
			e.printStackTrace();
		}
		for(int i=1;i<15;i++){
			System.out.println(NewsTitle[i]);
		}
		String Frame="<td width=\"1200\" height=\"750\"><iframe id=\"embed\" width=\"1200\" height=\"750\" src=\"embeded2.jsp?query="+qfrwd+"\"></iframe></td>";
		return(Frame);
	}
}
