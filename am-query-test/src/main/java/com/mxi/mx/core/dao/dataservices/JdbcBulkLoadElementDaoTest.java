
package com.mxi.mx.core.dao.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.BulkLoadElementKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.table.DataServices.BulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.BulkLoadElementTableRow;
import com.mxi.mx.core.table.DataServices.JdbcBulkLoadElementDao;


/*
 * @author indunilW
 *
 * @created January 24, 2019
 */
public class JdbcBulkLoadElementDaoTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private BulkLoadElementDao iBulkLoadElementDao = new JdbcBulkLoadElementDao();

   private BulkLoadElementKey iBulkLoadElementKey = new BulkLoadElementKey( 1234, 100, 1 );
   private RefBulkLoadFileActionKey iRefBulkLoadFileActionKey =
         new RefBulkLoadFileActionKey( 0, "SUPPLY_LOC_STOCK_LEVEL" );


   /**
    * GIVEN a row of data, WHEN the data is inserted into the BULK_LOAD_ELEMENT table using the dao,
    * THEN a corresponding record should be created in the table
    */
   @Test
   public void testCreateBulkLoadElementRecord() {
      BulkLoadElementTableRow lBulkLoadElementTableRow =
            iBulkLoadElementDao.create( iBulkLoadElementKey );
      lBulkLoadElementTableRow.setFileActionType( iRefBulkLoadFileActionKey );
      lBulkLoadElementTableRow.setStatus( RefBulkLoadStatusKey.QUEUED );
      lBulkLoadElementTableRow.setC0( "ATL/LINE" );
      lBulkLoadElementTableRow.setC1( "120" );
      BulkLoadElementKey lCreatedStagingRecordKey =
            iBulkLoadElementDao.insert( lBulkLoadElementTableRow );

      BulkLoadElementTableRow lFetchedRTableRow =
            iBulkLoadElementDao.findByPrimaryKey( lCreatedStagingRecordKey );
      assertEquals( iBulkLoadElementKey, lFetchedRTableRow.getBulkLoadElementKey() );
   }

}
