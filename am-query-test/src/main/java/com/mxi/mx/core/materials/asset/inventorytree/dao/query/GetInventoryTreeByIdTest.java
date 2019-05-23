
package com.mxi.mx.core.materials.asset.inventorytree.dao.query;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


/**
 * @author ascullion
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInventoryTreeByIdTest {

   private final int COLUMN_COUNT = 7;

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sDatabaseConnectionRule.getConnection(), GetInventoryTreeByIdTest.class,
            "GetInventoryTreeByIdTest.sql" );
   }


   @Test
   public void getFullInventoryTreeFromId() {

      DataSetArgument lArgs = new DataSetArgument();

      // Prepare input Arguments
      lArgs.add( "aId", "00000000000000000000000000000101" );

      String[] lClassCodes = { " " };
      lArgs.addWhereNotIn( "WHERE_IN_CLAUSE", "inv_inv.inv_class_cd", lClassCodes );

      // Execute Query
      QuerySet lQs = iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.inventorytree.dao.query.GetInventoryTreeById", lArgs );

      // Check dimensions of returned result
      Assert.assertEquals( "RowCount", 4, lQs.getRowCount() );
      Assert.assertEquals( "ColumnCount", COLUMN_COUNT, lQs.getColumnNames().length );

      // Check returned result
      lQs.next();

      Assert.assertEquals( "id", new InventoryId( "101" ).toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( "inv_no_sdesc", "Top1", lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", "ACFT", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "assmbl_bom_cd", "Aircraft", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "eqp_pos_cd", "501", lQs.getString( "eqp_pos_cd" ) );
      Assert.assertEquals( "parent_assmbl_pos", "501", lQs.getString( "parent_assmbl_pos" ) );
      Assert.assertEquals( "level", "1", lQs.getString( "level" ) );

      lQs.next();

      Assert.assertEquals( "id", new InventoryId( "102" ).toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( "inv_no_sdesc", "Upper1", lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", "SYS", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "assmbl_bom_cd", "10-00-00", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "eqp_pos_cd", "502", lQs.getString( "eqp_pos_cd" ) );
      Assert.assertEquals( "parent_assmbl_pos", "501", lQs.getString( "parent_assmbl_pos" ) );
      Assert.assertEquals( "level", "2", lQs.getString( "level" ) );

      lQs.next();

      Assert.assertEquals( "id", new InventoryId( "103" ).toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( "inv_no_sdesc", "Lower1", lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", "ASSY", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "assmbl_bom_cd", "10-10-00", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "eqp_pos_cd", "503", lQs.getString( "eqp_pos_cd" ) );
      Assert.assertEquals( "parent_assmbl_pos", "501", lQs.getString( "parent_assmbl_pos" ) );
      Assert.assertEquals( "level", "3", lQs.getString( "level" ) );

      lQs.next();

      Assert.assertEquals( "id", new InventoryId( "104" ).toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( "inv_no_sdesc", "Bottom1", lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", "TRK", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "assmbl_bom_cd", "10-10-10", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "eqp_pos_cd", "504", lQs.getString( "eqp_pos_cd" ) );
      Assert.assertEquals( "parent_assmbl_pos", "503", lQs.getString( "parent_assmbl_pos" ) );
      Assert.assertEquals( "level", "4", lQs.getString( "level" ) );

   }


   @Test
   public void getPartialInventoryTreeFromId() {

      DataSetArgument lArgs = new DataSetArgument();

      // Prepare input Arguments
      lArgs.add( "aId", "00000000000000000000000000000102" );

      String[] lClassCodes = { "TRK" };
      lArgs.addWhereIn( "WHERE_IN_CLAUSE", "inv_inv.inv_class_cd", lClassCodes );

      // Execute Query
      QuerySet lQs = iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.inventorytree.dao.query.GetInventoryTreeById", lArgs );

      // Check dimensions of returned result
      Assert.assertEquals( "RowCount", 1, lQs.getRowCount() );
      Assert.assertEquals( "ColumnCount", COLUMN_COUNT, lQs.getColumnNames().length );

      // Check returned result
      lQs.next();

      Assert.assertEquals( "id", new InventoryId( "104" ).toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( "inv_no_sdesc", "Bottom1", lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", "TRK", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "assmbl_bom_cd", "10-10-10", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "eqp_pos_cd", "504", lQs.getString( "eqp_pos_cd" ) );
      Assert.assertEquals( "parent_assmbl_pos", "503", lQs.getString( "parent_assmbl_pos" ) );
      Assert.assertEquals( "level", "3", lQs.getString( "level" ) );

   }


   @Test
   public void getEmptyInventoryTreeFromId() {

      DataSetArgument lArgs = new DataSetArgument();

      // Prepare input Arguments
      lArgs.add( "aId", "00000000000000000000000000000101" );

      String[] lClassCodes = { " " };
      lArgs.addWhereIn( "WHERE_IN_CLAUSE", "inv_inv.inv_class_cd", lClassCodes );

      // Execute Query
      QuerySet lQs = iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.inventorytree.dao.query.GetInventoryTreeById", lArgs );

      // Check dimensions of returned result
      Assert.assertEquals( "RowCount", 0, lQs.getRowCount() );

   }

}
