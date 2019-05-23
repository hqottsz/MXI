package com.mxi.mx.integration.ejb.reqpub;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;

import com.mxi.am.driver.integration.IntegrationMessageDriver;
import com.mxi.am.driver.integrationtesting.IntegrationMessageUtils;


public class RequestPublisherBeanTest {

   private final static String MX_USER = "mxintegration";
   private final static String MX_PASSWORD = "password";


   @Test
   public void testSecurityAttackRequest() throws Exception {

      IntegrationMessageDriver messageDriver = new IntegrationMessageDriver( MX_USER, MX_PASSWORD );

      SOAPMessage soapMessage = IntegrationMessageUtils.createSOAPMessage();

      XmlObject xmlMessage = XmlObject.Factory.newInstance();
      QName qName = new QName( "http://xml.mxi.com/xsd/integration/request/1.1", "request", "req" );

      XmlCursor xmlCursor = xmlMessage.newCursor();
      xmlCursor.toNextToken();
      xmlCursor.beginElement( qName );
      xmlCursor.insertAttributeWithValue( "msg_id", "test security id" );
      xmlCursor.insertAttributeWithValue( "msg_stream",
            "Test]]&gt;&lt;a xmlns:a=&apos;http://www.w3.org/1999/xhtml&apos;&gt;&lt;a:body\r\n"
                  + "onload=&apos;alert(String.fromCharCode(88,83,83))&apos;/&gt;&lt;/a&gt;&lt;![CDATA[" );
      xmlCursor.dispose();

      // Make request synchronous by adding mode header
      IntegrationMessageUtils.addMaintenixModeHeader( soapMessage, true );

      // Convert SOAP Message to String
      String soapMessageString = IntegrationMessageUtils.convertSOAPMessageToString( soapMessage );

      // Inject Request into SOAP Message
      soapMessageString =
            IntegrationMessageUtils.injectSOAPBody( soapMessageString, xmlMessage.toString() );

      Document docReceived = messageDriver.sendSecurityAttack( soapMessageString );

      if ( messageDriver.isErrorResponse( docReceived ) ) {
         if ( messageDriver.getDocumentAsString( docReceived ).contains( "stacktrace" ) ) {
            Assert.fail( "The returned message contains a stacktrace, which is a security risk." );
         }
      } else {
         Assert.fail( "The returned message should be an error response." );
      }

   }

}
