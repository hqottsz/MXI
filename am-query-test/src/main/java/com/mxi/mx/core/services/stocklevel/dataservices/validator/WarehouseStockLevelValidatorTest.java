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
import com.mxi.mx.core.services.inventory.exception.InvalidOwnerCodeException;
import com.mxi.mx.core.services.location.InvalidLocationCodeException;
import com.mxi.mx.core.services.location.NotWarehouseLocationException;
import com.mxi.mx.core.services.order.exception.PartWithFinancialTypeExpenseException;
import com.mxi.mx.core.services.stock.InvalidStockCodeException;
import com.mxi.mx.core.services.stocklevel.InvalidStockLowActionForWarehouseStockLevelException;
import com.mxi.mx.core.services.stocklevel.InvalidWarehouseStockLowsActionValidator;
import com.mxi.mx.core.services.stocklevel.StockLevelAlreadyExistWithLocalOwnerException;
import com.mxi.mx.core.services.stocklevel.StockLevelException;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelOptionalOwnerException;
import com.mxi.mx.core.services.stocklevel.dataservices.DuplicateStockCodeException;


/**
 * Tests the {@link WarehouseStockLevelValidator} class. Used to validate the order in which
 * exceptions are thrown.
 *
 * @author sufelk
 */
@RunWith( Parameterized.class )
public class WarehouseStockLevelValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   WarehouseStockLevelValidator iWarehouseStockLevelValidator = new WarehouseStockLevelValidator();

   // test parameters
   String iC0_StockCode;
   String iC1_StockLevelLocation;
   String iC2_Owner;
   String iC3_SafetyLevel;
   String iC4_RestockLevel;
   String iC5_MaxLevel;
   String iC6_StockLowAction;
   Class<Throwable> iException;

   // data
   private static final String STOCK_WITHASTOCKLEVEL = "STOCK_CD1";
   private static final String STOCK_NOSTOCKLEVELS = "STOCK_CD2";
   private static final String STOCK_DUPLICATEDINDB = "STOCK_CD3";
   private static final String STOCK_WITHEXPPART = "STOCK_CD4";
   private static final String AIRPORT = "BIA";
   private static final String SRVSTORE = "BIA/SRVSTORE";
   private static final String SRVSTORE2 = "ATL/SRVSTORE";
   private static final String OWNER_CD = "SOWRY";
   private static final String OWNER_LOCAL_CD = "ACE";
   private static final String REMOTE_OWNER = "REMOTESOWRY";
   private static final String MANUAL = RefStockLowActionKey.MANUAL.getCd();
   private static final String DISTREQ = RefStockLowActionKey.DISTREQ.getCd();
   private static final String SAFETY_LEVEL = "12";
   private static final String RESTOCK_LEVEL = "14";
   private static final String MAX_LEVEL = "92";


   /**
    *
    * Creates a new {@linkplain WarehouseStockLevelValidatorTest} object. C0..C6 are components of
    * an uploaded WarehouseStockLevel csv file formatted as a BulkLoadElementTO transfer object
    *
    * @param aC0
    *           maps to the Stock Code
    * @param aC1
    *           maps to the Stock Level Location
    * @param aC2
    *           maps to the Owner
    * @param aC3
    *           maps to the Safety Level
    * @param aC4
    *           maps to the safety level
    * @param aC5
    *           maps to the restock level
    * @param aC6
    *           maps to the max level
    * @param aException
    *           the type of exception the validator is expected to throw. Null if no exception is
    *           expected
    */
   public WarehouseStockLevelValidatorTest(String aC0, String aC1, String aC2, String aC3,
         String aC4, String aC5, String aC6, Class<Throwable> aException) {
      iC0_StockCode = aC0;
      iC1_StockLevelLocation = aC1;
      iC2_Owner = aC2;
      iC3_SafetyLevel = aC3;
      iC4_RestockLevel = aC4;
      iC5_MaxLevel = aC5;
      iC6_StockLowAction = aC6;
      iException = aException;
   }


   @Before
   public void setup() {
      StockBuilder lStockBuilder = new StockBuilder();

      // create valid stock numbers
      StockNoKey lStockKey = lStockBuilder.withStockCode( STOCK_WITHASTOCKLEVEL ).build();
      lStockBuilder.withStockCode( STOCK_NOSTOCKLEVELS ).build();

      // create a valid warehouse (srvstore) location
      LocationKey lSrvstoreLoc = Domain.createLocation( location -> {
         location.setCode( SRVSTORE );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( false );
      } );

      // create a valid another warehouse (srvstore) location
      LocationKey lSrvstoreLoc2 = Domain.createLocation( location -> {
         location.setCode( SRVSTORE2 );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( false );
      } );

      // create a non-srvstore location
      Domain.createLocation( location -> {
         location.setCode( AIRPORT );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
      } );

      // create an owner key
      OwnerKey lOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_CD );
         aOwner.setDefaultBool( true );
         aOwner.setLocalBool( true );
      } );
      // create a local owner
      OwnerKey lOwnerlocal = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_LOCAL_CD );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );
      // create a remote owner
      Domain.createOwner( aOwner -> {
         aOwner.setCode( REMOTE_OWNER );
         aOwner.setLocalBool( false );
      } );

      // create two stock numbers with the same code for the DuplicateStockLevelException test case
      lStockBuilder.withStockCode( STOCK_DUPLICATEDINDB ).build();
      lStockBuilder.withStockCode( STOCK_DUPLICATEDINDB ).build();

      // create a stock level with an owner for the the WarehouseStockLevelOptionalOwnerException
      // test case
      new StockLevelBuilder( lSrvstoreLoc, lStockKey, lOwner ).withIgnoreOwnerBool( true ).build();

      // create a stock level with an local owner for the the
      // StockLevelAlreadyExistWithLocalOwnerException
      // test case
      new StockLevelBuilder( lSrvstoreLoc2, lStockKey, lOwnerlocal ).withIgnoreOwnerBool( false )
            .build();

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
            { "WrongStockCd", SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL, MANUAL,
                  InvalidStockCodeException.class },

            // InvalidLocationCodeException when the location code does not exist
            { STOCK_NOSTOCKLEVELS, "WrongLocationCd", OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL,
                  MAX_LEVEL, MANUAL, InvalidLocationCodeException.class },

            // DuplicateStockCodeException when the stock code is duplicated
            { STOCK_DUPLICATEDINDB, SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, DuplicateStockCodeException.class },

            // WarehouseStockLevelOptionalOwnerException when an owner is defined in the db, but the
            // owner field in the csv is left blank
            { STOCK_WITHASTOCKLEVEL, SRVSTORE2, "", SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL, MANUAL,
                  WarehouseStockLevelOptionalOwnerException.class },

            // InvalidOwnerCodeException when an incorrect owner code is defined in the csv
            { STOCK_NOSTOCKLEVELS, SRVSTORE, "WrongOwner", SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, InvalidOwnerCodeException.class },
            // StockLevelAlreadyExistWithLocalOwnerException when another local owner is set in the
            // csv
            { STOCK_WITHASTOCKLEVEL, SRVSTORE2, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, StockLevelAlreadyExistWithLocalOwnerException.class },
            // NotWarehouseLocationException when the location code does not belong to a valid
            // non-supply SRVSTORE location
            { STOCK_NOSTOCKLEVELS, AIRPORT, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, NotWarehouseLocationException.class },

            // InvalidStockLowActionForWarehouseStockLevelException when the stock low action is
            // invalid
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  "WrongStckLowActn", InvalidStockLowActionForWarehouseStockLevelException.class },

            // InvalidWarehouseStockLowsActionValidator when creating a stock level with the DISTREQ
            // stock low action for a SRVSTORE which does not have a supply location defined
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  DISTREQ, InvalidWarehouseStockLowsActionValidator.class },

            // StockLevelException when the reorder level is smaller than the safety level
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_CD, "25", RESTOCK_LEVEL, MAX_LEVEL, MANUAL,
                  StockLevelException.class },

            // StockLevelException when the max level is smaller than the reorder level
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, "2", MANUAL,
                  StockLevelException.class },

            // PartWithFinancialTypeExpenseException when attempting to create a stock level for a
            // stock that contains part with the financial type EXPENSE, with a remote owner
            { STOCK_WITHEXPPART, SRVSTORE, REMOTE_OWNER, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, PartWithFinancialTypeExpenseException.class },

            // No exceptions thrown when the data is valid
            { STOCK_NOSTOCKLEVELS, SRVSTORE, OWNER_CD, SAFETY_LEVEL, RESTOCK_LEVEL, MAX_LEVEL,
                  MANUAL, null } } );

   }


   /**
    *
    * GIVEN a row of data in the form of a BulkLoadElementTO transfer object, WHEN the transfer
    * object is passed to the {@link WarehouseStockLevelValidator.validateCommon} method, THEN an
    * exception of the type specified in the iException parameter should be thrown.
    *
    * @throws Exception
    *            of the specified type
    */
   @Test
   public void test_validateBeforeInsert() throws Exception {

      // if an exception is expected, set the ExpectedException jUnit rule. This asserts whether the
      // exception is thrown- no additional assertions are needed.
      if ( iException != null ) {
         iExpectedException.expect( iException );
      }

      // create a BulkLoadElementTO transfer object and pass it to the validateBeforeInsert method
      // validateBeforInsert is called instead of validateCommon because it contains all validations
      iWarehouseStockLevelValidator.validateBeforeInsert( new BulkLoadElementTO( 0,

            RefBulkLoadFileActionKey.WAREHOUSE_STOCK_LEVEL, RefBulkLoadStatusKey.PROCESSING, "",
            iC0_StockCode, iC1_StockLevelLocation, iC2_Owner, iC3_SafetyLevel, iC4_RestockLevel,
            iC5_MaxLevel, iC6_StockLowAction ) );

   }

}
