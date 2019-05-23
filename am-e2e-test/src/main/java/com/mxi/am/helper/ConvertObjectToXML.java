package com.mxi.am.helper;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class ConvertObjectToXML {

   public static String convertToXMLString( Object aMessageObj ) throws JAXBException {
      StringWriter lWriter = new StringWriter();
      JAXBContext lContext = JAXBContext.newInstance( aMessageObj.getClass() );
      Marshaller lMarshaller = lContext.createMarshaller();
      lMarshaller.marshal( aMessageObj, lWriter );

      return lWriter.toString();
   }

}
