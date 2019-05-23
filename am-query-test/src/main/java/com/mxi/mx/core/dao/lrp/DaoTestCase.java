
package com.mxi.mx.core.dao.lrp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;


/**
 * This class privides some common functionality for LRP DAOs testing.
 *
 * @author yvakulenko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public abstract class DaoTestCase {

   public static final int LRP_TEST_DB_ID = 777; // db_id of LRP tests
   public static final int TEST_LRP_ID = 1; // ID of test Long Range Plan
   public static final int LOC_DB_ID = 4650; // db_id of Location tests
   public static final int LOC_ID = 100013; // ID of test Location Plan
   public static final int CAPACITY_PATTERN_DB_ID = 4650; // db_id of Location Capacity tests
   public static final int CAPACITY_PATTERN_ID = 1; // ID of test Location Capacity
   public static final int CAPACITY_STD_ID = 1; // ID of test Location Capacity
   public static final int CAPACITY_EXCEPT_ID = 1; // ID of test Location Capacity

   protected LrpDaoFactory iFactory = LrpDaoFactoryImpl.getInstance();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      // execute SQL for set up initial data into DB
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), TestData.class,
            "CreateLRPPlan.sql" );
   }
}
