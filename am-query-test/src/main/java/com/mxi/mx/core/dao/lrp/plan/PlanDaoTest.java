
package com.mxi.mx.core.dao.lrp.plan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.LRPPlan;
import com.mxi.mx.model.lrp.PlanNotFoundException;
import com.mxi.mx.model.user.User;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class PlanDaoTest extends DaoTestCase {

   private PlanDao iPlanDao;


   @Override
   @Before
   public void loadData() throws Exception {
      iPlanDao = iFactory.getPlanDao();
   }


   /**
    * Test Create operation.<br>
    * 1. Construct object<br>
    * 2. Write it into DB<br>
    * 3. Read data from DB and assert
    */
   @Test
   public void testCreate() {
      LRPPlan lPlanToSave = new LRPPlan();
      lPlanToSave.setDescription( "Plan" );
      lPlanToSave.setName( "Plan-Name" );

      User lCreateUser = new User();
      lCreateUser.setUserID( 15 );

      User lUpdateUser = lCreateUser;
      lPlanToSave.setCreateUser( lCreateUser );
      lPlanToSave.setUpdateUser( lUpdateUser );

      IdKey lPlanKey = iPlanDao.insertPlan( lPlanToSave );
      assertNotNull( lPlanKey );

      try {
         LRPPlan lOpenedPlan = iPlanDao.openPlan( lPlanKey );
         assertNotNull( lOpenedPlan );
         assertEquals( lPlanToSave.getName(), lOpenedPlan.getName() );
         assertEquals( lPlanToSave.getDescription(), lOpenedPlan.getDescription() );
         assertEquals( lPlanToSave.getCreateUser().getUserID(),
               lOpenedPlan.getCreateUser().getUserID() );
         assertEquals( lPlanToSave.getUpdateUser().getUserID(),
               lOpenedPlan.getUpdateUser().getUserID() );
      } catch ( PlanNotFoundException e ) {
         fail( "Unexpected Plan not found exception" );
      }
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testDelete() throws Exception {
      LRPPlan lPlanToSave = new LRPPlan();
      lPlanToSave.setDescription( "Plan" );
      lPlanToSave.setName( "Plan-Name" );

      User lCreateUser = new User();
      lCreateUser.setUserID( 15 );

      User lUpdateUser = lCreateUser;
      lPlanToSave.setCreateUser( lCreateUser );
      lPlanToSave.setUpdateUser( lUpdateUser );

      IdKey lPlanKey = iPlanDao.insertPlan( lPlanToSave );
      assertNotNull( lPlanKey );

      try {
         LRPPlan lOpenedPlan = iPlanDao.openPlan( lPlanKey );
         assertNotNull( lOpenedPlan );
      } catch ( PlanNotFoundException e ) {
         fail( "Unexpected Plan not found exception" );
      }

      iPlanDao.removePlan( lPlanKey );
      try {
         LRPPlan lOpenedPlan = iPlanDao.openPlan( lPlanKey );
         assertNull( lOpenedPlan );
      } catch ( PlanNotFoundException e ) {

         // expected exception
      }
   }


   /**
    * Reads aircrafts.<br>
    * Expects one actual and one future aircrafts.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testRead() throws Exception {

      try {
         LRPPlan lOpenedPlan = iPlanDao.openPlan( new IdKeyImpl( 0, 0 ) );
         assertNull( lOpenedPlan );
      } catch ( PlanNotFoundException e ) {
         // expected exception
      }

      // the read operation is already being tested in the testCreate and testUpdate tests. There
      // is no need to duplicate theses tests here
   }


   /**
    * Reads aircrafts.<br>
    * Expects one actual and one future aircrafts.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUpdate() throws Exception {
      LRPPlan lPlanToSave = new LRPPlan();
      lPlanToSave.setDescription( "Plan" );
      lPlanToSave.setName( "Plan-Name" );

      User lCreateUser = new User();
      lCreateUser.setUserID( 15 );

      User lUpdateUser = lCreateUser;
      lPlanToSave.setCreateUser( lCreateUser );
      lPlanToSave.setUpdateUser( lUpdateUser );

      IdKey lPlanKey = iPlanDao.insertPlan( lPlanToSave );
      assertNotNull( lPlanKey );

      try {
         LRPPlan lOpenedPlan = iPlanDao.openPlan( lPlanKey );
         assertNotNull( lOpenedPlan );
      } catch ( PlanNotFoundException e ) {
         fail( "Unexpected Plan not found exception" );
      }

      lPlanToSave.setPrimaryKey( lPlanKey );
      lPlanToSave.setDescription( "Updated-Plan" );
      lPlanToSave.setName( "Updated-Plan-Name" );

      User lNewUpdateUser = new User();
      lUpdateUser.setUserID( 100 );
      lPlanToSave.setUpdateUser( lNewUpdateUser );

      LRPPlan lUpdatedPlan = iPlanDao.updatePlan( lPlanToSave );
      assertNotNull( lUpdatedPlan );
      assertEquals( lPlanToSave.getName(), lUpdatedPlan.getName() );
      assertEquals( lPlanToSave.getDescription(), lUpdatedPlan.getDescription() );
      assertEquals( lPlanToSave.getCreateUser().getUserID(),
            lUpdatedPlan.getCreateUser().getUserID() );
      assertEquals( lNewUpdateUser.getUserID(), lUpdatedPlan.getUpdateUser().getUserID() );
   }
}
