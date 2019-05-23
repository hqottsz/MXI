
package com.mxi.mx.core.dao.org;

import com.mxi.mx.core.key.OrgKey;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
public class OrganizationDaoTestStub extends OrganizationDaoImpl {

   static int iNextId = 0;


   /**
    * DOCUMENT_ME
    *
    * @return DOCUMENT_ME
    */
   @Override
   public OrgKey generatePrimaryKey() {

      return new OrgKey( 1234, iNextId++ );
   }
}
