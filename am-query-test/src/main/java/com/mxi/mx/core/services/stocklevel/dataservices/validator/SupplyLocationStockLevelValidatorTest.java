package com.mxi.mx.core.services.stocklevel.dataservices.validator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.services.location.InvalidLocationCodeException;
import com.mxi.mx.core.services.location.NotSupplyLocationException;
import com.mxi.mx.core.services.order.exception.PartWithFinancialTypeExpenseException;
import com.mxi.mx.core.services.stock.InvalidStockCodeException;
import com.mxi.mx.core.services.stocklevel.InvalidStockLowsActionException;
import com.mxi.mx.core.services.stocklevel.StockLevelAlreadyExistWithLocalOwnerException;
import com.mxi.mx.core.services.stocklevel.StockLevelException;
import com.mxi.mx.core.services.stocklevel.dataservices.DuplicateStockCodeException;
import com.mxi.mx.core.services.stocklevel.dataservices.InvalidAllocationPercentageException;
import com.mxi.mx.core.services.stocklevel.dataservices.InvalidStockLowActionForSupplyLocStockLevelException;


/**
 * Tests {@link SupplyLocationStockLevelValidator} class. Used to validate the order in which
 * exceptions are thrown.
 *
 * @author sufelk
 *
 */
@RunWith( Parameterized.class )
public class SupplyLocationStockLevelValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   SupplyLocationStockLevelValidator iSupplyLocStockLevelValidator =
         new SupplyLocationStockLevelValidator();

   // test parameters
   String iC0_StockCode;
   String iC1_StockLevelLocation;
   String iC2_Owner;
   String iC3_WeightingFactor;
   String iC4_NumInboundFlights;
   String iC5_StationWeightFactor;
   String iC6_AllocationPct;
   String iC7_SafetyLevel;
   String iC8_RestockLevel;
   String iC9_MaxLevel;
   String iC10_ReorderShippingQt;
   String iC11_StockLowAction;
   Class<Throwable> iException;

   // data
   private static final String STOCK_WITHASTOCKLEVEL = "STOCK_CD1";
   private static final String STOCK_NOSTOCKLEVELS = "STOCK_CD2";
   private static final String STOCK_DUPLICATEDINDB = "STOCK_CD3";
   private static final String STOCK_WITHEXPPART = "STOCK_CD4";
   private static final String AIRPORT = "BIA";
   private static final String AIRPORT2 = "BOSTAN";
   private static final String AIRPORT_WOSUPPLIER = "LOL";
   private static final String SRVSTORE = "BIA/SRVSTORE";
   private static final String OWNER_LOCAL = "SOWRY";
   private static final String OWNER_LOCAL2 = "ACE";
   private static final String OWNER_REMOTE = "REMOTESOWRY";
   private static final String MANUAL = RefStockLowActionKey.MANUAL.getCd();
   private static final String DISTREQ = RefStockLowActionKey.DISTREQ.getCd();
   private static final String SHIPREQ = RefStockLowActionKey.SHIPREQ.getCd();
   private static final String WEIGHTING_FACTOR = "1";
   private static final String NUM_INBOUND_FLIGHTS = "19";
   private static final String STATION_WEIGHT_FACTOR = "2";
   private static final String ALLOCATION_PCT = "0.7";
   private static final String SAFETY_LEVEL = "12";
   private static final String RESTOCK_LEVEL = "14";
   private static final String MAX_LEVEL = "92";
   private static final String REORDER_QT = "5";
   private static final String NEGATIVE_QT = "-5";


   /**
    *
    * Creates a new {@linkplain SupplyLocationStockLevelValidatorTest} object. C0..C11 are
    * components of a csv containing Supply Location Stock Level data uploaded to Maintenix, in the
    * form of a BulkLoadElementTO transfer object
    *
    * @param aC0
    *           maps to the stock code
    * @param aC1
    *           maps to the stock level location
    * @param aC2
    *           maps to the owner code
    * @param aC3
    *           maps to the weighting factor
    * @param aC4
    *           maps to the number of inbound flights
    * @param aC5
    *           maps to the station weight factor
    * @param aC6
    *           maps to the allocation percentage
    * @param aC7
    *           maps to the safety level
    * @param aC8
    *           maps to the restock level
    * @param aC9
    *           maps to the max level
    * @param aC10
    *           maps to the reorder/shipping quantity
    * @param aC11
    *           maps to the stock low action
    * @param aException
    *           the type of exception the validator is expected to throw. Null if no exceptions are
    *           expected.
    */
   public SupplyLocationStockLevelValidatorTest(String aC0, String aC1, String aC2, String aC3,
         String aC4, String aC5, String aC6, String aC7, String aC8, String aC9, String aC10,
         String aC11, Class<Throwable> aException) {
      iC0_StockCode = aC0;
      iC1_StockLevelLocation = aC1;
      iC2_Owner = aC2;
      iC3_WeightingFactor = aC3;
      iC4_NumInboundFlights = aC4;
      iC5_StationWeightFactor = aC5;
      iC6_AllocationPct = aC6;
      iC7_SafetyLevel = aC7;
      iC8_RestockLevel = aC8;
      iC9_MaxLevel = aC9;
      iC10_ReorderShippingQt = aC10;
      iC11_StockLowAction = aC11;
      iException = aException;
   }


   @Before
   public void setup() {
      StockBuilder lStockBuilder = new StockBuilder();

      // create valid stock numbers
      StockNoKey lStockKey = lStockBuilder.withStockCode( STOCK_WITHASTOCKLEVEL ).build();
      lStockBuilder.withStockCode( STOCK_NOSTOCKLEVELS ).build();

      // create a warehouse (srvstore) location
      Domain.createLocation( location -> {
         location.setCode( SRVSTORE );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( false );
      } );

      // create a valid supply location without a supplier location
      LocationKey lSupplyWOSupplierLoc = Domain.createLocation( location -> {
         location.setCode( AIRPORT_WOSUPPLIER );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
      } );

      // create a valid supply location
      LocationKey lSupplyLoc = Domain.createLocation( location -> {
         location.setCode( AIRPORT );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
         location.setSupplyLocation( lSupplyWOSupplierLoc );
      } );

      // create a valid supply locatuon with local owner
      LocationKey lSupplyLocLocal = Domain.createLocation( location -> {
         location.setCode( AIRPORT2 );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
         location.setSupplyLocation( lSupplyWOSupplierLoc );
      } );
      // create a local owner key
      OwnerKey lOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_LOCAL );
         aOwner.setDefaultBool( true );
         aOwner.setLocalBool( true );
      } );
      // create another local owner key
      OwnerKey lOwnerlocal = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_LOCAL2 );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );
      // create a remote owner
      Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_REMOTE );
         aOwner.setLocalBool( false );
      } );

      // create two stock numbers with the same code for the DuplicateStockLevelException test case
      lStockBuilder.withStockCode( STOCK_DUPLICATEDINDB ).build();
      lStockBuilder.withStockCode( STOCK_DUPLICATEDINDB ).build();

      // create a stock level with an owner
      new StockLevelBuilder( lSupplyLoc, lStockKey, lOwner ).build();

      // create supply location stock level with local owner
      new StockLevelBuilder( lSupplyLocLocal, lStockKey, lOwnerlocal ).build();

      // data setup to test PartWithFinancialTypeExpenseException
      Domain.createPart( aPart -> {
         aPart.setStockNoKey( lStockBuilder.withStockCode( STOCK_WITHEXPPART ).build() );
         aPart.setFinancialType( RefFinanceTypeKey.EXPENSE );
      } );
   }


   @Parameterized.Parameters
   public static Collection<Object[]> ErrorScenarios() {
      return Arrays.asList( new Object[][] {
            // InvalidStockCodeException when the stock code does not exist
            { "WrongStockCd", AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, InvalidStockCodeException.class },

            // InvalidLocationCodeException when the location code does not exist
            { STOCK_NOSTOCKLEVELS, "WrongLocationCd", OWNER_LOCAL, WEIGHTING_FACTOR,
                  NUM_INBOUND_FLIGHTS, STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL,
                  RESTOCK_LEVEL, MAX_LEVEL, REORDER_QT, MANUAL,
                  InvalidLocationCodeException.class },
            // StockLevelAlreadyExistWithLocalOwnerException when another local owner is set in the
            // csv
            { STOCK_WITHASTOCKLEVEL, AIRPORT2, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, StockLevelAlreadyExistWithLocalOwnerException.class },

            // NotSupplyLocationException when the location exists, but is not a valid supply
            // location
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, NotSupplyLocationException.class },

            // DuplicateStockCodeException when the stock code is duplicated
            { STOCK_DUPLICATEDINDB, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, DuplicateStockCodeException.class },

            // InvalidStockLowActionForSupplyLocStockLevelException when stock low action is not
            // POREQ, SHIPREQ or MANUAL
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, DISTREQ, InvalidStockLowActionForSupplyLocStockLevelException.class },

            // InvalidStockLowActionException when attempting to create a stock level with the
            // SHIPREQ stock low action for a location that does not have a supplier location
            // defined
            { STOCK_NOSTOCKLEVELS, AIRPORT_WOSUPPLIER, OWNER_LOCAL, WEIGHTING_FACTOR,
                  NUM_INBOUND_FLIGHTS, STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL,
                  RESTOCK_LEVEL, MAX_LEVEL, REORDER_QT, SHIPREQ,
                  InvalidStockLowsActionException.class },

            // StockLevelException when the restock level is greater than the max level
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, "2",
                  REORDER_QT, MANUAL, StockLevelException.class },

            // InvalidAllocationPercentageException when the allocation percentage is greater than
            // 100
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, "65536", SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, InvalidAllocationPercentageException.class },

            // PartWithFinancialTypeExpenseException when attempting to create a stock level for a
            // stock that contains part with the financial type EXPENSE, with a remote owner
            { STOCK_WITHEXPPART, AIRPORT, OWNER_REMOTE, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, PartWithFinancialTypeExpenseException.class },

            // Negative Allocation Percentage
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, NEGATIVE_QT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, NegativeValueException.class },
            // Negative Safety Level
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, NEGATIVE_QT, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, NegativeValueException.class },
            // Negative Reorder Level
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, NEGATIVE_QT, MAX_LEVEL,
                  REORDER_QT, MANUAL, NegativeValueException.class },
            // Negative MAX Level
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, NEGATIVE_QT,
                  REORDER_QT, MANUAL, NegativeValueException.class },
            // No exceptions thrown when the data is valid
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_LOCAL, WEIGHTING_FACTOR, NUM_INBOUND_FLIGHTS,
                  STATION_WEIGHT_FACTOR, ALLOCATION_PCT, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  REORDER_QT, MANUAL, null } } );
   }


   /**
    *
    * GIVEN a row of data in the form of a BulkLoadElementTO transfer object, WHEN the transfer
    * object is passed to the {@link SupplyLocationStockLevelValidator.validateBeforeInsert} method,
    * THEN an exception of the type specified in the iException parameter should be thrown.
    *
    * @throws Exception
    *            of the specified type
    */
   @Test
   public void test_validateBeforeInsert() throws Exception {

      // if an exception is expected, set the ExpectedException jUnit rule.
      if ( iException != null ) {
         iExpectedException.expect( iException );
      }

      // create a BulkLoadElementTo transfer object and pass it to the validateBeforeInsert method
      // validateBeforInsert is called instead of validateCommon because it contains all validations
      iSupplyLocStockLevelValidator.validateBeforeInsert( new BulkLoadElementTO( 0,
            RefBulkLoadFileActionKey.SUPPLY_LOC_STOCK_LEVEL, RefBulkLoadStatusKey.PROCESSING, "",
            iC0_StockCode, iC1_StockLevelLocation, iC2_Owner, iC3_WeightingFactor,
            iC4_NumInboundFlights, iC5_StationWeightFactor, iC6_AllocationPct, iC7_SafetyLevel,
            iC8_RestockLevel, iC9_MaxLevel, iC10_ReorderShippingQt, iC11_StockLowAction ) );
   }

}
