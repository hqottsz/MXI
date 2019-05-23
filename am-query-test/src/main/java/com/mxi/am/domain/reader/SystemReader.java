package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;


/**
 * Reader for retrieving System information.
 *
 */
public class SystemReader {

   /**
    *
    * Retrieve the identifier for a System given its natural key.
    *
    * @param aParent
    *           - parent inventory identifier
    * @param aName
    *           - name of the System
    * @return
    */
   public static InventoryKey read( InventoryKey aParent, String aName ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_no_sdesc", aName );
      lArgs.add( aParent, "nh_inv_no_db_id", "nh_inv_no_id" );
      lArgs.add( RefInvClassKey.SYS, "inv_class_db_id", "inv_class_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_inv", lArgs );

      if ( lQs.getRowCount() == 0 ) {
         return null;
      } else if ( lQs.getRowCount() > 1 ) {
         throw new RuntimeException( "Unexpected number of SYS with name [" + aName
               + "] under parent with key=" + aParent );
      }

      lQs.first();

      return lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );
   }

}
