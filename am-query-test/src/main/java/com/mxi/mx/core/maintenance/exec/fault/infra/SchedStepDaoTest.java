package com.mxi.mx.core.maintenance.exec.fault.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepDao.ColumnName;
import com.mxi.mx.core.table.sched.SchedStepTableRow;


public class SchedStepDaoTest {

   private static final TaskKey EXISTING_TASK_KEY = new TaskKey( 4650, 1 );
   private static final TaskKey NEW_TASK_KEY = new TaskKey( 4650, 3 );
   private static final String STEP_DESC = "Step 1";
   private static final SchedStepKey EXISTING_SCHED_STEP_KEY =
         new SchedStepKey( EXISTING_TASK_KEY, 1 );
   private static final SchedStepKey NEW_SCHED_STEP_KEY = new SchedStepKey( EXISTING_TASK_KEY, 2 );

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private SchedStepDao schedStepDao;


   @Before
   public void setup() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );
   }


   @Test
   public void create_default() {
      SchedStepTableRow row = schedStepDao.create();

      assertNotNull( row );
      assertNull( row.getPk() );
   }


   @Test
   public void create_valid() {
      SchedStepTableRow row = schedStepDao.create( NEW_SCHED_STEP_KEY );
      assertEquals( NEW_SCHED_STEP_KEY, row.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      SchedStepTableRow row = schedStepDao.findByPrimaryKey( null );

      assertFalse( row.exists() );
   }


   @Test
   public void findByPrimaryKey_valid() {
      SchedStepTableRow row = schedStepDao.findByPrimaryKey( EXISTING_SCHED_STEP_KEY );
      assertTrue( row.exists() );
      assertEquals( EXISTING_SCHED_STEP_KEY, row.getSchedStepKey() );
   }


   @Test
   public void findByPrimaryKey_non_existent() {
      SchedStepTableRow row = schedStepDao.findByPrimaryKey( NEW_SCHED_STEP_KEY );
      assertFalse( row.exists() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      schedStepDao.insert( null );
   }


   @Test
   public void insert_withoutKeyGenerated() {
      SchedStepTableRow row = new SchedStepTableRow( NEW_SCHED_STEP_KEY );

      SchedStepKey schedStepKey = schedStepDao.insert( row );

      assertEquals( NEW_SCHED_STEP_KEY, schedStepKey );
      assertTrue( hasRecord( NEW_SCHED_STEP_KEY ) );
   }


   @Test
   public void insert_withKeyGenerated() {

      SchedStepKey key = schedStepDao.generatePrimaryKey( NEW_TASK_KEY );

      SchedStepTableRow row = new SchedStepTableRow( key );

      SchedStepKey schedStepKey = schedStepDao.insert( row );

      assertTrue( hasRecord( schedStepKey ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      schedStepDao.update( null );
   }


   @Test
   public void update_valid() {

      String description = " description updated";

      SchedStepTableRow row = new SchedStepTableRow( EXISTING_SCHED_STEP_KEY );
      row.setStepLdesc( description );

      schedStepDao.update( row );

      SchedStepTableRow retrievedTable = schedStepDao.findByPrimaryKey( EXISTING_SCHED_STEP_KEY );

      assertTrue( retrievedTable.exists() );

      assertEquals( description, retrievedTable.getStepLdesc() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      schedStepDao.refresh( null );
   }


   @Test
   public void refresh_valid() {

      String newDescription = "new description";

      SchedStepTableRow row = new SchedStepTableRow( EXISTING_SCHED_STEP_KEY );

      assertEquals( STEP_DESC, row.getStepLdesc() );

      DataSetArgument args = new DataSetArgument();
      args.add( ColumnName.STEP_LDESC.name(), newDescription );

      DataSetArgument whereArgs = new DataSetArgument();
      whereArgs.add( ColumnName.SCHED_DB_ID.name(), row.getPk().getDbId() );
      whereArgs.add( ColumnName.SCHED_ID.name(), row.getPk().getId() );
      whereArgs.add( ColumnName.STEP_ID.name(), row.getPk().getStepId() );

      MxDataAccess.getInstance().executeUpdate( SchedStepTableRow.TABLE_NAME, args, whereArgs );

      schedStepDao.refresh( row );

      assertEquals( newDescription, row.getStepLdesc() );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      schedStepDao.delete( null );
   }


   @Test
   public void delete_valid() {
      SchedStepTableRow row = new SchedStepTableRow( EXISTING_SCHED_STEP_KEY );

      assertTrue( schedStepDao.delete( row ) );
      assertFalse( hasRecord( EXISTING_SCHED_STEP_KEY ) );
   }


   @Test
   public void delete_invalid() {
      SchedStepTableRow row = new SchedStepTableRow( NEW_SCHED_STEP_KEY );

      assertFalse( schedStepDao.delete( row ) );
   }


   @Test
   public void generatePrimaryKey_unique() {
      SchedStepKey key1 = schedStepDao.generatePrimaryKey();
      SchedStepKey key2 = schedStepDao.generatePrimaryKey();

      assertFalse( key1.getId() == key2.getId() );
   }


   private boolean hasRecord( SchedStepKey aKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aKey, "sched_db_id", "sched_id", "step_id" );

      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( SchedStepTableRow.TABLE_NAME, args );

      if ( qs.next() ) {
         return true;
      }

      return false;
   }

}
