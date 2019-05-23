package com.mxi.mx.core.table.eqp.oil;

import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.ASSMBL_CD;
import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.ASSMBL_DB_ID;
import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.OIL_DATA_TYPE_DB_ID;
import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.OIL_DATA_TYPE_ID;
import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.TIME_DATA_TYPE_DB_ID;
import static com.mxi.mx.core.table.eqp.oil.EqpAssmblBomOilDao.ColumnName.TIME_DATA_TYPE_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpAssmblBomOilKey;
import com.mxi.mx.util.optional.Optional;


public class JdbcEqpAssmblBomOilDaoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Object under test
   private EqpAssmblBomOilDao iDao;

   // Default record preloaded, it uses LITRES and CYCLES
   private static final EqpAssmblBomOilKey ASSEMBLY_OIL_CONSUMPTION_KEY =
         new EqpAssmblBomOilKey( "4650:ABCD" );
   private static final DataTypeKey OIL_IN_LITRES = new DataTypeKey( "4650:1" );
   private static final DataTypeKey TIME_IN_CYCLES = new DataTypeKey( "4650:2" );

   // Other data types used for modifications
   private static final DataTypeKey OIL_IN_QUARTS = new DataTypeKey( "4650:3" );
   private static final DataTypeKey TIME_IN_HOURS = new DataTypeKey( "4650:4" );


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iDao = new JdbcEqpAssmblBomOilDao();
   }


   @Test
   public void insert_legacy() {

      EqpAssmblBomOilKey lExpectedKey = new EqpAssmblBomOilKey( "4650:TEST" );
      AssemblyKey lAssemblyKey = new AssemblyKey( lExpectedKey.toString() );

      EqpAssmblBomOilKey lInsertedKey = iDao.insert( lAssemblyKey, OIL_IN_LITRES, TIME_IN_CYCLES );

      assertEquals( lExpectedKey, lInsertedKey );
   }


   @Test( expected = NullPointerException.class )
   public void insert_legacy_null() {
      iDao.insert( null, null, null );
   }


   @Test
   public void insert_valid() {

      EqpAssmblBomOilKey lEqpAssmblBomOilKey = new EqpAssmblBomOilKey( "4650:TEST" );
      EqpAssmblBomOilTable lRow = new EqpAssmblBomOilTable( lEqpAssmblBomOilKey );
      {
         lRow.setAssemblyKey( new AssemblyKey( lEqpAssmblBomOilKey.toString() ) );
         lRow.setOilDataTypeKey( OIL_IN_LITRES );
         lRow.setTimeDataTypeKey( TIME_IN_CYCLES );
      }

      EqpAssmblBomOilKey lInsertedKey = iDao.insert( lRow );

      assertEquals( lEqpAssmblBomOilKey, lInsertedKey );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      iDao.insert( null );
   }


   @Test
   public void delete_valid() {
      EqpAssmblBomOilTable lRow = new EqpAssmblBomOilTable( ASSEMBLY_OIL_CONSUMPTION_KEY );

      assertTrue( iDao.delete( lRow ) );
      assertFalse( lookUp( ASSEMBLY_OIL_CONSUMPTION_KEY ).isPresent() );
   }


   @Test
   public void delete_doesNotExist() {
      EqpAssmblBomOilTable lRow = new EqpAssmblBomOilTable( new EqpAssmblBomOilKey( "4650:FAKE" ) );

      assertFalse( iDao.delete( lRow ) );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      iDao.delete( null );
   }


   @Test
   public void getOilDataTypeKey_validAssemblyKey() {

      DataTypeKey lOilDataTypeKey =
            iDao.getOilDataTypeKey( new AssemblyKey( ASSEMBLY_OIL_CONSUMPTION_KEY.toString() ) );

      assertEquals( OIL_IN_LITRES, lOilDataTypeKey );
   }


   @Test( expected = NullPointerException.class )
   public void getOilDataTypeKey_validAssemblyKey_noOilDataType() {
      modify( ASSEMBLY_OIL_CONSUMPTION_KEY, null, TIME_IN_CYCLES );

      iDao.getOilDataTypeKey( new AssemblyKey( ASSEMBLY_OIL_CONSUMPTION_KEY.toString() ) );
   }


   @Test( expected = NullPointerException.class )
   public void getOilDataTypeKey_nullAssemblyKey() {
      iDao.getOilDataTypeKey( null );
   }


   @Test
   public void findByPrimaryKey_valid() {
      EqpAssmblBomOilTable lTable = iDao.findByPrimaryKey( ASSEMBLY_OIL_CONSUMPTION_KEY );

      assertEquals( ASSEMBLY_OIL_CONSUMPTION_KEY, lTable.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      EqpAssmblBomOilTable lRow = iDao.findByPrimaryKey( null );

      assertNull( lRow.getPk() );
   }


   @Test
   public void update_valid() {

      EqpAssmblBomOilTable lRow = new EqpAssmblBomOilTable( ASSEMBLY_OIL_CONSUMPTION_KEY );
      {
         lRow.setOilDataTypeKey( OIL_IN_QUARTS );
         lRow.setTimeDataTypeKey( TIME_IN_HOURS );
      }

      iDao.update( lRow );

      Optional<Row> lRecord = lookUp( ASSEMBLY_OIL_CONSUMPTION_KEY );

      assertTrue( lRecord.isPresent() );
      assertEquals( OIL_IN_QUARTS, lRecord.get().getUptakeKey() );
      assertEquals( TIME_IN_HOURS, lRecord.get().getTimeKey() );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      iDao.update( null );
   }


   @Test
   public void refresh_valid() {

      EqpAssmblBomOilTable lRow = new EqpAssmblBomOilTable( ASSEMBLY_OIL_CONSUMPTION_KEY );

      // swap the data types
      modify( lRow.getPk(), OIL_IN_QUARTS, TIME_IN_CYCLES );

      iDao.refresh( lRow );

      assertEquals( OIL_IN_QUARTS, lRow.getOilDataTypeKey() );
      assertEquals( TIME_IN_CYCLES, lRow.getTimeDataTypeKey() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      iDao.refresh( null );
   }


   @Test
   public void create_null() {
      EqpAssmblBomOilTable lEqpAssmblBomOilTable = iDao.create( null );

      assertNotNull( lEqpAssmblBomOilTable );
      assertNull( lEqpAssmblBomOilTable.getPk() );
   }


   @Test
   public void create_valid() {
      EqpAssmblBomOilTable lEqpAssmblBomOilTable = iDao.create( ASSEMBLY_OIL_CONSUMPTION_KEY );

      assertEquals( ASSEMBLY_OIL_CONSUMPTION_KEY, lEqpAssmblBomOilTable.getPk() );
      assertEquals( OIL_IN_LITRES, lEqpAssmblBomOilTable.getOilDataTypeKey() );
      assertEquals( TIME_IN_CYCLES, lEqpAssmblBomOilTable.getTimeDataTypeKey() );
   }


   @Test
   public void create_default() {
      EqpAssmblBomOilTable lEqpAssmblBomOilTable = iDao.create();

      assertNotNull( lEqpAssmblBomOilTable );
      assertNull( lEqpAssmblBomOilTable.getPk() );
   }


   private void modify( EqpAssmblBomOilKey aPk, DataTypeKey aOilDataType,
         DataTypeKey aTimeDataType ) {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aOilDataType, OIL_DATA_TYPE_DB_ID.name(), OIL_DATA_TYPE_ID.name() );
      lArgs.add( aTimeDataType, TIME_DATA_TYPE_DB_ID.name(), TIME_DATA_TYPE_ID.name() );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aPk, ASSMBL_DB_ID.name(), ASSMBL_CD.name() );

      MxDataAccess.getInstance().executeUpdate( EqpAssmblBomOilTable.TABLE_NAME, lArgs,
            lWhereArgs );
   }


   private Optional<Row> lookUp( EqpAssmblBomOilKey aKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "assmbl_db_id", "assmbl_cd" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( EqpAssmblBomOilTable.TABLE_NAME, lArgs );

      if ( lQs.next() ) {
         return Optional
               .of( new Row( lQs.getKey( AssemblyKey.class, ASSMBL_DB_ID.name(), ASSMBL_CD.name() ),
                     lQs.getKey( DataTypeKey.class, OIL_DATA_TYPE_DB_ID.name(),
                           OIL_DATA_TYPE_ID.name() ),
                     lQs.getKey( DataTypeKey.class, TIME_DATA_TYPE_DB_ID.name(),
                           TIME_DATA_TYPE_ID.name() ) ) );
      } else {
         return Optional.empty();
      }
   }


   private static class Row {

      private final AssemblyKey iAssemblyKey;
      private final DataTypeKey iUptakeKey;
      private final DataTypeKey iTimeKey;


      public Row(AssemblyKey aAssemblyKey, DataTypeKey aUptakeKey, DataTypeKey aTimeKey) {
         iAssemblyKey = aAssemblyKey;
         iUptakeKey = aUptakeKey;
         iTimeKey = aTimeKey;
      }


      public AssemblyKey getAssemblyKey() {
         return iAssemblyKey;
      }


      public DataTypeKey getUptakeKey() {
         return iUptakeKey;
      }


      public DataTypeKey getTimeKey() {
         return iTimeKey;
      }

   }

}
