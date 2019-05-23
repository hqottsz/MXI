
package com.mxi.mx.core.dao.lrp.capability;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Capability;
import com.mxi.mx.model.lrp.key.LrpLocKey;
import com.mxi.mx.model.lrp.key.LrpLocKeyImpl;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class CapabilityDaoTest extends DaoTestCase {

   private CapabilityDao iDao;
   private IdKey iLocationKey = new IdKeyImpl( 4650, 1000001 );
   private IdKey iPlanKey = new IdKeyImpl( 4650, 35 );
   private LrpLocKey iLrpLocKey = new LrpLocKeyImpl( iPlanKey, iLocationKey );


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();

      iDao = iFactory.getCapabilityDao( iLrpLocKey );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testGetRecords() throws Exception {
      Collection<Capability> lCapabilities = iDao.readAll();
      assertEquals( 0, lCapabilities.size() );
   }
}
