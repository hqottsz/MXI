
package com.mxi.mx.web.query.assembly;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class runs a unit test on the OilConsumptionRateDefinition query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OilConsumptionRateDefinitionDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            OilConsumptionRateDefinitionDetailsTest.class );
   }


   /** The UPTAKE_CD_NAME */
   private static final String UPTAKE_CD_NAME = "H (HOURS)";

   /** The TIME_CD_NAME */
   private static final String TIME_CD_NAME = "EA (EACH)";

   /** The UPTAKE_CD_NAME_PARTIAL */
   private static final String UPTAKE_CD_NAME_PARTIAL = "HOUR";

   /** The UPTAKE_CD_NAME_UNIT */
   private static final String UPTAKE_CD_NAME_UNIT = "HOUR (Hours)";

   /** The TIME_CD_NAME_PARTIAL */
   private static final String TIME_CD_NAME_PARTIAL = "EA";

   /** The TIME_CD_NAME_UNIT */
   private static final String TIME_CD_NAME_UNIT = "EA (EACH)";

   /** The MEASUREMENT_DATA_TYPE_KEY */
   private static final String MEASUREMENT_DATA_TYPE_KEY = "4650:3012";

   /** The TIME_DATA_TYPE_KEY */
   private static final String TIME_DATA_TYPE_KEY = "4650:1001";


   /**
    * Execute OilConsumptionRateDefinition.qrx
    */
   @Test
   public void testOilConsumptionRateDefinitionDetails() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 4650 );
      lArgs.add( "aAssmblCd", "ENGINE-1" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Get the first row.
      lResult.next();

      // Verify the contents of the row returned.
      testRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if the following are returned :
      // mim_data_type.data_type_cd || ' (' || mim_data_type.data_type_sdesc || ')'
      MxAssert.assertEquals( UPTAKE_CD_NAME, aResult.getString( "uptake_cd_name" ) );

      // ref_eng_unit.eng_unit_cd
      MxAssert.assertEquals( UPTAKE_CD_NAME_PARTIAL, aResult.getString( "uptake_cd" ) );

      // ref_eng_unit.eng_unit_cd || ' (' || ref_eng_unit.desc_sdesc || ')'
      MxAssert.assertEquals( UPTAKE_CD_NAME_UNIT, aResult.getString( "uptake_cd_name_unit" ) );

      // time_data_type.data_type_cd || ' (' || time_data_type.data_type_sdesc || ')'
      MxAssert.assertEquals( TIME_CD_NAME, aResult.getString( "time_cd_name" ) );

      // time_eng_unit.eng_unit_cd
      MxAssert.assertEquals( TIME_CD_NAME_PARTIAL, aResult.getString( "time_cd" ) );

      // time_eng_unit.eng_unit_cd || ' (' || time_eng_unit.desc_sdesc || ')
      MxAssert.assertEquals( TIME_CD_NAME_UNIT, aResult.getString( "time_cd_name_unit" ) );

      // eqp_assmbl_bom_oil.oil_data_type_db_id || ':' || eqp_assmbl_bom_oil.oil_data_type_id
      MxAssert.assertEquals( MEASUREMENT_DATA_TYPE_KEY,
            aResult.getString( "measurement_data_type_key" ) );

      // eqp_assmbl_bom_oil.time_data_type_db_id || ':' || eqp_assmbl_bom_oil.time_data_type_id
      MxAssert.assertEquals( TIME_DATA_TYPE_KEY, aResult.getString( "time_data_type_key" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
