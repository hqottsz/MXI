
package com.mxi.am.db.sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * This is an example of a database test that changes the contents of the database. The behavior
 * here is that the database will be rolled back every test.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MutableExampleTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), ImmutableExampleTest.class );
   }


   @Test
   public void testMutableAction() {
      Assert.assertEquals( "1.2.3.4", getVersion( "V1" ) );

      updateVersion( "V1", "12.55" );

      Assert.assertEquals( "12.55", getVersion( "V1" ) );
   }


   @Test
   public void testMutableDataRolledBack() {
      Assert.assertEquals( "1.2.3.4", getVersion( "V1" ) );

      updateVersion( "V1", "12.55" );

      Assert.assertEquals( "12.55", getVersion( "V1" ) );
   }


   /**
    * Updates the version
    *
    * @param aModuleName
    *           the module name
    * @param aVersionNoSdesc
    *           the version no description
    */
   private void updateVersion( String aModuleName, String aVersionNoSdesc ) {
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( "version_no_sdesc", aVersionNoSdesc );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( "module_name", aModuleName );

      MxDataAccess.getInstance().executeUpdate( "UTL_VERSION", lSetArgs, lWhereArgs );
   }


   /**
    * Retrieves the version for a module.
    *
    * @param aModuleName
    *           the module name
    * @return the version no description
    */
   private String getVersion( String aModuleName ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aModuleName", aModuleName );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      Assert.assertTrue( lQs.first() );
      return lQs.getString( "version_no_sdesc" );
   }
}
