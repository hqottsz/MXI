package com.mxi.mx.core.materials.asset.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.materials.asset.model.AssetEntity;
import com.mxi.mx.core.materials.asset.model.AssetId;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.testing.matchers.MxMatchers;


/**
 * This class tests the {@link JdbcAssetDao} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcAssetDaoTest {

   private JdbcAssetDao iAssetDao;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the DAO is able to find an asset by its id.
    */
   @Test
   public void testFindById() {
      Date lManufactureDate = new Date();
      Date lReceiptDate = new Date();

      InventoryKey lRootInventoryKey = new InventoryBuilder().build();
      AssetId lRootId = new AssetId( new InvInv( lRootInventoryKey ).getAlternateKey() );

      PartNoKey lPartNo = new PartNoBuilder().withOemPartNo( "P123" )
            .manufacturedBy( new ManufacturerKey( 0, "MXI" ) ).build();

      InventoryKey lInventoryKey = new InventoryBuilder().withParentInventory( lRootInventoryKey )
            .withDescription( "asset description" ).withClass( RefInvClassKey.TRK )
            .withPartNo( lPartNo ).withSerialNo( "SN456" ).manufacturedOn( lManufactureDate )
            .receivedOn( lReceiptDate ).build();

      AssetId lId = new AssetId( new InvInv( lInventoryKey ).getAlternateKey() );
      AssetEntity lAsset = iAssetDao.find( lId ).now();

      assertEquals( "Description", "asset description", lAsset.getDescription() );
      assertEquals( "Asset Class Code", "TRK", lAsset.getInventoryClassCode() );
      assertEquals( "MXI", lAsset.getManufacturerCode() );
      assertEquals( "P123", lAsset.getPartNumber() );
      assertEquals( "SN456", lAsset.getSerialNumber() );
      assertEquals( "Primary Key", lInventoryKey, lAsset.getLegacyKey() );
      MxAssert.assertEquals( "Manufactured Date", lManufactureDate, lAsset.getManufacturedDate() );
      MxAssert.assertEquals( "Receipt Date", lReceiptDate, lAsset.getReceivedDate() );
      assertEquals( lRootId, lAsset.getRootInventory() );
      assertEquals( lRootInventoryKey, lAsset.getRootLegacyKey() );
   }


   /**
    * Tests that the DAO is able to find a callection of assets by id.
    */
   @Test
   public void testFindByIdCollection() {
      InventoryKey lInventory1 = new InventoryBuilder().build();
      AssetId lId1 = new AssetId( new InvInv( lInventory1 ).getAlternateKey() );

      InventoryKey lInventory2 = new InventoryBuilder().build();
      AssetId lId2 = new AssetId( new InvInv( lInventory2 ).getAlternateKey() );

      List<AssetId> lIds = new ArrayList<AssetId>( 2 );
      lIds.add( lId1 );
      lIds.add( lId2 );

      Collection<AssetEntity> lAssets = iAssetDao.find( lIds ).now();

      assertEquals( "Number of Assets", 2, lAssets.size() );

      Collection<InventoryKey> lKeys = new ArrayList<InventoryKey>( 2 );
      for ( AssetEntity lAsset : lAssets ) {
         lKeys.add( lAsset.getLegacyKey() );
      }

      MxMatchers.checkThat( lInventory1, MxMatchers.isIn( lKeys ) );
      MxMatchers.checkThat( lInventory2, MxMatchers.isIn( lKeys ) );
   }


   /**
    * Tests that the DAO is able to find a callection of assets by id, even when they have no part
    * number (e.g. a SYS).
    */
   @Test
   public void testFindByIdCollectionWhenAssetsHaveNoPartNumber() {
      InventoryKey lInventory1 = new InventoryBuilder().withPartNo( ( PartNoKey ) null ).build();
      AssetId lId1 = new AssetId( new InvInv( lInventory1 ).getAlternateKey() );

      InventoryKey lInventory2 = new InventoryBuilder().withPartNo( ( PartNoKey ) null ).build();
      AssetId lId2 = new AssetId( new InvInv( lInventory2 ).getAlternateKey() );

      List<AssetId> lIds = new ArrayList<AssetId>( 2 );
      lIds.add( lId1 );
      lIds.add( lId2 );

      Collection<AssetEntity> lAssets = iAssetDao.find( lIds ).now();

      assertEquals( "Number of Assets", 2, lAssets.size() );

      Collection<InventoryKey> lKeys = new ArrayList<InventoryKey>( 2 );
      for ( AssetEntity lAsset : lAssets ) {
         lKeys.add( lAsset.getLegacyKey() );
      }

      MxMatchers.checkThat( lInventory1, MxMatchers.isIn( lKeys ) );
      MxMatchers.checkThat( lInventory2, MxMatchers.isIn( lKeys ) );
   }


   /**
    * Tests that the DAO is able to find an asset by its id, even when that asset has no part number
    * (e.g. a SYS).
    */
   @Test
   public void testFindByIdWhenAssetHasNoPartNumber() {
      Date lManufactureDate = new Date();
      Date lReceiptDate = new Date();

      InventoryKey lRootInventoryKey = new InventoryBuilder().build();
      AssetId lRootId = new AssetId( new InvInv( lRootInventoryKey ).getAlternateKey() );

      // Inventory with no part number.
      InventoryKey lInventoryKey = new InventoryBuilder().withParentInventory( lRootInventoryKey )
            .withDescription( "asset description" ).withClass( RefInvClassKey.SYS )
            .withPartNo( ( PartNoKey ) null ).withSerialNo( "SN456" )
            .manufacturedOn( lManufactureDate ).receivedOn( lReceiptDate ).build();

      AssetId lId = new AssetId( new InvInv( lInventoryKey ).getAlternateKey() );
      AssetEntity lAsset = iAssetDao.find( lId ).now();

      assertEquals( "Description", "asset description", lAsset.getDescription() );
      assertEquals( "Asset Class Code", "SYS", lAsset.getInventoryClassCode() );
      assertEquals( "SN456", lAsset.getSerialNumber() );
      assertEquals( "Primary Key", lInventoryKey, lAsset.getLegacyKey() );
      MxAssert.assertEquals( "Manufactured Date", lManufactureDate, lAsset.getManufacturedDate() );
      MxAssert.assertEquals( "Receipt Date", lReceiptDate, lAsset.getReceivedDate() );
      assertEquals( lRootId, lAsset.getRootInventory() );
      assertEquals( lRootInventoryKey, lAsset.getRootLegacyKey() );

      assertNull( lAsset.getPartNumber() );
      assertNull( lAsset.getManufacturerCode() );
   }


   /**
    * Sets up the test case. Overrides logic parameters with a stub. Replaces singletons with stubs.
    * Creates the object under test.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {
      iAssetDao = new JdbcAssetDao( QuerySetFactory.getInstance() );
   }
}
