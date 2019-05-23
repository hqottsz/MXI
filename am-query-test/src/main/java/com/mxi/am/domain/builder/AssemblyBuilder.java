
package com.mxi.am.domain.builder;

import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.table.eqp.EqpAssmbl;


/**
 * Builds an <code>eqp_assmbl</code>
 */
public class AssemblyBuilder implements DomainBuilder<AssemblyKey> {

   private RefAssmblClassKey iAssemblyClass;
   private String iAssemblyCode;

   // Class variable to hold a unique id used to generate unique assembly codes.
   private static Integer sAssemblyCodeUniqueId = 0;


   /**
    * Creates a new {@linkplain AssemblyBuilder} object.
    *
    */
   public AssemblyBuilder() {
   }


   /**
    * Creates a new {@linkplain AssemblyBuilder} object.
    *
    * @param aAssemblyCode
    *           the assembly code
    */
   public AssemblyBuilder(String aAssemblyCode) {
      iAssemblyCode = aAssemblyCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AssemblyKey build() {

      if ( iAssemblyCode == null ) {
         iAssemblyCode = generateUniqueAssemblyCode();
      }

      // EqpAssmbl.setRefAssmblClass() requires global LOGIC parameter SPEC2000_UPPERCASE_ASSMBL_CD.
      if ( GlobalParameters.getInstance( "LOGIC" )
            .getBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD" ) == null ) {

         // Throw a more helpful exception.
         throw new RuntimeException( "AssemblyBuilder requires global LOGIC parameter "
               + "'SPEC2000_UPPERCASE_ASSMBL_CD' to be set!" );
      }

      EqpAssmbl lAssemblyTable = new EqpAssmbl();
      lAssemblyTable.setRefAssmblClass( iAssemblyClass );

      return lAssemblyTable.insert( iAssemblyCode );
   }


   /**
    * Sets the assembly class.
    *
    * @param aAssemblyClass
    *           the assembly class
    *
    * @return the builder
    */
   public AssemblyBuilder withClass( RefAssmblClassKey aAssemblyClass ) {
      iAssemblyClass = aAssemblyClass;

      return this;
   }


   private String generateUniqueAssemblyCode() {
      return "ASSY" + sAssemblyCodeUniqueId++;
   }

}
