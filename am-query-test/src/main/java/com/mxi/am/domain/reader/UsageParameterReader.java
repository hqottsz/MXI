package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;


/**
 * Reader for retrieving Usage Parameter information.
 *
 */
public class UsageParameterReader {

   /**
    *
    * Retrieve the identifier for a Usage Parameter given a configuration slot and a code (the code
    * is unique amongst its siblings).
    *
    * @param aConfigurationSlot
    * @param aCode
    * @return
    */
   public static DataTypeKey read( ConfigSlotKey aConfigurationSlot, String aCode ) {

      // Get the data type of the provided calculated parameter's code.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "data_type_cd", aCode );
      QuerySet lDataTypeQs =
            QuerySetFactory.getInstance().executeQueryTable( "mim_data_type", lArgs );
      if ( lDataTypeQs.isEmpty() ) {
         throw new RuntimeException( "Data type does not exist with code = " + aCode );
      }

      // There may be more than one data type with the same code in Mx.
      // But that code will be unique per config slot. So look it up under the provided config slot.
      while ( lDataTypeQs.next() ) {
         DataTypeKey lDataTypeKey =
               lDataTypeQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );

         lArgs = aConfigurationSlot.getPKWhereArg();
         lArgs.add( lDataTypeKey, "data_type_db_id", "data_type_id" );

         QuerySet lQs =
               QuerySetFactory.getInstance().executeQueryTable( "mim_part_numdata", lArgs );
         if ( !lQs.isEmpty() ) {
            return lDataTypeKey;
         }
      }

      return null;
   }

}
