
package com.mxi.mx.core.job.vendor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OrgVendorPoTypeKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.org.OrgVendor;
import com.mxi.mx.core.table.org.OrgVendorPoTypeTable;


/**
 * Tests the {@link UpdateVendorStatusCoreService} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateVendorStatusCoreServiceTest {

   private static final String CORE_MSG_VENDOR_CERT_EXPIRY_AND_APPROVAL_EXPIRY_DT =
         "core.msg.VENDOR_CERT_EXPIRY_AND_APPROVAL_EXPIRY_DT";

   private static final String CORE_MSG_APPROVAL_EXPIRY_DT = "core.msg.APPROVAL_EXPIRY_DT";

   private static final String CORE_MSG_VENDOR_CERT_EXPIRY = "core.msg.VENDOR_CERT_EXPIRY";

   private static final int EXPIRED_DATE = 1969;

   private static final int VALID_DATE = 2099;

   private OrgKey iOrgKey;

   private LocationKey iLocationKey;

   private VendorKey iVendorKey;

   private OrgVendorPoTypeKey iOrgVendorPoTypeKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that when certificate expiry date is expired, even through the approval expiry date is
    * still valid, unapproveExpiredVendors() method will update the vendor's status to UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidButCertExpiryDtExpired()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      setCertExpiryDt( EXPIRED_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the certificate date is expired, the vendor's status should be updated to
      // UNAPPROVED, and "This vendor was unapproved due to exipry of the vendor certificate" note
      // was generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_VENDOR_CERT_EXPIRY ), getUserStageNote() );

   }


   /**
    * Verify that when certificate expiry date is expired, and the approval expiry date is also
    * expired, unapproveExpiredVendors() method will update the vendor's status to UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredAndCertExpiryDtExpired()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      setCertExpiryDt( EXPIRED_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since both the certificate date and the approval date are expired, the vendor's
      // status should be updated to UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the vendor certificate and organization
      // approval date"
      // note was generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_VENDOR_CERT_EXPIRY_AND_APPROVAL_EXPIRY_DT ),
            getUserStageNote() );

   }


   /**
    * Verify that when certificate expiry date is valid, and the approval expiry date is also valid,
    * unapproveExpiredVendors() method will not change the vendor's status
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidAndCertExpiryDtValid()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      setCertExpiryDt( VALID_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since both the certificate date and the approval date are valid, after the vendor's
      // status was not changed and no note generated
      assertEquals( RefVendorStatusKey.APPROVED, lOrgVendorPoType.getVendorStatus() );
      assertNull( getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is expired, and the certificate expiry date is still
    * valid, unapproveExpiredVendors() method will update the vendor's status to UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredAndCertExpiryDtValid()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      setCertExpiryDt( VALID_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the certificate date is valid but the approval date is expired, the vendor's
      // status should be updated to UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the organization approval date" note was
      // generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_APPROVAL_EXPIRY_DT ), getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is expired, and leave the approval expiry date blank,
    * unapproveExpiredVendors() method will update the vendor's status to UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredAndCertExpiryDtBlank()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the approval date is expired, the vendor's status should be updated to
      // UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the organization approval date" note was
      // generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_APPROVAL_EXPIRY_DT ), getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is valid, and leave the approval expiry date blank,
    * unapproveExpiredVendors() method will not update the vendor's status
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidAndCertExpiryDtBlank()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.APPROVED );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the approval date is still valid, the vendor's status should not be changed
      // and no note generated
      assertEquals( RefVendorStatusKey.APPROVED, lOrgVendorPoType.getVendorStatus() );
      assertNull( getUserStageNote() );

   }


   /**
    * Verify that when certificate expiry date is expired, even through the approval expiry date is
    * still valid with Warning, unapproveExpiredVendors() method will update the vendor's status to
    * UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidWithWarningButCertExpiryDtExpired()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      setCertExpiryDt( EXPIRED_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the certificate date is expired, the vendor's status should be updated to
      // UNAPPROVED, and "This vendor was unapproved due to exipry of the vendor certificate" note
      // was generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_VENDOR_CERT_EXPIRY ), getUserStageNote() );

   }


   /**
    * Verify that when certificate expiry date is expired, and the approval expiry date is also
    * expired with Warning, unapproveExpiredVendors() method will update the vendor's status to
    * UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredWithWarningAndCertExpiryDtExpired()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      setCertExpiryDt( EXPIRED_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since both the certificate date and the approval date are expired, the vendor's
      // status should be updated to UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the vendor certificate and organization
      // approval date"
      // note was generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_VENDOR_CERT_EXPIRY_AND_APPROVAL_EXPIRY_DT ),
            getUserStageNote() );

   }


   /**
    * Verify that when certificate expiry date is valid, and the approval expiry date is also valid
    * with Warning, unapproveExpiredVendors() method will not change the vendor's status
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidWithWarningAndCertExpiryDtValid()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      setCertExpiryDt( VALID_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since both the certificate date and the approval date are valid, after the vendor's
      // status was not changed and no note generated
      assertEquals( RefVendorStatusKey.WARNING, lOrgVendorPoType.getVendorStatus() );
      assertNull( getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is expired, and the certificate expiry date is still
    * valid with Warning, unapproveExpiredVendors() method will update the vendor's status to
    * UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredWithWarningAndCertExpiryDtValid()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      setCertExpiryDt( VALID_DATE, iVendorKey );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the certificate date is valid but the approval date is expired, the vendor's
      // status should be updated to UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the organization approval date" note was
      // generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_APPROVAL_EXPIRY_DT ), getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is expired with Warning, and leave the approval expiry
    * date blank, unapproveExpiredVendors() method will update the vendor's status to UNAPPROVED
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtExpiredWithWarningAndCertExpiryDtBlank()
         throws Exception {

      setApprovalExpiryDt( EXPIRED_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the approval date is expired, the vendor's status should be updated to
      // UNAPPROVED, and
      // "This vendor was unapproved due to exipry of the organization approval date" note was
      // generated
      assertEquals( RefVendorStatusKey.UNAPPROVED, lOrgVendorPoType.getVendorStatus() );
      assertEquals( i18n.get( CORE_MSG_APPROVAL_EXPIRY_DT ), getUserStageNote() );

   }


   /**
    * Verify that when approval expiry date is valid with Warning, and leave the approval expiry
    * date blank, unapproveExpiredVendors() method will not update the vendor's status
    *
    * @throws Exception
    */
   @Test
   public void testUpdateVendorStatusWhenApprovalExpiryDtValidWithWarningAndCertExpiryDtBlank()
         throws Exception {

      setApprovalExpiryDt( VALID_DATE, iOrgKey, iVendorKey, iOrgVendorPoTypeKey,
            RefVendorStatusKey.WARNING );

      new UpdateVendorStatusCoreService().unapproveExpiredVendors();

      OrgVendorPoTypeTable lOrgVendorPoType =
            OrgVendorPoTypeTable.findByPrimaryKey( iOrgVendorPoTypeKey );

      // ASSERT: Since the approval date is still valid, the vendor's status should not be changed
      // and no note generated
      assertEquals( RefVendorStatusKey.WARNING, lOrgVendorPoType.getVendorStatus() );
      assertNull( getUserStageNote() );

   }


   @Before
   public void loadData() throws Exception {
      // Create an organization key
      iOrgKey = new OrganizationDomainBuilder().withCode( "TESTORG" )
            .withDescription( "TESTORGDESCRIPTION" ).build();

      // Create a vendor location key
      iLocationKey =
            new LocationDomainBuilder().withCode( "TESTVENLOC" ).withType( RefLocTypeKey.VENDOR ).build();

      // Create a vendor key
      iVendorKey = new VendorBuilder().withCode( "TESTVEND" ).atLocation( iLocationKey ).build();

      // Create an org_vendor_po_type key
      iOrgVendorPoTypeKey =
            new OrgVendorPoTypeKey( iOrgKey, iVendorKey, new RefPoTypeKey( 0, "PURCHASE" ) );

   }


   /**
    * DATA SETUP: Organization approves the vendor with an approval expiry date
    *
    * @param aApprovalExpiryDtYear
    *           the approval expiry date
    * @param aOrgKey
    *           the organization key
    * @param aVendorKey
    *           the vendor key
    * @param aOrgVendorPoTypeKey
    *           the org_vendor_po_type table key
    * @param aRefVendorStatusKey
    *           the ref vendor status key
    *
    */
   private void setApprovalExpiryDt( Integer aApprovalExpiryDtYear, OrgKey aOrgKey,
         VendorKey aVendorKey, OrgVendorPoTypeKey aOrgVendorPoTypeKey,
         RefVendorStatusKey aRefVendorStatusKey ) {

      // Approval expiry date
      Calendar lApprovalExpiryDt = GregorianCalendar.getInstance();
      lApprovalExpiryDt.set( Calendar.YEAR, aApprovalExpiryDtYear );

      // Organization approves the vendor's order type with an approval expiry date
      OrgVendorPoTypeTable lOrgVendorPoType = OrgVendorPoTypeTable.create( aOrgVendorPoTypeKey );
      lOrgVendorPoType.setVendorStatus( aRefVendorStatusKey );
      lOrgVendorPoType.setApprovalExpiryDt( lApprovalExpiryDt.getTime() );
      lOrgVendorPoType.insert();
   }


   /**
    * DATA SETUP: set the vendor's certificate expiry date
    *
    * @param aCertExpiryDtYear
    *           the certificate expiry date
    * @param aVendorKey
    *           the vendor key
    */
   private void setCertExpiryDt( Integer aCertExpiryDtYear, VendorKey aVendorKey ) {

      // Certificate expiry date
      Calendar lCertExpiryDt = GregorianCalendar.getInstance();
      lCertExpiryDt.set( Calendar.YEAR, aCertExpiryDtYear );

      // Set the vendor's certificate expiry date
      OrgVendor lOrgVendor = OrgVendor.findByPrimaryKey( aVendorKey );
      lOrgVendor.setCertExpiry( lCertExpiryDt.getTime() );
      lOrgVendor.update();
   }


   /**
    * Get user_stage_note from evt_stage table
    *
    * @return the user stage note
    */
   private String getUserStageNote() {

      // get evt_vendor table data by using vendor key
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "vendor_db_id", iVendorKey.getDbId() );
      lArgs.add( "vendor_id", iVendorKey.getId() );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "evt_vendor", lArgs );

      StageKey lStageKey = null;
      String lUserStageNote = null;
      if ( lQs.next() ) {

         // get event key from evt_vendor table
         Integer lEvtDbId = lQs.getInt( "event_db_id" );
         Integer lEvtId = lQs.getInt( "event_id" );
         EventKey lEventKey = new EventKey( lEvtDbId, lEvtId );

         // generate stage key by using event key
         lStageKey = new StageKey( lEventKey, 1 );

         // get user_stage_note from evt_stage table by using stage key
         EvtStageTable lEvtStage = EvtStageTable.findByPrimaryKey( lStageKey );
         lUserStageNote = lEvtStage.getStageNote();
      }
      return lUserStageNote;
   }

}
