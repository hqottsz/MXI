
package com.mxi.mx.core.materials.asset.availability;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLocallyAvailablePartQuantityTest {

   // Test Data
   private final String PART_ID = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private final String KIT_PART_ID = "F87786CC8A7643FF960A92E2E4E7037F";
   private final String SUPPLY_LOCATION_ID = "D809C115702545A5B0FA1E8330582F75";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetLocallyAvailablePartQuantityTest.class,
            "GetLocallyAvailablePartQuantityTest.xml" );
   }


   @Test
   public void testQuery() throws ParseException {
      List<String> lPartIds = new ArrayList<String>();
      lPartIds.add( PART_ID );
      lPartIds.add( KIT_PART_ID );
      QuerySet lQs = execute( SUPPLY_LOCATION_ID, lPartIds );

      // verify two rows are returned
      Assert.assertEquals( 2, lQs.getRowCount() );

      PartId lPartId = new PartId( PART_ID );

      while ( lQs.next() ) {
         int lQuantity = lQs.getInt( "total_available_quantity" );

         if ( lQs.getUuid( "partId" ).equals( lPartId.toUuid() ) ) {

            Assert.assertEquals( 2, lQuantity );
         }

         else {
            Assert.assertEquals( 1, lQuantity );
         }
      }

   }


   public QuerySet execute( String aSupplyLocationId, List<String> aPartIds ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSupplyLocationId", aSupplyLocationId );
      lArgs.addWhereIn( "WHERE_IN_CLAUSE", "rawtohex(eqp_part_no.alt_id)",
            aPartIds.toArray( new String[aPartIds.size()] ) );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.availability.dao.query.GetLocallyAvailablePartQuantity",
            lArgs );
   }
}
