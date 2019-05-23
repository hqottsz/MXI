package com.mxi.am.api.resource.materials.plan.stock.stocklevel.warehouse;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiMandatoryParameterMissingException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.api.resource.model.RawId;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelCheckWorkItem;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for WarehouseStockLevelResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class WarehouseStockLevelResourceBeanTest extends ResourceBeanTest {

   private static final String STOCK_1_ID = "601435E495494F34965B1588F5A6036B";
   private static final String STOCK_1_CODE = "STOCK_1";
   private static final String STOCK_1_KEY = "4650:1";
   private static final String STOCK_2_ID = "601435E495494F34965B1588F5A6036C";
   private static final String STOCK_3_ID = "601435E495494F34965B1588F5A6036E";
   private static final String STOCK_3_KEY = "4650:3";
   private static final String STOCK_4_ID = "601435E495494F34965B1588F5A6036F";
   private static final String STOCK_5_ID = "601435E495494F34965B1588F5A6036D";
   private static final String NOT_FOUND_STOCK_ID = "700035E495494F34965B1588F5A6036A";
   private static final String STOCK_1_STOCKLEVEL_1_ID = "A01435E495494F34965B1588F5A60361";
   private static final String STOCK_1_STOCKLEVEL_2_ID = "B01435E495494F34965B1588F5A60362";
   private static final String STOCK_4_STOCKLEVEL_1_ID = "B01435E495494F34965B1588F5A60363";
   private static final String STOCK_5_STOCKLEVEL_1_ID = "C01435E495494F34965B1588F5A60361";
   private static final String WAREHOUSE_LOCATION_1_ID = "101435E495494F34965B1588F5A60362";
   private static final String WAREHOUSE_LOCATION_1_CODE = "AIRPORT1/STORE1";
   private static final String WAREHOUSE_LOCATION_2_ID = "201435E495494F34965B1588F5A60362";
   private static final String WAREHOUSE_LOCATION_2_CODE = "AIRPORT1/STORE2";
   private static final String WAREHOUSE_LOCATION_2_KEY = "4650:2";
   private static final String WAREHOUSE_LOCATION_WITH_SUPPLIER_ID =
         "201435E495494F34965B1588F5A60364";
   private static final String WAREHOUSE_LOCATION_WITH_SUPPLIER_KEY = "4650:4";
   private static final String AIRPORT_LOCATION_ID = "201435E495494F34965B1588F5A60363";
   private static final String AIRPORT_LOCATION_CD = "AIRPORT1";
   private static final String INVALID_LOCATION_ID = "123";
   private static final String OWNER_ID = "301435E495494F34965B1588F5A60362";
   private static final String OWNER_CODE = "OWNER_1";
   private static final String INVALID_OWNER_ID = "123";
   private static final String EXCEPTION_TYPE_ID = "Stock";
   private static final String MANUAL_STOCK_LOW_ACTION = RefStockLowActionKey.MANUAL.getCd();
   private static final String DISTREQ_STOCK_LOW_ACTION = RefStockLowActionKey.DISTREQ.getCd();
   private static final String SHIPREQ_STOCK_LOW_ACTION = RefStockLowActionKey.SHIPREQ.getCd();
   private static final String STOCK_4_STOCKLEVEL_1_REVISIONDATE = "2018-01-12 00:00:00";

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Inject
   WarehouseStockLevelResourceBean warehouseStockLevelResourceBean;

   @Inject
   WarehouseStockLevelSearchResourceBean warehouseStockLevelSearchResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;

   private List<WarehouseStockLevel> expectedWarehouseStockLevelsForStock1;
   private List<WarehouseStockLevel> expectedWarehouseStockLevelsForStock4;
   private List<WarehouseStockLevel> expectedWarehouseStockLevelsForStock5;

   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            disableTriggers();
         }, () -> {
            enableTriggers();
         } );


   static void enableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_INV_LOC_STOCK" );
   }


   static void disableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_INV_LOC_STOCK" );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(),
            WarehouseStockLevelResourceBeanTest.class );
      initializeSecurityContext();
   }


   @Before
   public void setUp() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      warehouseStockLevelResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      constructExpectedResults();
   }


   @Test
   public void testGetListReturnedSuccess() throws AmApiResourceNotFoundException {
      List<WarehouseStockLevel> warehouseStockLevels =
            warehouseStockLevelResourceBean.get( STOCK_1_ID );
      assertWarehouseStockLevels( expectedWarehouseStockLevelsForStock1, warehouseStockLevels );
   }


   @Test
   public void testGetEmptyListSuccess() throws AmApiResourceNotFoundException {
      List<WarehouseStockLevel> warehouseStockLevels =
            warehouseStockLevelResourceBean.get( STOCK_2_ID );

      Assert.assertNotNull( "Expected an empty list of warehouse stock levels",
            warehouseStockLevels );
      Assert.assertEquals( "Expected an empty list of warehouse stock levels", 0,
            warehouseStockLevels.size() );
   }


   @Test
   public void testGetStockNotFound() {
      try {
         warehouseStockLevelResourceBean.get( NOT_FOUND_STOCK_ID );
         Assert.fail( "Did not throw AmApiResourceNotFoundException" );
      } catch ( AmApiResourceNotFoundException e ) {
         String message = EXCEPTION_TYPE_ID + " " + NOT_FOUND_STOCK_ID + " not found";
         Assert.assertEquals( "Error Message", message, e.getMessage() );
      } catch ( Exception e ) {
         Assert.fail( "Did not throw a AmApiResourceNotFoundException, instead threw "
               + e.getClass().getName() );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchSuccess() {

      WarehouseStockLevelSearchParameters parms = new WarehouseStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( STOCK_1_ID, STOCK_5_ID ) );

      List<WarehouseStockLevel> warehouseStockLevels =
            warehouseStockLevelSearchResourceBean.search( parms );

      List<WarehouseStockLevel> expectedResults = new ArrayList<WarehouseStockLevel>();
      expectedResults.addAll( expectedWarehouseStockLevelsForStock1 );
      expectedResults.addAll( expectedWarehouseStockLevelsForStock5 );

      assertWarehouseStockLevels( expectedResults, warehouseStockLevels );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchEmptyListSuccess() {
      // search for stock levels for a stock which doesn't exist
      // and for a stock with no stock levels
      WarehouseStockLevelSearchParameters parms = new WarehouseStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( NOT_FOUND_STOCK_ID, STOCK_3_ID ) );

      List<WarehouseStockLevel> warehouseStockLevels =
            warehouseStockLevelSearchResourceBean.search( parms );

      Assert.assertNotNull( "Expected an empty list of warehouse stock levels",
            warehouseStockLevels );
      Assert.assertEquals( "Expected an empty list of warehouse stock levels", 0,
            warehouseStockLevels.size() );
   }


   @Test
   public void testSearchMandatoryParameterMissing() {
      try {
         WarehouseStockLevelSearchParameters parms = new WarehouseStockLevelSearchParameters();
         warehouseStockLevelSearchResourceBean.search( parms );
         Assert.fail( "Did not throw AmApiMandatoryParameterMissingException" );
      } catch ( AmApiMandatoryParameterMissingException e ) {
         // pass
      }
   }


   @Test
   public void testSearchNoParameters() {
      try {
         warehouseStockLevelSearchResourceBean.search( null );
         Assert.fail( "Did not throw AmApiMandatoryParameterMissingException" );
      } catch ( AmApiMandatoryParameterMissingException e ) {
         // pass
      }
   }


   @Test
   public void testPutWithNoPayloadFailure() throws AmApiResourceNotFoundException {
      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, null );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Missing warehouse stock level payload.", ex.getMessage() );
      }
   }


   @Test
   public void testCreateMandatoryFieldsOnlySuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      WarehouseStockLevel warehouseStockLevel1 =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      WarehouseStockLevel warehouseStockLevel2 =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_2_ID );
      List<WarehouseStockLevel> warehouseStockLevelList =
            Arrays.asList( warehouseStockLevel1, warehouseStockLevel2 );

      List<WarehouseStockLevel> actualWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            actualWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 2,
            actualWarehouseStockLevelList.size() );

      assertWarehouseStockLevelCreatedWithDefaultFields( actualWarehouseStockLevelList.get( 0 ),
            STOCK_3_ID, WAREHOUSE_LOCATION_1_ID );
      assertWarehouseStockLevelCreatedWithDefaultFields( actualWarehouseStockLevelList.get( 1 ),
            STOCK_3_ID, WAREHOUSE_LOCATION_2_ID );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testCreateAllFieldsSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_WITH_SUPPLIER_ID );
      warehouseStockLevel.setStockId( STOCK_3_ID );
      warehouseStockLevel.setOwnerId( OWNER_ID );
      warehouseStockLevel.setStockLowAction( DISTREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      List<WarehouseStockLevel> actualWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            actualWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            actualWarehouseStockLevelList.size() );

      warehouseStockLevel.setId( actualWarehouseStockLevelList.get( 0 ).getId() );
      warehouseStockLevel.setSafetyLevel( 0 );
      warehouseStockLevel.setRestockLevel( 0 );
      Assert.assertTrue(
            "Expected: " + warehouseStockLevel + "but was: "
                  + actualWarehouseStockLevelList.get( 0 ),
            warehouseStockLevel.equals( actualWarehouseStockLevelList.get( 0 ) ) );

      assertThatOneWorkItemGenerated( STOCK_3_KEY, WAREHOUSE_LOCATION_WITH_SUPPLIER_KEY );
   }


   @Test
   public void testCreateExistingWarehouseStockLevelFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setOwnerId( OWNER_ID );
      expectedWarehouseStockLevelsForStock1.add( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID,
               expectedWarehouseStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and owner ID '" + OWNER_ID
               + "' could not be created because of the following error: [MXERR-30042] Cannot create new stock level. A stock level already exists for Stock Number '"
               + STOCK_1_CODE + "', Owner '" + OWNER_CODE + "' at Location '"
               + WAREHOUSE_LOCATION_1_CODE + "'";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateExistingWarehouseStockLevelWithOwnerFailure()
         throws AmApiResourceNotFoundException {

      // Scenario: existing stock level has an owner, new stock level has same location and no owner
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setOwnerId( null );
      expectedWarehouseStockLevelsForStock1.add( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID,
               expectedWarehouseStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-50263] A warehouse stock level for Stock Number "
               + STOCK_1_CODE + " at Location " + WAREHOUSE_LOCATION_1_CODE
               + " already exists in the system with an owner. <br><br> To create a new stock level without an owner, remove the existing stock level and create a new one";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );

      }
   }


   @Test
   public void testCreateExistingWarehouseStockLevelWithoutOwnerFailure()
         throws AmApiResourceNotFoundException {
      // Scenario: existing stock level has no owner, new stock level same location and an owner
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_2_ID );
      warehouseStockLevel.setOwnerId( OWNER_ID );

      expectedWarehouseStockLevelsForStock1.add( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID,
               expectedWarehouseStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_2_ID
               + "' and owner ID '" + OWNER_ID
               + "' could not be created because of the following error: [MXERR-50262] A warehouse stock level for Stock Number "
               + STOCK_1_CODE + " at Location " + WAREHOUSE_LOCATION_2_CODE
               + " already exists in the system without an owner. <br><br> To create a new stock level with or without an owner, remove the existing stock level and create a new one.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );

      }
   }


   @Test
   public void testCreateWithNegativeSafetyLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setSafetyLevel( -3 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-10001] The 'safetyLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );

      }
   }


   @Test
   public void testCreateWithInvalidSafetyLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setSafetyLevel( 6 );
      warehouseStockLevel.setRestockLevel( 4 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: The Safety Level value is greater than the Restock Level value.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithSafetyLevelButNoRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setSafetyLevel( 1 );
      warehouseStockLevel.setRestockLevel( null );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: The Safety Level value is greater than the Restock Level value.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithNegativeRestockLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setRestockLevel( -3 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-10001] The 'restockLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithNegativeMaxLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setMaxLevel( -1 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-10001] The 'maxLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithInvalidMaxLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setRestockLevel( 4 );
      warehouseStockLevel.setMaxLevel( 3 );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-30076] The Max Level must be greater than the Restock Level for Location "
               + WAREHOUSE_LOCATION_1_CODE + ".";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithStockLowActionNotForWarehouseStockLevelFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setStockLowAction( SHIPREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: The Stock Low Action should be 'DISTREQ' or 'MANUAL' for warehouse stock levels.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithInvalidLocationFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( INVALID_LOCATION_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + INVALID_LOCATION_ID
               + "' and without an owner could not be created because of the following error: No location with the ID '"
               + INVALID_LOCATION_ID + "' could be found in Maintenix.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithNonWarehouseLocationFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( AIRPORT_LOCATION_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + AIRPORT_LOCATION_ID
               + "' and without an owner could not be created because of the following error: [MXERR-33110] The location chosen, "
               + AIRPORT_LOCATION_CD + " is not a warehouse location.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateWithInvalidOwnerFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setOwnerId( INVALID_OWNER_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and owner ID '" + INVALID_OWNER_ID
               + "' could not be created because of the following error: No owner with the ID '"
               + INVALID_OWNER_ID + "' could be found in Maintenix.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testCreateAtLocationWithoutSupplyLocationForDISTREQFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setStockLowAction( DISTREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: [MXERR-33111] The Stock Low Action cannot be set to 'DISTREQ' for Location '"
               + WAREHOUSE_LOCATION_1_CODE
               + "'. <br><br>Make sure you specify a Supplier Location for the warehouse Location from the Location Details page.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );

      }
   }


   @Test
   public void testCreateStockIdsNotMatchFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setStockId( STOCK_1_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateStockIdsNotMatchFailure() throws AmApiResourceNotFoundException {
      List<WarehouseStockLevel> warehouseStockLevelList =
            warehouseStockLevelResourceBean.get( STOCK_1_ID );
      // set an invalid stock ID
      warehouseStockLevelList.get( 0 ).setStockId( STOCK_2_ID );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateStockIDIsEmptyString() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setStockId( "" );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "The stock level with location ID '" + WAREHOUSE_LOCATION_1_ID
               + "' and without an owner could not be created because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateStockIDIsEmptyString() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setStockId( "" );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithNegativeSafetyLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setSafetyLevel( -3 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: [MXERR-10001] The 'safetyLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithInvalidSafetyLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setSafetyLevel( 3 );
      warehouseStockLevel.setRestockLevel( 2 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The Safety Level value is greater than the Restock Level value.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithSafetyLevelGreaterThanExistingRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setSafetyLevel( 2 );
      warehouseStockLevel.setRestockLevel( null );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The Safety Level value is greater than the Restock Level value.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithNegativeRestockLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setSafetyLevel( -2 );
      warehouseStockLevel.setRestockLevel( -2 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: [MXERR-10001] The 'restockLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithInvalidMaxLevelFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setRestockLevel( 4 );
      warehouseStockLevel.setMaxLevel( 3 );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: [MXERR-30076] The Max Level must be greater than the Restock Level for Location "
               + WAREHOUSE_LOCATION_1_CODE + ".";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );

      }
   }


   @Test
   public void testUpdateWithStockLowActionNotForWarehouseStockLevelFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setStockLowAction( SHIPREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The Stock Low Action should be 'DISTREQ' or 'MANUAL' for warehouse stock levels.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithInvalidLocationFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setLocationId( INVALID_LOCATION_ID );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The location and/or owner of an existing stock level cannot be updated, instead a new stock level must be created.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateWithInvalidOwnerFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setOwnerId( INVALID_OWNER_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: The location and/or owner of an existing stock level cannot be updated, instead a new stock level must be created.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @Test
   public void testUpdateAtLocationWithoutSupplyLocationForDISTREQFailure()
         throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( AIRPORT_LOCATION_ID );
      warehouseStockLevel.setId( STOCK_4_STOCKLEVEL_1_ID );
      warehouseStockLevel.setStockLowAction( DISTREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_4_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         String expectedMessage = "The stock level with ID '" + STOCK_4_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: [MXERR-33111] The Stock Low Action cannot be set to 'DISTREQ' for Location '"
               + AIRPORT_LOCATION_CD
               + "'. <br><br>Make sure you specify a Supplier Location for the warehouse Location from the Location Details page.";
         Assert.assertTrue( "Expected exception message: [" + expectedMessage + "] but was: ["
               + ex.getMessage() + "]", ex.getMessage().contains( expectedMessage ) );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdatedAllFieldsSuccess() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_2_ID );
      warehouseStockLevel.setStockId( STOCK_1_ID );
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_2_ID );
      warehouseStockLevel.setRestockLevel( 10 );
      warehouseStockLevel.setSafetyLevel( 10 );
      warehouseStockLevel.setMaxLevel( 10 );
      warehouseStockLevel.setStockLowAction( DISTREQ_STOCK_LOW_ACTION );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      List<WarehouseStockLevel> updatedWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            updatedWarehouseStockLevelList.size() );

      Assert.assertEquals( "Restock Level", new Integer( 10 ),
            updatedWarehouseStockLevelList.get( 0 ).getRestockLevel() );
      Assert.assertEquals( "Safety Level", new Integer( 10 ),
            updatedWarehouseStockLevelList.get( 0 ).getSafetyLevel() );
      Assert.assertEquals( "Max Level", new Integer( 10 ),
            updatedWarehouseStockLevelList.get( 0 ).getMaxLevel() );
      Assert.assertEquals( "Stock Low Action", DISTREQ_STOCK_LOW_ACTION,
            updatedWarehouseStockLevelList.get( 0 ).getStockLowAction() );

      assertThatOneWorkItemGenerated( STOCK_1_KEY, WAREHOUSE_LOCATION_2_KEY );
   }


   @Test
   public void testUpdateOneFieldSuccess() throws AmApiResourceNotFoundException, ParseException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( AIRPORT_LOCATION_ID );
      warehouseStockLevel.setId( STOCK_4_STOCKLEVEL_1_ID );
      warehouseStockLevel.setMaxLevel( 4 );

      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      WarehouseStockLevel expectedUpdateResult = expectedWarehouseStockLevelsForStock4.get( 0 );
      expectedUpdateResult.setMaxLevel( 4 );

      List<WarehouseStockLevel> updatedWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_4_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            updatedWarehouseStockLevelList.size() );

      WarehouseStockLevel actualUpdateResult = updatedWarehouseStockLevelList.get( 0 );
      Assert.assertTrue( "expected: " + expectedUpdateResult + " /n but was: " + actualUpdateResult,
            expectedUpdateResult.equals( actualUpdateResult ) );

      assertRevisionDateChanged( true, expectedUpdateResult.getId(),
            STOCK_4_STOCKLEVEL_1_REVISIONDATE );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateNoChangesSuccess() throws AmApiResourceNotFoundException, ParseException {

      List<WarehouseStockLevel> warehouseStockLevelList =
            warehouseStockLevelResourceBean.get( STOCK_4_ID );

      List<WarehouseStockLevel> updatedWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_4_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            updatedWarehouseStockLevelList.size() );
      Assert.assertTrue(
            "expected: " + warehouseStockLevelList + " /n but was: "
                  + updatedWarehouseStockLevelList,
            warehouseStockLevelList.equals( updatedWarehouseStockLevelList ) );

      assertRevisionDateChanged( false, warehouseStockLevelList.get( 0 ).getId(),
            STOCK_4_STOCKLEVEL_1_REVISIONDATE );
   }


   @Test
   public void testUpdateStockLevelNotInStockFailure() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      // set an ID which doesn't match any existing stock level for stock #3
      warehouseStockLevel.setId( STOCK_1_STOCKLEVEL_1_ID );
      warehouseStockLevel.setStockId( STOCK_3_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
               + "' could not be updated because of the following error: A matching stock level could not be found for the given stock.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateStockLevelIDIsEmptyString() throws AmApiResourceNotFoundException {
      WarehouseStockLevel warehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      warehouseStockLevel.setId( "" );
      warehouseStockLevel.setStockId( STOCK_3_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel );

      try {
         warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "The stock level with ID '' could not be updated because of the following error: A matching stock level could not be found for the given stock.",
               ex.getMessage() );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testDeleteOneStockLevelSuccess() throws AmApiResourceNotFoundException {
      List<WarehouseStockLevel> warehouseStockLevelList =
            warehouseStockLevelResourceBean.get( STOCK_1_ID );
      warehouseStockLevelList.remove( expectedWarehouseStockLevelsForStock1.get( 0 ) );

      List<WarehouseStockLevel> updatedWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_1_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            updatedWarehouseStockLevelList.size() );
      Assert.assertTrue( expectedWarehouseStockLevelsForStock1.get( 1 )
            .equals( updatedWarehouseStockLevelList.get( 0 ) ) );
   }


   @Test
   public void testDeleteAllStockLevelsSuccess() throws AmApiResourceNotFoundException {
      List<WarehouseStockLevel> warehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_1_ID, new ArrayList<>() );

      Assert.assertNotNull( "Expected an empty list of warehouse stock levels",
            warehouseStockLevelList );
      Assert.assertTrue( "Expected an empty list of warehouse stock levels",
            CollectionUtils.isEmpty( warehouseStockLevelList ) );
   }


   @Test
   public void testDeleteAndCreateSameStockLevelSuccess() throws AmApiResourceNotFoundException {

      // setting the stock level ID of an existing stock level to null will cause
      // that stock level to be deleted (since its ID will not be in the updated list)
      // and for an identical stock level to be created (null ID = create)
      expectedWarehouseStockLevelsForStock1.get( 0 ).setId( null );
      List<WarehouseStockLevel> updatedWarehouseStockLevelList = warehouseStockLevelResourceBean
            .update( STOCK_1_ID, expectedWarehouseStockLevelsForStock1 );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 2,
            updatedWarehouseStockLevelList.size() );

      // find the ID of the newly created stock level
      String newId = updatedWarehouseStockLevelList.get( 0 ).getId();
      if ( updatedWarehouseStockLevelList.get( 0 ).getId().equals( STOCK_1_STOCKLEVEL_2_ID ) ) {
         // we don't want the ID of the updated stock level, we want the other one
         newId = updatedWarehouseStockLevelList.get( 1 ).getId();
      }
      Assert.assertTrue( "Expected a new stock level to be created",
            !( STOCK_1_STOCKLEVEL_1_ID.equals( newId ) ) );

      // set the new ID on the expected stock level so we can compare the lists
      expectedWarehouseStockLevelsForStock1.get( 0 ).setId( newId );
      Assert.assertTrue(
            "Expected: " + expectedWarehouseStockLevelsForStock1 + " /n but was: "
                  + updatedWarehouseStockLevelList,
            updatedWarehouseStockLevelList.containsAll( expectedWarehouseStockLevelsForStock1 ) );
   }


   @Test
   public void testUpdateMultipleChangesSuccess() throws AmApiResourceNotFoundException {

      // update the first stock level
      expectedWarehouseStockLevelsForStock1.get( 0 ).setMaxLevel( 10 );

      // delete the second stock level
      expectedWarehouseStockLevelsForStock1.remove( 1 );

      // create a new stock level
      WarehouseStockLevel newWarehouseStockLevel =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_WITH_SUPPLIER_ID );
      newWarehouseStockLevel.setStockId( STOCK_1_ID );
      newWarehouseStockLevel.setRestockLevel( 3 );
      newWarehouseStockLevel.setSafetyLevel( 3 );
      newWarehouseStockLevel.setMaxLevel( 3 );
      newWarehouseStockLevel.setStockLowAction( RefStockLowActionKey.MANUAL.getCd() );
      expectedWarehouseStockLevelsForStock1.add( newWarehouseStockLevel );

      List<WarehouseStockLevel> updatedWarehouseStockLevelList = warehouseStockLevelResourceBean
            .update( STOCK_1_ID, expectedWarehouseStockLevelsForStock1 );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 2,
            updatedWarehouseStockLevelList.size() );

      // find the ID of the newly created stock level
      String newId = updatedWarehouseStockLevelList.get( 0 ).getId();
      if ( updatedWarehouseStockLevelList.get( 0 ).getId().equals( STOCK_1_STOCKLEVEL_1_ID ) ) {
         // we don't want the ID of the updated stock level, we want the other one
         newId = updatedWarehouseStockLevelList.get( 1 ).getId();
      }
      Assert.assertTrue( "Expected a new stock level to be created",
            !( STOCK_1_STOCKLEVEL_2_ID.equals( newId ) ) );

      // set the new ID on the expected stock level so we can compare the lists
      expectedWarehouseStockLevelsForStock1.get( 1 ).setId( newId );
      Assert.assertTrue(
            "Expected: " + expectedWarehouseStockLevelsForStock1 + " /n but was: "
                  + updatedWarehouseStockLevelList,
            updatedWarehouseStockLevelList.containsAll( expectedWarehouseStockLevelsForStock1 ) );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateAfterCreateWithMinimumFields() throws AmApiResourceNotFoundException {

      // create a stock level with minimum fields
      WarehouseStockLevel warehouseStockLevel1 =
            buildWarehouseStockLevelMandatoryFields( WAREHOUSE_LOCATION_1_ID );
      List<WarehouseStockLevel> warehouseStockLevelList = Arrays.asList( warehouseStockLevel1 );

      List<WarehouseStockLevel> actualWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_3_ID, warehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            actualWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            actualWarehouseStockLevelList.size() );

      assertWarehouseStockLevelCreatedWithDefaultFields( actualWarehouseStockLevelList.get( 0 ),
            STOCK_3_ID, WAREHOUSE_LOCATION_1_ID );

      // update that same stock level in another call
      actualWarehouseStockLevelList.get( 0 ).setMaxLevel( 8 );
      List<WarehouseStockLevel> updatedWarehouseStockLevelList =
            warehouseStockLevelResourceBean.update( STOCK_3_ID, actualWarehouseStockLevelList );

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            updatedWarehouseStockLevelList );
      Assert.assertEquals( "Number of warehouse stock levels", 1,
            updatedWarehouseStockLevelList.size() );

      Assert.assertEquals(
            "Expected: " + actualWarehouseStockLevelList.get( 0 ) + ", but was: "
                  + updatedWarehouseStockLevelList.get( 0 ),
            actualWarehouseStockLevelList.get( 0 ), updatedWarehouseStockLevelList.get( 0 ) );
   }


   private void assertWarehouseStockLevelCreatedWithDefaultFields(
         WarehouseStockLevel warehouseStockLevel, String expectedStockId,
         String expectedLocationId ) {
      Assert.assertEquals( "Stock ID", expectedStockId, warehouseStockLevel.getStockId() );
      Assert.assertEquals( "Location ID", expectedLocationId, warehouseStockLevel.getLocationId() );
      Assert.assertNull( "Expected no owner", warehouseStockLevel.getOwnerId() );
      Assert.assertEquals( "Restock Level", new Integer( 0 ),
            warehouseStockLevel.getRestockLevel() );
      Assert.assertEquals( "Safety Level", new Integer( 0 ), warehouseStockLevel.getSafetyLevel() );
      Assert.assertNull( "Expected max level to be null", warehouseStockLevel.getMaxLevel() );
      Assert.assertEquals( "Stock Low Action", MANUAL_STOCK_LOW_ACTION,
            warehouseStockLevel.getStockLowAction() );
   }


   private WarehouseStockLevel buildWarehouseStockLevelMandatoryFields( String locationId ) {
      WarehouseStockLevel warehouseStockLevel = new WarehouseStockLevel();
      warehouseStockLevel.setLocationId( locationId );
      return warehouseStockLevel;
   }


   private void constructExpectedResults() {

      // STOCK 1
      WarehouseStockLevel expectedWarehouseStockLevel1ForStock1 = new WarehouseStockLevel();
      expectedWarehouseStockLevel1ForStock1.setId( STOCK_1_STOCKLEVEL_1_ID );
      expectedWarehouseStockLevel1ForStock1.setStockId( STOCK_1_ID );
      expectedWarehouseStockLevel1ForStock1.setLocationId( WAREHOUSE_LOCATION_1_ID );
      expectedWarehouseStockLevel1ForStock1.setOwnerId( OWNER_ID );
      expectedWarehouseStockLevel1ForStock1.setRestockLevel( 1 );
      expectedWarehouseStockLevel1ForStock1.setSafetyLevel( 1 );
      expectedWarehouseStockLevel1ForStock1.setMaxLevel( 1 );
      expectedWarehouseStockLevel1ForStock1.setStockLowAction( MANUAL_STOCK_LOW_ACTION );

      WarehouseStockLevel expectedWarehouseStockLevel2ForStock1 = new WarehouseStockLevel();
      expectedWarehouseStockLevel2ForStock1.setId( STOCK_1_STOCKLEVEL_2_ID );
      expectedWarehouseStockLevel2ForStock1.setStockId( STOCK_1_ID );
      expectedWarehouseStockLevel2ForStock1.setLocationId( WAREHOUSE_LOCATION_2_ID );
      expectedWarehouseStockLevel2ForStock1.setRestockLevel( 2 );
      expectedWarehouseStockLevel2ForStock1.setSafetyLevel( 2 );
      expectedWarehouseStockLevel2ForStock1.setMaxLevel( 2 );
      expectedWarehouseStockLevel2ForStock1.setStockLowAction( MANUAL_STOCK_LOW_ACTION );

      expectedWarehouseStockLevelsForStock1 = new ArrayList<WarehouseStockLevel>();
      expectedWarehouseStockLevelsForStock1.add( expectedWarehouseStockLevel1ForStock1 );
      expectedWarehouseStockLevelsForStock1.add( expectedWarehouseStockLevel2ForStock1 );

      // STOCK 4
      WarehouseStockLevel expectedWarehouseStockLevel1ForStock4 = new WarehouseStockLevel();
      expectedWarehouseStockLevel1ForStock4.setId( STOCK_4_STOCKLEVEL_1_ID );
      expectedWarehouseStockLevel1ForStock4.setStockId( STOCK_4_ID );
      expectedWarehouseStockLevel1ForStock4.setLocationId( AIRPORT_LOCATION_ID );
      expectedWarehouseStockLevel1ForStock4.setRestockLevel( 3 );
      expectedWarehouseStockLevel1ForStock4.setSafetyLevel( 3 );
      expectedWarehouseStockLevel1ForStock4.setMaxLevel( 3 );
      expectedWarehouseStockLevel1ForStock4.setStockLowAction( MANUAL_STOCK_LOW_ACTION );

      expectedWarehouseStockLevelsForStock4 = new ArrayList<WarehouseStockLevel>();
      expectedWarehouseStockLevelsForStock4.add( expectedWarehouseStockLevel1ForStock4 );

      // STOCK 5
      WarehouseStockLevel expectedWarehouseStockLevel1ForStock5 = new WarehouseStockLevel();
      expectedWarehouseStockLevel1ForStock5.setId( STOCK_5_STOCKLEVEL_1_ID );
      expectedWarehouseStockLevel1ForStock5.setStockId( STOCK_5_ID );
      expectedWarehouseStockLevel1ForStock5.setLocationId( WAREHOUSE_LOCATION_1_ID );
      expectedWarehouseStockLevel1ForStock5.setRestockLevel( 3 );
      expectedWarehouseStockLevel1ForStock5.setSafetyLevel( 2 );
      expectedWarehouseStockLevel1ForStock5.setMaxLevel( 4 );
      expectedWarehouseStockLevel1ForStock5.setStockLowAction( DISTREQ_STOCK_LOW_ACTION );

      expectedWarehouseStockLevelsForStock5 = new ArrayList<WarehouseStockLevel>();
      expectedWarehouseStockLevelsForStock5.add( expectedWarehouseStockLevel1ForStock5 );
   }


   /**
    * Assert the actual warehouse stock levels match the expected warehouse stock levels
    *
    * @param expectedWarehouseStockLevels
    * @param actualWarehouseStockLevels
    */
   private void assertWarehouseStockLevels( List<WarehouseStockLevel> expectedWarehouseStockLevels,
         List<WarehouseStockLevel> actualWarehouseStockLevels ) {

      Assert.assertNotNull( "Expected a list of warehouse stock levels",
            actualWarehouseStockLevels );
      Assert.assertEquals( "Number of warehouse stock levels", expectedWarehouseStockLevels.size(),
            actualWarehouseStockLevels.size() );
      Assert.assertTrue(
            "Expected: " + expectedWarehouseStockLevels + " /n but was: "
                  + actualWarehouseStockLevels,
            actualWarehouseStockLevels.containsAll( expectedWarehouseStockLevels ) );
   }


   private void assertRevisionDateChanged( boolean isChanged, String stockLevelId,
         String originalDate ) throws ParseException {
      DataSetArgument args = new DataSetArgument();
      args.add( "alt_id", new RawId( stockLevelId ) );

      QuerySet querySet = QuerySetFactory.getInstance().executeQueryTable( "inv_loc_stock", args );
      querySet.next();
      Date revisionDate = querySet.getDate( "REVISION_DT" );

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date originalRevisionDate = simpleDateFormat.parse( originalDate );

      if ( isChanged ) {
         Assert.assertTrue( "Expected revision date to be updated, but was the same",
               !revisionDate.equals( originalRevisionDate ) );
      } else {
         Assert.assertTrue( "Expected revision date to not be updated, but was changed",
               revisionDate.equals( originalRevisionDate ) );
      }

   }


   private void assertThatOneWorkItemGenerated( String stockNoKey, String locationKey ) {
      DataSetArgument args = new DataSetArgument();
      args.add( "type", WarehouseStockLevelCheckWorkItem.WORK_TYPE );
      QuerySet querySet =
            QuerySetFactory.getInstance().executeQuery( "utl_work_item", args, "data" );

      // Assert a new work item is generated with expected data
      Assert.assertEquals( "Expected row count: ", 1, querySet.getRowCount() );

      querySet.next();
      Assert.assertEquals( "Expected StockNoKey:LocationKey:NULL : ",
            stockNoKey + ":" + locationKey + ":[NULL]", querySet.getString( "data" ) );
   }

}
