package com.mxi.mx.core.services.part;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.services.fnc.InvalidRepairOrderAccountException;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;


/**
 * This class tests PartNoService.editFinancials().
 *
 * @author ydai
 * @created September 8, 2016
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class EditFinancialInformationTest {

   private FncAccountKey iRepairOrderAccount;
   private FncAccountKey iNewRepairOrderAccount;
   private FncAccountKey iAssetAccount;
   private static final String ASSET_ACCOUNT = "ASSET_ACCOUNT";
   private static final String REPAIR_ORDER_ACCOUNT = "REPAIR_ORDER_ACCOUNT";
   private static final String NEW_REPAIR_ORDER_ACCOUNT = "NEW_REPAIR_ACCOUNT";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * To check if a default repair order account can be added when editing the part financials
    *
    * @throws Exception
    */
   @Test
   public void testAddDefaultRepairOrderAccount() throws Exception {

      // Create a part without any default repair order account
      PartNoKey lPartNo = createPart().build();

      // Run the logic to be tested: add a default repair order account for this part

      PartNoService.editFinancials( lPartNo, getPartFinancialsTO( REPAIR_ORDER_ACCOUNT ), null );

      // Assert the result
      assertEquals( EqpPartNoTable.findByPrimaryKey( lPartNo ).getRepairOrderAccount(),
            iRepairOrderAccount );

   }


   /**
    * To check if the default repair order account can be changed editing the part financials
    *
    * @throws Exception
    */
   @Test
   public void testChangeDefaultRepairOrderAccount() throws Exception {

      // Create a part with a default repair order account
      PartNoKey lPartNo = createPart().withRepairOrderAccount( iRepairOrderAccount ).build();

      // Run the logic to be tested: change to a new repair order account

      PartNoService.editFinancials( lPartNo, getPartFinancialsTO( NEW_REPAIR_ORDER_ACCOUNT ),
            null );

      // Assert the result
      assertEquals( EqpPartNoTable.findByPrimaryKey( lPartNo ).getRepairOrderAccount(),
            iNewRepairOrderAccount );

   }


   /**
    * To check only EXPENSE account can be the default repair order account when editing the part
    * financials
    *
    * @throws Exception
    */
   @Test
   public void testOnlyExpenseTypeAccountCanBeDefaultRepairOrderAccount() throws Exception {

      try {
         // Create a part without any default repair order account
         PartNoKey lPartNo = createPart().build();

         // Run the logic to be tested: try to add a not EXPENSE account as the repair order account

         PartNoService.editFinancials( lPartNo, getPartFinancialsTO( ASSET_ACCOUNT ), null );

      } catch ( Exception aException ) {

         // Assert the result: there is an exception to reject this type of repair order account
         Assert.assertTrue( aException instanceof InvalidRepairOrderAccountException );
         return;
      }

      Assert.fail();

   }


   /**
    * Create a part
    *
    * @return
    * @throws MxException
    */
   private PartNoBuilder createPart() throws MxException {

      return new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withAssetAccount( iAssetAccount ).withAverageUnitPrice( BigDecimal.TEN );

   }


   /**
    * Part Financials Data setup
    *
    * @param aRepairOrderAccount
    *           Repair Order Account
    *
    * @throws MxException
    */
   private PartFinancialsTO getPartFinancialsTO( String aRepairOrderAccount ) throws MxException {

      PartFinancialsTO lTO = new PartFinancialsTO();
      lTO.setAssetAccount( ASSET_ACCOUNT );
      lTO.setRepairOrderAccount( aRepairOrderAccount );
      lTO.setFinancialClass( RefFinanceTypeKey.CONSUM.getCd() );

      return lTO;

   }


   @Before
   public void createTestData() {

      // create an EXPENSE type account
      iRepairOrderAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( REPAIR_ORDER_ACCOUNT ).build();

      // create another EXPENSE type account
      iNewRepairOrderAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( NEW_REPAIR_ORDER_ACCOUNT ).build();

      // create an asset account
      iAssetAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( ASSET_ACCOUNT ).isDefault().build();
   }

}
