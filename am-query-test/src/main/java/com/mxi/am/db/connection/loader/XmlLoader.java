
package com.mxi.am.db.connection.loader;

import java.sql.Connection;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.mxi.mx.common.utils.XMLUtils;
import com.mxi.mx.core.unittest.MxUnittestDao;


/**
 * Loads a dataset via xml.
 */
public final class XmlLoader {

   private XmlLoader() {
      // utility class
   }


   public static void load( Connection aConnection, Class<?> aClass, String aResource ) {

      // Build a JDOM document for the file
      Document lXmlDoc = XMLUtils.buildJDOMDocument( ResourceReader.get( aClass, aResource ) );

      // Get the root element, and ensure it's properly formatted
      final Element lRootElt = lXmlDoc.getRootElement();
      if ( !lRootElt.getName().equals( "dataset" ) ) {
         throw new RuntimeException(
               "XML dataset document has an improperly formatted root element." );
      }

      // Iterate over all rows, inserting each
      final List<?> lChildren = lRootElt.getChildren();
      for ( Object lChild : lChildren ) {

         Element lChildElement = ( Element ) lChild;
         if ( lChildElement.getName().equalsIgnoreCase( "mim_db" ) ) {
            MxUnittestDao.insertIfNonetExists( lChildElement, aConnection );
         } else {
            if ( lChildElement.getName().equalsIgnoreCase( "dao_update" ) ) {
               MxUnittestDao.update( lChildElement, aConnection );
            } else {
               MxUnittestDao.insert( lChildElement, aConnection );
            }
         }
      }
   }
}
