package com.mxi.mx.core.services.inventory.config;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;


/**
 * Test the DefaultChangePartNoService class
 */
@RunWith( Theories.class )
public final class DefaultChangePartNoServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartNoKey iAltPart;

   private FncAccountKey iAssetAccount;

   private DefaultChangePartNoService iDefaultChangePartNoService =
         new DefaultChangePartNoService();

   private HumanResourceKey iHr;

   private RefInvCondKey iInvConditionKey;

   private InventoryKey iInventory;

   private OwnerKey iOwner;

   private PartNoKey iPart;

   private PartGroupKey iPartGroup;

   private EqpPartNoTable iTable;

   @DataPoints
   public static final RefFinanceTypeKey[] FINANCIAL_TYPES = new RefFinanceTypeKey[] {
         RefFinanceTypeKey.CONSUM, RefFinanceTypeKey.EXPENSE, RefFinanceTypeKey.ROTABLE };

   @DataPoints
   public static final RefInvClassKey[] INV_CLASSES = new RefInvClassKey[] { RefInvClassKey.ASSY,
         RefInvClassKey.SER, RefInvClassKey.TRK, RefInvClassKey.BATCH };


   /**
    * Test the setPartNoEvt() method. The inventory's condition is IN SERVICE, as result the total
    * spares quantities for the involved part numbers will not change.
    *
    * @throws MxException
    *            If an error occurs.
    */
   @Theory
   public void testSetPartNoAndInventoryIsInService( RefFinanceTypeKey aFinanceTypeKey,
         RefInvClassKey aInvClassKey ) throws MxException {

      setUpParts( aFinanceTypeKey, aInvClassKey );

      // pre-check total spares
      // for part A
      iTable = EqpPartNoTable.findByPrimaryKey( iPart );
      assertEquals( iTable.getTotalQt(), new BigDecimal( 1 ) );

      // for part B
      iTable = EqpPartNoTable.findByPrimaryKey( iAltPart );
      assertEquals( iTable.getTotalQt(), BigDecimal.ZERO );

      // the inventory is IN SERVICE condition
      iInvConditionKey = RefInvCondKey.INSRV;
      iInventory = new InventoryBuilder().withCondition( iInvConditionKey ).withPartNo( iPart )
            .withOwner( iOwner ).build();

      // edit the inventory to set the part no with the alternat part from the part group
      iDefaultChangePartNoService.setPartNoEvt( iInventory, iAltPart, false, iHr, true, null );

      // post-check total spares, have not changed
      // for part A
      iTable = EqpPartNoTable.findByPrimaryKey( iPart );
      assertEquals( iTable.getTotalQt(), new BigDecimal( 1 ) );

      // for part B
      iTable = EqpPartNoTable.findByPrimaryKey( iAltPart );
      assertEquals( iTable.getTotalQt(), BigDecimal.ZERO );
   }


   /**
    * Test the setPartNoEvt() method. The inventory's condition is different than IN SERVICE, as
    * result the total spares quantities for the involved part numbers will be updated
    *
    * @throws MxException
    *            If an error occurs.
    */
   @Theory
   public void testSetPartNoAndInventoryIsNotInService( RefFinanceTypeKey aFinanceTypeKey,
         RefInvClassKey aInvClassKey ) throws MxException {

      setUpParts( aFinanceTypeKey, aInvClassKey );

      // pre-checks total spares
      // for part A
      iTable = EqpPartNoTable.findByPrimaryKey( iPart );
      assertEquals( iTable.getTotalQt(), new BigDecimal( 1 ) );

      // for part B
      iTable = EqpPartNoTable.findByPrimaryKey( iAltPart );
      assertEquals( iTable.getTotalQt(), BigDecimal.ZERO );

      // the inventory is not IN SERVICE condition
      iInvConditionKey = RefInvCondKey.RFI;

      iInventory = new InventoryBuilder().withCondition( iInvConditionKey ).withPartNo( iPart )
            .withOwner( iOwner ).withBinQt( 1 ).build();

      // edit the inventory to set the part no with the alternate part from the part group
      iDefaultChangePartNoService.setPartNoEvt( iInventory, iAltPart, false, iHr, true, null );

      // post-check total spares, have changed
      // for part A, decreased by one
      iTable = EqpPartNoTable.findByPrimaryKey( iPart );
      assertEquals( iTable.getTotalQt(), BigDecimal.ZERO );

      // for part B, increased by one
      iTable = EqpPartNoTable.findByPrimaryKey( iAltPart );
      assertEquals( iTable.getTotalQt(), new BigDecimal( 1 ) );
   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void setUp() {
      // create a part group
      iPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).build();

      // create HR
      iHr = new HumanResourceDomainBuilder().build();

      // create inventory owner
      iOwner = new OwnerDomainBuilder().build();
   }


   public void setUpParts( RefFinanceTypeKey aFinancialTypeKey, RefInvClassKey aInvClassKey ) {
      // create a part
      iPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "STD_PART" )
            .withShortDescription( "Part A" ).withAssetAccount( iAssetAccount )
            .withTotalValue( new BigDecimal( 90 ) ).withInventoryClass( aInvClassKey )
            .withFinancialType( aFinancialTypeKey ).withStatus( RefPartStatusKey.ACTV )

            // total spares quantity
            .withTotalQuantity( new BigDecimal( 1 ) )
            .withAverageUnitPrice( new BigDecimal( 125.50 ) ).isAlternateIn( iPartGroup ).build();

      // create a part that is an alternate to the first part
      iAltPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "ALT_PART" )
            .withShortDescription( "Part B" ).withAssetAccount( iAssetAccount )
            .withTotalValue( new BigDecimal( 100 ) ).withInventoryClass( aInvClassKey )
            .withFinancialType( aFinancialTypeKey ).withStatus( RefPartStatusKey.ACTV )

            // total spares quantity
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 74.20 ) ).isAlternateIn( iPartGroup ).build();
   }
}
