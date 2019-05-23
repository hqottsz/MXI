package com.mxi.mx.core.maintenance.exec.fault.infra;

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

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class FaultReferenceDaoTest {

   private static final FaultReferenceKey NEW_REFERENCE_KEY = new FaultReferenceKey( "1:4650" );
   private static final FaultReferenceKey EXISTING_REFERENCE_KEY =
         new FaultReferenceKey( "5:4650" );
   private static final FaultKey NEW_FAULT = new FaultKey( "4650:200" );
   private static final FaultKey EXISTING_FAULT = new FaultKey( "4650:300" );

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private FaultReferenceDao referenceDao;


   @Before
   public void setup() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      referenceDao = InjectorContainer.get().getInstance( FaultReferenceDao.class );
   }


   @Test
   public void create_default() {
      FaultReferenceTableRow row = referenceDao.create();

      assertNotNull( row );
      assertNull( row.getPk() );
   }


   @Test
   public void create_valid() {
      FaultReferenceTableRow row = referenceDao.create( NEW_REFERENCE_KEY );
      assertEquals( NEW_REFERENCE_KEY, row.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      FaultReferenceTableRow row = referenceDao.findByPrimaryKey( null );

      assertNull( row.getPk() );
   }


   @Test
   public void findByPrimaryKey_valid() {
      FaultReferenceTableRow row = referenceDao.findByPrimaryKey( EXISTING_REFERENCE_KEY );
      assertNotNull( row );
      assertEquals( EXISTING_FAULT, row.getFaultKey() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      referenceDao.insert( null );
   }


   @Test
   public void insert_doesNotGenerateKey() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( NEW_REFERENCE_KEY );

      FaultReferenceKey repairReferenceKey = referenceDao.insert( row );

      assertEquals( NEW_REFERENCE_KEY, repairReferenceKey );
      assertTrue( hasRecord( NEW_REFERENCE_KEY ) );
   }


   @Test
   public void insert_generatesKey() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( null );

      FaultReferenceKey repairReferenceKey = referenceDao.insert( row );

      assertTrue( hasRecord( repairReferenceKey ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      referenceDao.update( null );
   }


   @Test
   public void update_valid() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( EXISTING_REFERENCE_KEY );
      row.setFaultKey( NEW_FAULT );

      referenceDao.update( row );

      FaultReferenceTableRow retrievedTable =
            referenceDao.findByPrimaryKey( EXISTING_REFERENCE_KEY );

      assertEquals( NEW_FAULT, retrievedTable.getFaultKey() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      referenceDao.refresh( null );
   }


   @Test
   public void refresh_valid() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( EXISTING_REFERENCE_KEY );

      assertEquals( EXISTING_FAULT, row.getFaultKey() );

      DataSetArgument args = new DataSetArgument();
      args.add( NEW_FAULT, ColumnName.FAULT_DB_ID.name(), ColumnName.FAULT_ID.name() );

      DataSetArgument whereArgs = new DataSetArgument();
      whereArgs.add( row.getPk(), ColumnName.FAULT_REF_ID.name(),
            ColumnName.FAULT_REF_DB_ID.name() );

      MxDataAccess.getInstance().executeUpdate( FaultReferenceTableRow.TABLE_NAME, args,
            whereArgs );

      referenceDao.refresh( row );

      assertEquals( NEW_FAULT, row.getFaultKey() );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      referenceDao.delete( null );
   }


   @Test
   public void delete_valid() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( EXISTING_REFERENCE_KEY );

      assertTrue( referenceDao.delete( row ) );
      assertFalse( hasRecord( EXISTING_REFERENCE_KEY ) );
   }


   @Test
   public void delete_invalid() {
      FaultReferenceTableRow row = new FaultReferenceTableRow( NEW_REFERENCE_KEY );

      assertFalse( referenceDao.delete( row ) );
   }


   @Test
   public void generatePrimaryKey_unique() {
      FaultReferenceKey key1 = referenceDao.generatePrimaryKey();
      FaultReferenceKey key2 = referenceDao.generatePrimaryKey();

      assertFalse( key1.getKeyId() == key2.getKeyId() );
   }


   private boolean hasRecord( FaultReferenceKey aKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aKey, "fault_ref_id", "fault_ref_db_id" );

      QuerySet qs = QuerySetFactory.getInstance()
            .executeQueryTable( FaultReferenceTableRow.TABLE_NAME, args );

      if ( qs.next() ) {
         return true;
      }

      return false;
   }

}
