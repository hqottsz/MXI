package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartGroupKey;


/**
 * Reader for retrieving Part Group information.
 *
 */
public class PartGroupReader {

   /**
    *
    * Retrieve the identifier for a Part Group given a configuration slot and a code (the code is
    * unique amongst its siblings).
    *
    * @param aConfigurationSlot
    * @param aPartGroupCode
    * @return
    */
   public static PartGroupKey read( ConfigSlotKey aConfigurationSlot, String aCode ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigurationSlot, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
      lArgs.add( "bom_part_cd", aCode );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_bom_part", lArgs );
      if ( lQs.isEmpty() ) {
         return null;
      } else if ( lQs.getRowCount() > 1 ) {
         throw new RuntimeException(
               "Unexpected number (" + lQs.getRowCount() + ") of part groups with code [" + aCode
                     + "] assigned to configuration slot key=" + aConfigurationSlot );
      }
      lQs.first();

      return lQs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" );
   }

}
