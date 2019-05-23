package com.mxi.mx.core.services.part;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipSegmentBuilder;
import com.mxi.am.domain.builder.ShipSegmentMapBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.fnc.FncAccount;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.ref.RefFinancialClass;


/**
 *
 * @author kTandon
 * @created August 5, 2016
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FinancialClassAndAccountUpdateTest {

   private static final BigDecimal RECEIVED_QT = new BigDecimal( 1 );

   private HumanResourceKey iHr;
   private FncAccountKey iOldAccount;
   private FncAccountKey iNewAccount;
   private PartNoKey iPartNo;
   private PurchaseOrderLineKey iPurchaseOrderLine;
   private static final String OLD_ACCOUNT_CD_FIXASSET = "FIXASSET_OLD";
   private static final String NEW_ACCOUNT_CD_FIXASSET = "FIXASSET_NEW";
   private static final String OLD_ACCOUNT_CD_INVASSET = "INVASSET_OLD";
   private static final String NEW_ACCOUNT_CD_INVASSET = "INVASSET_NEW";
   private static final String OLD_ACCOUNT_CD_EXPENSE = "EXPENSE-OLD";
   private static final String NEW_ACCOUNT_CD_EXPENSE = "EXPENSE-NEW";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void testBothFinancialClassAndPurchaseOrderLineAccountChanged() throws Exception {

      // Create the old finance type and asset account for the part
      createRotablePartWithPurchaseOrderLine();

      // Create the new finance type, financial class and asset account for change.
      RefFinanceTypeKey lNewFinanceType = RefFinanceTypeKey.CONSUM;

      RefFinancialClass lRefFinancialClass = RefFinancialClass.create( lNewFinanceType.getCd() );
      lRefFinancialClass.setFinancialType( lNewFinanceType );
      lRefFinancialClass.insert();

      iNewAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( NEW_ACCOUNT_CD_INVASSET ).isDefault().build();

      // Run the logic to be tested
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, lNewFinanceType ), iHr );

      // Assert the result
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass().getCd(),
            lNewFinanceType.getCd() );

      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iNewAccount ).getPk() );

   }


   /**
    * To check if finance Type is not changed and it is "Expense" then order line account is not
    * changed
    *
    * @throws Exception
    */
   @Test
   public void testOrderLineAccountNotChangedWhenExpenseFinanceTypeNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, null, null );

      // post condition data set up
      new AccountBuilder().withType( RefAccountTypeKey.EXPENSE ).withCode( NEW_ACCOUNT_CD_EXPENSE )
            .isDefault().build();

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_EXPENSE, RefFinanceTypeKey.EXPENSE ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.EXPENSE.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is closed then order line account should not change
    *
    * @throws Exception
    */
   @Test
   public void testClosedOrderLineAccountNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, null, RefEventStatusKey.POCLOSED );

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.ROTABLE, RefAccountTypeKey.FIXASSET,
            NEW_ACCOUNT_CD_FIXASSET );

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_FIXASSET, RefFinanceTypeKey.ROTABLE ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.ROTABLE.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is BORROW then financial type can change but order line account should not
    * change
    *
    * @throws Exception
    */
   @Test
   public void testBorrowOrderLineAccountNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, RefPoLineTypeKey.BORROW, null );

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is EXCHANGE then financial type can change but order line account should not
    * change
    *
    * @throws Exception
    */
   @Test
   public void testExchangeOrderLineAccountNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, RefPoLineTypeKey.EXCHANGE, null );

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );;

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is repair then financial type can change but order line account should not
    * change
    *
    * @throws Exception
    */
   @Test
   public void testRepairOrderLineAccountNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, RefPoLineTypeKey.REPAIR, null );

      // post condition data set up
      // set new account and financial class
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );;

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is consign then financial type can change but order line account should not
    * change
    *
    * @throws Exception
    */
   @Test
   public void testConsignOrderLineAccountNotChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.EXPENSE, OLD_ACCOUNT_CD_EXPENSE,
            RefFinanceTypeKey.EXPENSE, RefPoLineTypeKey.CONSIGN, null );

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if order is non closed Purchase Order with received inventory then financial class
    * should change but order line account should not change
    *
    * @throws Exception
    */
   @Test
   public void testPurchaseOrderWithReceivedInvLineAccountNotChanged() throws Exception {

      // Create the old finance type and asset account for the part
      createRotablePartWithPurchaseOrderLine();

      // create shipment for order
      ShipmentKey lShipment = new ShipmentDomainBuilder().withType( RefShipmentTypeKey.PURCHASE )
            .withOrder( iPurchaseOrderLine.getPurchaseOrderKey() ).build();

      // create shipment line
      new ShipmentLineBuilder( lShipment ).forOrderLine( iPurchaseOrderLine )
            .withReceivedQuantity( RECEIVED_QT.doubleValue() ).build();

      // create a vendor location
      LocationKey lLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      // create a segment
      ShipSegmentKey lShipSegment = new ShipSegmentBuilder().fromLocation( lLocation ).build();

      // create a map to link segment to shipment
      new ShipSegmentMapBuilder().withShipment( lShipment ).withSegment( lShipSegment )
            .withOrder( 1 ).build();

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iOldAccount ).getPk() );

   }


   /**
    * to check if Purchase Order is non closed with no inventory then financial class and order line
    * account should change
    *
    * @throws Exception
    */
   @Test
   public void testPurchaseOrderLineAccountChanged() throws Exception {

      // pre condition data set up
      createPartAndOrderLine( RefAccountTypeKey.INVASSET, OLD_ACCOUNT_CD_INVASSET,
            RefFinanceTypeKey.ROTABLE, RefPoLineTypeKey.PURCHASE, RefEventStatusKey.POOPEN );

      // post condition data set up
      createPostAccountAndFinancialClass( RefFinanceTypeKey.CONSUM, RefAccountTypeKey.INVASSET,
            NEW_ACCOUNT_CD_INVASSET );;

      // update the financial class
      PartNoService.editFinancials( iPartNo,
            getPartFinancialsTO( NEW_ACCOUNT_CD_INVASSET, RefFinanceTypeKey.CONSUM ), iHr );

      // assert post conditions
      assertEquals( EqpPartNoTable.findByPrimaryKey( iPartNo ).getFinancialClass(),
            RefFinanceTypeKey.CONSUM.getCd() );
      assertEquals( POLineTable.findByPrimaryKey( iPurchaseOrderLine ).getAccount(),
            FncAccount.findByPrimaryKey( iNewAccount ).getPk() );

   }


   /**
    * Data setup
    *
    * @param aAccountType1
    *           Account Type Key
    * @param aAcount1
    *           Account Code
    * @param aAccountType2
    *           Account Type Key
    * @param aAcount2
    *           Account Code
    * @param aFinanceType
    *           Finance Type
    * @param aPotype
    *           Order Type
    * @param aEventStatus
    *           Event Status
    * @throws MxException
    * @throws ParseException
    */
   private void createPartAndOrderLine( RefAccountTypeKey aAccountType, String aAcount,
         RefFinanceTypeKey aFinanceType, RefPoLineTypeKey aPoLinetype,
         RefEventStatusKey aEventStatus ) throws MxException {

      // create a finance account
      iOldAccount =
            new AccountBuilder().withType( aAccountType ).withCode( aAcount ).isDefault().build();

      // create a part. The financial class is created in the part no builder with the same code of
      // finance type.
      iPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( aFinanceType ).withStatus( RefPartStatusKey.ACTV )
            .withAssetAccount( iOldAccount ).withAverageUnitPrice( BigDecimal.TEN ).build();

      // create a purchase order line
      iPurchaseOrderLine =
            new OrderLineBuilder( new OrderBuilder().withStatus( aEventStatus ).build() )
                  .forPart( iPartNo ).withAccount( iOldAccount ).withLineType( aPoLinetype )
                  .build();

   }


   /**
    * Create rotable part purchase order line.
    *
    * @throws ParseException
    * @throws MxException
    *
    */
   private void createRotablePartWithPurchaseOrderLine() throws MxException {

      createPartAndOrderLine( RefAccountTypeKey.FIXASSET, OLD_ACCOUNT_CD_FIXASSET,
            RefFinanceTypeKey.ROTABLE, RefPoLineTypeKey.PURCHASE, RefEventStatusKey.POOPEN );

   }


   /**
    * Data setup
    *
    * @param setAssetAccount
    *           Asset Account
    * @param aFinanceType
    *           Finance Type
    *
    * @throws MxException
    * @throws ParseException
    */
   private PartFinancialsTO getPartFinancialsTO( String setAssetAccount,
         RefFinanceTypeKey aFinanceType ) throws MxException {

      PartFinancialsTO lTO = new PartFinancialsTO();
      lTO.setAssetAccount( setAssetAccount );
      lTO.setFinancialClass( aFinanceType.getCd() );

      return lTO;

   }


   /**
    * Data setup
    *
    * @param aFinanceType
    *           Finance Type
    *
    * @throws MxException
    * @throws ParseException
    */
   private void createPostAccountAndFinancialClass( RefFinanceTypeKey aFinanceType,
         RefAccountTypeKey aAccountType, String aAcount ) throws MxException {

      iNewAccount =
            new AccountBuilder().withType( aAccountType ).withCode( aAcount ).isDefault().build();

      RefFinancialClass lRefFinancialClass = RefFinancialClass.create( aFinanceType.getCd() );
      lRefFinancialClass.setFinancialType( aFinanceType );
      lRefFinancialClass.insert();

   }

}
