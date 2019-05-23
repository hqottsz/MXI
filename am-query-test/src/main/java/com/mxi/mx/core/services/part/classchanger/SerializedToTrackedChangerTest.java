
package com.mxi.mx.core.services.part.classchanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.part.classchange.SerializedToTrackedChanger;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;


/**
 * Ensures {@link SerializedToTrackedChanger} works properly
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SerializedToTrackedChangerTest {

   private static final HumanResourceKey HR = new HumanResourceKey( 4650, 1 );
   private static final PartGroupKey PART_GROUP = new PartGroupKey( 4650, 1 );

   private static final PartNoKey PART_NO = new PartNoKey( 4650, 1 );
   private static final InventoryKey INV = new InventoryKey( 4650, 1 );

   private SerializedToTrackedChanger iChanger;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "BULK_RECORD_INDUCTION_MODE",
            true );

      iChanger = new SerializedToTrackedChanger( PART_NO, PART_GROUP, HR );
   }


   @After
   public void tearDown() {
      MxCoreLicense.setValidator( null );
   }


   /**
    * Ensures that we can change an locked/archived inventory's part
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testChangeOnLockedInv() throws Exception {

      // archive and lock the inventory
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( INV );
      lInventoryTable.setLockedBool( true );
      lInventoryTable.setInvCond( RefInvCondKey.ARCHIVE );
      lInventoryTable.update();

      iChanger.change();

      // refresh the data
      lInventoryTable = InvInvTable.findByPrimaryKey( INV );

      assertEquals( RefInvClassKey.TRK, lInventoryTable.getInvClass() );
      assertTrue( lInventoryTable.isLocked() );
   }


   /**
    * Ensure that the part's class is changed
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testInventoryClassChangedOnInventory() throws Exception {
      iChanger.change();

      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( INV );

      assertEquals( RefInvClassKey.TRK, lInventoryTable.getInvClass() );
   }


   /**
    * Ensure that the inventory's class is changed
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testInventoryClassChangedOnPart() throws Exception {
      iChanger.change();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( PART_NO );

      assertEquals( RefInvClassKey.TRK, lPartNoTable.getInvClass() );
   }


   /**
    * Ensure that the inventory has been flagged for sync
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testSyncRequestedOnInv() throws Exception {

      // Precondition: assert the queue is empty
      QuerySet lSet = QuerySetFactory.getInstance().executeQueryTable( "INV_SYNC_QUEUE", null );
      assertTrue( "No inventory expected in inv_sync_queue", lSet.getRowCount() == 0 );

      iChanger.change();

      // Assert the queue is not empty
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_no_db_id", INV.getDbId() );
      lArgs.add( "inv_no_id", INV.getId() );
      lSet = QuerySetFactory.getInstance().executeQueryTable( "INV_SYNC_QUEUE", lArgs );
      assertTrue( "Inventory expected in inv_sync_queue", lSet.getRowCount() > 0 );
   }


   /**
    * Ensure that the inventory's usage is updated
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testUsageUpdatedOnInv() throws Exception {
      iChanger.change();

      InvCurrUsage.assertExist( INV, DataTypeKey.HOURS );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
