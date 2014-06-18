<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.net.*,java.io.*,javax.xml.parsers.SAXParser,javax.xml.parsers.SAXParserFactory,org.xml.sax.Attributes,org.xml.sax.SAXException,org.xml.sax.helpers.DefaultHandler"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results are as follows</title>

</head>
<body>
<%

String Query=request.getParameter("q");
out.println("Query has reached the server as : "+ Query);

%>
Running the query
<%
//Collection1 query:-
String Response="";
try{
	    URL query = new URL("http://localhost:8983/solr/collection1/select/?q="+Query);
	    BufferedReader in = new BufferedReader(
	    new InputStreamReader(query.openStream()));

	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    {
	    	Response=Response+"\n"+inputLine;
	        //System.out.println(inputLine);
	    	//out.println(inputLine);
	    }
	    in.close();
	}catch(Exception ex){}
	    	   
	    
	   try {
	    	 
	    	SAXParserFactory factory = SAXParserFactory.newInstance();
	    	SAXParser saxParser = factory.newSAXParser();
	     
	    	DefaultHandler handler = new DefaultHandler() {
	     
	    	boolean doc = false;
	    	boolean blname = false;
	    	boolean bnname = false;
	    	boolean bsalary = false;
	     
	    	public void startElement(String uri, String localName,String qName, 
	                    Attributes attributes) throws SAXException {
	     
	    		System.out.println("Start Element :" + qName);
	     
	    		if (qName.equalsIgnoreCase("doc")) {
	    			System.out.println("I am in");
	    			doc = true;
	    		}
	     
	    		if (qName.equalsIgnoreCase("LASTNAME")) {
	    			blname = true;
	    		}
	     
	    		if (qName.equalsIgnoreCase("NICKNAME")) {
	    			bnname = true;
	    		}
	     
	    		if (qName.equalsIgnoreCase("SALARY")) {
	    			bsalary = true;
	    		}
	     
	    	}
	     
	    	public void endElement(String uri, String localName,
	    		String qName) throws SAXException {
	     
	    		System.out.println("End Element :" + qName);
	     
	    	}
	     
	    	public void characters(char ch[], int start, int length) throws SAXException {
	     
	    		if (doc) {
	    			String temp=new String(ch, start, length);
	    			System.out.println("Document : " + new String(ch, start, length));
	    			doc = false;
	    		}
	     
	    		if (blname) {
	    			System.out.println("Last Name : " + new String(ch, start, length));
	    			blname = false;
	    		}
	     
	    		if (bnname) {
	    			System.out.println("Nick Name : " + new String(ch, start, length));
	    			bnname = false;
	    		}
	     
	    		if (bsalary) {
	    			System.out.println("Salary : " + new String(ch, start, length));
	    			bsalary = false;
	    		}
	     
	    	}
	     
	         };
	     		
	           saxParser.parse(Response, handler);
	         } catch (Exception e) {
	           e.printStackTrace();
	         } 
	         //out.println(Response);
	    
	         
%>
 </body>
</html>