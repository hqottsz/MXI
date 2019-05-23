package com.mxi.mx.core.table.sched;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.core.key.TaskKey;


public class JdbcSchedWPSignReqDaoTest {

   private static final TaskKey PARTIALLY_SIGNED_OFF_WORK_PACKAGE = new TaskKey( "4650:1" );
   private static final TaskKey SIGNED_OFF_WORK_PACKAGE = new TaskKey( "4650:2" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   private SchedWPSignReqDao iDao;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iDao = new JdbcSchedWPSignReqDao();
   }


   @Test
   public void isAllSignOffRequirementsComplete_null() {
      boolean lIsComplete = iDao.isAllSignOffRequirementsComplete( null );

      assertFalse( lIsComplete );
   }


   @Test
   public void isAllSignOffRequirementsComplete_incomplete() {
      boolean lIsComplete =
            iDao.isAllSignOffRequirementsComplete( PARTIALLY_SIGNED_OFF_WORK_PACKAGE );

      assertFalse( lIsComplete );
   }


   @Test
   public void isAllSignOffRequirementsComplete_complete() {
      boolean lIsComplete = iDao.isAllSignOffRequirementsComplete( SIGNED_OFF_WORK_PACKAGE );

      assertTrue( lIsComplete );
   }

}
