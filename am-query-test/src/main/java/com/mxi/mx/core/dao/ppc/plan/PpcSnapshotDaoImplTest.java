
package com.mxi.mx.core.dao.ppc.plan;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.model.ppc.key.UuidKey;
import com.mxi.mx.model.ppc.key.UuidKeyImpl;
import com.mxi.mx.model.ppc.transfer.baseline.WorkpackageBaseline;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * Ppc Snapshot Dao Implementation Test
 *
 * @author slevert
 */
public class PpcSnapshotDaoImplTest extends DaoTestCase {

   PpcSnapshotDaoImpl iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();
      iDao = new PpcSnapshotDaoImpl();
   }


   /**
    * Test that we get all the snapshots associated to a work package which is identified by it's
    * UUID.
    */
   @Test
   public void testGetWpSnapshots() {
      Map<UuidKey, WorkpackageBaseline> lWpSnapshots = iDao.getWpBaselines(
            new UuidKeyImpl( UuidUtils.fromHexString( "121E889099034BC399E6D78EB23C5A00" ) ) );

      MxAssert.assertEquals( 2, lWpSnapshots.size() );

      lWpSnapshots = iDao.getWpBaselines(
            new UuidKeyImpl( UuidUtils.fromHexString( "78F6982D49A3463285036E600B8BAE51" ) ) );

      MxAssert.assertEquals( 1, lWpSnapshots.size() );
   }
}
