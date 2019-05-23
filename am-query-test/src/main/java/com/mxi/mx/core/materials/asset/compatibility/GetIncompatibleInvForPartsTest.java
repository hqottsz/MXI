
package com.mxi.mx.core.materials.asset.compatibility;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.maintenance.eng.config.part.model.PartId;
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetIncompatibleInvForPartsTest {

   // Query Arguments
   private final String INCOMPAT_PART_ID = "1234567890ABCDEF1234567890ABCDEF";
   private final String INCOMPAT_PART_ID2 = "1234567890ABCDEF1234567890ABCDF0";
   private final String COMPAT_PART_ID = "ABCDEF1234567890ABCDEF1234567890";
   private final String PART_GROUP_ID = "234567890ABCDEF1234567890ABCDEF1";
   private final String POSITION_ID = "1";
   private final String TASK_ID = "34567890ABCDEF1234567890ABCDEF12";

   // Expected Results
   private final String INCOMPAT_INV_ID = "4567890ABCDEF1234567890ABCDEF123";
   private final String INCOMPAT_INV_PART = "TEST PART";
   private final String INCOMPAT_INV_PART_NO = "123456-789";
   private final String INCOMPAT_INV_SERIAL = "12-345-6789";
   private final String INCOMPAT_INV_POSITION = "2";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetIncompatibleInvForPartsTest.class,
            "GetIncompatibleInvForPartsTest.xml" );
   }


   @Test
   public void testIncompatibleSinglePartForInventory() {
      // Create the Id objects
      PartId lPartId = new PartId( INCOMPAT_PART_ID );
      InventoryId lInventoryId = new InventoryId( INCOMPAT_INV_ID );

      // Create Part Id List
      Collection<PartId> lPartIdList = new HashSet<PartId>();
      lPartIdList.add( lPartId );

      // Execute the query
      QuerySet lQs = execute( lPartIdList, PART_GROUP_ID, POSITION_ID, TASK_ID );

      // Ensure there is a result
      Assert.assertTrue( lQs.first() );

      // Ensure there is only 1 record
      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the fields are correct
      Assert.assertEquals( lInventoryId.toUuid(), lQs.getUuid( "incompat_inv_id" ) );
      Assert.assertEquals( INCOMPAT_INV_PART, lQs.getString( "incompat_inv_part" ) );
      Assert.assertEquals( INCOMPAT_INV_PART_NO, lQs.getString( "incompat_inv_part_no" ) );
      Assert.assertEquals( INCOMPAT_INV_SERIAL, lQs.getString( "incompat_inv_serial" ) );
      Assert.assertEquals( INCOMPAT_INV_POSITION, lQs.getString( "incompat_inv_position" ) );
      Assert.assertEquals( lPartId.toUuid(), lQs.getUuid( "loose_part_id" ) );
   }


   @Test
   public void testIncompatiblePartsForInventory() {
      // Create the Id objects
      PartId lPartId1 = new PartId( INCOMPAT_PART_ID );
      PartId lPartId2 = new PartId( INCOMPAT_PART_ID2 );

      // Create Part Id List
      Collection<PartId> lPartIdList = new HashSet<PartId>();
      lPartIdList.add( lPartId1 );
      lPartIdList.add( lPartId2 );

      // Execute the query
      QuerySet lQs = execute( lPartIdList, PART_GROUP_ID, POSITION_ID, TASK_ID );

      // Ensure there is a result
      Assert.assertTrue( lQs.first() );

      // Ensure there are 2 records
      Assert.assertEquals( 2, lQs.getRowCount() );
   }


   @Test
   public void testCompatibleSinglePartForInventory() {
      // Create Part Id List
      Collection<PartId> lPartIdList = new HashSet<PartId>();
      lPartIdList.add( new PartId( COMPAT_PART_ID ) );

      // Execute the query
      QuerySet lQs = execute( lPartIdList, PART_GROUP_ID, POSITION_ID, TASK_ID );

      // Ensure there are no results
      Assert.assertTrue( lQs.isEmpty() );
   }


   private QuerySet execute( Collection<PartId> aPartIdList, String aPartGroupId,
         String aPositionId, String aTaskId ) {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addUniqueIdArray( "aPartAltIdArray", aPartIdList );
      lArgs.add( "aPartGroupId", aPartGroupId );
      lArgs.add( "aPositionId", aPositionId );
      lArgs.add( "aTaskId", aTaskId );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.compatibility.dao.query.GetIncompatibleInvForParts",
            lArgs );
   }

}
