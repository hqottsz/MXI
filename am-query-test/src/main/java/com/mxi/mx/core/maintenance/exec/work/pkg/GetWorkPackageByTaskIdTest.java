
package com.mxi.mx.core.maintenance.exec.work.pkg;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.mxi.mx.core.erp.loc.model.LocationId;
import com.mxi.mx.core.maintenance.exec.work.pkg.model.WorkPackageId;
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkPackageByTaskIdTest {

   // Test Data
   private final String TASK_ID = "CB1A12F6346A43B284D59CE7260840A9";
   private final String WORKPACKAGE_ID = "94AABE1460404ED598E4422CD45FF371";
   private final String LEGACY_KEY = "4650:131745";
   private final String BARCODE = "T000171Z";
   private final String WORK_ORDER_NUMBER = "WO - 131745";
   private final String WORK_PACKAGE_NAME = "MX-6138 Work Package";
   private final String WORK_PACKAGE_DESC = "Description of Workpackage for testing";
   private final String STATUS = "IN WORK";
   private final String INVENTORY_ID = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private final String LOCATION_ID = "D809C115702545A5B0FA1E8330582F75";
   private final String START_DATE = "2016-01-08 08:43:00";
   private final String END_DATE = "2016-01-09 08:43:00";
   private final String CLASS_CODE = "CHECK";
   private final String DUE_DATE = "2016-01-09 08:43:00";
   private final String STATUS_DESC = "Task In Work";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetWorkPackageByTaskIdTest.class,
            "GetWorkPackageByTaskIdTest.xml" );
   }


   @Test
   public void testQuery() throws ParseException {
      QuerySet lQs = execute( TASK_ID );

      Assert.assertTrue( lQs.first() );

      WorkPackageId lWorkPackageId = new WorkPackageId( WORKPACKAGE_ID );
      Assert.assertEquals( lWorkPackageId.toUuid(), lQs.getUuid( "id" ) );

      Assert.assertEquals( LEGACY_KEY, lQs.getString( "legacy_key" ) );
      Assert.assertEquals( BARCODE, lQs.getString( "barcode" ) );
      Assert.assertEquals( WORK_ORDER_NUMBER, lQs.getString( "wo_number" ) );
      Assert.assertEquals( WORK_PACKAGE_DESC, lQs.getString( "workpackage_description" ) );
      Assert.assertEquals( WORK_PACKAGE_NAME, lQs.getString( "name" ) );
      Assert.assertEquals( STATUS, lQs.getString( "status_cd" ) );
      Assert.assertEquals( STATUS_DESC, lQs.getString( "status_description" ) );
      Assert.assertEquals( CLASS_CODE, lQs.getString( "class_code" ) );
      Assert.assertNull( lQs.getString( "subclass_code" ) );
      Assert.assertFalse( lQs.getBoolean( "heavy_maintenance_bool" ) );

      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Assert.assertEquals( lDateFormat.parse( START_DATE ), lQs.getDate( "start_dt" ) );
      Assert.assertEquals( lDateFormat.parse( END_DATE ), lQs.getDate( "end_dt" ) );
      Assert.assertNull( lQs.getDate( "due_dt" ) );

      InventoryId lInventoryId = new InventoryId( INVENTORY_ID );
      Assert.assertEquals( lInventoryId.toUuid(), lQs.getUuid( "inv_id" ) );

      LocationId lLocationId = new LocationId( LOCATION_ID );
      Assert.assertEquals( lLocationId.toUuid(), lQs.getUuid( "loc_id" ) );

      Assert.assertNull( lQs.getUuid( "vendor_id" ) );

   }


   public QuerySet execute( String aTaskId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTaskId", aTaskId );

      return iQao.executeQuery(
            "com.mxi.mx.core.maintenance.exec.work.pkg.dao.query.GetWorkPackageByTaskId", lArgs );
   }
}
