
/**
 *
 */
package com.mxi.mx.common.table;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.common.key.MxDbKey;
import com.mxi.mx.common.key.MxKey;


/**
 * Not all tables have easy ways to create the test data (i.e.: ref terms). This allows a generic
 * way to validate these tables
 */
public class TestTable<T extends MxDbKey> extends AbstractTable<T> {

   /**
    * Creates a new {@linkplain TestTable} object.
    */
   protected TestTable() {
      super();
   }


   /**
    * Retrieves a {@linkplain TestTable} object.
    *
    * @param aPk
    *           the primary key
    */
   protected TestTable(T aPk) {
      super( aPk );
   }


   /**
    * Create the test data with a given key
    *
    * @param aPk
    *           the primary key
    *
    * @return the test table
    */
   public static <T extends MxDbKey> TestTable<T> create( T aPk ) {
      TestTable<T> lTable = new TestTable<T>();
      lTable.iPk = aPk;

      return lTable;
   }


   /**
    * Returns the unit test data for a given key
    *
    * @param aPk
    *           the primary key
    *
    * @return the test data
    */
   public static <T extends MxDbKey> TestTable<T> findByPrimaryKey( T aPk ) {
      return new TestTable<T>( aPk );
   }


   /**
    * Gets the value for the column name
    *
    * @param aColumnName
    *           the column name
    *
    * @return the value for the column name
    */
   public Object get( ColumnNameInt aColumnName ) {
      return super.getObject( aColumnName );
   }


   /**
    * Gets the value for the column names with a specified key class
    *
    * @param aClass
    *           the columns data class
    * @param aColumnNames
    *           the columns
    *
    * @return the key
    */
   public <V extends MxKey> V get( Class<V> aClass, ColumnNameInt... aColumnNames ) {
      return super.getKey( aClass, aColumnNames );
   }


   /**
    * Gets the value for the column name with a specified type
    *
    * @param aClass
    *           the column data class
    * @param aColumnName
    *           the column name
    *
    * @return the column value
    */
   @SuppressWarnings( "unchecked" )
   public <V> V get( Class<V> aClass, ColumnNameInt aColumnName ) {
      return ( V ) get( aColumnName );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public T insert() {
      return super.insert( iPk );
   }


   /**
    * Set the value for the specified column name. Note that there are no string-equivalence; since
    * this is a generic solution, ColumnNameInt ensures that unit tests do not get out of sync with
    * the database schema model.
    *
    * @param aColumnName
    *           the column name
    * @param aValue
    *           the column name value
    */
   public void set( ColumnNameInt aColumnName, Object aValue ) {
      if ( aValue instanceof String ) {
         setString( aColumnName, ( String ) aValue );
      } else if ( aValue instanceof Integer ) {
         setInteger( aColumnName, ( Integer ) aValue );
      } else if ( aValue instanceof Boolean ) {
         setBoolean( aColumnName, ( Boolean ) aValue );
      } else if ( aValue instanceof BigDecimal ) {
         setBigDecimal( aColumnName, ( BigDecimal ) aValue );
      } else if ( aValue instanceof Date ) {
         setDate( aColumnName, ( Date ) aValue );
      } else if ( aValue instanceof Double ) {
         setDouble( aColumnName, ( Double ) aValue );
      } else if ( aValue instanceof Float ) {
         setFloat( aColumnName, ( Float ) aValue );
      } else if ( aValue instanceof MxDbKey ) {
         setMxKey( ( MxDbKey ) aValue, aColumnName );
      } else {
         throw new RuntimeException( "Could not determine object type" );
      }
   }


   /**
    * Sets the value for the specified column names.
    *
    * @param aFk
    *           the foreign key
    * @param aColumnNames
    *           the column names
    */
   public void set( MxDbKey aFk, ColumnNameInt... aColumnNames ) {
      setMxKey( aFk, aColumnNames );
   }
}
