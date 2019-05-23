package com.mxi.am.domain.builder;

import com.mxi.am.domain.Owner;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.table.inv.InvOwnerTable;


/**
 * Domain Builder for Owner Entity
 *
 */
public class OwnerBuilder {

   private static int iOwnerId = 0;


   public static OwnerKey build() {

      InvOwnerTable lInvOwnerTable =
            InvOwnerTable.create( new OwnerKey( Table.Util.getDatabaseId(), iOwnerId++ ) );
      // Local_Bool column is needed to be set to a true value
      lInvOwnerTable.setLocalBool( true );
      lInvOwnerTable.setOrgKey( OrgKey.ADMIN );
      lInvOwnerTable.setDefaultBool( true );
      return lInvOwnerTable.insert();

   }


   public static OwnerKey build( Owner owner ) {

      InvOwnerTable lInvOwnerTable =
            InvOwnerTable.create( new OwnerKey( Table.Util.getDatabaseId(), iOwnerId++ ) );

      lInvOwnerTable.setOwnerCd( owner.getCode() );
      lInvOwnerTable.setOwnerName( owner.getName() );
      lInvOwnerTable.setLocalBool( owner.isLocalBool() );
      lInvOwnerTable.setDefaultBool( owner.isDefaultBool() );
      lInvOwnerTable.setOrgKey( OrgKey.ADMIN );

      return lInvOwnerTable.insert();
   }
}
