
package com.mxi.mx.core.dao.assembly;

import com.mxi.mx.core.key.AssemblySubtypeKey;


/**
 * DOCUMENT_ME
 *
 * @author hmuradyan
 */
public class AssemblySubtypeDaoTestStub extends AssemblySubtypeDaoImpl {

   static int iNextId = 0;


   /**
    * DOCUMENT_ME
    *
    * @return DOCUMENT_ME
    */
   @Override
   public AssemblySubtypeKey generatePrimaryKey() {

      return new AssemblySubtypeKey( 1234, iNextId++ );
   }
}
