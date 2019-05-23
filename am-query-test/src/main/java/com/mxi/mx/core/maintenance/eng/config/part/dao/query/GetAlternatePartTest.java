
package com.mxi.mx.core.maintenance.eng.config.part.dao.query;

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


/**
 * Query test for query file:
 * mxcoreejb.src.main.resources.com.mxi.mx.core.maintenance.eng.config.part
 * .dao.query.GetAlternatePart.qrx
 *
 * @author tdomitrovits
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetAlternatePartTest {

   // Query Arguments
   private final String PART_GROUP_WITH_SINGLE_PART_ID = "1234567890ABCDEF1234567890ABCDEF";
   private final String PART_GROUP_WITH_MULTIPLE_PARTS_ID = "1234567890ABCDEF1234567890ABCDF0";
   private final String PART_GROUP_WITH_NO_PARTS_ID = "1234567890ABCDEF1234567890ABCDF1";

   // Expected Results
   private final String PART_ID = "ABCDEF1234567890ABCDEF1234567890";
   private final String PART_KEY = "1234:5678";
   private final String BOM_PART_KEY = "1234:4321";
   private final String BOM_ITEM_POS_KEY = "4321:F-2000:123:1";
   private final String PART_NO_OEM = "12-345678-90";
   private final String PART_NO_SDESC = "Test Part";
   private final String CONDITIONS_LDESC = null;
   private final Boolean STANDARD_BOOL = true;
   private final String APPL_EFF_LDESC = null;
   private final String STATUS_CD = "ACTV";
   private final Integer INTERCHANGEABILITY_ORDER = 2;

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetAlternatePartTest.class,
            "GetAlternatePartTest.xml" );
   }


   @Test
   public void testSingleResult() {
      // Execute the query with the part group id
      QuerySet lQs = executeQuery( PART_GROUP_WITH_SINGLE_PART_ID );
      PartId lPartId = new PartId( PART_ID );

      // There should be a result
      Assert.assertTrue( lQs.next() );

      // There should be exactly one result
      Assert.assertEquals( 1, lQs.getRowCount() );

      // The data should match up correctly
      Assert.assertEquals( lPartId.toUuid(), lQs.getUuid( "part_id" ) );
      Assert.assertEquals( PART_KEY, lQs.getString( "part_key" ) );
      Assert.assertEquals( BOM_PART_KEY, lQs.getString( "bom_part_key" ) );
      Assert.assertEquals( BOM_ITEM_POS_KEY, lQs.getString( "bom_item_pos_key" ) );
      Assert.assertEquals( PART_NO_OEM, lQs.getString( "part_no_oem" ) );
      Assert.assertEquals( PART_NO_SDESC, lQs.getString( "part_no_sdesc" ) );
      Assert.assertEquals( CONDITIONS_LDESC, lQs.getString( "conditions_ldesc" ) );
      Assert.assertEquals( STANDARD_BOOL, lQs.getBoolean( "standard_bool" ) );
      Assert.assertEquals( APPL_EFF_LDESC, lQs.getString( "appl_eff_ldesc" ) );
      Assert.assertEquals( STATUS_CD, lQs.getString( "part_status_cd" ) );
      Assert.assertEquals( INTERCHANGEABILITY_ORDER, lQs.getInteger( "interchangeability_order" ) );

   }


   @Test
   public void testMultipleResults() {
      // Execute the query with the part group id
      QuerySet lQs = executeQuery( PART_GROUP_WITH_MULTIPLE_PARTS_ID );

      // There should be exactly 3 results
      Assert.assertEquals( 3, lQs.getRowCount() );
   }


   @Test
   public void testNoResults() {
      // Execute the query with the part group id
      QuerySet lQs = executeQuery( PART_GROUP_WITH_NO_PARTS_ID );

      // There should not be any results
      Assert.assertTrue( lQs.isEmpty() );
   }


   private QuerySet executeQuery( String aPartGroupId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aPartGroupId", aPartGroupId );

      return iQao.executeQuery(
            "com.mxi.mx.core.maintenance.eng.config.part.dao.query.GetAlternatePart", lArgs );
   }

}
