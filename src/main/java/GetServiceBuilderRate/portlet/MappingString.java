package GetServiceBuilderRate.portlet;
import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.util.PwdGenerator;

import posadas_wc_sb.model.WebContent;
import posadas_wc_sb.service.WebContentLocalService;
import posadas_wc_sb.service.WebContentLocalServiceUtil;



public class MappingString {
	


	public String getDynamicContent(String es, String en){
		String id_es="es_ES";
		String id_en="en_US";
		String dynamic="<dynamic-content language-id=\""+id_es+"\"><![CDATA["+es+"]]></dynamic-content>"
		+"<dynamic-content language-id=\""+id_en+"\"><![CDATA["+en+"]]></dynamic-content>";
		return dynamic;
	}
	
    public String getDynamicContentString(String es, String en){
		String id_es="es_ES";
		String id_en="en_US";
		String dynamic="<dynamic-content language-id=\""+id_es+"\"><![CDATA[[\""+es+"\"]]]></dynamic-content>"
		+"<dynamic-content language-id=\""+id_en+"\"><![CDATA[[\""+en+"\"]]]></dynamic-content>";
		return dynamic;
	}
    
    public String getDynamicContentRateLink(String es, String en,String classPK){
		
		String id_es="es_ES";
		String id_en="en_US";
		
		String dynamic="<dynamic-content language-id=\""+id_es+"\"><![CDATA["+"{\"className\":\"com.liferay.journal.model.JournalArticle\",\"classPK\":\""+ classPK +"\"}"+"]]></dynamic-content>"
					+"<dynamic-content language-id=\""+id_en+"\"><![CDATA["+"{\"className\":\"com.liferay.journal.model.JournalArticle\",\"classPK\":\""+ classPK+"\"}"+"]]></dynamic-content>";
				
		 return dynamic;
	}
	
	public String DynamicHeader(String content){
		String number="1.0";
		String lang_deafult="es_ES,en_US";
		@SuppressWarnings("unused")
		String lang_deafult_es="es_ES";
		String lang="es_ES";
		String contents = null;
		if(!content.equals(null)){
			 contents ="<?xml version=\""+number+"\"?><root available-locales=\""+lang_deafult+"\" default-locale=\""+lang+"\">"+content+"</root>";

		}
		
		return contents;
	}
	
	public String DynamicElement(String name,String type,String index,String dynamic){
		String dynamicElement=null;
		
		if(!dynamic.equals(null)){
			dynamicElement="<dynamic-element name=\""+name+"\"  instance-id=\""+generateInstanceId()+"\" type=\""+type+"\" index-type=\""+index+"\">"+dynamic+
					  "</dynamic-element>";
		}
		
		return dynamicElement;
	}
	
	public String DynamicElementRateLink(String name,String type,String index, String es, String en){
		String dynamicRateLink = "";
		WebContentLocalService service = WebContentLocalServiceUtil.getService();
		
		List<WebContent> contents = new ArrayList<WebContent>();
		
		List<WebContent> con = service.getWebContents();
		
		for(WebContent webContent: con){
			if(webContent.isNew()){
				contents.add(webContent);
			}
		}
		
		for(WebContent cont: contents){
			dynamicRateLink+="<dynamic-element name=\""+name+"\"  instance-id=\""+generateInstanceId()+"\" type=\""+type+"\" index-type=\""+index+"\">"+getDynamicContentRateLink(es, en,cont.getClasspk())+
					  "</dynamic-element>"; 
		}
		return dynamicRateLink;
	}
	
	public String generateInstanceId() {
		   StringBuilder instanceId = new StringBuilder(8);

		   String key = PwdGenerator.KEY1 + PwdGenerator.KEY2 + PwdGenerator.KEY3;
		       
		   for (int i = 0; i < 8; i++) {
		       int pos = (int)Math.floor(Math.random() * key.length());

		       instanceId.append(key.charAt(pos));
		   }
		   return instanceId.toString();
		}
	
    
	
  

	

	

	
	

}

