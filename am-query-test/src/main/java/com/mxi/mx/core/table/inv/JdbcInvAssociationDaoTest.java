package com.mxi.mx.core.table.inv;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetException;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Integration unit test for {@link JdbcInvAssociationDao}
 *
 */
public class JdbcInvAssociationDaoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   private static final InventoryKey INV_1 = new InventoryKey( 1, 1 );
   private static final InventoryKey INV_2 = new InventoryKey( 1, 2 );
   private static final InventoryKey INV_3 = new InventoryKey( 1, 3 );

   private static final Integer ASSOCIATED_ID_1 = 1;
   private static final Integer ASSOCIATED_ID_2 = 2;

   private static final InvAssociationKey PK_1 = new InvAssociationKey( INV_1 );
   private static final InvAssociationKey PK_2 = new InvAssociationKey( INV_2 );
   private static final InvAssociationKey PK_3 = new InvAssociationKey( INV_3 );

   private static final String INV_NO_DB_ID_COLUMN =
         InvAssociationTableRow.ColumnName.INV_NO_DB_ID.toString();
   private static final String INV_NO_ID_COLUMN =
         InvAssociationTableRow.ColumnName.INV_NO_ID.toString();
   private static final String ASSOCIATION_ID_COLUMN =
         InvAssociationTableRow.ColumnName.ASSOCIATION_ID.toString();

   // Unit under test.
   private JdbcInvAssociationDao iDao;


   @Before
   public void before() {
      iDao = new JdbcInvAssociationDao();
   }


   /**
    *
    * Verify inserting a single row is persisted.
    *
    */
   @Test
   public void insertSingleRowIsPersisted() {

      // When inserting single row.
      InvAssociationTableRow lRow = iDao.create( PK_1 );
      lRow.setAssociationId( ASSOCIATED_ID_1 );
      iDao.insert( lRow );

      // Then a row is persisted.
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, null );
      lQs.next();

      assertThat( lQs.getRowCount(), is( 1 ) );
      assertThat( lQs.getKey( InvAssociationKey.class, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN ),
            is( PK_1 ) );
      assertThat( lQs.getInteger( ASSOCIATION_ID_COLUMN ), is( ASSOCIATED_ID_1 ) );
   }


   /**
    *
    * Verify inserting multiple rows with same association id are persisted.
    *
    */
   @Test
   public void insertMultipleRowsArePersisted() {

      // When multiple rows are inserted with the same association id.
      InvAssociationTableRow lRow = iDao.create( PK_1 );
      lRow.setAssociationId( ASSOCIATED_ID_1 );
      iDao.insert( lRow );

      lRow = iDao.create( PK_2 );
      lRow.setAssociationId( ASSOCIATED_ID_1 );
      iDao.insert( lRow );

      // Then the rows are persisted.
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, null );

      List<InvAssociationKey> lPks = new ArrayList<>();
      List<Integer> lAssociationIds = new ArrayList<>();

      while ( lQs.next() ) {
         lPks.add( lQs.getKey( InvAssociationKey.class, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN ) );
         lAssociationIds.add( lQs.getInteger( ASSOCIATION_ID_COLUMN ) );
      }

      assertThat( lPks.size(), is( 2 ) );
      assertThat( lPks, hasItem( is( PK_1 ) ) );
      assertThat( lPks, hasItem( is( PK_2 ) ) );
      assertThat( lAssociationIds, everyItem( is( ASSOCIATED_ID_1 ) ) );
   }


   /**
    *
    * Verify inserting row when PK already exists throws an exception.
    *
    */
   @Test
   public void insertRowWhenPkAlreadyExists() {

      // Set up for the "Then" below.
      iExpectedException.expect( MxRuntimeException.class );
      iExpectedException.expectCause( Matchers.isA( DataSetException.class ) );

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When inserting a row with the same PK.
      InvAssociationTableRow lRow = iDao.create( PK_1 );
      lRow.setAssociationId( ASSOCIATED_ID_2 );
      iDao.insert( lRow );

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
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When finding a row by primary key.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_1 );

      // Then the row is retrieved.
      assertNotNull( lRow );
      assertThat( lRow.getPk(), is( PK_1 ) );
      assertThat( lRow.getAssociationId(), is( ASSOCIATED_ID_1 ) );
   }


   /**
    *
    * Verify finding a row by non-existing primary key retrieves no rows.
    *
    */
   @Test
   public void findByPrimaryKeyDoesNotReturnRowWhenNotExist() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When finding a row by non-existing primary key.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_2 );

      // Then no rows are retrieved.
      assertNotNull( lRow );
      assertThat( lRow.exists(), is( false ) );
   }


   /**
    *
    * Verify finding rows by existing association id retrieves the row.
    *
    */
   @Test
   public void findByAssociationIdReturnsRowWhenExists() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When finding rows by association id.
      List<InvAssociationTableRow> lRows = iDao.findByAssociation( ASSOCIATED_ID_1 );

      // Then the row is retrieved.
      assertNotNull( lRows );
      assertThat( lRows.size(), is( 1 ) );
      assertThat( lRows.get( 0 ).getPk(), is( PK_1 ) );
      assertThat( lRows.get( 0 ).getAssociationId(), is( ASSOCIATED_ID_1 ) );
   }


   /**
    *
    * Verify finding rows by non-existing association id retrieves no rows.
    *
    */
   @Test
   public void findByAssociationIdDoesNotReturnRowWhenNotExist() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When finding rows by non-existing association id.
      List<InvAssociationTableRow> lRows = iDao.findByAssociation( ASSOCIATED_ID_2 );

      // Then no rows are retrieved.
      assertNotNull( lRows );
      assertThat( lRows.size(), is( 0 ) );
   }


   /**
    *
    * Verify finding rows by existing association id retrieves all rows when more than one row with
    * same association id.
    *
    */
   @Test
   public void findByAssociationIdReturnsAllRowWithSameAssociationId() {

      // Given many rows with same association id.
      addRow( PK_1, ASSOCIATED_ID_1 );
      addRow( PK_2, ASSOCIATED_ID_1 );

      // Given another row with a different association id.
      addRow( PK_3, ASSOCIATED_ID_2 );

      // When finding rows by association id.
      List<InvAssociationTableRow> lRows = iDao.findByAssociation( ASSOCIATED_ID_1 );

      // Then all the rows with that association id are retrieved.
      List<InvAssociationKey> lPks = new ArrayList<>();
      List<Integer> lAssociationIds = new ArrayList<>();

      lRows.forEach( aRow -> lPks.add( aRow.getPk() ) );
      lRows.forEach( aRow -> lAssociationIds.add( aRow.getAssociationId() ) );

      assertNotNull( lRows );
      assertThat( lRows.size(), is( 2 ) );
      assertThat( lPks, hasItem( PK_1 ) );
      assertThat( lPks, hasItem( PK_2 ) );
      assertThat( lAssociationIds, everyItem( is( ASSOCIATED_ID_1 ) ) );
   }


   /**
    *
    * Verify updating a row is persisted.
    *
    */
   @Test
   public void updateRowIsPersisted() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When the association id of the row is updated.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_1 );
      lRow.setAssociationId( ASSOCIATED_ID_2 );
      iDao.update( lRow );

      // Then the update is persisted.
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, null );
      lQs.next();
      assertThat( lQs.getRowCount(), is( 1 ) );
      assertThat( lQs.getKey( InvAssociationKey.class, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN ),
            is( PK_1 ) );
      assertThat( lQs.getInteger( ASSOCIATION_ID_COLUMN ), is( ASSOCIATED_ID_2 ) );
   }


   /**
    *
    * Verify updating one row does not affect other rows.
    *
    */
   @Test
   public void updateOneRowOnlyModifiesThatOneRow() {

      // Given many rows exist.
      addRow( PK_1, ASSOCIATED_ID_1 );
      addRow( PK_2, ASSOCIATED_ID_1 );

      // When the association id of one row is updated.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_1 );
      lRow.setAssociationId( ASSOCIATED_ID_2 );
      iDao.update( lRow );

      // Then the other rows are not affected.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_2, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, lArgs );
      lQs.next();
      assertThat( lQs.getInteger( ASSOCIATION_ID_COLUMN ), is( ASSOCIATED_ID_1 ) );
   }


   /**
    * Verify deleting a row removes that row.
    */
   @Test
   public void deletingRowCausesItToBeRevomed() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When deleting that row.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_1 );
      iDao.delete( lRow );

      // Then the row is removed.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PK_1, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, lArgs );
      assertThat( lQs.getRowCount(), is( 0 ) );
   }


   /**
    * Verify deleting a non-existing row causes no effects.
    */
   @Test
   public void deletingNonExistingRowHasNoAffect() {

      // Given a row exists.
      addRow( PK_1, ASSOCIATED_ID_1 );

      // When deleting a non-existing row.
      InvAssociationTableRow lRow = iDao.findByPrimaryKey( PK_2 );
      iDao.delete( lRow );

      // Then the existing row is unaffected.
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvAssociationTableRow.TABLE_NAME, null );
      lQs.next();

      assertThat( lQs.getRowCount(), is( 1 ) );
      assertThat( lQs.getKey( InvAssociationKey.class, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN ),
            is( PK_1 ) );
      assertThat( lQs.getInteger( ASSOCIATION_ID_COLUMN ), is( ASSOCIATED_ID_1 ) );

   }


   private void addRow( InvAssociationKey aPk, Integer aAssociatedId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPk, INV_NO_DB_ID_COLUMN, INV_NO_ID_COLUMN );
      lArgs.add( ASSOCIATION_ID_COLUMN, aAssociatedId );
      MxDataAccess.getInstance().executeInsert( InvAssociationTableRow.TABLE_NAME, lArgs );
   }

}
