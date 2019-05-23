
package com.mxi.mx.common.unittest.dao;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.key.MxKey;
import com.mxi.mx.common.utils.DataTypeUtils;


/**
 * {@link DataSet} builder.
 */
public class DataSetBuilder {

   /** Columns to be added to the data set. */
   private String[] iColumnNames;

   /** Data set constructed by the builder. */
   private DataSet iDataSet = new DataSet();


   /**
    * Creates a new {@linkplain TestDataSet} object.
    *
    * @param aColNames
    *           column names for the data set.
    */
   public DataSetBuilder(Object... aColNames) {
      iColumnNames = new String[aColNames.length];

      for ( int i = 0; i < aColNames.length; i++ ) {
         String lColName = aColNames[i].toString();
         String lDataType = getColumnNameDataType( lColName );

         iColumnNames[i] = lColName;
         iDataSet.addEmptyColumn( lColName, lDataType );
      }
   }


   /**
    * Adds a row to the data set. The order of values should match the order of column names passed
    * into the <tt>TestDataSet</tt> constructor. If an {@link MxKey} is part of the array, its key
    * values will be added as individual columns.
    *
    * @param aValues
    *           column values for a data set row.
    *
    * @return this TestDataSet.
    */
   public DataSetBuilder addRow( Object... aValues ) {

      Object[] lTranslatedValues = new Object[iColumnNames.length];

      int lColIndex = 0;
      for ( Object aValue : aValues ) {

         if ( aValue instanceof MxKey ) {
            MxKey lKey = ( MxKey ) aValue;

            for ( int lKeyIndex = 0; lKeyIndex < lKey.getNumKeys(); lKeyIndex++ ) {
               lTranslatedValues[lColIndex++] = lKey.getKeyValue( lKeyIndex + 1 );
            }
         } else {
            lTranslatedValues[lColIndex++] = aValue;
         }
      }

      iDataSet.addRow( lTranslatedValues );

      return this;
   }


   public DataSet getDs() {
      return iDataSet;
   }


   /**
    * Add a final row to the data set, then return the data set.
    *
    * @param aValues
    *           row values.
    *
    * @return the data set.
    */
   public DataSet lastRow( Object... aValues ) {
      addRow( aValues );

      return iDataSet;
   }


   /**
    * Converts a column name into a data set type. Not an exhaustive algorithm.
    *
    * @param aColumnName
    *           column name for data type guessing
    *
    * @return type of the column (default is STRING.)
    */
   private String getColumnNameDataType( Object aColumnName ) {
      String lColName = aColumnName.toString();
      String lDataType;
      if ( lColName.endsWith( "_dt" ) || lColName.endsWith( "_gdt" )
            || lColName.equals( "timestamp" ) ) {

         lDataType = DataTypeUtils.DATE;
      } else if ( lColName.endsWith( "_id" ) ) {
         lDataType = DataTypeUtils.INTEGER;
      } else if ( lColName.endsWith( "_bool" ) ) {
         lDataType = DataTypeUtils.INTEGER;
      } else if ( lColName.endsWith( "_qt" ) ) {
         lDataType = DataTypeUtils.FLOAT;
      } else {
         lDataType = DataTypeUtils.STRING;
      }

      return lDataType;
   }
}
