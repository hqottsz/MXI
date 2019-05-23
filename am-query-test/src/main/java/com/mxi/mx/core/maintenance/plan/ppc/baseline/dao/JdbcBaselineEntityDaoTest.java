
package com.mxi.mx.core.maintenance.plan.ppc.baseline.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.maintenance.plan.ppc.baseline.model.BaselineEntity;
import com.mxi.mx.core.maintenance.plan.ppc.baseline.model.BaselineId;
import com.mxi.mx.core.services.ppc.PpcServiceImpl;
import com.mxi.mx.model.ppc.key.UuidKey;
import com.mxi.mx.model.ppc.key.UuidKeyImpl;
import com.mxi.mx.model.ppc.transfer.baseline.ActivityBaseline;
import com.mxi.mx.model.ppc.transfer.baseline.WorkpackageBaseline;
import com.mxi.mx.model.ppc.transfer.baseline.WorkpackageBaseline.WorkpackageBaselineBuilder;
import com.mxi.mx.model.ppc.transfer.baseline.WpBaseline;
import com.mxi.mx.testing.matchers.MxMatchers;


/**
 * This class tests the {@link JdbcBaselineEntityDao} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcBaselineEntityDaoTest {

   private JdbcBaselineEntityDao iDao;
   private PpcServiceImpl iPpcService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that the DAO properly loads the data from the database when data exists.
    *
    * @throws Exception
    */
   @Test
   public void testThatDaoFindsAllBaselines() throws Exception {

      // create the data
      WpBaseline lWpBaseline1 = createWPBaseline();
      WpBaseline lWpBaseline2 = createWPBaseline();

      BaselineId lId1 = new BaselineId( lWpBaseline1.getWpBaselineId().getUuid() );
      BaselineId lId2 = new BaselineId( lWpBaseline2.getWpBaselineId().getUuid() );

      // attempt to get the data
      List<BaselineEntity> lEntities = iDao.findAll();

      Assert.assertEquals( 0, lEntities.size() );

      // persist the data
      iPpcService.saveWorkpackageBaseline( lWpBaseline1 );
      iPpcService.saveWorkpackageBaseline( lWpBaseline2 );

      // attempt to get the data
      lEntities = iDao.findAll();

      Assert.assertEquals( 2, lEntities.size() );

      for ( BaselineEntity lBaselineEntity : lEntities ) {
         MxMatchers.assertThat( lBaselineEntity.getId(), MxMatchers.isOneOf( lId1, lId2 ) );
      }
   }


   /**
    * Tests that the DAO properly loads the data when no data exists
    */
   @Test
   public void testThatDaoFindsNoBaselines() {
      List<BaselineEntity> lEntities = iDao.findAll();

      Assert.assertEquals( 0, lEntities.size() );
   }


   /**
    * Sets up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {
      iDao = new JdbcBaselineEntityDao();
      iPpcService = new PpcServiceImpl();
   }


   /**
    * Create a {@link WpBaseline}
    *
    * @return {@link WpBaseline}
    */
   private WpBaseline createWPBaseline() {

      // create the data
      WorkpackageBaselineBuilder lBuilder = new WorkpackageBaseline.WorkpackageBaselineBuilder();

      lBuilder.name( "TestBaseline" );
      lBuilder.description( "test baseline description..." );
      lBuilder.baselineDate( new Date() );

      WorkpackageBaseline lBaseline = lBuilder.build();

      Map<UuidKey, ActivityBaseline> lActivitySnapshots = new HashMap<UuidKey, ActivityBaseline>();
      UuidKey lWpId = new UuidKeyImpl();

      WpBaseline lWpBaseline = new WpBaseline( lWpId, lBaseline, lActivitySnapshots );

      return lWpBaseline;
   }

}
