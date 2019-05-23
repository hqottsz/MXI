package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This test class tests the CondemnedInventory.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CondemnedInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String NOTE = "User note";

   private HumanResourceKey iHumanResource;
   private LocationKey iSupplyLocation;
   private OwnerKey iLocalOwner;


   @Before
   public void setUp() {

      iSupplyLocation = Domain.createLocation( aSupplyLocation -> {
         aSupplyLocation.setCode( "YYZ" );
         aSupplyLocation.setIsSupplyLocation( true );
      } );

      iLocalOwner = Domain.createOwner();

      iHumanResource = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setSupplyLocations( iSupplyLocation );
      } );

   }


   @Test
   public void testCondemnedInventoryRetrieved() throws MxException, TriggerException {
      InventoryKey lInventory = createInventoryWithCondition( RefInvCondKey.CONDEMN );

      DataSet lDs = executeQuery();
      assertExist( lDs );
      lDs.next();

      InventoryKey lInventoryKey = new InventoryKey( lDs.getString( "inv_key" ) );
      assertEquals( "Unfortunately, Wrong inventory is fetched.", lInventory, lInventoryKey );

      InvInvTable lInv = InvInvTable.findByPrimaryKey( lInventoryKey );
      assertEquals( "Unfortunately, Inventory is not condemned.", RefInvCondKey.CONDEMN,
            lInv.getInvCondCd() );

   }


   /**
    * Create an unserviceable inventory with a given condition.
    *
    * @param aInventoryCondition
    * @return the new inventory key
    */
   private InventoryKey createInventoryWithCondition( RefInvCondKey aInventoryCondition ) {
      return new InventoryBuilder().atLocation( iSupplyLocation )
            .withCondition( aInventoryCondition ).withOwner( iLocalOwner ).build();
   }


   private DataSet executeQuery() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iHumanResource, new String[] { "aHrDbId", "aHrId" } );
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }


   private void assertExist( DataSet aDs ) {
      if ( !aDs.hasNext() ) {
         Assert.fail( "There are no condemned inventories." );
      }
      assertEquals( 1, aDs.getTotalRowCount() );
   }

}
