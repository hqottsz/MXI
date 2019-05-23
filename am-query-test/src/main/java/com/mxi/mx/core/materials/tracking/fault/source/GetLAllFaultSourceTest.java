
package com.mxi.mx.core.materials.tracking.fault.source;

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
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLAllFaultSourceTest {

   // Test Data
   private final String TEST_FAULT_SOURCE = "S_CODE";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetLAllFaultSourceTest.class,
            "GetAllFaultSourceTest.xml" );
   }


   @Test
   public void testQuery() throws ParseException {

      QuerySet lQs = iQao.executeQuery(
            "com.mxi.mx.core.materials.tracking.fault.source.dao.query.GetAllFaultSource", null );

      // verify 5 rows are returned: 4 0-level rows plus the new row added by
      // GetAllFaultSourceTest.xml
      Assert.assertEquals( 5, lQs.getRowCount() );
      List<String> lFaultSource = new ArrayList<String>();

      while ( lQs.next() ) {
         lFaultSource.add( lQs.getString( "code" ) );

      }
      Assert.assertEquals( true, lFaultSource.contains( TEST_FAULT_SOURCE ) );

   }

}
