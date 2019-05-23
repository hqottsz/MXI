package com.mxi.mx.core.maintenance.eng.config.configurationslot.dao.query;

import java.util.UUID;

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
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Tests the GetConfigSlotsByAssembly.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetConfigSlotsByAssemblyTest {

   // Query Access Object
   private QuerySet iQs;

   @ClassRule
   public static DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   /*
    * Returns multiple SYS config slot records
    */
   @Test
   public void testMultipleRecordsRetrieved() {

      // execute query
      final String lAssemblyId = "ABCDEF1234567890ABCDEF1234567890";
      iQs = executeQuery( lAssemblyId );

      // assert results
      Assert.assertEquals( 2, iQs.getRowCount() );

   }


   /*
    * Returns a single SYS config slot record
    */
   @Test
   public void testSingleRecordRetrieved() {

      // expected results
      final String lAssemblyBomCode = "10-00-00";
      final String lAssemblyBomName = "FUSELAGE";

      // execute query
      final String lAssemblyId = "798A10E57E484EE69BC820FAB3138E64";
      iQs = executeQuery( lAssemblyId );

      // assert results
      iQs.first();
      Assert.assertEquals( 1, iQs.getRowCount() );

      Assert.assertEquals( lAssemblyBomCode, iQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( lAssemblyBomName, iQs.getString( "assmbl_bom_name" ) );
   }


   /*
    * Executes the query with a random Assembly ID which returns an empty dataset
    */
   @Test
   public void testNoRecordsRetrieved() {

      // execute query
      String lAssemblyId = UUID.randomUUID().toString().replaceAll( "-", "" ).toUpperCase();

      iQs = executeQuery( lAssemblyId );

      // assert results
      Assert.assertTrue( iQs.isEmpty() );
   }


   /*
    * Execute the query under test
    */
   private QuerySet executeQuery( String aAssemblyId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssemblyId", aAssemblyId );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.maintenance.eng.config.configurationslot.dao.query.GetConfigSlotsByAssembly",
            lArgs );
   }


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(), GetConfigSlotsByAssemblyTest.class,
            "GetConfigSlotsByAssembly.xml" );
   }

}
