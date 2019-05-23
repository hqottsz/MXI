package com.mxi.mx.core.maintenance.exec.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

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
import com.mxi.mx.core.api.resource.model.RawId;
import com.mxi.mx.core.maintenance.plan.logbookfault.model.TaskId;
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTasksByFieldsTest {

   // Test Data
   private final String INVALID_TASK_ID = "ABB24AAF643E496A880A3E35907C3313";
   private final String TASK_ID1 = "CB1A12F6346A43B284D59CE7260840A9";
   private final String TASK_ID2 = "9801631AD6D645AAA6C52B3B92FD4122";
   private final String TASK_ID3 = "B16B0528A0464679B493998B6DAC6F51";
   private final String BARCODE = "T12345";
   private final String STATUS = "OPEN";
   private final String INVENTORY_ID = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private final String TASK_CLASS = "INSP";
   private final String TASK_SUBCLASS = "VISUAL";
   private final String PRIORITY = "HIGH";
   private final String TASK_1_LABOUR_ID = "6CFBA066DA9033E587B1FB2D7B6472DF";
   private final String INVALID_LABOUR_ID = "4CFBA012DA9033E587A1FB2D7B6472DB";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetTasksByFieldsTest.class,
            "GetTasksByFieldsTest.xml" );
   }


   @Test
   public void testQueryWithSingleValidTaskId() throws ParseException {

      Collection<String> lIds = new ArrayList<String>();
      lIds.add( TASK_ID1 );
      QuerySet lQs = execute( lIds, null );
      Assert.assertEquals( lQs.getRowCount(), 1 );

      Assert.assertTrue( lQs.first() );

      TaskId lTaskId = new TaskId( TASK_ID1 );
      Assert.assertEquals( lTaskId.toUuid(), lQs.getUuid( "id" ) );
      Assert.assertEquals( BARCODE, lQs.getString( "barcode" ) );
      Assert.assertEquals( PRIORITY, lQs.getString( "priority" ) );
      Assert.assertEquals( TASK_CLASS, lQs.getString( "task_class" ) );
      Assert.assertEquals( TASK_SUBCLASS, lQs.getString( "task_subclass" ) );
      Assert.assertEquals( STATUS, lQs.getString( "status" ) );

      InventoryId lInventoryId = new InventoryId( INVENTORY_ID );
      Assert.assertEquals( lInventoryId.toUuid(), lQs.getUuid( "inventory_id" ) );

   }


   @Test
   public void testQueryWithMultipleValidTaskIds() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      lIds.add( TASK_ID1 );
      lIds.add( TASK_ID2 );
      lIds.add( TASK_ID3 );
      QuerySet lQs = execute( lIds, null );

      Assert.assertEquals( lQs.getRowCount(), 3 );

   }


   @Test
   public void testQueryWithInvalidTaskId() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      lIds.add( INVALID_TASK_ID );
      QuerySet lQs = execute( lIds, null );
      Assert.assertTrue( lQs.isEmpty() );
   }


   @Test
   public void testQueryWithInvalidLabourId() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      QuerySet lQs = execute( lIds, INVALID_LABOUR_ID );
      Assert.assertTrue( lQs.isEmpty() );
   }


   @Test
   public void testQueryWithValidLabourId() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      QuerySet lQs = execute( lIds, TASK_1_LABOUR_ID );
      Assert.assertEquals( lQs.getRowCount(), 1 );
   }


   @Test
   public void testQueryWithValidLabourIdButMismatchedWithTaskId() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      lIds.add( TASK_ID2 );
      QuerySet lQs = execute( lIds, TASK_1_LABOUR_ID );
      Assert.assertTrue( lQs.isEmpty() );
   }


   @Test
   public void testQueryWithValidLabourIdAndMatchedWithTaskId() throws ParseException {
      Collection<String> lIds = new ArrayList<String>();
      lIds.add( TASK_ID1 );
      QuerySet lQs = execute( lIds, TASK_1_LABOUR_ID );
      Assert.assertEquals( lQs.getRowCount(), 1 );
   }


   public QuerySet execute( Collection<String> aTaskIds, String aLabourId ) {
      DataSetArgument lArgs = new DataSetArgument();

      boolean lTaskIdsNullOrEmpty = true;
      if ( aTaskIds != null && !aTaskIds.isEmpty() ) {
         lArgs.addUniqueIdArray( "aTaskIds",
               RawId.toRawIdList( new ArrayList<String>( aTaskIds ) ) );
         lTaskIdsNullOrEmpty = false;

      } else {
         lArgs.addUniqueIdArray( "aTaskIds", new ArrayList<RawId>() );
      }
      lArgs.add( "aTaskIdsNullOrEmpty", lTaskIdsNullOrEmpty );

      boolean isLabourIdGiven = false;
      if ( aLabourId != null ) {
         lArgs.add( "aLabourId", aLabourId );
         isLabourIdGiven = true;
      } else {
         String aNullLabourId = null;
         lArgs.add( "aLabourId", aNullLabourId );
      }
      lArgs.add( "aLabourIdGiven", isLabourIdGiven );

      return iQao.executeQuery( "com.mxi.mx.core.maintenance.exec.task.dao.query.GetTasksByFields",
            lArgs );
   }

}
