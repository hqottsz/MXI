
package com.mxi.mx.web.query.kit;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EqpKitPartGroupMapKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.kit.KitContents
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class KitContentsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testKitContents() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aKitPartNoDbId", 4650 );
      lArgs.add( "aKitPartNoId", 30069 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // first row: part 1
      lDs.next();
      testRow( lDs, new EqpKitPartGroupMapKey( "4650:30069:4650:10001" ), "2.0", "EA", "0",
            "ASSMBL-1", new AssemblyKey( "4650:ASSMBL-1" ), "4650:101",
            "group-1234 (bom part name)", new PartNoKey( "4650:30070" ), "BATCH-TEST-1 (desc-1)",
            "0", "75", "1", "30,5", // format: (pref vendor price,average unit price) of stanard
                                    // part
            true // because we have more than one alt parts in one kit content line, the check box
                 // is
                 // editable
      );

      // second row: part 2 (stanadard part with pref vendor)
      lDs.next();
      testRow( lDs, new EqpKitPartGroupMapKey( "4650:30069:4650:10001" ), "2.0", "EA", "0",
            "ASSMBL-1", new AssemblyKey( "4650:ASSMBL-1" ), "4650:101",
            "group-1234 (bom part name)", new PartNoKey( "4650:30071" ), "BATCH-TEST-2 (desc-2)",
            "1", "75", "1", "30,5", // format: (pref vendor price,average unit price) of stanard
                                    // part
            true // because we have more than one alt parts in one kit content line, the check box
                 // is
                 // editable
      );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), KitContentsTest.class,
            KitContentsData.getDataFile() );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    * @param aEqpKitLineKey
    * @param aPartQty
    * @param aQtyUnitCd
    * @param aDecimalPlacesQt
    * @param aAssemblyCd
    * @param aAssemblyKey
    * @param aBomPartKey
    * @param aBomPartDesc
    * @param aAltPartNoKey
    * @param aAltPartNo
    * @param aAltPartStdBool
    * @param aValuePct
    * @param aDisabled
    * @param aUnitPrices
    * @param aIsEditable
    */
   private void testRow( DataSet aDs, EqpKitPartGroupMapKey aEqpKitLineKey, String aPartQty,
         String aQtyUnitCd, String aDecimalPlacesQt, String aAssemblyCd, AssemblyKey aAssemblyKey,
         String aBomPartKey, String aBomPartDesc, PartNoKey aAltPartNoKey, String aAltPartNo,
         String aAltPartStdBool, String aValuePct, String aDisabled, String aUnitPrices,
         boolean aIsEditable ) {
      MxAssert.assertEquals( "kit_line_key", aEqpKitLineKey, aDs.getString( "kit_line_key" ) );
      MxAssert.assertEquals( "part_qty", aPartQty, aDs.getString( "part_qty" ) );
      MxAssert.assertEquals( "qty_unit_cd", aQtyUnitCd, aDs.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( "decimal_places_qt", aDecimalPlacesQt,
            aDs.getString( "decimal_places_qt" ) );
      MxAssert.assertEquals( "assembly_cd", aAssemblyCd, aDs.getString( "assembly_cd" ) );
      MxAssert.assertEquals( "assembly_key", aAssemblyKey, aDs.getString( "assembly_key" ) );
      MxAssert.assertEquals( "bom_part_key", aBomPartKey, aDs.getString( "bom_part_key" ) );
      MxAssert.assertEquals( "bom_part_desc", aBomPartDesc, aDs.getString( "bom_part_desc" ) );
      MxAssert.assertEquals( "alt_part_no", aAltPartNo, aDs.getString( "alt_part_no" ) );
      MxAssert.assertEquals( "alt_part_no_key", aAltPartNoKey, aDs.getString( "alt_part_no_key" ) );
      MxAssert.assertEquals( "alt_part_std_bool", aAltPartStdBool,
            aDs.getString( "alt_part_std_bool" ) );
      MxAssert.assertEquals( "value_pct", aValuePct, aDs.getString( "value_pct" ) );
      MxAssert.assertEquals( "disable", aDisabled, aDs.getString( "disable" ) );
      MxAssert.assertEquals( "unit_prices", aUnitPrices, aDs.getString( "unit_prices" ) );
      MxAssert.assertEquals( "is_editable", aIsEditable, aDs.getBoolean( "is_editable" ) );
   }
}
