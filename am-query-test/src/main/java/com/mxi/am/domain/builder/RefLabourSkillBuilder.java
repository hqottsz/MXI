
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.table.ref.RefLabourSkillTable;


/**
 *
 * Builds a <code>ref_labour_skill</code> object
 *
 */
public class RefLabourSkillBuilder implements DomainBuilder<RefLabourSkillKey> {

   private String iCode;
   private boolean iESigRequired;


   /**
    * {@inheritDoc}
    */
   @Override
   public RefLabourSkillKey build() {
      RefLabourSkillTable lRefLabourSkillTable = RefLabourSkillTable.create();
      {
         lRefLabourSkillTable.setCode( iCode );
         lRefLabourSkillTable.setESigRequired( iESigRequired );
      }
      return lRefLabourSkillTable.insert();
   }


   /**
    *
    * Sets the labour skill's e-signature requirement.
    *
    * @param aESigRequired
    * @return
    */
   public RefLabourSkillBuilder withESigRequired( boolean aESigRequired ) {
      iESigRequired = aESigRequired;
      return this;
   }


   /**
    * Sets the labour skill's code.
    *
    * @param aCode
    * @return
    */
   public RefLabourSkillBuilder withCode( String aCode ) {
      iCode = aCode;
      return this;
   }

}
