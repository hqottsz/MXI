
package com.mxi.mx.common.dao.job;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.key.JobKey;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * This test class tests the MxJobDao class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MxJobDaoTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the isActive method.
    */
   @Test
   public void testIsActive() {
      MxJobDao lJobDao = new MxJobDao();

      Assert.assertTrue( lJobDao.isActive( JobKey.MX_CORE_PROCESS_AUTO_RSRV_CONTROLLER ) );

      Assert.assertFalse( lJobDao.isActive( JobKey.MX_CORE_ABCRECALCULATION ) );
   }
}
