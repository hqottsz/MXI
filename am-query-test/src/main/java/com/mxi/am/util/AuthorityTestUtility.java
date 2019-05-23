package com.mxi.am.util;

import java.util.Optional;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;


public class AuthorityTestUtility {

   public void giveAuthority( HumanResourceKey humanResource, AuthorityKey authority ) {
      DataSetArgument args = new DataSetArgument();
      {
         args.add( authority, "authority_db_id", "authority_id" );
         args.add( humanResource, "hr_db_id", "hr_id" );
      }
      MxDataAccess.getInstance().executeInsert( "org_hr_authority", args );
   }


   public void setAllAuthority( HumanResourceKey humanResource, boolean hasAllAuthority ) {
      DataSetArgument setArgs = new DataSetArgument();
      {
         setArgs.add( "all_authority_bool", hasAllAuthority );
      }
      DataSetArgument whereArgs = new DataSetArgument();
      {
         whereArgs.add( humanResource, "hr_db_id", "hr_id" );
      }
      MxDataAccess.getInstance().executeUpdate( HumanResourceKey.TABLE_NAME, setArgs, whereArgs );
   }


   public void setAuthority( InventoryKey inventory, Optional<AuthorityKey> authority ) {
      DataSetArgument setArgs = new DataSetArgument();
      {
         if ( authority.isPresent() ) {
            setArgs.add( authority.get(), "authority_db_id", "authority_id" );
         } else {
            setArgs.add( null, "authority_db_id", "authority_id" );
         }
      }
      DataSetArgument whereArgs = new DataSetArgument();
      {
         whereArgs.add( inventory, "inv_no_db_id", "inv_no_id" );
      }
      MxDataAccess.getInstance().executeUpdate( InventoryKey.TABLE_NAME, setArgs, whereArgs );
   }

}
