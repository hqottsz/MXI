package com.mxi.mx.core.maintenance.eng.prog.req.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.eng.prog.req.model.ReqDefnId;
import com.mxi.mx.core.maintenance.eng.prog.req.model.ReqRevision;
import com.mxi.mx.core.maintenance.eng.prog.req.model.ReqRevisionId;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests the {@link JdbcReqRevisionDao} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcReqRevisionDaoTest {

   private JdbcReqRevisionDao iReqRevisionDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the data access object can find a requirement by its id and populates all the
    * correct fields.
    */
   @Test
   public void testFindById() {
      TaskTaskKey lReqKey = new TaskRevisionBuilder().withTaskCode( "REQ_CODE" )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.BUILD )
            .isRecurring().isMandatory().isScheduledManually().build();

      TaskTaskTable lTaskTask = TaskTaskTable.findByPrimaryKey( lReqKey );
      ReqRevisionId lId = new ReqRevisionId( lTaskTask.getAlternateKey() );
      TaskDefnKey lReqDefnKey = lTaskTask.getTaskDefn();
      ReqDefnId lDefnId =
            new ReqDefnId( TaskDefnTable.findByPrimaryKey( lReqDefnKey ).getAlternateKey() );

      ReqRevision lReqRevision = iReqRevisionDao.find( lId ).now();

      assertEquals( "Requirement Code", "REQ_CODE", lReqRevision.getRequirementCode() );
      assertEquals( "Task Definition Status", "BUILD", lReqRevision.getStatus() );
      assertEquals( "Requirement Definition Id", lDefnId, lReqRevision.getReqDefnId() );
      assertEquals( "Legacy Key", lReqKey, lReqRevision.getLegacyKey() );
      assertEquals( "Legacy Definition Key", lReqDefnKey, lReqRevision.getLegacyTaskDefnKey() );
      assertTrue( "Manual Scheduing", lReqRevision.isManualScheduling() );
      assertFalse( "On Condition", lReqRevision.isOnCondition() );
      assertTrue( "Recurring", lReqRevision.isRecurring() );

      // scheduling rules were not requested
      assertNull( lReqRevision.getSchedulingRules() );
   }


   @Before
   public void setUp() throws Exception {
      iReqRevisionDao = new JdbcReqRevisionDao( QuerySetFactory.getInstance() );
   }

}
