
package com.mxi.mx.web.query.dropdown;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.dropdown.UnitOfMeasure.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UnitOfMeasureTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UnitOfMeasureTest.class );
   }


   /** The UOM_KEY */
   private static final String UOM_KEY = "0:MINS";

   /** The ENG_UNIT_CD_DESC */
   private static final String ENG_UNIT_CD_DESC = "MINS (MINUTES)";


   /**
    * Tests the retrieval of unit of measure on the edit measurements screen..
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetUnitOfMeasure() throws Exception {

      int lDataTypeDbId = 0;
      int lDataTypeId = 50;

      // Retrieves the set of data for UOM.
      DataSet lDataSet = this.execute( new DataTypeKey( lDataTypeDbId, lDataTypeId ) );

      lDataSet.next();

      this.testRow( lDataSet );
   }


   /**
    * This method executes the query in UnitOfMeasure.qrx
    *
    * @param aDataTypeKey
    *           The data type key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( DataTypeKey aDataTypeKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aDataTypeDbId", aDataTypeKey.getDbId() );
      lDataSetArgument.add( "aDataTypeId", aDataTypeKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Verify the content of the row returned.
    *
    * @param aDataSet
    *           the dataset.
    */
   private void testRow( DataSet aDataSet ) {

      // Verify if the following are returned:
      // ref_eng_unit.eng_unit_db_id || ':' ||ref_eng_unit.eng_unit_cd
      MxAssert.assertEquals( "uom_key", UOM_KEY, aDataSet.getString( "uom_key" ) );

      // ref_eng_unit.eng_unit_cd || ' (' || ref_eng_unit.desc_sdesc || ')'
      MxAssert.assertEquals( "eng_unit_cd_desc", ENG_UNIT_CD_DESC,
            aDataSet.getString( "eng_unit_cd_desc" ) );
   }
}
