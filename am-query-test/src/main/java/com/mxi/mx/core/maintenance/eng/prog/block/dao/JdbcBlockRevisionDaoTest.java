package com.mxi.mx.core.maintenance.eng.prog.block.dao;

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
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.eng.prog.block.model.BlockDefnId;
import com.mxi.mx.core.maintenance.eng.prog.block.model.BlockRevision;
import com.mxi.mx.core.maintenance.eng.prog.block.model.BlockRevisionId;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests the JdbcBlockRevisionDao class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcBlockRevisionDaoTest {

   private JdbcBlockRevisionDao iBlockRevisionDao;
   private RefTaskClassKey iBlockTaskClass;

   private GlobalParametersStub iLogicParameters;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the data access object can find a block by its id and populates all the correct
    * fields.
    */
   @Test
   public void testFindById() {
      TaskTaskKey lBlockKey = new TaskRevisionBuilder().withTaskCode( "BLK_CODE" )
            .withTaskClass( iBlockTaskClass ).withStatus( RefTaskDefinitionStatusKey.BUILD )
            .isRecurring().isMandatory().isScheduledManually().build();

      TaskTaskTable lTaskTask = TaskTaskTable.findByPrimaryKey( lBlockKey );
      BlockRevisionId lId = new BlockRevisionId( lTaskTask.getAlternateKey() );
      TaskDefnKey lBlockDefnKey = lTaskTask.getTaskDefn();
      BlockDefnId lDefnId =
            new BlockDefnId( TaskDefnTable.findByPrimaryKey( lBlockDefnKey ).getAlternateKey() );

      BlockRevision lBlockRevision = iBlockRevisionDao.find( lId ).now();

      assertEquals( "Block Code", "BLK_CODE", lBlockRevision.getBlockCode() );
      assertEquals( "Task Definition Status", "BUILD", lBlockRevision.getStatus() );
      assertEquals( "Block Definition Id", lDefnId, lBlockRevision.getBlockDefnId() );
      assertEquals( "Legacy Key", lBlockKey, lBlockRevision.getLegacyKey() );
      assertEquals( "Legacy Definition Key", lBlockDefnKey, lBlockRevision.getLegacyTaskDefnKey() );
      assertTrue( "Manual Scheduing", lBlockRevision.isManualScheduling() );
      assertFalse( "On Condition", lBlockRevision.isOnCondition() );
      assertTrue( "Recurring", lBlockRevision.isRecurring() );

      // scheduling rules were not requested
      assertNull( lBlockRevision.getSchedulingRules() );
   }


   @Before
   public void loadData() throws Exception {
      DataSetArgument lRefTaskClassArgs = new DataSetArgument();
      lRefTaskClassArgs.add( "task_class_db_id", 0 );
      lRefTaskClassArgs.add( "task_class_cd", "BLOCK" );
      lRefTaskClassArgs.add( "class_mode_cd", "BLOCK" );

      MxDataAccess.getInstance().executeInsert( "ref_task_class", lRefTaskClassArgs );

      iBlockTaskClass = new RefTaskClassKey( 0, "BLOCK" );

      iBlockRevisionDao = new JdbcBlockRevisionDao( QuerySetFactory.getInstance() );
   }
}
