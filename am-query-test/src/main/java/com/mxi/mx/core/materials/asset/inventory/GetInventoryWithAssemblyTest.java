package com.mxi.mx.core.materials.asset.inventory;

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
import com.mxi.mx.core.materials.asset.inventory.model.InventoryId;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInventoryWithAssemblyTest {

   // Test Data
   private final String INVALID_INVENTORY_ID = "40D0C76490724C7688A33C51EE9F3232";
   private final String INVENTORY_ID1 = "90D0C76490724C7688A33C51EE9F3298";
   private final String INVENTORY_ID2 = "7801631AD6D645AAA5C52B3B92FD4121";
   private final String INVENTORY_ID3 = "A16B0521A0464679B493698B6DAC6F21";
   private final String INVENTORY_CLASS = "ACFT";
   private final String INVENTORY_SDESC = "Falcon 2000 - 701";
   private final String SERIAL_NO = "sn701";
   private final String MANUFACT_CD = "DASSAULT";
   private final String INVENTORY_COND = "REPREQ";
   private final String PART_NO = "MD-2000";
   private final String QTY_UNIT_CD = "EA";
   private final String ASSEMBLY_CD = "F-2000";
   private final String ORIGINAL_ASSEMBLY_CD = "F-2000";
   private final String ASSEMBLY_BARCODE = "L543210";
   private final String ASSEMBLY_APPLICABILITY_CODE = "A15";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetInventoryWithAssemblyTest.class,
            "GetInventoryWithAssemblyTest.xml" );
   }


   @Test
   public void testQueryWithSingleValidInventoryId() throws ParseException {

      QuerySet lQs = execute( INVENTORY_ID1 );
      Assert.assertEquals( lQs.getRowCount(), 1 );

      Assert.assertTrue( lQs.first() );

      InventoryId lInventoryId = new InventoryId( INVENTORY_ID1 );
      Assert.assertEquals( lInventoryId.toUuid(), lQs.getUuid( "alt_id" ) );
      Assert.assertEquals( INVENTORY_CLASS, lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( INVENTORY_SDESC, lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( INVENTORY_COND, lQs.getString( "inv_cond_cd" ) );
      Assert.assertEquals( MANUFACT_CD, lQs.getString( "manufact_cd" ) );
      Assert.assertEquals( SERIAL_NO, lQs.getString( "serial_no_oem" ) );
      Assert.assertEquals( PART_NO, lQs.getString( "part_no_oem" ) );
      Assert.assertEquals( QTY_UNIT_CD, lQs.getString( "qty_unit_cd" ) );
      Assert.assertEquals( ASSEMBLY_CD, lQs.getString( "assmbl_cd" ) );
      Assert.assertEquals( ORIGINAL_ASSEMBLY_CD, lQs.getString( "orig_assmbl_cd" ) );
      Assert.assertEquals( ASSEMBLY_BARCODE, lQs.getString( "assmbl_barcode" ) );
      Assert.assertEquals( ASSEMBLY_APPLICABILITY_CODE, lQs.getString( "assmbl_appl_cd" ) );

   }


   @Test
   public void testQueryWithMultipleValidInventoryIds() throws ParseException {

      QuerySet lQs = execute( INVENTORY_ID1, INVENTORY_ID2, INVENTORY_ID3 );

      Assert.assertEquals( lQs.getRowCount(), 3 );

   }


   @Test
   public void testQueryWithInvalidInventoryId() throws ParseException {
      QuerySet lQs = execute( INVALID_INVENTORY_ID );
      Assert.assertTrue( lQs.isEmpty() );
   }


   public QuerySet execute( String... aIds ) {
      DataSetArgument lArgs = new DataSetArgument();
      Collection<InventoryId> lIds = new ArrayList<InventoryId>();
      for ( String lId : aIds ) {
         lIds.add( new InventoryId( lId ) );
      }

      lArgs.addUniqueIdArray( "aAltIdArray", lIds );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.inventory.dao.query.GetInventoryWithAssembly", lArgs );
   }

}
