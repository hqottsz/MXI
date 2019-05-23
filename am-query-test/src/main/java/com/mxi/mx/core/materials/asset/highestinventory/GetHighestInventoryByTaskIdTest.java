
package com.mxi.mx.core.materials.asset.highestinventory;

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
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


/**
 * @author tdomitrovits
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHighestInventoryByTaskIdTest {

   // Query Arguments
   private final String TASK_ID = "1234567890ABCDEF1234567890ABCDEF";

   // Expected Results
   private final String HIGHEST_INV_ID = "ABCDEF1234567890ABCDEF1234567890";
   private final Integer ASSMBL_DB_ID = 4650;
   private final String ASSMBL_CD = "B767-200";
   private final Integer ASSMBL_BOM_ID = 123;
   private final Integer ASSMBL_POS_ID = 1;

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetHighestInventoryByTaskIdTest.class,
            "GetHighestInventoryByTaskIdTest.xml" );
   }


   @Test
   public void willFindHighestInventoryFromTaskId() {
      /**
       * Arrange
       */
      InventoryId lHighestInvId = new InventoryId( HIGHEST_INV_ID );

      /**
       * Act
       */
      QuerySet aQs = this.execute( TASK_ID );

      /**
       * Assert
       */
      // Ensure there is a result
      Assert.assertTrue( aQs.first() );

      // Ensure there is exactly one result
      Assert.assertEquals( 1, aQs.getRowCount() );

      // Ensure the correct ID is returned from the query
      Assert.assertEquals( lHighestInvId.toUuid(), aQs.getUuid( "id" ) );
      Assert.assertEquals( ASSMBL_DB_ID, aQs.getInteger( "assmbl_db_id" ) );
      Assert.assertEquals( ASSMBL_CD, aQs.getString( "assmbl_cd" ) );
      Assert.assertEquals( ASSMBL_BOM_ID, aQs.getInteger( "assmbl_bom_id" ) );
      Assert.assertEquals( ASSMBL_POS_ID, aQs.getInteger( "assmbl_pos_id" ) );
   }


   private QuerySet execute( String aTaskId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTaskId", aTaskId );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.highestinventory.dao.query.GetHighestInventoryByTaskId",
            lArgs );
   }

}
