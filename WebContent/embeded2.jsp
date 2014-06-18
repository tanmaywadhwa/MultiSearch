<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.io.*, java.io.BufferedReader, java.io.StringReader,javax.xml.parsers.SAXParser,javax.xml.parsers.SAXParserFactory,org.xml.sax.Attributes,org.xml.sax.InputSource,org.xml.sax.SAXException,org.xml.sax.helpers.DefaultHandler,java.net.URL,java.net.URLConnection" %>
    



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%
String Q="",qURL="";
Q=request.getParameter("query");
qURL=Q;
String BooksResult="",QuotesResult="",NewsResult="";
final String[] BooksText=new String[20],QuotesText=new String[20],NewsText=new String[20];
final String[] BooksTitle=new String[20],QuotesTitle=new String[20],NewsTitle=new String[20];
final String[] BooksId=new String[20],QuotesId=new String[20],NewsId=new String[20];
qURL=qURL.replace(" ","%20");
%>
<!-- Shitty Code  -->

<%
//Books
try{
	URL query = new URL("http://localhost:8983/solr/WikiBooks/select?q="+qURL+"&wt=xml&indent=true&hl=true&hl.fl=text");
	URLConnection conn = query.openConnection();
	BufferedReader in = new BufferedReader(
			new InputStreamReader(conn.getInputStream()));

	String inputline = null;
	while ((inputline = in.readLine()) != null) 
	{   inputline=inputline.replaceAll("<br>", "br!");
		if(!inputline.contains("<str") && !inputline.contains("<arr name")&&!inputline.contains("</arr>")&&!inputline.contains("</lst>")&&!inputline.contains("<doc>")&&!inputline.contains("</doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
		{
		inputline=inputline.replaceAll("<[^>]*>", "");
		inputline=inputline.replaceAll("<|>","");
		}
		inputline=inputline.replaceAll("&lt;em&gt;", "em!");
		inputline=inputline.replaceAll("&lt;/em&gt;", "em*");
		System.out.println("initial output"+inputline);
	BooksResult=BooksResult+inputline+"\n";

	}
	
	BooksResult=BooksResult.replaceAll("&[^\\s]*;","");
	BooksResult=BooksResult.replace("&","");
	
	in.close();
	 
}catch(Exception ex){
	System.out.println("Books exception: ");
	ex.printStackTrace();
}

//Books parser

try {
				 
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
			 
				DefaultHandler handler = new DefaultHandler() {
					int count=0;
					int count_lst=0;
					boolean flag_doc;
					boolean flag_str=false;
					boolean flag_hl=false;
					private StringBuilder characters = new StringBuilder(100);
					int count_str=0;
					int count_str1=0;
				
					public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
						
						if(qName.equalsIgnoreCase("doc"))
						{//System.out.println("in doc");
						count++;
						count_str1=0;
						flag_doc= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&flag_doc==true)
						{ 
						count_str1++;
						flag_str= true;
						}
						
						if(qName.equalsIgnoreCase("lst"))
						{
							count_lst++;
						flag_hl= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&count_lst>2)
						{ 
						count_str++;
						flag_str= true;
						}
						
			 
				}
				
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if(qName.equalsIgnoreCase("str")&&flag_doc==true){
					//	System.out.println("end LOL count_str1="+count_str1);
					//	System.out.println("count_lst="+count_lst);
						if(count_str1==1)
						BooksId[count]=characters.toString();
						else if(count_str1==2){
			//			System.out.println(characters.toString());
						BooksTitle[count]=characters.toString();}
						characters.setLength(0);
						flag_str = false;
						}
					
					if(qName.equalsIgnoreCase("doc"))
					flag_doc=false;
					
					if(qName.equalsIgnoreCase("str")&&count_lst>2){
						//System.out.println(count_str+"\n"+characters.toString());
						System.out.println("rsultpage after parsing"+characters.toString());
						BooksText[count_str]=characters.toString();
						characters.setLength(0);
						flag_str = false;
						}
					
					
					}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
					
					if(flag_str&&count_lst==2)
						characters.append(new String(ch,start,length));
					
					if(flag_str&&count_lst>2)
					characters.append(new String(ch,start,length));
					
				}
				
			     };
			 
			     
			     saxParser.parse(new InputSource(new StringReader(BooksResult)), handler);
			     } catch (Exception e) {
			       e.printStackTrace();
			     }
			     
			     int counter=0;
					for(int i=1; i<11;i++)
					{
						if(QuotesText[i]!=null)
						{counter++;
						BooksText[i]=BooksText[i].replace("br!","<br>");
						BooksText[i]=BooksText[i].replace("em!","<b style=\" background:yellow; \"><font color=\"red\">");
						BooksText[i]=BooksText[i].replace("em*","</b></font>");
						System.out.println("doc no "+i+"\n"+BooksTitle[i]+"\n"+BooksText[i]);
						}
					}
					for(int i=1;i<15;i++){
						System.out.println(BooksTitle[i]);
					}
//Quotes

try{
	URL query = new URL("http://localhost:8983/solr/WikiQuotes/select?q="+qURL+"&wt=xml&indent=true&hl=true&hl.fl=text");
	URLConnection conn = query.openConnection();
	BufferedReader in = new BufferedReader(
			new InputStreamReader(conn.getInputStream()));

	String inputline = null;
	while ((inputline = in.readLine()) != null) 
	{   inputline=inputline.replaceAll("<br>", "br!");
		if(!inputline.contains("<str") && !inputline.contains("<arr name")&&!inputline.contains("</arr>")&&!inputline.contains("</lst>")&&!inputline.contains("<doc>")&&!inputline.contains("</doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
		{
		inputline=inputline.replaceAll("<[^>]*>", "");
		inputline=inputline.replaceAll("<|>","");
		}
		inputline=inputline.replaceAll("&lt;em&gt;", "em!");
		inputline=inputline.replaceAll("&lt;/em&gt;", "em*");
		System.out.println("initial output"+inputline);
	QuotesResult=QuotesResult+inputline+"\n";

	}
	
	QuotesResult=QuotesResult.replaceAll("&[^\\s]*;","");
	QuotesResult=QuotesResult.replace("&","");
	
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
					int count=0;
					int count_lst=0;
					boolean flag_doc;
					boolean flag_str=false;
					boolean flag_hl=false;
					private StringBuilder characters = new StringBuilder(100);
					int count_str=0;
					int count_str1=0;
				
					public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
						
						if(qName.equalsIgnoreCase("doc"))
						{//System.out.println("in doc");
						count++;
						count_str1=0;
						flag_doc= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&flag_doc==true)
						{ 
						count_str1++;
						flag_str= true;
						}
						
						if(qName.equalsIgnoreCase("lst"))
						{
							count_lst++;
						flag_hl= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&count_lst>2)
						{ 
						count_str++;
						flag_str= true;
						}
						
			 
				}
				
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if(qName.equalsIgnoreCase("str")&&flag_doc==true){
					//	System.out.println("end LOL count_str1="+count_str1);
					//	System.out.println("count_lst="+count_lst);
						if(count_str1==1)
						QuotesId[count]=characters.toString();
						else if(count_str1==2){
			//			System.out.println(characters.toString());
						QuotesTitle[count]=characters.toString();}
						characters.setLength(0);
						flag_str = false;
						}
					
					if(qName.equalsIgnoreCase("doc"))
					flag_doc=false;
					
					if(qName.equalsIgnoreCase("str")&&count_lst>2){
						//System.out.println(count_str+"\n"+characters.toString());
						System.out.println("rsultpage after parsing"+characters.toString());
						QuotesText[count_str]=characters.toString();
						characters.setLength(0);
						flag_str = false;
						}
					
					
					}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
					
					if(flag_str&&count_lst==2)
						characters.append(new String(ch,start,length));
					
					if(flag_str&&count_lst>2)
					characters.append(new String(ch,start,length));
					
				}
				
			     };
			 
			     
			     saxParser.parse(new InputSource(new StringReader(QuotesResult)), handler);
			     } catch (Exception e) {
			       e.printStackTrace();
			     }
			     
			     counter=0;
					for(int i=1; i<11;i++)
					{
						if(QuotesText[i]!=null)
						{counter++;
						QuotesText[i]=QuotesText[i].replace("br!","<br>");
						QuotesText[i]=QuotesText[i].replace("em!","<b style=\" background:yellow; \"><font color=\"red\">");
						QuotesText[i]=QuotesText[i].replace("em*","</b></font>");
						System.out.println("doc no "+i+"\n"+QuotesTitle[i]+"\n"+QuotesText[i]);
						}
					}
					
					
for(int i=1;i<15;i++){
	System.out.println(QuotesTitle[i]);
}

//News
try{
	URL query = new URL("http://localhost:8983/solr/WikiNews/select?q="+qURL+"&wt=xml&indent=true&hl=true&hl.fl=text");
	URLConnection conn = query.openConnection();
	BufferedReader in = new BufferedReader(
			new InputStreamReader(conn.getInputStream()));

	String inputline = null;
	while ((inputline = in.readLine()) != null) 
	{   inputline=inputline.replaceAll("<br>", "br!");
		if(!inputline.contains("<str") && !inputline.contains("<arr name")&&!inputline.contains("</arr>")&&!inputline.contains("</lst>")&&!inputline.contains("<doc>")&&!inputline.contains("</doc>")&&!inputline.contains("<long name=")&&!inputline.contains("<response>")&&!inputline.contains("</response>")&&!inputline.contains("<lst")&&!inputline.contains("<int name=")&&!inputline.contains("</str>")&&!inputline.contains("<result name")&&!inputline.contains("</result>"))
		{
		inputline=inputline.replaceAll("<[^>]*>", "");
		inputline=inputline.replaceAll("<|>","");
		}
		inputline=inputline.replaceAll("&lt;em&gt;", "em!");
		inputline=inputline.replaceAll("&lt;/em&gt;", "em*");
		System.out.println("initial output"+inputline);
	NewsResult=NewsResult+inputline+"\n";

	}
	
	NewsResult=NewsResult.replaceAll("&[^\\s]*;","");
	NewsResult=NewsResult.replace("&","");
	
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
					int count=0;
					int count_lst=0;
					boolean flag_doc;
					boolean flag_str=false;
					boolean flag_hl=false;
					private StringBuilder characters = new StringBuilder(100);
					int count_str=0;
					int count_str1=0;
				
					public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
						
						if(qName.equalsIgnoreCase("doc"))
						{//System.out.println("in doc");
						count++;
						count_str1=0;
						flag_doc= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&flag_doc==true)
						{ 
						count_str1++;
						flag_str= true;
						}
						
						if(qName.equalsIgnoreCase("lst"))
						{
							count_lst++;
						flag_hl= true;
						}
						
						if(qName.equalsIgnoreCase("str")&&count_lst>2)
						{ 
						count_str++;
						flag_str= true;
						}
						
			 
				}
				
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					
					if(qName.equalsIgnoreCase("str")&&flag_doc==true){
					//	System.out.println("end LOL count_str1="+count_str1);
					//	System.out.println("count_lst="+count_lst);
						if(count_str1==1)
						NewsId[count]=characters.toString();
						else if(count_str1==2){
			//			System.out.println(characters.toString());
						NewsTitle[count]=characters.toString();}
						characters.setLength(0);
						flag_str = false;
						}
					
					if(qName.equalsIgnoreCase("doc"))
					flag_doc=false;
					
					if(qName.equalsIgnoreCase("str")&&count_lst>2){
						//System.out.println(count_str+"\n"+characters.toString());
						System.out.println("rsultpage after parsing"+characters.toString());
						NewsText[count_str]=characters.toString();
						characters.setLength(0);
						flag_str = false;
						}
					
					
					}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
					
					if(flag_str&&count_lst==2)
						characters.append(new String(ch,start,length));
					
					if(flag_str&&count_lst>2)
					characters.append(new String(ch,start,length));
					
				}
				
			     };
			 
			     
			     saxParser.parse(new InputSource(new StringReader(QuotesResult)), handler);
			     } catch (Exception e) {
			       e.printStackTrace();
			     }
			     
			     counter=0;
					for(int i=1; i<11;i++)
					{
						if(NewsText[i]!=null)
						{counter++;
						NewsText[i]=NewsText[i].replace("br!","<br>");
						NewsText[i]=NewsText[i].replace("em!","<b style=\" background:yellow; \"><font color=\"red\">");
						NewsText[i]=NewsText[i].replace("em*","</b></font>");
						System.out.println("doc no "+i+"\n"+NewsTitle[i]+"\n"+NewsText[i]);
						}
					}
					
for(int i=1;i<15;i++){
	System.out.println(NewsTitle[i]);
}
%>
<!-- End of Shitty Code  -->
<style>	
#frame {border: 1px solid black; }
#frame { zoom: 0.25; -moz-transform: scale(1.00); -moz-transform-origin: 0 0; -o-transform: scale(1.00); -o-transform-origin: 0 0; -webkit-transform: scale(1.00); -webkit-transform-origin: 0 0;}
</style>
</head>
<body>
<font size="1">
<table border="1">
<tr height=><th>Wiki Page</th><th>Books About "<%out.println(Q);%>"</th><th>Quotes About "<%out.println(Q);%>"</th><th>News About "<%out.println(Q);%>"</th></tr>
<tr>
<!-- <td width="500" height="700"></td>  -->
<td style=" table-layout:fixed; overflow:hidden;" width="600" height="700"><iframe  width="550" height="700" src="http://www.wikipedia.com/wiki/<%out.println(Q);%>"></iframe></td>
<td style=" table-layout:fixed; overflow:hidden;" width="500px" height="700"><frame width="500" height="700"><% for(int i=1;i<=10;i++){out.println("<a href=\"http://en.wikibooks.org/wiki/"+BooksTitle[i]+"\">"+BooksTitle[i]+"</a><br>"+BooksText[i]+"<br><br>");} %></frame></td>
<td style=" table-layout:fixed; overflow:hidden;" width="500px" height="700"><frame width="500" height="700"><%for(int i=1;i<=10;i++){out.println("<a href=\"http://en.wikiquote.org/wiki/"+QuotesTitle[i]+"\">"+QuotesTitle[i]+"</a><br>"+QuotesText[i]+"<br><br>");} %></frame></td>
<td style=" table-layout:fixed; overflow:hidden;" width="500px" height="700"><frame width="500" height="700"><%for(int i=1;i<=10;i++){out.println("<a href=\"http://en.wikinews.org/wiki/"+NewsTitle[i]+"\">"+NewsTitle[i]+"</a><br>"+NewsText[i]+"<br><br>");}%></frame></td>
</tr>
</table>
</font>
</body>
</html>