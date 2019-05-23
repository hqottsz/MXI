
package com.mxi.mx.core.assembler.lrp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;


/**
 * This class privides some common functionality for LRP DAOs testing.
 *
 * @author yvakulenko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public abstract class AssemblerTestCase {

   public static final int TEST_DB_ID = 777; // db_id of LRP tests
   public static final int TEST_ID = 1; // ID of test Long Range Plan
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }

}
