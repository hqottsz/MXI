package com.mxi.mx.core.materials.tracking.measurement.datatype.dao.query;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;


/**
 * Unit test for /mxcoreejb/src/main/resources/com/mxi/mx/core/materials/tracking
 * /measurement/datatype/dao/query/GetDataTypeByUUID.qrx.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetDataTypeByUUIDTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetDataTypeByUUIDTest.class );
   }


   private static final UUID TEST_UUID = UUID.fromString( "00000000-0000-0000-0000-000000000001" );
   private static final String TEST_DATA_TYPE_CODE = "TEST_ALTID";
   private static final String TEST_ENG_UNIT_CODE = "TEST_CD";
   private static final int TEST_ENTRY_PREC_QUANTITY = 3;
   private static final String TEST_LEGACY_KEY = "4650:1000";


   @Test
   public void testQuery() throws ParseException {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( "aUUID", TEST_UUID );
      }
      QuerySet lQuerySet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      Assert.assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.first();

      Assert.assertEquals( TEST_DATA_TYPE_CODE, lQuerySet.getString( "data_type_cd" ) );
      Assert.assertEquals( TEST_ENG_UNIT_CODE, lQuerySet.getString( "eng_unit_cd" ) );
      Assert.assertEquals( TEST_ENTRY_PREC_QUANTITY, lQuerySet.getInt( "entry_prec_qt" ) );
      Assert.assertEquals( TEST_LEGACY_KEY, lQuerySet.getString( "legacy_key" ) );
   }
}
