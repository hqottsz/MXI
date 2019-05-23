package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.table.inv.InvOwnerTable;


/**
 * Builds a <code>inv_owner</code> object
 */
public class OwnerDomainBuilder implements DomainBuilder<OwnerKey> {

   private boolean iDefault;

   private boolean iLocal = true;


   /**
    * {@inheritDoc}
    */
   @Override
   public OwnerKey build() {
      InvOwnerTable lInvOwner = InvOwnerTable.create();
      lInvOwner.setLocalBool( iLocal );
      lInvOwner.setDefaultBool( iDefault );

      return lInvOwner.insert();
   }


   public OwnerDomainBuilder isDefault() {
      iDefault = true;

      return this;
   }


   public OwnerDomainBuilder isNonLocal() {
      iLocal = false;

      return this;
   }
}
