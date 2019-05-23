package com.mxi.mx.report.query.inventory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.FinancialTransactionBuilder;
import com.mxi.am.domain.builder.FinancialTransactionDetailBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactoryFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncTCodeKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.fnc.FncTCode;


/**
 * This class test the package.function that feeds data to the Detailed Financial Log report
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class DetailedFinancialLogReportTest {

   @Rule
   public DatabaseConnectionRule sConnection = new DatabaseConnectionRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private Date iTransactionDate;
   private Date iFromDate;
   private Date iToDate;
   private Calendar iCalendar = Calendar.getInstance();
   private QuerySet iQs;
   private GlobalParametersStub iGlobalParams;

   private static final int ISSUE_TRANSACTION_ID = 1;
   private static final int TURNIN_TRANSACTION_ID = 2;
   private static final String ISSUE_XACTION_TYPE = "ISSUE";
   private static final String TURNIN_XACTION_TYPE = "TURN IN";
   private static final String INVASSET_ACCOUNT_CD = "TESTINVASSET";
   private static final String EXPENSE_ACCOUNT_CD = "TESTEXPENSE";
   private static final String INVASSET_ACCOUNT_TYPE_CD = "INVASSET";
   private static final String EXPENSE_ACCOUNT_TYPE_CD = "EXPENSE";
   private static final String ACCOUNT_EXT_DESC_1 = "EXT-REF1";
   private static final String ACCOUNT_EXT_DESC_2 = "EXT-REF2";
   private static final String INVASSET_ACCOUNT_T_CODE = "TCODE1";
   private static final String EXPENSE_ACCOUNT_T_CODE = "TCODE2";
   private static final String INSTALL_SERIAL_NO = "INSTALL_SERIAL_NO";
   private static final String TURNIN_SERIAL_NO = "TURNIN_SERIAL_NO";
   private static final String OEM_PART_NO = "PART_NO_1";
   private static final String WO_REF_SDESC = "TEST-WP";
   private static final String AC_REG_CD = "TEST-AC";
   private static final String LOC_CD = "TEST-LOC";
   private static final String SUPPLY_LOC_CD = "TEST-SUPPLY-LOC";
   private static final Double CREDIT_AMOUNT_1 = 10.0;
   private static final Double DEBIT_AMOUNT_1 = 20.0;
   private static final Double CREDIT_AMOUNT_2 = 20.0;
   private static final Double DEBIT_AMOUNT_2 = 10.0;


   /**
    * This test passes the proper parameters to the function that feeds data to the report with the
    * purpose of retrieving four rows:
    *
    * Scenario: Under an Aircraft, a work package is created. There is a task in that work package
    * to remove and install a part inventory. A part request has been created and inventory
    * reserved. The reserved inventory is Auto-issued during installation and the removed inventory
    * is turned-in.
    *
    * We expect 2 transactions. One is Issue inventory transaction, another one is Turn In inventory
    * transaction. Both of them will have credit and debit transaction lines.
    *
    */
   @Test
   public void testReportWithAutoIssueAndTurnInData() {

      testReportWithIssueAndTurnInData( false );

   }


   /**
    * This test passes the proper parameters to the function that feeds data to the report with the
    * purpose of retrieving four rows:
    *
    * Scenario: Under an Aircraft, a work package is created. There is a task in that work package
    * to remove and install a part inventory. A part request has been created and inventory
    * reserved. The reserved inventory is MANUALLY issued and the removed inventory is turned-in.
    *
    * We expect 2 transactions. One is Issue inventory transaction, another one is Turn In inventory
    * transaction. Both of them will have credit and debit transaction lines.
    *
    */
   @Test
   public void testReportWithManualIssueAndTurnInData() {

      testReportWithIssueAndTurnInData( true );

   }


   private void testReportWithIssueAndTurnInData( boolean aIsManualIssue ) {

      // create transaction date
      iCalendar.add( Calendar.DAY_OF_MONTH, -1 );

      // create test data
      createData( aIsManualIssue );

      // create date arguments
      iCalendar.add( Calendar.DAY_OF_MONTH, -2 );
      iFromDate = iCalendar.getTime();
      iToDate = new Date();

      // execute report
      execute();

      // assert report is not empty.
      Assert.assertTrue( !iQs.isEmpty() );

      // assert report show 4 records
      Assert.assertEquals( iQs.getRowCount(), 4 );

      // assert column values for all rows
      while ( iQs.next() ) {

         boolean lIssueXaction = iQs.getString( "xaction_type_cd" ).equals( ISSUE_XACTION_TYPE );
         boolean lTurnInXaction =
               iQs.getString( "xaction_type_cd" ).equals( TURNIN_TRANSACTION_ID );
         boolean lCredit = iQs.getString( "account_cd" ).equals( INVASSET_ACCOUNT_CD );
         boolean lDebit = iQs.getString( "account_cd" ).equals( EXPENSE_ACCOUNT_CD );

         if ( lIssueXaction && lCredit ) {
            assertRecords( ISSUE_TRANSACTION_ID, ISSUE_XACTION_TYPE, null, null,
                  INVASSET_ACCOUNT_CD, INVASSET_ACCOUNT_TYPE_CD, ACCOUNT_EXT_DESC_1,
                  CREDIT_AMOUNT_1, 0.0, INVASSET_ACCOUNT_T_CODE, null, null, OEM_PART_NO,
                  INSTALL_SERIAL_NO, WO_REF_SDESC, AC_REG_CD, LOC_CD, SUPPLY_LOC_CD );
         }
         if ( lIssueXaction && lDebit ) {
            assertRecords( ISSUE_TRANSACTION_ID, ISSUE_XACTION_TYPE, null, null, EXPENSE_ACCOUNT_CD,
                  EXPENSE_ACCOUNT_TYPE_CD, ACCOUNT_EXT_DESC_2, 0.0, DEBIT_AMOUNT_1,
                  EXPENSE_ACCOUNT_T_CODE, null, null, OEM_PART_NO, INSTALL_SERIAL_NO, WO_REF_SDESC,
                  AC_REG_CD, LOC_CD, SUPPLY_LOC_CD );
         }

         if ( lTurnInXaction && lDebit ) {
            assertRecords( TURNIN_TRANSACTION_ID, TURNIN_XACTION_TYPE, null, null,
                  INVASSET_ACCOUNT_CD, INVASSET_ACCOUNT_TYPE_CD, ACCOUNT_EXT_DESC_1, 0.0,
                  DEBIT_AMOUNT_2, INVASSET_ACCOUNT_T_CODE, null, null, OEM_PART_NO,
                  TURNIN_SERIAL_NO, WO_REF_SDESC, AC_REG_CD, LOC_CD, SUPPLY_LOC_CD );
         }
         if ( lTurnInXaction && lCredit ) {
            assertRecords( TURNIN_TRANSACTION_ID, TURNIN_XACTION_TYPE, null, null,
                  EXPENSE_ACCOUNT_CD, EXPENSE_ACCOUNT_TYPE_CD, ACCOUNT_EXT_DESC_2, CREDIT_AMOUNT_2,
                  0.0, EXPENSE_ACCOUNT_T_CODE, null, null, OEM_PART_NO, TURNIN_SERIAL_NO,
                  WO_REF_SDESC, AC_REG_CD, LOC_CD, SUPPLY_LOC_CD );
         }
      }

   }


   /**
    * This test passes the proper parameters to the function that feeds data to the report with the
    * purpose of not retrieving any data
    *
    */
   @Test
   public void testReportIsEmpty() {

      // create transaction date
      iCalendar.add( Calendar.DAY_OF_MONTH, 0 );

      // create test data
      createData( false );

      // create date arguments
      iCalendar.add( Calendar.DAY_OF_MONTH, -3 );
      iFromDate = iCalendar.getTime();
      iCalendar.add( Calendar.DAY_OF_MONTH, -4 );
      iToDate = iCalendar.getTime();

      // execute report
      execute();

      // report do not show data
      Assert.assertTrue( iQs.isEmpty() );

   }


   /**
    * Set up the global parameters.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {

      iGlobalParams = new GlobalParametersStub( "LOGIC" );
      iGlobalParams.setBoolean( "SPEC2000_UPPERCASE_PART_NO_OEM", false );
      iGlobalParams.setBoolean( "SPEC2000_UPPERCASE_ACCOUNT_CD", false );

      GlobalParameters.setInstance( iGlobalParams );
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );
      SequenceGeneratorFactory.setInstance( new SequenceGeneratorFactoryFake() );
      EjbFactory.setSingleton( new EjbFactoryStub() );
      TriggerFactory.setInstance( new TriggerFactoryStub() );

   }


   /**
    * Tears down the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @After
   public void tearDown() throws Exception {
      PrimaryKeyService.setSingleton( null );
      SequenceGeneratorFactory.setInstance( null );
      EjbFactory.setSingleton( null );
      TriggerFactory.setInstance( null );
      GlobalParameters.setInstance( "LOGIC", null );

   }


   /**
    * Create test data
    *
    */
   private void createData( boolean aIsManualIssue ) {

      // build INVASSET account TCode
      FncTCode lInvassetTCode = FncTCode.create();
      lInvassetTCode.setTCodeCd( INVASSET_ACCOUNT_T_CODE );
      FncTCodeKey lInvassetTCd = lInvassetTCode.insert();

      // build EXPENSE account TCode
      FncTCode lExpenseTCode = FncTCode.create();
      lExpenseTCode.setTCodeCd( EXPENSE_ACCOUNT_T_CODE );
      FncTCodeKey lExpenseTCd = lExpenseTCode.insert();

      // build INVASSET finance account
      FncAccountKey lInvAssetTypeAccount = new AccountBuilder()
            .withType( RefAccountTypeKey.INVASSET ).withCode( INVASSET_ACCOUNT_CD )
            .withTCode( lInvassetTCd ).withExternalKey( ACCOUNT_EXT_DESC_1 ).build();

      // build EXPENSE finance account
      FncAccountKey lExpenseTypeAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( EXPENSE_ACCOUNT_CD ).withExternalKey( ACCOUNT_EXT_DESC_2 )
            .withTCode( lExpenseTCd ).build();

      // build part
      PartNoKey lPart = new PartNoBuilder().withFinancialType( RefFinanceTypeKey.CONSUM )
            .withOemPartNo( OEM_PART_NO ).build();

      // create aircraft inventory and reg cd
      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .withRegistrationCode( AC_REG_CD ).build();

      // create installed inventory
      InventoryKey lInstInventory = new InventoryBuilder().withSerialNo( INSTALL_SERIAL_NO )
            .withHighestInventory( lAircraft ).withClass( RefInvClassKey.SER ).build();

      // create removed and then turned in inventory
      InventoryKey lTurnInInventory = new InventoryBuilder().withSerialNo( TURNIN_SERIAL_NO )
            .withClass( RefInvClassKey.SER ).build();

      // create a supply location
      LocationKey lSupplyLocation = new LocationDomainBuilder().withCode( SUPPLY_LOC_CD )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // build a location
      LocationKey lLocation = new LocationDomainBuilder().withCode( LOC_CD )
            .withType( RefLocTypeKey.LINE ).withSupplyLocation( lSupplyLocation ).build();

      // create a work package on the aircraft
      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WO_REF_SDESC )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      // create a task under this work package
      TaskKey lTask = new TaskBuilder().withParentTask( lWorkPackage ).onInventory( lAircraft )
            .withWorkOrderNumber( WO_REF_SDESC ).build();

      // create a part request with the installed inventory as the reserved inventory
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 1.0 ).forTask( lTask ).withReservedInventory( lInstInventory )
            .withReqAircraft( lAircraft ).build();

      // create issue inv_xfer entry and create entries in evt_loc
      TransferKey lIssueTransferKey = new InventoryTransferBuilder().withQuantity( BigDecimal.ONE )
            .withType( RefXferTypeKey.ISSUE ).withInitEvent( lPartRequest.getEventKey() )
            .withInventory( lInstInventory ).build();

      // create turn in inv_xfer entry and create entries in evt_loc
      TransferKey lTurnInTransferKey = new InventoryTransferBuilder().withQuantity( BigDecimal.ONE )
            .withType( RefXferTypeKey.TURNIN ).withInventory( lTurnInInventory )
            .withInitEvent( lIssueTransferKey.getEventKey() ).build();

      // set up transaction date
      iTransactionDate = iCalendar.getTime();

      // Build ISSUE financial transaction log
      // The event will be null for autoissue, and issue transfer key for manual issue */
      EventKey lFinancialTransactionEvent = aIsManualIssue ? lIssueTransferKey.getEventKey() : null;
      FncXactionLogKey lFncXactionLogIssueKey =
            new FinancialTransactionBuilder().withTransactionDate( iTransactionDate )
                  .withEvent( lFinancialTransactionEvent ).withType( RefXactionTypeKey.ISSUE )
                  .withPart( lPart ).withInventory( lInstInventory ).withTask( lTask ).build();

      // build TURN IN financial transaction log
      FncXactionLogKey lFncXactionLogTurnInKey = new FinancialTransactionBuilder()
            .withTransactionDate( iTransactionDate ).withEvent( lTurnInTransferKey.getEventKey() )
            .withType( RefXactionTypeKey.TURNIN ).withPart( lPart )
            .withInventory( lTurnInInventory ).withTask( lTask ).build();

      // build ISSUE financial transaction log details
      new FinancialTransactionDetailBuilder( lFncXactionLogIssueKey, 1 )
            .withAccount( lInvAssetTypeAccount ).withCredit( new BigDecimal( CREDIT_AMOUNT_1 ) )
            .build();
      new FinancialTransactionDetailBuilder( lFncXactionLogIssueKey, 2 )
            .withAccount( lExpenseTypeAccount ).withDebit( new BigDecimal( DEBIT_AMOUNT_1 ) )
            .build();

      // build TURN IN financial transaction log details
      new FinancialTransactionDetailBuilder( lFncXactionLogTurnInKey, 1 )
            .withAccount( lInvAssetTypeAccount ).withDebit( new BigDecimal( DEBIT_AMOUNT_2 ) )
            .build();
      new FinancialTransactionDetailBuilder( lFncXactionLogTurnInKey, 2 )
            .withAccount( lExpenseTypeAccount ).withCredit( new BigDecimal( CREDIT_AMOUNT_2 ) )
            .build();

   }


   /**
    * Assert query set record
    *
    */
   private void assertRecords( int aTransactionId, String aTransactionType, String aTransactionDes,
         String aCurrencyCd, String aAccountCode, String aAccountTypeCode, String aAccountExtDesc,
         Double aCredit, Double aDebit, String aTCode, String aPoNum, String aInvoiceNum,
         String aPartNoOem, String aSerialNumber, String aWorkPackage, String aAcRegCd,
         String aLocCd, String aSupplyLocCd ) {

      Assert.assertEquals( aTransactionId, iQs.getInt( "xaction_id" ) );
      Assert.assertNotNull( iQs.getDate( "xaction_dt" ) );
      Assert.assertEquals( aTransactionType, iQs.getString( "xaction_type_cd" ) );
      Assert.assertEquals( aTransactionDes, iQs.getString( "xaction_ldesc" ) );
      Assert.assertEquals( aCurrencyCd, iQs.getString( "currency_cd" ) );
      Assert.assertEquals( aAccountCode, iQs.getString( "account_cd" ) );
      Assert.assertEquals( aAccountTypeCode, iQs.getString( "account_type_cd" ) );
      Assert.assertEquals( aAccountExtDesc, iQs.getString( "ext_key_sdesc" ) );
      Assert.assertEquals( aCredit, iQs.getDoubleObj( "credit_cost" ) );
      Assert.assertEquals( aDebit, iQs.getDoubleObj( "debit_cost" ) );
      Assert.assertEquals( aTCode, iQs.getString( "tcode_cd" ) );
      Assert.assertEquals( aPoNum, iQs.getString( "po_number" ) );
      Assert.assertEquals( aInvoiceNum, iQs.getString( "invoice_number" ) );
      Assert.assertEquals( aPartNoOem, iQs.getString( "part_no_oem" ) );
      Assert.assertEquals( aSerialNumber, iQs.getString( "serial_no_oem" ) );
      Assert.assertEquals( aWorkPackage, iQs.getString( "wo_ref_sdesc" ) );
      Assert.assertEquals( aAcRegCd, iQs.getString( "ac_reg_cd" ) );
      Assert.assertEquals( aLocCd, iQs.getString( "loc_cd" ) );
      Assert.assertEquals( aSupplyLocCd, iQs.getString( "supply_loc_cd" ) );

   }


   /**
    * Execute the report function
    *
    */
   private void execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aidt_from_date", iFromDate );
      lArgs.add( "aidt_to_date", iToDate );
      lArgs.add( "aiv_account_code", "*ALL" );
      lArgs.add( "ait_account_type_code", "INVASSET, EXPENSE" );

      iQs = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.report.query.inventory.DetailedFinancialLogReportQuery", lArgs );
   }

}
