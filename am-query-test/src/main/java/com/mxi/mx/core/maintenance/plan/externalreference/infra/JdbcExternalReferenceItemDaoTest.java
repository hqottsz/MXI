package com.mxi.mx.core.maintenance.plan.externalreference.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.maintenance.plan.externalreference.infra.ExternalReferenceItemDao.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcExternalReferenceItemDaoTest {

   private static final ExternalReferenceItemKey NEW_KEY = new ExternalReferenceItemKey( "1:4650" );
   private static final ExternalReferenceItemKey EXISTING_KEY =
         new ExternalReferenceItemKey( "5:4650" );
   private static final String EXISTING_NAME = "IPC 123";
   private static final String NEW_NAME = "IPC 456";

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( ExternalReferenceItemDao.class ).to( JdbcExternalReferenceItemDao.class );
            }
         } );

   private ExternalReferenceItemDao externalReferenceItemDao;


   @Before
   public void setup() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      externalReferenceItemDao =
            InjectorContainer.get().getInstance( ExternalReferenceItemDao.class );
   }


   @Test
   public void create_default() {
      ExternalReferenceItemTableRow row = externalReferenceItemDao.create();

      assertNotNull( row );
      assertNull( row.getPk() );
   }


   @Test
   public void create_valid() {
      ExternalReferenceItemTableRow row = externalReferenceItemDao.create( NEW_KEY );
      assertEquals( NEW_KEY, row.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      ExternalReferenceItemTableRow row = externalReferenceItemDao.findByPrimaryKey( null );

      assertNull( row.getPk() );
   }


   @Test
   public void findByPrimaryKey_valid() {
      ExternalReferenceItemTableRow row = externalReferenceItemDao.findByPrimaryKey( EXISTING_KEY );
      assertNotNull( row );
      assertEquals( EXISTING_NAME, row.getReferenceItemName() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      externalReferenceItemDao.insert( null );
   }


   @Test
   public void insert_doesNotGenerateKey() {
      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( NEW_KEY );

      ExternalReferenceItemKey repairReferenceRequestKey = externalReferenceItemDao.insert( row );

      assertEquals( NEW_KEY, repairReferenceRequestKey );
      assertTrue( hasRecord( NEW_KEY ) );
   }


   @Test
   public void insert_generatesKey() {
      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( null );

      ExternalReferenceItemKey repairReferenceRequestKey = externalReferenceItemDao.insert( row );

      assertTrue( hasRecord( repairReferenceRequestKey ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      externalReferenceItemDao.update( null );
   }


   @Test
   public void update_valid() {
      ExternalReferenceItemTableRow oldTable =
            externalReferenceItemDao.findByPrimaryKey( EXISTING_KEY );

      assertEquals( EXISTING_NAME, oldTable.getReferenceItemName() );

      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( EXISTING_KEY );
      row.setReferenceItemName( NEW_NAME );

      externalReferenceItemDao.update( row );

      ExternalReferenceItemTableRow updatedTable =
            externalReferenceItemDao.findByPrimaryKey( EXISTING_KEY );

      assertEquals( NEW_NAME, updatedTable.getReferenceItemName() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      externalReferenceItemDao.refresh( null );
   }


   @Test
   public void refresh_valid() {
      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( EXISTING_KEY );

      assertEquals( EXISTING_NAME, row.getReferenceItemName() );

      DataSetArgument args = new DataSetArgument();
      args.add( ColumnName.REFERENCE_ITEM_NAME.name(), NEW_NAME );

      DataSetArgument whereArgs = new DataSetArgument();
      whereArgs.add( row.getPk(), ColumnName.EXT_REF_ITEM_ID.name(),
            ColumnName.EXT_REF_ITEM_DB_ID.name() );

      MxDataAccess.getInstance().executeUpdate( ExternalReferenceItemTableRow.TABLE_NAME, args,
            whereArgs );

      externalReferenceItemDao.refresh( row );

      assertEquals( NEW_NAME, row.getReferenceItemName() );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      externalReferenceItemDao.delete( null );
   }


   @Test
   public void delete_valid() {
      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( EXISTING_KEY );

      assertTrue( externalReferenceItemDao.delete( row ) );
      assertFalse( hasRecord( EXISTING_KEY ) );
   }


   @Test
   public void delete_invalid() {
      ExternalReferenceItemTableRow row = new ExternalReferenceItemTableRow( NEW_KEY );

      assertFalse( externalReferenceItemDao.delete( row ) );
   }


   @Test
   public void generatePrimaryKey_unique() {
      ExternalReferenceItemKey key1 = externalReferenceItemDao.generatePrimaryKey();
      ExternalReferenceItemKey key2 = externalReferenceItemDao.generatePrimaryKey();

      assertFalse( key1.getId() == key2.getId() );
   }


   private boolean hasRecord( ExternalReferenceItemKey aKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aKey, "ext_ref_item_id", "ext_ref_item_db_id" );

      QuerySet qs = QuerySetFactory.getInstance()
            .executeQueryTable( ExternalReferenceItemTableRow.TABLE_NAME, args );

      if ( qs.next() ) {
         return true;
      }

      return false;
   }

}
