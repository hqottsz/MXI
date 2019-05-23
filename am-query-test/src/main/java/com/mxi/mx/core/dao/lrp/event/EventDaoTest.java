
package com.mxi.mx.core.dao.lrp.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.dao.lrp.LrpReferenceCache;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Event;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class EventDaoTest extends DaoTestCase {

   private EventDao iDao;
   private IdKey iPlanKey = new IdKeyImpl( 4650, 35 );


   /**
    * Creates a new EventDaoTest object.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testDeleteAllEventsForPlan() throws Exception {
      IdKey lOtherPlanKey = new IdKeyImpl( 4650, 43 );
      iDao = iFactory.getEventDao();

      LrpReferenceCache lReferenceCache = new LrpReferenceCache();
      iDao.setReferenceCache( lReferenceCache );

      Collection<Event> lRecords = iDao.readAll( lOtherPlanKey );
      assertEquals( 21, lRecords.size() );

      lRecords = iDao.readAll( iPlanKey );
      assertEquals( 21, lRecords.size() );

      iDao.deleteAllEventsForPlan( iPlanKey );

      lRecords = iDao.readAll( lOtherPlanKey );
      assertEquals( 21, lRecords.size() );

      lRecords = iDao.readAll( iPlanKey );
      assertEquals( 0, lRecords.size() );
   }


   /**
    * Tests the readAll method.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testRead() throws Exception {
      iDao = iFactory.getEventDao();

      LrpReferenceCache lReferenceCache = new LrpReferenceCache();
      iDao.setReferenceCache( lReferenceCache );

      Collection<Event> lRecords = iDao.readAll( iPlanKey );

      for ( Event lEvent : lRecords ) {
         assertEquals( "WORK_EVENT", lEvent.getType().name() );

         // make sure Event name is not NULL ( empty string is a name default value )
         assertNotNull( "Event name cannot be null.", lEvent.getName() );
      }
   }
}
