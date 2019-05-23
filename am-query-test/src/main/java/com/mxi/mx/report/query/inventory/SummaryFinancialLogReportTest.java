
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
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactoryFake;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncTCodeKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.table.fnc.FncTCode;


/**
 * This class test the package.function that feeds data to the Summary Financial Log report
 *
 * @author gpichetto
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class SummaryFinancialLogReportTest {

   @Rule
   public DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   private FncAccountKey iInvAssetTypeAccount;
   private FncAccountKey iAdjQtyTypeAccount;

   private PartNoKey iPart;
   private Date iTransactionDate;
   private Date iFromDate;
   private Date iToDate;
   private Calendar iCalendar = Calendar.getInstance();
   private QuerySet iQs;
   private GlobalParametersStub iGlobalParams;

   private static final String ACCOUNT_CODE_2 = "TESTINVASSET";
   private static final String ACCOUNT_CODE_1 = "TESTADJQTY";
   private static final String ACCOUNT_TYPE_CODE_2 = "INVASSET";
   private static final String ACCOUNT_TYPE_CODE_1 = "ADJQTY";
   private static final String ACCOUNT_EXT_DESC_2 = "EXT-REF1";
   private static final String ACCOUNT_EXT_DESC_1 = "EXT-REF2";
   private static final Double CREDIT_AMOUNT_2 = 0.0;
   private static final Double DEBIT_AMOUNT_2 = 20.0;
   private static final Double CREDIT_AMOUNT_1 = 20.0;
   private static final Double DEBIT_AMOUNT_1 = 0.0;
   private static final String T_CODE = "TCODE1";


   /**
    * Set up the test case.
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

      // build TCode
      FncTCode lFncTCode = FncTCode.create();
      lFncTCode.setTCodeCd( "TCODE1" );
      FncTCodeKey lFncTCodeKey = lFncTCode.insert();

      // build INVASSET finance account
      iInvAssetTypeAccount =
            new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).withCode( "TESTINVASSET" )
                  .withTCode( lFncTCodeKey ).withExternalKey( "EXT-REF1" ).build();

      // build ADJQTY finance account
      iAdjQtyTypeAccount = new AccountBuilder().withType( RefAccountTypeKey.ADJQTY )
            .withCode( "TESTADJQTY" ).withExternalKey( "EXT-REF2" ).build();

      // build part
      iPart = new PartNoBuilder().withFinancialType( RefFinanceTypeKey.CONSUM )
            .withOemPartNo( "PART_NO_1" ).build();

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
   private void createData() {

      // set up transaction date
      iTransactionDate = iCalendar.getTime();

      // build financial transaction log
      FncXactionLogKey lFncXactionLogKey = new FinancialTransactionBuilder()
            .withTransactionDate( iTransactionDate ).withType( RefXactionTypeKey.ADJPRICE )
            .withDescription( "Created 10 EA" ).withPart( iPart ).build();

      // build financial transaction log details
      new FinancialTransactionDetailBuilder( lFncXactionLogKey, 1 )
            .withAccount( iInvAssetTypeAccount ).withDebit( new BigDecimal( 20 ) ).build();

      new FinancialTransactionDetailBuilder( lFncXactionLogKey, 2 )
            .withAccount( iAdjQtyTypeAccount ).withCredit( new BigDecimal( 20 ) ).build();

   }


   /**
    * This test passes the proper parameters to the function that feeds data to the report with the
    * purpose of retrieving two rows
    *
    */
   @Test
   public void testReportWithData() {

      // create transaction date
      iCalendar.add( Calendar.DAY_OF_MONTH, -1 );

      // create test data
      createData();

      // create date arguments
      iCalendar.add( Calendar.DAY_OF_MONTH, -2 );
      iFromDate = iCalendar.getTime();
      iToDate = new Date();

      // execute report
      execute();

      // assert report is not empty.
      Assert.assertTrue( !iQs.isEmpty() );

      // assert report show 2 records
      Assert.assertTrue( iQs.getRowCount() == 2 );

      // assert column values for the first row
      iQs.first();

      this.assertRecords( ACCOUNT_CODE_1, ACCOUNT_TYPE_CODE_1, ACCOUNT_EXT_DESC_1, CREDIT_AMOUNT_1,
            DEBIT_AMOUNT_1, null );

      // assert column values for the second row
      iQs.last();

      this.assertRecords( ACCOUNT_CODE_2, ACCOUNT_TYPE_CODE_2, ACCOUNT_EXT_DESC_2, CREDIT_AMOUNT_2,
            DEBIT_AMOUNT_2, T_CODE );

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
      createData();

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
    * Execute the report function
    *
    */
   private void execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aidt_from_date", iFromDate );
      lArgs.add( "aidt_to_date", iToDate );
      lArgs.add( "aiv_account_code", "*ALL" );
      lArgs.add( "ait_account_type_code", "INVASSET, ADJQTY" );

      iQs = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.report.query.inventory.SummaryFinancialLogReportQuery", lArgs );
   }


   /**
    * Assert query set record
    *
    */
   private void assertRecords( String aAccountCode, String aAccountTypeCode, String aAccountExtDesc,
         Double aCredit, Double aDebit, String aTCode ) {

      Assert.assertEquals( iQs.getString( "account_cd" ), aAccountCode );
      Assert.assertEquals( iQs.getString( "account_type_cd" ), aAccountTypeCode );
      Assert.assertEquals( iQs.getString( "ext_key_sdesc" ), aAccountExtDesc );
      Assert.assertEquals( iQs.getDoubleObj( "credit_cost" ), aCredit );
      Assert.assertEquals( iQs.getDoubleObj( "debit_cost" ), aDebit );
      Assert.assertEquals( iQs.getString( "tcode_cd" ), aTCode );

   }

}
