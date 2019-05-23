package com.mxi.mx.core.dao.inventory.aircraft;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.utils.uuid.UuidConverter;
import com.mxi.mx.domain.inventory.aircraft.OilUptakeRecording;


/**
 * Test class for {@link JdbcInventoryOilUptakeDao}.
 */
public class JdbcInventoryOilUptakeDaoTest {

   private InventoryOilUptakeDao iInventoryOilUptakeDao;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // Test Data
   private static final UUID INVENTORY_1_ID =
         new UuidConverter().convertStringToUuid( "7417120C7429487EAF69CD31AA2F17B1" );
   private static final UUID INVENTORY_2_ID =
         new UuidConverter().convertStringToUuid( "7417120C7429487EAF69CD31AA2F17B2" );
   private static final UUID INVENTORY_3_ID =
         new UuidConverter().convertStringToUuid( "7417120C7429487EAF69CD31AA2F17B3" );
   private static final UUID INVENTORY_4_ID =
         new UuidConverter().convertStringToUuid( "7417120C7429487EAF69CD31AA2F17B4" );
   private static final String INVENTORY_1_NAME = "Engine 1";
   private static final String INVENTORY_2_NAME = "Engine 2";
   private static final String INVENTORY_3_NAME = "Engine 3";
   private static final String INVENTORY_4_NAME = "Engine 4";
   private static final BigDecimal QUANTITY_1 = new BigDecimal( "2.5" );
   private static final BigDecimal QUANTITY_2 = new BigDecimal( "5.64" );
   private static final BigDecimal QUANTITY_3 = new BigDecimal( "8.19" );
   private static final BigDecimal QUANTITY_NULL = null;
   private static final UUID DATA_TYPE_ID =
         new UuidConverter().convertStringToUuid( "ABCDEF0123456789ABCDEF0123456789" );
   private static final String DATA_TYPE_CODE = "QT";
   private static final String DATA_TYPE_NAME = "Quart (U.S.)";
   private static final int DATA_TYPE_PRECISION = 2;
   private static final RefAssmblClassKey ENGINE_ASSEMBLY_CODE = RefAssmblClassKey.ENG;
   private static final String AIRCRAFT_KEY_WITH_SINGLE_RECORD = "8417120C7429487EAF69CD31AA2F17B1";
   private static final String AIRCRAFT_KEY_WITH_NO_RECORD = "8417120C7429487EAF69CD31AA2F17B2";
   private static final String AIRCRAFT_KEY_WITH_MULTIPLE_RECORD =
         "8417120C7429487EAF69CD31AA2F17B3";
   private static final String AIRCRAFT_KEY_WITH_SINGLE_ENGINE_NO_OIL_UPTAKE_RECORD =
         "8417120C7429487EAF69CD31AA2F17B4";


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInventoryOilUptakeDao = new JdbcInventoryOilUptakeDao();
   }


   @Test
   public void getOilUptakeForAircraftWithNoRecords() {
      UUID lAircraftKey = new UuidConverter().convertStringToUuid( AIRCRAFT_KEY_WITH_NO_RECORD );

      List<OilUptakeRecording> lPreviousInventoryOilUptakes =
            iInventoryOilUptakeDao.getLatestAssemblyOilUptakeRecordings( lAircraftKey );

      assertEquals( 0, lPreviousInventoryOilUptakes.size() );

   }


   @Test
   public void getOilUptakeWithSingleRecord() {

      UUID lAircraftKey =
            new UuidConverter().convertStringToUuid( AIRCRAFT_KEY_WITH_SINGLE_RECORD );

      List<OilUptakeRecording> lPreviousInventoryOilUptakes =
            iInventoryOilUptakeDao.getLatestAssemblyOilUptakeRecordings( lAircraftKey );

      assertEquals( 1, lPreviousInventoryOilUptakes.size() );

      List<OilUptakeRecording> lExpectedRecording = Arrays
            .asList( buildOilUptakeRecording( INVENTORY_1_ID, INVENTORY_1_NAME, QUANTITY_1 ) );

      assertEquals( lExpectedRecording, lPreviousInventoryOilUptakes );

   }


   @Test
   public void getOilUptakeWithMultipleRecords() {

      UUID lAircraftKey =
            new UuidConverter().convertStringToUuid( AIRCRAFT_KEY_WITH_MULTIPLE_RECORD );

      List<OilUptakeRecording> lPreviousInventoryOilUptakes =
            iInventoryOilUptakeDao.getLatestAssemblyOilUptakeRecordings( lAircraftKey );

      assertEquals( 2, lPreviousInventoryOilUptakes.size() );

      List<OilUptakeRecording> lExpectedRecording =
            Arrays.asList( buildOilUptakeRecording( INVENTORY_2_ID, INVENTORY_2_NAME, QUANTITY_2 ),
                  buildOilUptakeRecording( INVENTORY_3_ID, INVENTORY_3_NAME, QUANTITY_3 ) );

      assertEquals( lExpectedRecording, lPreviousInventoryOilUptakes );

   }


   @Test
   public void getInventoryInfoWithNoOilUptakeRecords() {

      UUID lAircraftKey = new UuidConverter()
            .convertStringToUuid( AIRCRAFT_KEY_WITH_SINGLE_ENGINE_NO_OIL_UPTAKE_RECORD );

      List<OilUptakeRecording> lPreviousInventoryOilUptakes =
            iInventoryOilUptakeDao.getLatestAssemblyOilUptakeRecordings( lAircraftKey );

      assertEquals( 1, lPreviousInventoryOilUptakes.size() );

      List<OilUptakeRecording> lExpectedRecording = Arrays
            .asList( buildOilUptakeRecording( INVENTORY_4_ID, INVENTORY_4_NAME, QUANTITY_NULL ) );

      assertEquals( lExpectedRecording, lPreviousInventoryOilUptakes );

   }


   private static OilUptakeRecording buildOilUptakeRecording( UUID aInventoryId,
         String aInventoryName, BigDecimal aQuantity ) {
      return OilUptakeRecording.builder().inventoryId( aInventoryId )
            .inventoryName( aInventoryName ).quantity( aQuantity ).dataTypeId( DATA_TYPE_ID )
            .dataTypeCode( DATA_TYPE_CODE ).dataTypeName( DATA_TYPE_NAME )
            .assemblyClassKey( ENGINE_ASSEMBLY_CODE ).dataTypePrecision( DATA_TYPE_PRECISION )
            .build();
   }
}
