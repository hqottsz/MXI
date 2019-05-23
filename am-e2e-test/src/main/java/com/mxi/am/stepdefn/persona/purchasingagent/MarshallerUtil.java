
package com.mxi.am.stepdefn.persona.purchasingagent;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


/**
 * Marshaller to convert object to xml String
 *
 */
public class MarshallerUtil {

   /**
    * Convert given object to a xml String
    *
    * @param aMessageObj
    *           Object
    * @return Xml string version of the object
    * @throws JAXBException
    */
   public static String convertToXMLString( Object aMessageObj ) throws JAXBException {
      StringWriter lWriter = new StringWriter();
      JAXBContext lContext = JAXBContext.newInstance( aMessageObj.getClass() );
      Marshaller lMarshaller = lContext.createMarshaller();
      lMarshaller.marshal( aMessageObj, lWriter );

      return lWriter.toString();
   }
}
