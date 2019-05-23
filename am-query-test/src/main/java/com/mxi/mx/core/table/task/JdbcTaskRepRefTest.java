package com.mxi.mx.core.table.task;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetException;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Integration unit test for {@link JdbcTaskRepRefDao}
 *
 */
public class JdbcTaskRepRefTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   private static final TaskRepRefKey PK_1 = new TaskRepRefKey( 1234, 1 );
   private static final TaskRepRefKey PK_2 = new TaskRepRefKey( 1234, 2 );

   private static final String TASK_DB_ID_COLUMN =
         TaskRepRefTableRow.ColumnName.TASK_DB_ID.toString();
   private static final String TASK_ID_COLUMN = TaskRepRefTableRow.ColumnName.TASK_ID.toString();

   private JdbcTaskRepRefDao dao;


   @Before
   public void before() {
      dao = new JdbcTaskRepRefDao();
   }


   /**
    *
    * Verify inserting a single row is persisted.
    *
    */
   @Test
   public void insertSingleRowIsPersisted() {

      // When inserting single row.
      TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition();
      TaskRepRefTableRow row = dao.create( reqDefinitionKey );
      dao.insert( row );

      // Then a row is persisted.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_1, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      qs.next();

      assertThat( qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpectedly,incorrect key is returned",
            qs.getKey( TaskRepRefKey.class, TASK_DB_ID_COLUMN, TASK_ID_COLUMN ),
            is( new TaskRepRefKey( reqDefinitionKey ) ) );
   }


   /**
    *
    * Verify inserting row when PK already exists throws an exception.
    *
    */
   @Test
   public void insertRowWhenPkAlreadyExists() {

      // Set up for the "Then" below.
      expectedException.expect( MxRuntimeException.class );
      expectedException.expectCause( Matchers.isA( DataSetException.class ) );

      // Given a row exists.
      addRow( PK_1 );

      // When inserting a row with the same PK.
      TaskRepRefTableRow lRow = dao.create( PK_1 );
      dao.insert( lRow );

      // Then and exception is thrown.
      fail( "Expecting exception to be thrown." );
   }


   /**
    *
    * Verify finding a row by existing primary key retrieves the row.
    *
    */
   @Test
   public void findByPrimaryKeyReturnsRowWhenExists() {

      // Given a row exists.
      addRow( PK_1 );

      // When finding a row by primary key.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_1 );

      // Then the row is retrieved.
      assertNotNull( "Expected a row to be returned", row );
      assertThat(
            "Unexpectedly, the retrieved primary key doesn't match with the one inserted in data setup",
            row.getPk(), is( PK_1 ) );
   }


   /**
    *
    * Verify finding a row by non-existing primary key retrieves no rows.
    *
    */
   @Test
   public void findByPrimaryKeyDoesNotReturnRowWhenNotExist() {

      // Given a row exists.
      addRow( PK_1 );

      // When finding a row by non-existing primary key.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_2 );

      // Then no rows are retrieved.
      assertThat( "Unexpectedly, a row was returned", row.exists(), is( false ) );
   }


   /**
    *
    * Verify updating a row is persisted.
    *
    */
   @Test
   public void updateRowIsPersisted() {

      // Given a row exists.
      addRow( PK_1 );

      // When the association id of the row is updated.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_1 );
      row.setDamagedComponentBool( true );
      dao.update( row );

      // Then the update is persisted.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_1, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      qs.next();
      assertThat( "Unexpectedly, more than one row was returned", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpectedly, incorrect primary key is returned",
            qs.getKey( TaskRepRefKey.class, TASK_DB_ID_COLUMN, TASK_ID_COLUMN ), is( PK_1 ) );
      assertThat( "Unexpectedly, the row was not updated",
            qs.getBoolean( TaskRepRefTableRow.ColumnName.DAMAGED_COMPONENT_BOOL.toString() ),
            is( true ) );
   }


   /**
    *
    * Verify updating one row does not affect other rows.
    *
    */
   @Test
   public void updateOneRowDoesntModifyOtherRows() {

      // Given many rows exist.
      addRow( PK_1 );
      addRow( PK_2 );

      // When the association id of one row is updated.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_1 );
      row.setDamagedComponentBool( true );
      dao.update( row );

      // Then the other rows are not affected.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_2, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      qs.next();
      assertThat( "Unexpectedly, the row got updated",
            qs.getBoolean( TaskRepRefTableRow.ColumnName.DAMAGED_COMPONENT_BOOL.toString() ),
            is( false ) );
   }


   /**
    * Verify deleting a row removes that row.
    */
   @Test
   public void deletingRowCausesItToBeRevomed() {

      // Given a row exists.
      addRow( PK_1 );

      // When deleting that row.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_1 );
      dao.delete( row );

      // Then the row is removed.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_1, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      assertThat( "Unexpectedly, a row was returned", lQs.getRowCount(), is( 0 ) );
   }


   /**
    * Verify deleting a non-existing row causes no effects.
    */
   @Test
   public void deletingNonExistingRowHasNoAffect() {

      // Given a row exists.
      addRow( PK_1 );
      addRow( PK_2 );

      // When deleting a non-existing row.
      TaskRepRefTableRow row = dao.findByPrimaryKey( PK_2 );
      dao.delete( row );

      // Then the existing row is unaffected.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_1, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      qs.next();

      assertThat( "Expected 1 row to be returned", qs.getRowCount(), is( 1 ) );
      assertThat( "Expected primary key 1234:1 to be returned",
            qs.getKey( TaskRepRefKey.class, TASK_DB_ID_COLUMN, TASK_ID_COLUMN ), is( PK_1 ) );

      lArgs.clear();
      lArgs.add( PK_2, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      qs = QuerySetFactory.getInstance().executeQueryTable( TaskRepRefTableRow.TABLE_NAME, lArgs );
      qs.next();

      assertThat( "Expected no rows to be returned", qs.getRowCount(), is( 0 ) );
   }


   private void addRow( TaskRepRefKey aPk ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPk, TASK_DB_ID_COLUMN, TASK_ID_COLUMN );
      MxDataAccess.getInstance().executeInsert( TaskTaskTable.TABLE_NAME, lArgs );
      MxDataAccess.getInstance().executeInsert( TaskRepRefTableRow.TABLE_NAME, lArgs );
   }

}
