package com.mxi.mx.core.table.utl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefQuickTextTypeKey;
import com.mxi.mx.core.key.UtlQuickTextKey;


public class UtlQuickTextDaoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final UtlQuickTextKey EXISTING_QUICKTEXT = new UtlQuickTextKey( "1:4650" );
   private static final UtlQuickTextKey NON_EXISTING_QUICKTEXT = new UtlQuickTextKey( "2:4650" );

   private UtlQuickTextDao iDao;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iDao = new JdbcUtlQuickTextDao();
   }


   @Test
   public void create_buildsEmptyTableRowObject() {
      assertNotNull( iDao.create() );
   }


   @Test
   public void create_nullBuildsEmptyTableRowObject() {
      assertNotNull( iDao.create( null ) );
   }


   @Test
   public void create_buildsTableRowFromExistingTableRowObject() {
      iDao.create( EXISTING_QUICKTEXT );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      iDao.delete( null );
   }


   @Test
   public void delete_removesRecord() {
      UtlQuickTextTableRow lExistingRow = iDao.findByPrimaryKey( EXISTING_QUICKTEXT );
      boolean lWasDeleted = iDao.delete( lExistingRow );

      assertTrue( lWasDeleted );
      assertFalse( lExistingRow.exists() );
   }


   @Test
   public void delete_returnsFalseWhenRecordDidNotExist() {
      UtlQuickTextTableRow lNonExistingRow = iDao.findByPrimaryKey( NON_EXISTING_QUICKTEXT );

      boolean lWasDeleted = iDao.delete( lNonExistingRow );

      assertFalse( lWasDeleted );
   }


   @Test
   public void findByPrimaryKey_null() {
      UtlQuickTextTableRow lRow = iDao.findByPrimaryKey( null );

      assertFalse( lRow.exists() );
   }


   @Test
   public void findByPrimaryKey_findsExistingRow() {
      UtlQuickTextTableRow lRow = iDao.findByPrimaryKey( EXISTING_QUICKTEXT );

      assertTrue( lRow.exists() );
      assertEquals( EXISTING_QUICKTEXT, lRow.getPk() );
      assertEquals( "TYPE_1", lRow.getType().getCd() );
      assertEquals( "A quick text.", lRow.getValue() );
   }


   @Test
   public void findByPrimaryKey_doesNotFindNonExistingRow() {
      UtlQuickTextTableRow lRow = iDao.findByPrimaryKey( NON_EXISTING_QUICKTEXT );

      assertFalse( lRow.exists() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      iDao.insert( null );
   }


   @Test
   public void insert_savesRecord() {
      UtlQuickTextTableRow lRow = iDao.create();
      {
         lRow.setType( new RefQuickTextTypeKey( "TYPE_1" ) );
         lRow.setValue( "Testing" );
      }

      UtlQuickTextKey lInsertedKey = iDao.insert( lRow );

      assertNotNull( lInsertedKey );
      assertTrue( lRow.exists() );
   }


   @Test( expected = MxRuntimeException.class )
   public void insert_nonUniqueRecord() {
      UtlQuickTextTableRow lRow = iDao.create( EXISTING_QUICKTEXT );
      {
         lRow.setType( new RefQuickTextTypeKey( "TYPE_1" ) );
         lRow.setValue( "Testing" );
      }
      iDao.insert( lRow );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      iDao.refresh( null );
   }


   @Test
   public void refresh_fetchesUpdatedRecordData() {
      UtlQuickTextTableRow lExisting = new UtlQuickTextTableRow( EXISTING_QUICKTEXT );
      DataSetArgument lSetArgs = new DataSetArgument();
      String lModifiedValue = "A changed value.";
      {
         lSetArgs.add( UtlQuickTextDao.ColumnName.VALUE_MDESC.name(), lModifiedValue );
      }
      DataSetArgument lWhereArgs = new DataSetArgument();
      {
         lWhereArgs.add( EXISTING_QUICKTEXT, UtlQuickTextDao.ColumnName.QUICKTEXT_ID.name(),
               UtlQuickTextDao.ColumnName.QUICKTEXT_DB_ID.name() );
      }
      MxDataAccess.getInstance().executeUpdate( UtlQuickTextTableRow.TABLE_NAME, lSetArgs,
            lWhereArgs );

      iDao.refresh( lExisting );

      assertEquals( lModifiedValue, lExisting.getValue() );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      iDao.update( null );
   }


   @Test
   public void update_rowIsUpdatedWithNewData() {
      UtlQuickTextTableRow lRow = new UtlQuickTextTableRow( EXISTING_QUICKTEXT );
      {
         lRow.setValue( "Updated value." );
      }

      iDao.update( lRow );

      UtlQuickTextTableRow lUpdatedRow = iDao.findByPrimaryKey( EXISTING_QUICKTEXT );
      assertEquals( "Updated value.", lUpdatedRow.getValue() );
   }

}
