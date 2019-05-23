
package com.mxi.mx.core.materials.asset.installedpart;

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
public class GetInstalledPartByFieldsTest {

   // Query Arguments
   private final String TASK_ID = "ABCDEF1234567890ABCDEF1234567891";
   private final String PART_GROUP_ID = "ABCDEF1234567890ABCDEF1234567890";
   private final String POSITION_ID = "1";
   private final String INVALID_TASK_ID = "BCDEF1234567890ABCDEF1234567890A";

   // Expected Results
   private String PART_ID = "1234567890ABCDEF1234567890ABCDEF";
   private String PART_NAME = "TEST PART";
   private String PART_NO = "1234-ABCD";
   private String SERIAL_NUMBER = "XYZ";
   private Integer INTERCHANGEABILITY_ORDER = 2;
   private String INVENTORY_ID = "234567890ABCDEF1234567890ABCDEF1";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetInstalledPartByFieldsTest.class,
            "GetInstalledPartByFieldsTest.xml" );
   }


   @Test
   public void testGetInstalledPartByFields() {
      // Execute the query
      QuerySet lQs = execute( TASK_ID, PART_GROUP_ID, POSITION_ID );

      // Wrap the ID strings in Id objects
      PartId lPartId = new PartId( PART_ID );
      InventoryId lInventoryId = new InventoryId( INVENTORY_ID );

      // There should be a result
      Assert.assertTrue( lQs.first() );

      // There should not be more than 1
      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the fields are populated correctly
      Assert.assertEquals( lPartId.toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( PART_NAME, lQs.getString( "part_name" ) );
      Assert.assertEquals( PART_NO, lQs.getString( "part_no" ) );
      Assert.assertEquals( SERIAL_NUMBER, lQs.getString( "serial_number" ) );
      Assert.assertEquals( INTERCHANGEABILITY_ORDER, lQs.getInteger( "part_interchg_ord" ) );
      Assert.assertEquals( lInventoryId.toUuid(), lQs.getUuid( "inv_id" ) );
   }


   @Test
   public void testInvalidInstalledPartByFields() {
      // Execute the query
      QuerySet lQs = execute( INVALID_TASK_ID, PART_GROUP_ID, POSITION_ID );

      // There result set should be empty
      Assert.assertTrue( lQs.isEmpty() );
   }


   private QuerySet execute( String aTaskId, String aPartGroupId, String aPositionId ) {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskId", aTaskId );
      lArgs.add( "aPartGroupId", aPartGroupId );
      lArgs.add( "aPositionId", aPositionId );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.installedpart.dao.query.GetInstalledPartByFields",
            lArgs );
   }

}
