import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SolrParser {

	public static void main(String[] args) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader("C:\\response2.xml"));
		String WikiResult="";
		
		String inputline = null;
		while ((inputline = reader.readLine()) != null) 
		{
			WikiResult=WikiResult+inputline+"\n";
		}
		
		WikiResult=WikiResult.replace("<br>","<br />");
		WikiResult=WikiResult.replace("<p>","<p />");
		WikiResult=WikiResult.replace("<small>","<small />");
		
		String start="<?xml version=\"1.0\"?>\n"+"<!DOCTYPE sresponse [\n"+"<!ENTITY nbsp \"&#160;\">\n"+"]>\n ";
		
		WikiResult=start+WikiResult;
		reader.close();
		 System.out.println(WikiResult);
		final String[] text=new String[20];
		final String[] title=new String[20];
		final String[] id=new String[20];
		
		
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
						id[0]="abc";
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
						id[count]=characters.toString();
						else if(count_str==2)
						title[count]=characters.toString();
						else if(count_str==3)
						text[count]=characters.toString();
						
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
			       e.printStackTrace();
			     }
			
			   }


}