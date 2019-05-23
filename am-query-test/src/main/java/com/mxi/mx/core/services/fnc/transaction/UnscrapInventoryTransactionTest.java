package com.mxi.mx.core.services.fnc.transaction;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.EventInventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.ref.RefFinancialClass;
import com.mxi.mx.core.unittest.table.inv.InvInv;


public class UnscrapInventoryTransactionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final int USER_ID = 0;
   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "SYSTEM" );

   private InventoryKey iInventoryKey;
   private PartNoKey iPartNoKey = new PartNoKey( 4650, 1 );
   private OwnerKey iOwnerKey = new OwnerKey( 4650, 1 );
   private EventKey iEventKey = new EventKey( 4650, 1 );
   private InvCndChgEventKey iInvCndChgEventKey = new InvCndChgEventKey( 4650, 1 );
   private UnscrapInventoryTransactionStub iUnscrapInventoryTransactionStub;

   private InvCndChgEventDao iInvCndChgEventDao;
   private InvCndChgInvDao iInvCndChgInvDao;


   @Test
   public void executeTransaction_inventorywithFinanceStatusCodeNew() {

      updateInventory( null, InvInvTable.FinanceStatusCd.NEW );

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // no transaction created
      assertNull( iUnscrapInventoryTransactionStub.iRotableTransaction );
      assertNull( iUnscrapInventoryTransactionStub.iFinancialTransaction );
   }


   @Test
   public void executeTransaction_consumable_notIssued() {

      updateFinancialClass();

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // financial transaction was created
      assertNotNull( iUnscrapInventoryTransactionStub.iFinancialTransaction );
   }


   @Test
   public void executeTransaction_consumable_issued() {

      updateFinancialClass();
      updateInventory( true, null );

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // no financial transaction created
      assertNull( iUnscrapInventoryTransactionStub.iFinancialTransaction );
   }


   @Test
   public void executeTansaction_rotable_issued() {

      // update the inventory to be issued
      updateInventory( true, null );

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // rotable transaction was created
      assertNotNull( iUnscrapInventoryTransactionStub.iRotableTransaction );

   }


   @Test
   public void executeTransaction_rotable_notIssued() {

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // rotable transaction was created
      assertNotNull( iUnscrapInventoryTransactionStub.iRotableTransaction );
   }


   @Test
   public void executeTransaction_inventoryOwnerIsNotLocal() {

      InvOwnerTable lInvOwnerTable = InvOwnerTable.findByPrimaryKey( iOwnerKey );
      lInvOwnerTable.setLocalBool( false );
      lInvOwnerTable.update();

      iUnscrapInventoryTransactionStub =
            new UnscrapInventoryTransactionStub( iInventoryKey, FncAccountKey.VOID, 1, "Notes" );

      // subject under test
      iUnscrapInventoryTransactionStub.execute();

      // no financial transaction created
      assertNull( iUnscrapInventoryTransactionStub.iRotableTransaction );
      assertNull( iUnscrapInventoryTransactionStub.iFinancialTransaction );
   }


   private void updateFinancialClass() {

      RefFinancialClass lRefFinancialClassTable = RefFinancialClass.create( "CONSUMABLE" );
      lRefFinancialClassTable.setFinancialType( RefFinanceTypeKey.CONSUM );
      RefFinancialClassKey lRefFinancialClassKey = lRefFinancialClassTable.insert();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lEqpPartNoTable.setFinancialClass( lRefFinancialClassKey );
      lEqpPartNoTable.update();
   }


   private void updateInventory( Boolean aIssueBool, FinanceStatusCd aCondition ) {
      InvInvTable lInvInvTable = InvInv.findByPrimaryKey( iInventoryKey );

      if ( aIssueBool != null ) {
         lInvInvTable.setIssuedBool( aIssueBool );
      }

      if ( aCondition != null ) {
         lInvInvTable.setFinanceStatusCd( aCondition );
      }

      lInvInvTable.update();
   }


   @Before
   public void setUp() {

      iInvCndChgInvDao = InjectorContainer.get().getInstance( InvCndChgInvDao.class );
      iInvCndChgEventDao = InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // create a financial class
      RefFinancialClass lRefFinancialClassTable = RefFinancialClass.create( "ROTABLE" );
      lRefFinancialClassTable.setFinancialType( RefFinanceTypeKey.ROTABLE );
      RefFinancialClassKey lRefFinancialClassKey = lRefFinancialClassTable.insert();

      // create an inventory owner
      InvOwnerTable lInvOwnerTable = InvOwnerTable.create( iOwnerKey );
      lInvOwnerTable.setLocalBool( true );
      lInvOwnerTable.insert();

      // create a rotable part
      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.create( iPartNoKey );
      lEqpPartNoTable.setTotalQt( BigDecimal.ONE );
      lEqpPartNoTable.setTotalValue( new BigDecimal( 150 ) );
      lEqpPartNoTable.setPartNoOem( "PART_TEST_1" );
      lEqpPartNoTable.setQtyUnit( RefQtyUnitKey.EA );
      lEqpPartNoTable.setAvgUnitPrice( new BigDecimal( 2.5 ) );
      lEqpPartNoTable.setFinancialClass( lRefFinancialClassKey );
      lEqpPartNoTable.insert();

      // create an un-scrapped inventory
      iInventoryKey = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aBuilder ) {
            aBuilder.setPartNumber( iPartNoKey );
            aBuilder.setOwner( iOwnerKey );
            aBuilder.setCondition( RefInvCondKey.RFI );
            aBuilder.setIssued( false );
         }
      } );

      // create an historic event for the inventory
      EvtEventTable lEvtEventTable = EvtEventTable.create( iEventKey );
      lEvtEventTable.setEventType( RefEventTypeKey.AC );
      lEvtEventTable.setEventStatus( RefEventStatusKey.ACRFI );
      lEvtEventTable.insert();

      new EventInventoryBuilder().withEventKey( iEventKey ).withInventoryKey( iInventoryKey )
            .isMainInv( true ).build();

      // Data setup for post conversion.
      InvCndChgInvTable lInvCndChgInvTable = iInvCndChgInvDao.create();
      lInvCndChgInvTable.setInventory( iInventoryKey );
      lInvCndChgInvTable.setEventKey( iInvCndChgEventKey );
      lInvCndChgInvTable.setMainInvBool( true );
      iInvCndChgInvDao.insert( lInvCndChgInvTable );

      // Data setup for post conversion.
      InvCndChgEventTable lInvCndChgEventTable = iInvCndChgEventDao.create( iInvCndChgEventKey );
      lInvCndChgEventTable.setEventStatus( RefEventStatusKey.ACRFI );
      iInvCndChgEventDao.insert( lInvCndChgEventTable );
   }


   /**
    * Stub class for UnscrapInventoryTransaction that removes internal dependencies
    */
   static class UnscrapInventoryTransactionStub extends UnscrapInventoryTransaction {

      FinancialTransaction iFinancialTransaction = null;
      RotableTransaction iRotableTransaction = null;
      List<TransactionLine> iTransactionLines = new ArrayList<>();


      public UnscrapInventoryTransactionStub(InventoryKey aInventory, FncAccountKey aAccount,
            double aQty, String aUserNote) {
         super( aInventory, aAccount, aQty, aUserNote );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void addTransactionLine( FncAccountKey aAccount, BigDecimal aAmount,
            boolean aDebit ) {
         iTransactionLines.add( new TransactionLine( aAccount, aAmount, aDebit ) );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void createFinancialTransaction( RefXactionTypeKey aXactionType, Date aXactionDt,
            String aXactionLdesc, InventoryKey aInventory, PartNoKey aPartNo, EventKey aEvent,
            InvCndChgEventKey aInvCndChgEvent, PurchaseOrderLineKey aPOLine,
            PurchaseInvoiceLineKey aPOInvoiceLine, BigDecimal aQuantity, BigDecimal aUnitPrice,
            RefCurrencyKey aTransactionCurrency, BigDecimal aExchangeRate ) {
         iFinancialTransaction = new FinancialTransaction( aXactionType, aInventory, aPartNo,
               aEvent, aInvCndChgEvent, aQuantity );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void createRotablePartAdjust( RefXactionTypeKey aXactionType, HumanResourceKey aHr,
            Date aDate, double aAdjustQty, String aUserNote, String aSystemNote, EventKey aEvent,
            InvCndChgEventKey aInvCndChgEvent, PurchaseOrderLineKey aOrderLine ) {

         iRotableTransaction = new RotableTransaction( aXactionType, aAdjustQty, aSystemNote,
               aEvent, aInvCndChgEvent );
      }


      /**
       * Financial transaction for rotables
       */
      static class RotableTransaction {

         final double iQuantity;
         final RefXactionTypeKey iXactionType;
         final String iNotes;
         final EventKey iEvent;
         final InvCndChgEventKey iInvCndChgEvent;


         RotableTransaction(RefXactionTypeKey aRefXactionTypeKey, double aQuantity, String aNotes,
               EventKey aEvent, InvCndChgEventKey aInvCndChgEvent) {
            iXactionType = aRefXactionTypeKey;
            iQuantity = aQuantity;
            iNotes = aNotes;
            iEvent = aEvent;
            iInvCndChgEvent = aInvCndChgEvent;
         }
      }

      /**
       * The financial transaction
       */
      static class FinancialTransaction {

         final EventKey iEvent;
         final InventoryKey iInventory;
         final PartNoKey iPartNo;
         final BigDecimal iQuantity;
         final RefXactionTypeKey iXactionType;
         final InvCndChgEventKey iInvCndChgEvent;


         FinancialTransaction(RefXactionTypeKey aXactionType, InventoryKey aInventory,
               PartNoKey aPartNo, EventKey aEvent, InvCndChgEventKey aInvCndChgEvent,
               BigDecimal aQuantity) {
            iXactionType = aXactionType;
            iInventory = aInventory;
            iPartNo = aPartNo;
            iEvent = aEvent;
            iQuantity = aQuantity;
            iInvCndChgEvent = aInvCndChgEvent;
         }
      }

      /**
       * Stub Transaction Line
       */
      static class TransactionLine {

         final FncAccountKey iAccount;
         final BigDecimal iAmount;
         final boolean iDebit;


         TransactionLine(FncAccountKey aAccount, BigDecimal aAmount, boolean aDebit) {
            iAccount = aAccount;
            iAmount = aAmount;
            iDebit = aDebit;
         }
      }
   }
}
