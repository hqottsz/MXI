package com.mxi.am.api.resource.materials.plan.stock.stocklevel.supply;

import static com.mxi.mx.core.key.RefStockLowActionKey.MANUAL;
import static com.mxi.mx.core.key.RefStockLowActionKey.POREQ;
import static com.mxi.mx.core.key.RefStockLowActionKey.SHIPREQ;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
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
import com.mxi.am.api.exception.AmApiMandatoryParameterMissingException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.services.stocklevel.StockLevelCheckWorkItem;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query-Test for SupplyStockLevelResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class SupplyStockLevelResourceBeanTest extends ResourceBeanTest {

   private static final String STOCK_1_ID = "601435E495494F34965B1588F5A6036B";
   private static final String STOCK_1_CD = "STOCK_1";
   private static final String STOCK_2_ID = "601435E495494F34965B1588F5A6036C";
   private static final String STOCK_2_KEY = "4650:2";
   private static final String STOCK_3_ID = "601435E495494F34965B1588F5A6036D";
   private static final String STOCK_4_ID = "601435E495494F34965B1588F5A6036E";
   private static final String STOCK_5_ID = "0601435E495494F34965B1588F5A6036";
   private static final String STOCK_6_ID = "0701435E495494F34965B1588F5A6037";
   private static final String STOCK_6_KEY = "4650:6";
   private static final String NOT_FOUND_STOCK_ID = "700035E495494F34965B1588F5A6036A";
   private static final String EXCEPTION_TYPE_ID = "Stock";
   private static final String STOCK_1_STOCKLEVEL_1_ID = "A01435E495494F34965B1588F5A60361";
   private static final String STOCK_1_STOCKLEVEL_2_ID = "B01435E495494F34965B1588F5A60362";
   private static final String STOCK_4_STOCKLEVEL_1_ID = "C01435E495494F34965B1588F5A60362";
   private static final String STOCK_5_STOCKLEVEL_1_ID = "A01435E495494F34965B1588F5A60366";
   private static final String STOCK_5_STOCKLEVEL_2_ID = "A01435E495494F34965B1588F5A60367";
   private static final String STOCK_6_STOCKLEVEL_1_ID = "A01435E495494F34965B1588F5A60368";
   private static final String OWNER_1_ID = "301435E495494F34965B1588F5A60362";
   private static final String OWNER_1_CD = "OWNER_1";
   private static final String DEFAULT_OWNER_ID = "301435E495494F34965B1588F5A60364";
   private static final String AIRPORT_1_ID = "101435E495494F34965B1588F5A60362";
   private static final String AIRPORT_1_CD = "AIRPORT1";
   private static final String AIRPORT_2_ID = "201435E495494F34965B1588F5A60362";
   private static final String AIRPORT_2_SUPPLY_LOC_ID = "AEDB927A7FCF433F8EFC08C8EBF9576A";
   private static final String AIRPORT_2_SUPPLY_LOC_KEY = "4650:3";
   private static final String AIRPORT_2_SUPPLY_LOC_CD = "AIRPORT2/SUPPLY";
   private static final String AIRPORT_2_WAREHOUSE_LOC_ID = "AEDB927A7FCF433F8EFC08C8EBF9576B";
   private static final String AIRPORT_2_WAREHOUSE_LOC_CD = "AIRPORT2/STORE";
   private static final String AIRPORT_3_ID = "BEDA927A7FCF433F8EFC08C8EBF9576C";
   private static final String AIRPORT_3_KEY = "4650:6";

   private static final String INVALID_ID = "XXXXXXXXXXXXXXXX8EFC08C8EBF9576A";
   private static final String INVALID_STOCK_LOW_ACTION = "INVALID";
   private static final String AUTHORIZED_HR_KEY = "4650:101";

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Mock
   private EJBContext eJBContext;

   @Mock
   private Principal principal;

   @Inject
   private SupplyStockLevelResourceBean supplyStockLevelResourceBean;

   @Inject
   private SupplyStockLevelSearchResourceBean supplyStockLevelSearchResourceBean;

   private LegacyKeyUtil legacyKeyUtil;

   private List<SupplyStockLevel> expectedExistingSupplyStockLevelsForStock1;
   private List<SupplyStockLevel> expectedExistingSupplyStockLevelsForStock4;
   private List<SupplyStockLevel> expectedExistingSupplyStockLevelsForStock5;
   private List<SupplyStockLevel> expectedExistingSupplyStockLevelsForStock6;

   private List<SupplyStockLevel> expectedNewCreatedSupplyStockLevelsForStock2;

   private SupplyStockLevel expectedNewCreatedSupplyStockLevelForStock5;


   @Before
   public void setUp() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      supplyStockLevelResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      legacyKeyUtil = new LegacyKeyUtil();

      constructExpectedResults();
   }


   @Test
   public void testGetListReturnedSuccess() throws AmApiResourceNotFoundException {
      List<SupplyStockLevel> supplyStockLevels = supplyStockLevelResourceBean.get( STOCK_1_ID );
      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1, supplyStockLevels );
   }


   @Test
   public void testGetEmptyListSuccess() throws AmApiResourceNotFoundException {
      List<SupplyStockLevel> supplyStockLevels = supplyStockLevelResourceBean.get( STOCK_2_ID );

      Assert.assertNotNull( "Expected an empty list of supply stock levels", supplyStockLevels );
      Assert.assertEquals( "Expected an empty list of supply stock levels", 0,
            supplyStockLevels.size() );
   }


   @Test
   public void testGetStockNotFound() {
      try {
         supplyStockLevelResourceBean.get( NOT_FOUND_STOCK_ID );
         Assert.fail( "Did not throw AmApiResourceNotFoundException" );
      } catch ( AmApiResourceNotFoundException e ) {
         String message = EXCEPTION_TYPE_ID + " " + NOT_FOUND_STOCK_ID + " not found";
         Assert.assertEquals( "Error Message", message, e.getMessage() );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchSuccess() {

      SupplyStockLevelSearchParameters parms = new SupplyStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( STOCK_1_ID, STOCK_4_ID ) );

      List<SupplyStockLevel> stockLevels = supplyStockLevelSearchResourceBean.search( parms );

      List<SupplyStockLevel> expectedResults = new ArrayList<SupplyStockLevel>();
      expectedResults.addAll( expectedExistingSupplyStockLevelsForStock1 );
      expectedResults.addAll( expectedExistingSupplyStockLevelsForStock4 );

      assertSupplyStockLevels( expectedResults, stockLevels );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchEmptyListSuccess() {
      // search for stock levels for a stock which doesn't exist
      // and for a stock with no stock levels
      SupplyStockLevelSearchParameters parms = new SupplyStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( NOT_FOUND_STOCK_ID, STOCK_3_ID ) );

      List<SupplyStockLevel> stockLevels = supplyStockLevelSearchResourceBean.search( parms );

      Assert.assertNotNull( "Expected an empty list of supply stock levels", stockLevels );
      Assert.assertEquals( "Expected an empty list of supply stock levels", 0, stockLevels.size() );
   }


   @Test
   public void testSearchMandatoryParameterMissing() {
      try {
         SupplyStockLevelSearchParameters parms = new SupplyStockLevelSearchParameters();
         supplyStockLevelSearchResourceBean.search( parms );
         Assert.fail( "Did not throw AmApiMandatoryParameterMissingException" );
      } catch ( AmApiMandatoryParameterMissingException e ) {
         // pass
      }
   }


   @Test
   public void testSearchNoParameters() {
      try {
         supplyStockLevelSearchResourceBean.search( null );
         Assert.fail( "Did not throw AmApiMandatoryParameterMissingException" );
      } catch ( AmApiMandatoryParameterMissingException e ) {
         // pass
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testCreateWithAllFieldsExceptForAllocationPercentageSuccess()
         throws AmApiResourceNotFoundException {
      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ).setAllocationPercentage( null );
      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ).setStockLowAction( SHIPREQ.getCd() );

      List<SupplyStockLevel> actualSupplyStockLevelList = supplyStockLevelResourceBean.update(
            STOCK_2_ID, Arrays.asList( expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ) ) );

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, actualSupplyStockLevelList.size() );

      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 )
            .setId( actualSupplyStockLevelList.get( 0 ).getId() );
      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ).setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      Assert.assertTrue(
            "Expected: " + expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ) + "but was: "
                  + actualSupplyStockLevelList.get( 0 ),
            expectedNewCreatedSupplyStockLevelsForStock2.get( 1 )
                  .equals( actualSupplyStockLevelList.get( 0 ) ) );

      assertOtherDefaults( actualSupplyStockLevelList.get( 0 ).getId() );
   }


   @Test
   public void testCreateWithMandatoryFieldsSuccess() throws AmApiResourceNotFoundException {
      List<SupplyStockLevel> supplyStockLevelList =
            Arrays.asList( buildSupplyStockLevelMandatoryFields( AIRPORT_2_SUPPLY_LOC_ID ) );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, supplyStockLevelList );

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, actualSupplyStockLevelList.size() );

      expectedNewCreatedSupplyStockLevelsForStock2.get( 0 )
            .setId( actualSupplyStockLevelList.get( 0 ).getId() );

      Assert.assertTrue(
            "Expected: " + expectedNewCreatedSupplyStockLevelsForStock2.get( 0 ) + "but was: "
                  + actualSupplyStockLevelList.get( 0 ),
            expectedNewCreatedSupplyStockLevelsForStock2.get( 0 )
                  .equals( actualSupplyStockLevelList.get( 0 ) ) );

      assertOtherDefaults( actualSupplyStockLevelList.get( 0 ).getId() );

      // test one work item was generated when create a supply stock level WITHOUT allocation
      // percentage
      assertThatOneWorkItemGenerated( STOCK_2_KEY, AIRPORT_2_SUPPLY_LOC_KEY, AUTHORIZED_HR_KEY );
   }


   @Test
   public void testCreateMultipleStockLevelsSuccess() throws AmApiResourceNotFoundException {

      // create new stock level 1
      expectedNewCreatedSupplyStockLevelsForStock2.get( 0 )
            .setAllocationPercentage( new BigDecimal( 51.3451 ) );

      // create new stock level 2
      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 )
            .setAllocationPercentage( new BigDecimal( 48.6542 ) );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_2_ID, expectedNewCreatedSupplyStockLevelsForStock2 );

      setStockLevelIds( expectedNewCreatedSupplyStockLevelsForStock2, actualSupplyStockLevels );

      expectedNewCreatedSupplyStockLevelsForStock2.get( 0 ).setAllocationPercentage(
            new BigDecimal( 51.3451 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedNewCreatedSupplyStockLevelsForStock2.get( 1 ).setAllocationPercentage(
            new BigDecimal( 48.6542 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      assertSupplyStockLevels( expectedNewCreatedSupplyStockLevelsForStock2,
            actualSupplyStockLevels );
   }


   @Test
   public void testCreateWithAllFieldsManualStockLowActionSuccess()
         throws AmApiResourceNotFoundException {
      SupplyStockLevel supplyStockLevel = buildSupplyStockLevelAllFields( STOCK_2_ID,
            AIRPORT_2_SUPPLY_LOC_ID, MANUAL.getCd(), 3 );
      List<SupplyStockLevel> expectedSupplyStockLevelList = Arrays.asList( supplyStockLevel );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, expectedSupplyStockLevelList );

      // quantity field should be ignored for MANUAL stock low action
      expectedSupplyStockLevelList.get( 0 ).setQuantity( null );
      assertSupplyStockLevels( expectedSupplyStockLevelList, actualSupplyStockLevelList );
   }


   @Test
   public void testCreateWithPOREQStockLowActionAllFieldsSuccess()
         throws AmApiResourceNotFoundException {
      SupplyStockLevel supplyStockLevel =
            buildSupplyStockLevelAllFields( STOCK_2_ID, AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd(), 3 );
      List<SupplyStockLevel> expectedSupplyStockLevelList = Arrays.asList( supplyStockLevel );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, expectedSupplyStockLevelList );

      assertSupplyStockLevels( expectedSupplyStockLevelList, actualSupplyStockLevelList );

      // test one work item was generated when create a supply stock level with allocation
      // percentage
      assertThatOneWorkItemGenerated( STOCK_2_KEY, AIRPORT_2_SUPPLY_LOC_KEY, AUTHORIZED_HR_KEY );
   }


   @Test
   public void testCreateWithSHIPREQStockLowActionAllFieldsSuccess()
         throws AmApiResourceNotFoundException {
      SupplyStockLevel supplyStockLevel = buildSupplyStockLevelAllFields( STOCK_2_ID,
            AIRPORT_2_SUPPLY_LOC_ID, SHIPREQ.getCd(), 3 );
      List<SupplyStockLevel> expectedSupplyStockLevelList = Arrays.asList( supplyStockLevel );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, expectedSupplyStockLevelList );

      assertSupplyStockLevels( expectedSupplyStockLevelList, actualSupplyStockLevelList );
   }


   @Test
   public void testCreateWithPOREQStockLowActionGivenStockRestockQuantitySuccess()
         throws AmApiResourceNotFoundException {
      SupplyStockLevel supplyStockLevel =
            buildSupplyStockLevelMandatoryFields( AIRPORT_2_SUPPLY_LOC_ID );
      supplyStockLevel.setStockLowAction( POREQ.getCd() );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_3_ID, Arrays.asList( supplyStockLevel ) );

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, actualSupplyStockLevelList.size() );
      assertStockLevelWithDefaults( actualSupplyStockLevelList.get( 0 ), STOCK_3_ID,
            AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd(), new Integer( 5 ) );
   }


   @Test
   public void testCreateWithPOREQStockLowActionSuccess() throws AmApiResourceNotFoundException {
      // create stock level with mandatory fields + stock low action (POREQ)
      SupplyStockLevel supplyStockLevel =
            buildSupplyStockLevelMandatoryFields( AIRPORT_2_SUPPLY_LOC_ID );
      supplyStockLevel.setStockLowAction( POREQ.getCd() );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, actualSupplyStockLevelList.size() );
      assertStockLevelWithDefaults( actualSupplyStockLevelList.get( 0 ), STOCK_2_ID,
            AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd(), new Integer( 0 ) );
   }


   @Test
   public void testCreateWithSHIPREQStockLowActionSuccess() throws AmApiResourceNotFoundException {
      // create stock level with mandatory fields + stock low action (SHIPREQ)
      SupplyStockLevel supplyStockLevel =
            buildSupplyStockLevelMandatoryFields( AIRPORT_2_SUPPLY_LOC_ID );
      supplyStockLevel.setStockLowAction( SHIPREQ.getCd() );

      List<SupplyStockLevel> actualSupplyStockLevelList =
            supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, actualSupplyStockLevelList.size() );
      assertStockLevelWithDefaults( actualSupplyStockLevelList.get( 0 ), STOCK_2_ID,
            AIRPORT_2_SUPPLY_LOC_ID, SHIPREQ.getCd(), new Integer( 1 ) );
   }


   @Test
   public void testCreateExistingStockLevelFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setId( null );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when the stock level to be created already exists : ",
               "The stock level with location ID '" + AIRPORT_1_ID + "' and owner ID '" + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-30042] Cannot create new stock level. A stock level already exists for Stock Number '"
                     + STOCK_1_CD + "', Owner '" + OWNER_1_CD + "' at Location '" + AIRPORT_1_CD
                     + "'",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithStockIdNotMatchingFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setStockId( STOCK_1_ID );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock ids do not match : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithEmptyStockIdFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setStockId( "" );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock id is an empty string : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithNegativeSafetyLevelFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setSafetyLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when safety level is negative : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-10001] The 'safetyLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithSafetyLevelGreaterThanRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setRestockLevel( 2 );
         supplyStockLevel.setSafetyLevel( 3 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when safety level is greater than restock level : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: The Safety Level value is greater than the Restock Level value.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithSafetyLevelButNoRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );
         supplyStockLevel.setSafetyLevel( 3 );
         supplyStockLevel.setRestockLevel( null );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when safety level is greater than restock level : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: The Safety Level value is greater than the Restock Level value.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithNegativeRestockLevelFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setRestockLevel( -1 );
         supplyStockLevel.setSafetyLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when restock level is negative : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-10001] The 'restockLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithNegativeMaxLevelFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setMaxLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when max level is negative : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-10001] The 'maxLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithRestockLevelGreaterThanMaxLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setRestockLevel( 3 );
         supplyStockLevel.setMaxLevel( 2 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when restock level is greater than max level : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-30076] The Max Level must be greater than the Restock Level for Location AIRPORT2/SUPPLY.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithPOREQStockLowActionAndNegativeQuantityFailure()
         throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, POREQ.getCd() );

         supplyStockLevel.setQuantity( -1 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when stock low action is POREQ and quantity is negative : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-10001] The 'quantity' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithSHIPREQStockLowActionAndInvalidQuantityFailure()
         throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID, SHIPREQ.getCd() );

         supplyStockLevel.setQuantity( 0 );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when stock low action is SHIPREQ and quantity is invalid : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: [MXERR-30864] The Stock Low Action cannot be set to 'SHIPREQ' for Location '"
                     + AIRPORT_2_SUPPLY_LOC_CD
                     + "'. The Stock must have a Shipping Quantity specified.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithInvalidLocationIdFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_2_ID,
               Arrays.asList( buildSupplyStockLevelMandatoryFields( INVALID_ID ) ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when location id is invalid : ",
               "The stock level with location ID '" + INVALID_ID
                     + "' and no specified owner could not be created because of the following error: No location with the ID '"
                     + INVALID_ID + "' could be found in Maintenix.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithNonSupplyLocationFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays
               .asList( buildSupplyStockLevelMandatoryFields( AIRPORT_2_WAREHOUSE_LOC_ID ) ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when the location is a non supply location : ",
               "The stock level with location ID '" + AIRPORT_2_WAREHOUSE_LOC_ID
                     + "' and no specified owner could not be created because of the following error: [MXERR-30458] The location chosen, "
                     + AIRPORT_2_WAREHOUSE_LOC_CD + " is not a supply location.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithInvalidOwnerIdFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel =
               buildSupplyStockLevelMandatoryFields( AIRPORT_2_SUPPLY_LOC_ID );
         supplyStockLevel.setOwnerId( INVALID_ID );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when owner id is invalid : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + INVALID_ID
                     + "' could not be created because of the following error: No owner with the ID '"
                     + INVALID_ID + "' could be found in Maintenix.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateWithInvalidStockLowActionFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_2_ID,
               Arrays.asList( buildSupplyStockLevelWithFields( AIRPORT_2_SUPPLY_LOC_ID,
                     INVALID_STOCK_LOW_ACTION ) ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock low action is invalid : ",
               "The stock level with location ID '" + AIRPORT_2_SUPPLY_LOC_ID + "' and owner ID '"
                     + OWNER_1_ID
                     + "' could not be created because of the following error: The Stock Low Action should be 'SHIPREQ' or 'MANUAL' or 'POREQ' for supply stock levels.",
               ex.getMessage() );
      }
   }


   @Test
   public void testCreateAllocationPercentageTotalIsNot100Failure()
         throws AmApiResourceNotFoundException {
      try {
         expectedNewCreatedSupplyStockLevelsForStock2.get( 0 )
               .setAllocationPercentage( new BigDecimal( 10 ) );

         expectedNewCreatedSupplyStockLevelsForStock2.get( 1 )
               .setAllocationPercentage( new BigDecimal( 20 ) );

         supplyStockLevelResourceBean.update( STOCK_2_ID,
               expectedNewCreatedSupplyStockLevelsForStock2 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertTrue( "Expected error message when allocation percentage total is not 100 : ",
               ex.getMessage().contains(
                     "[MXERR-30210] The stock percentage is not equal to 100 for 'Total Stock Allocation %'." ) );
      }
   }


   @Test
   public void testCreateRoundedAllocationPercentageTotalIsNot100Failure()
         throws AmApiResourceNotFoundException {
      try {
         // the allocation % will be 34.56 after rounding
         expectedNewCreatedSupplyStockLevelsForStock2.get( 0 )
               .setAllocationPercentage( new BigDecimal( 34.55511111 ) );

         // the allocation % will be 65.45 after rounding
         expectedNewCreatedSupplyStockLevelsForStock2.get( 1 )
               .setAllocationPercentage( new BigDecimal( 65.44526546 ) );

         supplyStockLevelResourceBean.update( STOCK_2_ID,
               expectedNewCreatedSupplyStockLevelsForStock2 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertTrue( "Expected error message when allocation percentage total is not 100 : ",
               ex.getMessage().contains(
                     "[MXERR-30210] The stock percentage is not equal to 100 for 'Total Stock Allocation %'." ) );
      }
   }


   @Test
   public void testPutWithNoPayloadFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_1_ID, null );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Missing supply stock level payload.", ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithStockIdNotMatchingFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_2_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock ids do not match : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithEmptyStockIdFailure() throws AmApiResourceNotFoundException {
      try {
         SupplyStockLevel supplyStockLevel = expectedExistingSupplyStockLevelsForStock1.get( 0 );

         supplyStockLevel.setStockId( "" );

         supplyStockLevelResourceBean.update( STOCK_2_ID, Arrays.asList( supplyStockLevel ) );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock id is an empty string : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithNegativeSafetyLevelFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setSafetyLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when safety level is negative : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-10001] The 'safetyLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithSafetyLevelGreaterThanRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setRestockLevel( 2 );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setSafetyLevel( 3 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when safety level is greater than restock level : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The Safety Level value is greater than the Restock Level value.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithSafetyLevelGreaterThanExistingRestockLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setRestockLevel( null );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setSafetyLevel( 3 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when safety level is greater than restock level : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The Safety Level value is greater than the Restock Level value.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithNegativeRestockLevelFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setRestockLevel( -1 );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setSafetyLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when restock level is negative : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-10001] The 'restockLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithNegativeMaxLevelFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setMaxLevel( -1 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when max level is negative : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-10001] The 'maxLevel' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithRestockLevelGreaterThanMaxLevelFailure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setRestockLevel( 3 );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setMaxLevel( 2 );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when restock level is greater than max level : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-30076] The Max Level must be greater than the Restock Level for Location AIRPORT1.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithPOREQStockLowActionAndNegativeQuantityFailure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setQuantity( -1 );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setStockLowAction( POREQ.getCd() );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when stock low action is POREQ and quantity is negative : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-10001] The 'quantity' cannot be negative.<br><br>Please enter a value greater than or equal to 0.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithSHIPREQStockLowActionAndInvalidQuantityFailure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setQuantity( 0 );
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setStockLowAction( SHIPREQ.getCd() );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when stock low action is SHIPREQ and quantity is invalid : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: [MXERR-30864] The Stock Low Action cannot be set to 'SHIPREQ' for Location '"
                     + AIRPORT_1_CD + "'. The Stock must have a Shipping Quantity specified.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithInvalidLocationIdFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setLocationId( INVALID_ID );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when location id is invalid : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The location and/or owner of an existing stock level cannot be updated, instead a new stock level must be created. ",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithInvalidOwnerIdFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setOwnerId( INVALID_ID );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when owner id is invalid : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The location and/or owner of an existing stock level cannot be updated, instead a new stock level must be created. ",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateWithInvalidStockLowActionFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 )
               .setStockLowAction( INVALID_STOCK_LOW_ACTION );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock low action is invalid : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The Stock Low Action should be 'SHIPREQ' or 'MANUAL' or 'POREQ' for supply stock levels.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateAllocationPercentageTotalIsNot100Failure()
         throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 )
               .setAllocationPercentage( new BigDecimal( 10 ) );

         expectedExistingSupplyStockLevelsForStock1.get( 1 )
               .setAllocationPercentage( new BigDecimal( 20 ) );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertTrue( "Expected error message when allocation percentage total is not 100 : ",
               ex.getMessage().contains(
                     "[MXERR-30210] The stock percentage is not equal to 100 for 'Total Stock Allocation %'." ) );
      }
   }


   @Test
   public void testUpdateRoundedAllocationPercentageTotalIsNot100Failure()
         throws AmApiResourceNotFoundException {
      try {
         // the allocation % will be 99.56 after rounding
         expectedExistingSupplyStockLevelsForStock1.get( 0 )
               .setAllocationPercentage( new BigDecimal( 99.555 ) );

         // the allocation % will be 0.45 after rounding
         expectedExistingSupplyStockLevelsForStock1.get( 1 )
               .setAllocationPercentage( new BigDecimal( 0.445 ) );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertTrue( "Expected error message when allocation percentage total is not 100 : ",
               ex.getMessage().contains(
                     "[MXERR-30210] The stock percentage is not equal to 100 for 'Total Stock Allocation %'." ) );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateAllFieldsExceptForAllocationPercentageSuccess()
         throws AmApiResourceNotFoundException {
      expectedExistingSupplyStockLevelsForStock1.get( 0 ).setAllocationPercentage( null );

      SupplyStockLevel supplyStockLevelToUpdate =
            expectedExistingSupplyStockLevelsForStock1.get( 1 );
      supplyStockLevelToUpdate.setAllocationPercentage( null );
      supplyStockLevelToUpdate.setMaxLevel( 10 );
      supplyStockLevelToUpdate.setRestockLevel( 10 );
      supplyStockLevelToUpdate.setSafetyLevel( 3 );
      supplyStockLevelToUpdate.setStockLowAction( POREQ.getCd() );
      supplyStockLevelToUpdate.setQuantity( 3 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_1_ID, expectedExistingSupplyStockLevelsForStock1 );

      expectedExistingSupplyStockLevelsForStock1.get( 0 ).setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedExistingSupplyStockLevelsForStock1.get( 1 )
            .setAllocationPercentage( new BigDecimal( 0 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1,
            actualSupplyStockLevels );
   }


   @Test
   public void testUpdateMultipleStockLevelsSuccess() throws AmApiResourceNotFoundException {
      // update all fields for stock level 1
      SupplyStockLevel supplyStockLevel1 = expectedExistingSupplyStockLevelsForStock1.get( 0 );
      supplyStockLevel1.setAllocationPercentage( new BigDecimal( 88.2552145 ) );
      supplyStockLevel1.setMaxLevel( 10 );
      supplyStockLevel1.setRestockLevel( 10 );
      supplyStockLevel1.setSafetyLevel( 3 );
      supplyStockLevel1.setStockLowAction( POREQ.getCd() );
      supplyStockLevel1.setQuantity( 3 );

      // update one field for stock level 2
      expectedExistingSupplyStockLevelsForStock1.get( 1 )
            .setAllocationPercentage( new BigDecimal( 11.7442456 ) );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_1_ID, expectedExistingSupplyStockLevelsForStock1 );

      // allocation percentages should be rounded to two decimals
      expectedExistingSupplyStockLevelsForStock1.get( 0 ).setAllocationPercentage(
            new BigDecimal( 88.2552145 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedExistingSupplyStockLevelsForStock1.get( 1 ).setAllocationPercentage(
            new BigDecimal( 11.7442456 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1,
            actualSupplyStockLevels );

      assertOtherDefaults( actualSupplyStockLevels.get( 0 ).getId() );
      assertOtherDefaults( actualSupplyStockLevels.get( 1 ).getId() );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateOneFieldSuccess() throws AmApiResourceNotFoundException {
      SupplyStockLevel supplyStockLevel = new SupplyStockLevel();
      supplyStockLevel.setId( STOCK_6_STOCKLEVEL_1_ID );
      supplyStockLevel.setStockId( STOCK_6_ID );
      supplyStockLevel.setLocationId( AIRPORT_3_ID );
      supplyStockLevel.setOwnerId( OWNER_1_ID );
      supplyStockLevel.setMaxLevel( 4 );

      List<SupplyStockLevel> actualSupplyStockLevels =
            supplyStockLevelResourceBean.update( STOCK_6_ID, Arrays.asList( supplyStockLevel ) );

      expectedExistingSupplyStockLevelsForStock6.get( 0 ).setMaxLevel( 4 );
      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock6,
            actualSupplyStockLevels );

      // test one work item was generated when update a supply stock level WITHOUT allocation
      // percentage
      assertThatOneWorkItemGenerated( STOCK_6_KEY, AIRPORT_3_KEY, AUTHORIZED_HR_KEY );
   }


   @Test
   public void testUpdateStockLevelWithDefaultsSuccess() throws AmApiResourceNotFoundException {

      expectedExistingSupplyStockLevelsForStock6.get( 0 ).setRestockLevel( 3 );
      expectedExistingSupplyStockLevelsForStock6.get( 0 ).setMaxLevel( 3 );
      expectedExistingSupplyStockLevelsForStock6.get( 0 ).setStockLowAction( "POREQ" );
      expectedExistingSupplyStockLevelsForStock6.get( 0 ).setQuantity( 4 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_6_ID, expectedExistingSupplyStockLevelsForStock6 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock6,
            actualSupplyStockLevels );

      // check that the values have not changed (were not defaulted because they were already set)
      // for station weight factor, # of inbound flights and weighting factor
      InvLocStockKey invLocStockKey = legacyKeyUtil
            .getLegacyKeyByAltId( actualSupplyStockLevels.get( 0 ).getId(), InvLocStockKey.class );
      InvLocStockTable invLocStock = InvLocStockTable.findByPrimaryKey( invLocStockKey );
      Assert.assertEquals( "Station Weight Factor", new Double( 2 ),
            invLocStock.getWeightFactorQt() );

      InvLocTable invLoc = InvLocTable.findByPrimaryKey( invLocStockKey.getLocationKey() );
      Assert.assertEquals( "# of Inbound Flights", new Double( 2 ), invLoc.getInboundFlightsQt() );

      EqpStockNoTable eqpStockNo =
            EqpStockNoTable.findByPrimaryKey( invLocStockKey.getStockNoKey() );
      Assert.assertEquals( "Weighting Factor", new Double( 2 ), eqpStockNo.getMaxMultQt() );

      // test one work item was generated when update a supply stock level with allocation
      // percentage
      assertThatOneWorkItemGenerated( STOCK_6_KEY, AIRPORT_3_KEY, AUTHORIZED_HR_KEY );
   }


   @Test
   public void testUpdateStockLevelNotBelongToStockFailure() throws AmApiResourceNotFoundException {
      try {
         supplyStockLevelResourceBean.update( STOCK_2_ID,
               expectedExistingSupplyStockLevelsForStock1 );
         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals(
               "Expected error message when the stock level does not belong to the stock : ",
               "The stock level with ID '" + STOCK_1_STOCKLEVEL_1_ID
                     + "' could not be updated because of the following error: The stock ID of the stock level did not match the stock ID of the stock whose stock levels are being processed.",
               ex.getMessage() );
      }
   }


   @Test
   public void testUpdateInvalidStockLevelIdFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setId( "" );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when stock level id is invalid : ",
               "The stock level with ID '' could not be updated because of the following error: A matching stock level could not be found for the given stock.",
               ex.getMessage() );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testDeleteStockLevelWithZeroAllocPctSuccess() throws AmApiResourceNotFoundException {
      // remove one stock level that has alloction percentage 0
      expectedExistingSupplyStockLevelsForStock1.remove( 1 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_1_ID, expectedExistingSupplyStockLevelsForStock1 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1,
            actualSupplyStockLevels );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testDeleteOnlyStockLevelWith100AllocPctSuccess()
         throws AmApiResourceNotFoundException {
      // remove the only one stock level that has alloction percentage 100
      expectedExistingSupplyStockLevelsForStock4.remove( 0 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_4_ID, expectedExistingSupplyStockLevelsForStock4 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock4,
            actualSupplyStockLevels );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testDeleteStockLevelWith100AllocPctAndSet100ToAnotherStockLevelSuccess()
         throws AmApiResourceNotFoundException {
      expectedExistingSupplyStockLevelsForStock1.get( 1 ).setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      expectedExistingSupplyStockLevelsForStock1.remove( 0 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_1_ID, expectedExistingSupplyStockLevelsForStock1 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1,
            actualSupplyStockLevels );
   }


   @Test
   public void testDeleteAllWhereOneStockLevelWith100AllocPctAndRestWithZeroAllocPctSuccess()
         throws AmApiResourceNotFoundException {
      expectedExistingSupplyStockLevelsForStock1
            .removeAll( expectedExistingSupplyStockLevelsForStock1 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_1_ID, expectedExistingSupplyStockLevelsForStock1 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock1,
            actualSupplyStockLevels );
   }


   @Test
   public void testDeleteAllWhereStockLevelsWithAllocPctBetween1To99Success()
         throws AmApiResourceNotFoundException {
      expectedExistingSupplyStockLevelsForStock5
            .removeAll( expectedExistingSupplyStockLevelsForStock5 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_5_ID, expectedExistingSupplyStockLevelsForStock5 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock5,
            actualSupplyStockLevels );
   }


   @Test
   public void testDeleteRemainingStockLevelsMustHave100AllocPctFailure()
         throws AmApiResourceNotFoundException {
      try {
         // delete stock level with 100% allocation
         expectedExistingSupplyStockLevelsForStock1.remove( 0 );

         // remove allocation percentage from payload for remaining stock level (now index 0)
         // this means "keep the same percentage as before" which was 0%
         expectedExistingSupplyStockLevelsForStock1.get( 0 ).setAllocationPercentage( null );

         supplyStockLevelResourceBean.update( STOCK_1_ID,
               expectedExistingSupplyStockLevelsForStock1 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertTrue(
               "Expected error message when a stock level with allocation percentage > 0 is deleted and the allocation percentage of reamining stock levels is not updated : ",
               ex.getMessage().contains(
                     "[MXERR-30210] The stock percentage is not equal to 100 for 'Total Stock Allocation %'." ) );
      }

   }


   @Test
   public void testUpdatePartiallyProvideAllocPctFailure() throws AmApiResourceNotFoundException {
      try {
         expectedExistingSupplyStockLevelsForStock5.get( 0 ).setAllocationPercentage( null );

         supplyStockLevelResourceBean.update( STOCK_5_ID,
               expectedExistingSupplyStockLevelsForStock5 );

         Assert.fail( "Did not throw AmApiBadRequestException" );
      } catch ( AmApiBadRequestException ex ) {
         Assert.assertEquals( "Expected error message when allocation % is partially provided : ",
               "Allocation percentage must be provided for every single stock level or for none.",
               ex.getMessage() );
      }
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testMultipleOperationsWithPercentageSuccess() throws AmApiResourceNotFoundException {
      // update
      expectedExistingSupplyStockLevelsForStock5.get( 1 ).setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      // delete
      expectedExistingSupplyStockLevelsForStock5.remove( 0 );

      // create
      expectedExistingSupplyStockLevelsForStock5.add( expectedNewCreatedSupplyStockLevelForStock5 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_5_ID, expectedExistingSupplyStockLevelsForStock5 );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock5,
            actualSupplyStockLevels );
   }


   @Test
   public void testMultipleOperationsWithoutPercentageSuccess()
         throws AmApiResourceNotFoundException {
      // update stock level 1
      expectedExistingSupplyStockLevelsForStock5.get( 0 ).setAllocationPercentage( null );
      expectedExistingSupplyStockLevelsForStock5.get( 0 ).setSafetyLevel( 0 );

      // update stock level 2
      expectedExistingSupplyStockLevelsForStock5.get( 1 ).setAllocationPercentage( null );
      expectedExistingSupplyStockLevelsForStock5.get( 1 ).setSafetyLevel( 0 );

      // create new stock level 3
      expectedNewCreatedSupplyStockLevelForStock5.setAllocationPercentage( null );
      expectedExistingSupplyStockLevelsForStock5.add( expectedNewCreatedSupplyStockLevelForStock5 );

      List<SupplyStockLevel> actualSupplyStockLevels = supplyStockLevelResourceBean
            .update( STOCK_5_ID, expectedExistingSupplyStockLevelsForStock5 );

      // set the expected percentages
      expectedExistingSupplyStockLevelsForStock5.get( 0 ).setAllocationPercentage(
            new BigDecimal( 20 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedExistingSupplyStockLevelsForStock5.get( 1 ).setAllocationPercentage(
            new BigDecimal( 80 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedNewCreatedSupplyStockLevelForStock5
            .setAllocationPercentage( new BigDecimal( 0 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );

      assertSupplyStockLevels( expectedExistingSupplyStockLevelsForStock5,
            actualSupplyStockLevels );
   }


   /**
    * Assert the actual supply stock levels match the expected supply stock levels
    *
    * @param expectedSupplyStockLevels
    * @param actualSupplyStockLevels
    */
   private void assertSupplyStockLevels( List<SupplyStockLevel> expectedSupplyStockLevels,
         List<SupplyStockLevel> actualSupplyStockLevels ) {

      Assert.assertNotNull( "Expected a list of supply stock levels", actualSupplyStockLevels );
      Assert.assertEquals( "Number of supply stock levels", expectedSupplyStockLevels.size(),
            actualSupplyStockLevels.size() );

      if ( checkNullStockLevelIds( expectedSupplyStockLevels ) ) {
         setStockLevelIds( expectedSupplyStockLevels, actualSupplyStockLevels );
      }

      // Assert that actual contains expected AND that expected contains actual. Both are necessary
      // in order to determine that the lists are identical to cover the case where we have [A,B,C]
      // and [A,A,B] since [A,B,C] contains [A,A,B], but not vice versa.
      Assert.assertTrue(
            "Expected: " + expectedSupplyStockLevels + " /n but was: " + actualSupplyStockLevels,
            expectedSupplyStockLevels.containsAll( actualSupplyStockLevels )
                  && actualSupplyStockLevels.containsAll( expectedSupplyStockLevels ) );
   }


   /**
    *
    * Assert the fields of a stock level against their default values or the values passed in.
    *
    * @param stockLevel
    * @param stock
    * @param location
    * @param stockLowAction
    * @param quantity
    */
   private void assertStockLevelWithDefaults( SupplyStockLevel stockLevel, String stock,
         String location, String stockLowAction, Integer quantity ) {
      Assert.assertEquals( "Stock", stock, stockLevel.getStockId() );
      Assert.assertEquals( "Location", location, stockLevel.getLocationId() );
      Assert.assertEquals( "Stock Low Action", stockLowAction, stockLevel.getStockLowAction() );
      Assert.assertEquals( "Quantity", quantity, stockLevel.getQuantity() );
      Assert.assertEquals( "Owner", DEFAULT_OWNER_ID, stockLevel.getOwnerId() );
      Assert.assertEquals( "Restock Level", new Integer( 0 ), stockLevel.getRestockLevel() );
      Assert.assertEquals( "Safety Level", new Integer( 0 ), stockLevel.getSafetyLevel() );
      Assert.assertEquals( "Max Level", new Integer( 0 ), stockLevel.getMaxLevel() );

   }


   /**
    *
    * Assert that values not included in the stock level API were defaulted properly. This includes
    * station weight factor, # of inbound flights and weighting factor.
    *
    * @param stockLevelId
    */
   private void assertOtherDefaults( String stockLevelId ) {
      InvLocStockKey invLocStockKey =
            legacyKeyUtil.getLegacyKeyByAltId( stockLevelId, InvLocStockKey.class );
      InvLocStockTable invLocStock = InvLocStockTable.findByPrimaryKey( invLocStockKey );
      Assert.assertEquals( "Station Weight Factor", new Double( 0 ),
            invLocStock.getWeightFactorQt() );

      InvLocTable invLoc = InvLocTable.findByPrimaryKey( invLocStockKey.getLocationKey() );
      Assert.assertEquals( "# of Inbound Flights", new Double( 0 ), invLoc.getInboundFlightsQt() );

      EqpStockNoTable eqpStockNo =
            EqpStockNoTable.findByPrimaryKey( invLocStockKey.getStockNoKey() );
      Assert.assertEquals( "Weighting Factor", new Double( 1 ), eqpStockNo.getMaxMultQt() );
   }


   private void assertThatOneWorkItemGenerated( String stockNoKey, String locationKey,
         String authorizedHrKey ) {
      DataSetArgument args = new DataSetArgument();
      args.add( "type", StockLevelCheckWorkItem.WORK_TYPE );
      QuerySet querySet =
            QuerySetFactory.getInstance().executeQuery( "utl_work_item", args, "data" );

      // Assert a new work item is generated with expected data
      Assert.assertEquals( "Expected row count: ", 1, querySet.getRowCount() );

      querySet.next();
      Assert.assertEquals( "Expected StockNoKey:LocationKey:HrKey :",
            stockNoKey + ":" + locationKey + ":" + authorizedHrKey, querySet.getString( "data" ) );
   }


   private boolean checkNullStockLevelIds( List<SupplyStockLevel> stockLevels ) {
      for ( SupplyStockLevel stockLevel : stockLevels ) {
         if ( stockLevel.getId() == null ) {
            return true;
         }
      }
      return false;
   }


   private void setStockLevelIds( List<SupplyStockLevel> expectedStockLevels,
         List<SupplyStockLevel> actualStockLevels ) {
      for ( SupplyStockLevel actualStockLevel : actualStockLevels ) {
         for ( SupplyStockLevel expectedStockLevel : expectedStockLevels ) {
            if ( StringUtils.areEqual( actualStockLevel.getLocationId(),
                  expectedStockLevel.getLocationId() )
                  && StringUtils.areEqual( actualStockLevel.getOwnerId(),
                        expectedStockLevel.getOwnerId() ) ) {
               expectedStockLevel.setId( actualStockLevel.getId() );
               break;
            }
         }
      }
   }


   private SupplyStockLevel buildSupplyStockLevelWithFields( String locationId,
         String stockLowAction ) {
      SupplyStockLevel supplyStockLevel = buildSupplyStockLevelMandatoryFields( locationId );
      supplyStockLevel.setOwnerId( OWNER_1_ID );
      supplyStockLevel.setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      supplyStockLevel.setMaxLevel( 2 );
      supplyStockLevel.setRestockLevel( 2 );
      supplyStockLevel.setSafetyLevel( 1 );
      supplyStockLevel.setStockLowAction( stockLowAction );
      return supplyStockLevel;
   }


   private SupplyStockLevel buildSupplyStockLevelAllFields( String stockId, String locationId,
         String stockLowAction, Integer quantity ) {
      SupplyStockLevel supplyStockLevel =
            buildSupplyStockLevelWithFields( locationId, stockLowAction );
      supplyStockLevel.setStockId( stockId );
      supplyStockLevel.setQuantity( quantity );
      return supplyStockLevel;
   }


   private SupplyStockLevel buildSupplyStockLevelMandatoryFields( String locationId ) {
      SupplyStockLevel supplyStockLevel = new SupplyStockLevel();
      supplyStockLevel.setLocationId( locationId );
      return supplyStockLevel;
   }


   private void constructExpectedResults() {
      // STOCK 1 -- existing stock levels
      SupplyStockLevel expectedSupplyStockLevel1OfStock1 = new SupplyStockLevel();
      expectedSupplyStockLevel1OfStock1.setId( STOCK_1_STOCKLEVEL_1_ID );
      expectedSupplyStockLevel1OfStock1.setStockId( STOCK_1_ID );
      expectedSupplyStockLevel1OfStock1.setLocationId( AIRPORT_1_ID );
      expectedSupplyStockLevel1OfStock1.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel1OfStock1.setRestockLevel( 1 );
      expectedSupplyStockLevel1OfStock1.setSafetyLevel( 1 );
      expectedSupplyStockLevel1OfStock1.setMaxLevel( 1 );
      expectedSupplyStockLevel1OfStock1.setQuantity( null );
      expectedSupplyStockLevel1OfStock1.setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel1OfStock1.setStockLowAction( MANUAL.getCd() );

      SupplyStockLevel expectedSupplyStockLevel2OfStock1 = new SupplyStockLevel();
      expectedSupplyStockLevel2OfStock1.setId( STOCK_1_STOCKLEVEL_2_ID );
      expectedSupplyStockLevel2OfStock1.setStockId( STOCK_1_ID );
      expectedSupplyStockLevel2OfStock1.setLocationId( AIRPORT_2_ID );
      expectedSupplyStockLevel2OfStock1.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel2OfStock1.setRestockLevel( 2 );
      expectedSupplyStockLevel2OfStock1.setSafetyLevel( 2 );
      expectedSupplyStockLevel2OfStock1.setMaxLevel( 2 );
      expectedSupplyStockLevel2OfStock1.setQuantity( 1 );
      expectedSupplyStockLevel2OfStock1
            .setAllocationPercentage( new BigDecimal( 0 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel2OfStock1.setStockLowAction( SHIPREQ.getCd() );

      expectedExistingSupplyStockLevelsForStock1 = new ArrayList<SupplyStockLevel>();
      expectedExistingSupplyStockLevelsForStock1.add( expectedSupplyStockLevel1OfStock1 );
      expectedExistingSupplyStockLevelsForStock1.add( expectedSupplyStockLevel2OfStock1 );

      // STOCK 2 -- new stock levels to be created during test
      SupplyStockLevel expectedSupplyStockLevel1OfStock2 = new SupplyStockLevel();
      expectedSupplyStockLevel1OfStock2.setStockId( STOCK_2_ID );
      expectedSupplyStockLevel1OfStock2.setLocationId( AIRPORT_2_SUPPLY_LOC_ID );
      expectedSupplyStockLevel1OfStock2.setOwnerId( DEFAULT_OWNER_ID );
      expectedSupplyStockLevel1OfStock2.setRestockLevel( 0 );
      expectedSupplyStockLevel1OfStock2.setSafetyLevel( 0 );
      expectedSupplyStockLevel1OfStock2.setMaxLevel( 0 );
      expectedSupplyStockLevel1OfStock2.setQuantity( null );
      expectedSupplyStockLevel1OfStock2.setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel1OfStock2.setStockLowAction( MANUAL.getCd() );

      SupplyStockLevel expectedSupplyStockLevel2OfStock2 = new SupplyStockLevel();
      expectedSupplyStockLevel2OfStock2.setStockId( STOCK_2_ID );
      expectedSupplyStockLevel2OfStock2.setLocationId( AIRPORT_2_SUPPLY_LOC_ID );
      expectedSupplyStockLevel2OfStock2.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel2OfStock2.setRestockLevel( 2 );
      expectedSupplyStockLevel2OfStock2.setSafetyLevel( 1 );
      expectedSupplyStockLevel2OfStock2.setMaxLevel( 2 );
      expectedSupplyStockLevel2OfStock2.setQuantity( 1 );
      expectedSupplyStockLevel2OfStock2
            .setAllocationPercentage( new BigDecimal( 0 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel2OfStock2.setStockLowAction( POREQ.getCd() );

      expectedNewCreatedSupplyStockLevelsForStock2 = new ArrayList<SupplyStockLevel>();
      expectedNewCreatedSupplyStockLevelsForStock2.add( expectedSupplyStockLevel1OfStock2 );
      expectedNewCreatedSupplyStockLevelsForStock2.add( expectedSupplyStockLevel2OfStock2 );

      // STOCK 4 -- existing stock levels
      SupplyStockLevel expectedSupplyStockLevel1OfStock4 = new SupplyStockLevel();
      expectedSupplyStockLevel1OfStock4.setId( STOCK_4_STOCKLEVEL_1_ID );
      expectedSupplyStockLevel1OfStock4.setStockId( STOCK_4_ID );
      expectedSupplyStockLevel1OfStock4.setLocationId( AIRPORT_2_ID );
      expectedSupplyStockLevel1OfStock4.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel1OfStock4.setRestockLevel( 4 );
      expectedSupplyStockLevel1OfStock4.setSafetyLevel( 3 );
      expectedSupplyStockLevel1OfStock4.setMaxLevel( 5 );
      expectedSupplyStockLevel1OfStock4.setQuantity( 2 );
      expectedSupplyStockLevel1OfStock4.setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel1OfStock4.setStockLowAction( POREQ.getCd() );

      expectedExistingSupplyStockLevelsForStock4 = new ArrayList<SupplyStockLevel>();
      expectedExistingSupplyStockLevelsForStock4.add( expectedSupplyStockLevel1OfStock4 );

      // STOCK 5 -- existing stock levels
      SupplyStockLevel expectedSupplyStockLevel1OfStock5 = new SupplyStockLevel();
      expectedSupplyStockLevel1OfStock5.setId( STOCK_5_STOCKLEVEL_1_ID );
      expectedSupplyStockLevel1OfStock5.setStockId( STOCK_5_ID );
      expectedSupplyStockLevel1OfStock5.setLocationId( AIRPORT_1_ID );
      expectedSupplyStockLevel1OfStock5.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel1OfStock5.setRestockLevel( 1 );
      expectedSupplyStockLevel1OfStock5.setSafetyLevel( 1 );
      expectedSupplyStockLevel1OfStock5.setMaxLevel( 1 );
      expectedSupplyStockLevel1OfStock5.setQuantity( null );
      expectedSupplyStockLevel1OfStock5.setAllocationPercentage(
            new BigDecimal( 20 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel1OfStock5.setStockLowAction( MANUAL.getCd() );

      SupplyStockLevel expectedSupplyStockLevel2OfStock5 = new SupplyStockLevel();
      expectedSupplyStockLevel2OfStock5.setId( STOCK_5_STOCKLEVEL_2_ID );
      expectedSupplyStockLevel2OfStock5.setStockId( STOCK_5_ID );
      expectedSupplyStockLevel2OfStock5.setLocationId( AIRPORT_1_ID );
      expectedSupplyStockLevel2OfStock5.setOwnerId( DEFAULT_OWNER_ID );
      expectedSupplyStockLevel2OfStock5.setRestockLevel( 1 );
      expectedSupplyStockLevel2OfStock5.setSafetyLevel( 1 );
      expectedSupplyStockLevel2OfStock5.setMaxLevel( 1 );
      expectedSupplyStockLevel2OfStock5.setQuantity( null );
      expectedSupplyStockLevel2OfStock5.setAllocationPercentage(
            new BigDecimal( 80 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel2OfStock5.setStockLowAction( MANUAL.getCd() );

      expectedExistingSupplyStockLevelsForStock5 = new ArrayList<SupplyStockLevel>();
      expectedExistingSupplyStockLevelsForStock5.add( expectedSupplyStockLevel1OfStock5 );
      expectedExistingSupplyStockLevelsForStock5.add( expectedSupplyStockLevel2OfStock5 );

      // -- new stock level to be created during test
      expectedNewCreatedSupplyStockLevelForStock5 = new SupplyStockLevel();
      expectedNewCreatedSupplyStockLevelForStock5.setStockId( STOCK_5_ID );
      expectedNewCreatedSupplyStockLevelForStock5.setLocationId( AIRPORT_2_SUPPLY_LOC_ID );
      expectedNewCreatedSupplyStockLevelForStock5.setOwnerId( DEFAULT_OWNER_ID );
      expectedNewCreatedSupplyStockLevelForStock5.setRestockLevel( 0 );
      expectedNewCreatedSupplyStockLevelForStock5.setSafetyLevel( 0 );
      expectedNewCreatedSupplyStockLevelForStock5.setMaxLevel( 0 );
      expectedNewCreatedSupplyStockLevelForStock5.setQuantity( null );
      expectedNewCreatedSupplyStockLevelForStock5
            .setAllocationPercentage( new BigDecimal( 0 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedNewCreatedSupplyStockLevelForStock5.setStockLowAction( MANUAL.getCd() );

      // STOCK 6 -- existing stock levels
      SupplyStockLevel expectedSupplyStockLevel1OfStock6 = new SupplyStockLevel();
      expectedSupplyStockLevel1OfStock6.setId( STOCK_6_STOCKLEVEL_1_ID );
      expectedSupplyStockLevel1OfStock6.setStockId( STOCK_6_ID );
      expectedSupplyStockLevel1OfStock6.setLocationId( AIRPORT_3_ID );
      expectedSupplyStockLevel1OfStock6.setOwnerId( OWNER_1_ID );
      expectedSupplyStockLevel1OfStock6.setRestockLevel( 1 );
      expectedSupplyStockLevel1OfStock6.setSafetyLevel( 1 );
      expectedSupplyStockLevel1OfStock6.setMaxLevel( 1 );
      expectedSupplyStockLevel1OfStock6.setAllocationPercentage(
            new BigDecimal( 100 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
      expectedSupplyStockLevel1OfStock6.setStockLowAction( MANUAL.getCd() );

      expectedExistingSupplyStockLevelsForStock6 = new ArrayList<SupplyStockLevel>();
      expectedExistingSupplyStockLevelsForStock6.add( expectedSupplyStockLevel1OfStock6 );
   }

}
