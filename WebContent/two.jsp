<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.net.*,java.io.*,javax.xml.parsers.SAXParser,javax.xml.parsers.SAXParserFactory,org.xml.sax.Attributes,org.xml.sax.SAXException,org.xml.sax.helpers.DefaultHandler"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
try{
    URL query = new URL("www.google.com");
    BufferedReader in = new BufferedReader(
    new InputStreamReader(query.openStream()));

    String inputLine;
    while ((inputLine = in.readLine()) != null)
    {
    	out.println(inputLine);
    }
    in.close();
}catch(Exception ex){}
%>
</body>
</html>