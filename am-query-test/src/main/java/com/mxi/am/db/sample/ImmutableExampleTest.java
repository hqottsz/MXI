
package com.mxi.am.db.sample;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * This is an example of a database test that does not change the data in the database. The behavior
 * here is that all tests will run within a single transaction (super fast!)
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ImmutableExampleTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ImmutableExampleTest.class );
   }


   @Test
   public void testImmutableQuery() {
      Assert.assertEquals( "1.2.3.4", getVersion( "V1" ) );
   }


   @Test
   public void testOtherImmutableQuery() {
      Assert.assertEquals( "2.0.0.0", getVersion( "V2" ) );
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
            .executeQuery( "com.mxi.am.db.sample.ImmutableExample", lArgs );

      Assert.assertTrue( lQs.first() );
      return lQs.getString( "version_no_sdesc" );
   }
}
