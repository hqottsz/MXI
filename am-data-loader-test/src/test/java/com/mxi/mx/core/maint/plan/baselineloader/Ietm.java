package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on ietm_import.
 *
 * @author ALICIA QIAN
 */
public class Ietm extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iIETM_CD_1 = "AIETM";
   public String iIETM_NAME_1 = "AIETMNAME";
   public String iIETM_LDESC_1 = "AIETMDESC";
   public String iIETM_PREFIX_LDESC_1 = "A";
   public String iIETM_ASSEMBLY_CD_LIST_1 = "ACFT_T1,COMHW,TSE,APU_CD1";

   public String iIETM_CD_2 = "BIETM";
   public String iIETM_NAME_2 = "BIETMNAME";
   public String iIETM_LDESC_2 = "BIETMDESC";
   public String iIETM_PREFIX_LDESC_2 = "B";
   public String iIETM_ASSEMBLY_CD_LIST_2 = "ACFT_T1,COMHW,TSE,APU_CD1";

   public String iATTACH_TOPIC_SDESC_1 = "AIETMTOPICATT";
   public String iATTACH_OPERATOR_CD_LIST_1 = "ATLD,MXI";
   public String iATTACH_PRINT_BOOL_1 = "N";
   public String iATTACH_DESC_LDESC_1 = "AIETMTOPICATTLDESC";
   public String iATTACH_TOPIC_NOTE_1 = "AIETMTOPICATTNOTE";
   public String iATTACH_ATTACH_TYPE_CD_1 = "CONTRACT";
   public String iATTACH_ATTACH_BLOB_1 = "CONTRACTBLOB";
   public String iATTACH_ATTACH_FILENAME_1 = "ATTFILE1";
   public String iATTACH_ATTACH_CONTENT_TYPE_1 = "ATTCONTENT1";

   public String iATTACH_TOPIC_SDESC_2 = "AIETMTOPICATT2";
   public String iATTACH_OPERATOR_CD_LIST_2 = "ATLD,MXI";
   public String iATTACH_PRINT_BOOL_2 = "N";
   public String iATTACH_DESC_LDESC_2 = "AIETMTOPICATTLDESC2";
   public String iATTACH_TOPIC_NOTE_2 = "AIETMTOPICATTNOTE2";
   public String iATTACH_ATTACH_TYPE_CD_2 = "CONTRACT";
   public String iATTACH_ATTACH_BLOB_2 = "CONTRACTBLOB";
   public String iATTACH_ATTACH_FILENAME_2 = "ATTFILE2";
   public String iATTACH_ATTACH_CONTENT_TYPE_2 = "ATTCONTENT2";

   public String iTECH_TOPIC_SDESC_1 = "AIETMTECH";
   public String iTECH_OPERATOR_CD_LIST_1 = "ATLD,MXI";
   public String iTECH_CMDLINE_PARM_LDESC_1 = "AIETMCMD";
   public String iTECH_PRINT_BOOL_1 = "Y";
   public String iTECH_DESC_LDESC_1 = "AIETMTECHLDESC";
   public String iTECH_TOPIC_NOTE_1 = "AIETMTECHNOTE";
   public String iTECH_IETM_TYPE_CD_1 = "ATIETM1";
   public String iTECH_TASKDEFN_CXT_LDESC_1 = "defncxt";
   public String iTECH_TASK_CXT_LDESC_1 = "taskcxn";
   public String iTECH_APPLICABILITY_RANGE_1 = "1-10,20";

   public String iTECH_TOPIC_SDESC_2 = "AIETMTECH2";
   public String iTECH_OPERATOR_CD_LIST_2 = "ATLD,MXI";
   public String iTECH_CMDLINE_PARM_LDESC_2 = "AIETMCMD2";
   public String iTECH_PRINT_BOOL_2 = "Y";
   public String iTECH_DESC_LDESC_2 = "AIETMTECHLDESC2";
   public String iTECH_TOPIC_NOTE_2 = "AIETMTECHNOTE2";
   public String iTECH_IETM_TYPE_CD_2 = "ATIETM1";
   public String iTECH_TASKDEFN_CXT_LDESC_2 = "defncxt2";
   public String iTECH_TASK_CXT_LDESC_2 = "taskcxn2";
   public String iTECH_APPLICABILITY_RANGE_2 = "10,20";

   public simpleIDs iIETM_ID_1 = null;
   public simpleIDs iIETM_ID_2 = null;


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

   }


   /**
    * This test is to verify validation functionality on ietm_import with one item, one attachment,
    * and one topic.
    *
    */
   @Test
   public void testSingleIetmTopicAtt_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1, null );

      // C_IETM_TOPIC_TECH_REF table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iTECH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iTECH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on ietm_import with one item, one attachment, and
    * one topic.
    *
    */
   @Test
   public void testSingleIetmTopicAtt_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSingleIetmTopicAtt_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iIETM_ID_1 = null;
      iIETM_ID_2 = null;

      // Verify item_ietm table
      iIETM_ID_1 = getIetmIds( iIETM_CD_1 );
      veryIetmIetm( iIETM_ID_1, iIETM_NAME_1, iIETM_LDESC_1, iIETM_PREFIX_LDESC_1 );

      // verify ietm_topic (attach)
      String lietmtopicID = getIetmTopicIds( iIETM_ID_1, iATTACH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, null, "0", null, iATTACH_DESC_LDESC_1,
            iATTACH_TOPIC_NOTE_1, iATTACH_ATTACH_TYPE_CD_1, iATTACH_ATTACH_FILENAME_1,
            iATTACH_ATTACH_CONTENT_TYPE_1, null, null, null );

      String temp = getBlobvalue( TableUtil.IETM_TOPIC, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1,
            "where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID()
                  + " and IETM_TOPIC_ID=" + lietmtopicID );

      Assert.assertTrue( "Check BLOB value.", temp.contains( iATTACH_ATTACH_BLOB_1 ) );

      // verify ietm_topic (topic)
      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iTECH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, iTECH_CMDLINE_PARM_LDESC_1, "1",
            iTECH_IETM_TYPE_CD_1, iTECH_DESC_LDESC_1, iTECH_TOPIC_NOTE_1, null, null, null,
            iTECH_TASKDEFN_CXT_LDESC_1, iTECH_TASK_CXT_LDESC_1, iTECH_APPLICABILITY_RANGE_1 );

      // Verify ietm_assmbl
      // check ACFT_CD1
      String lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='ACFT_T1'";
      Assert.assertTrue( "Check ACFT_CD1 is in the list.", RecordsExist( lcheck ) == true );

      // check COMHW
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='COMHW'";
      Assert.assertTrue( "Check COMHW is in the list.", RecordsExist( lcheck ) == true );

      // check TSE
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='TSE'";
      Assert.assertTrue( "Check TSE is in the list.", RecordsExist( lcheck ) == true );

      // check APU_CD1
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='APU_CD1'";
      Assert.assertTrue( "Check APU_CD1 is in the list.", RecordsExist( lcheck ) == true );

   }


   /**
    * This test is to verify validation functionality on ietm_import with 2 item, 2 attachment, and
    * one topic.
    *
    */
   @Test
   public void testSingleIetmMultipleTopicAtt_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      // first record
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );

      // second record
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_2 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_2 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_2 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_2 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_2 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_2 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_2 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1,
            " where TOPIC_SDESC='" + iATTACH_TOPIC_SDESC_1 + "'" );

      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_2,
            " where TOPIC_SDESC='" + iATTACH_TOPIC_SDESC_2 + "'" );

      // C_IETM_TOPIC_TECH_REF table
      // first record
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iTECH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iTECH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // second record
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iTECH_TOPIC_SDESC_2 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iTECH_OPERATOR_CD_LIST_2 + "\'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_2 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_2 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_2 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_2 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_2 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_2 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_2 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on ietm_import with one item, 2 attachment, and 2
    * topic.
    *
    */
   @Test
   public void testSingleIetmMultipleTopicAtt_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSingleIetmMultipleTopicAtt_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iIETM_ID_1 = null;
      iIETM_ID_2 = null;

      // Verify item_ietm table
      iIETM_ID_1 = getIetmIds( iIETM_CD_1 );
      veryIetmIetm( iIETM_ID_1, iIETM_NAME_1, iIETM_LDESC_1, iIETM_PREFIX_LDESC_1 );

      // verify ietm_topic (attach)
      String lietmtopicID = getIetmTopicIds( iIETM_ID_1, iATTACH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, null, "0", null, iATTACH_DESC_LDESC_1,
            iATTACH_TOPIC_NOTE_1, iATTACH_ATTACH_TYPE_CD_1, iATTACH_ATTACH_FILENAME_1,
            iATTACH_ATTACH_CONTENT_TYPE_1, null, null, null );

      String temp = getBlobvalue( TableUtil.IETM_TOPIC, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1,
            "where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID()
                  + " and IETM_TOPIC_ID=" + lietmtopicID );

      Assert.assertTrue( "Check BLOB value.", temp.contains( iATTACH_ATTACH_BLOB_1 ) );

      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iATTACH_TOPIC_SDESC_2 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, null, "0", null, iATTACH_DESC_LDESC_2,
            iATTACH_TOPIC_NOTE_2, iATTACH_ATTACH_TYPE_CD_2, iATTACH_ATTACH_FILENAME_2,
            iATTACH_ATTACH_CONTENT_TYPE_2, null, null, null );

      temp = getBlobvalue( TableUtil.IETM_TOPIC, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_2,
            "where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID()
                  + " and IETM_TOPIC_ID=" + lietmtopicID );

      Assert.assertTrue( "Check BLOB value.", temp.contains( iATTACH_ATTACH_BLOB_2 ) );

      // verify ietm_topic (topic)
      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iTECH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, iTECH_CMDLINE_PARM_LDESC_1, "1",
            iTECH_IETM_TYPE_CD_1, iTECH_DESC_LDESC_1, iTECH_TOPIC_NOTE_1, null, null, null,
            iTECH_TASKDEFN_CXT_LDESC_1, iTECH_TASK_CXT_LDESC_1, iTECH_APPLICABILITY_RANGE_1 );

      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iTECH_TOPIC_SDESC_2 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, iTECH_CMDLINE_PARM_LDESC_2, "1",
            iTECH_IETM_TYPE_CD_2, iTECH_DESC_LDESC_2, iTECH_TOPIC_NOTE_2, null, null, null,
            iTECH_TASKDEFN_CXT_LDESC_2, iTECH_TASK_CXT_LDESC_2, iTECH_APPLICABILITY_RANGE_2 );

      // Verify ietm_assmbl
      // check ACFT_CD1
      String lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='ACFT_T1'";
      Assert.assertTrue( "Check ACFT_CD1 is in the list.", RecordsExist( lcheck ) == true );

      // check COMHW
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='COMHW'";
      Assert.assertTrue( "Check COMHW is in the list.", RecordsExist( lcheck ) == true );

      // check TSE
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='TSE'";
      Assert.assertTrue( "Check TSE is in the list.", RecordsExist( lcheck ) == true );

      // check APU_CD1
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='APU_CD1'";
      Assert.assertTrue( "Check APU_CD1 is in the list.", RecordsExist( lcheck ) == true );

   }


   /**
    * This test is to verify validation functionality on ietm_import with 2 items, one attachment,
    * and one topic.
    *
    */
   @Test
   public void testMultipleIetm_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      // first record
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // second
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_2 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_2 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_2 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_2 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1, null );

      // C_IETM_TOPIC_TECH_REF table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iTECH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iTECH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on ietm_import with 2 items, one attachment, and
    * one topic.
    *
    */
   @Test
   public void testMultipleIetm_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultipleIetm_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iIETM_ID_1 = null;
      iIETM_ID_2 = null;

      // Verify item_ietm table
      iIETM_ID_1 = getIetmIds( iIETM_CD_1 );
      veryIetmIetm( iIETM_ID_1, iIETM_NAME_1, iIETM_LDESC_1, iIETM_PREFIX_LDESC_1 );

      iIETM_ID_2 = getIetmIds( iIETM_CD_2 );
      veryIetmIetm( iIETM_ID_2, iIETM_NAME_2, iIETM_LDESC_2, iIETM_PREFIX_LDESC_2 );

      // verify ietm_topic (attach)
      String lietmtopicID = getIetmTopicIds( iIETM_ID_1, iATTACH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, null, "0", null, iATTACH_DESC_LDESC_1,
            iATTACH_TOPIC_NOTE_1, iATTACH_ATTACH_TYPE_CD_1, iATTACH_ATTACH_FILENAME_1,
            iATTACH_ATTACH_CONTENT_TYPE_1, null, null, null );

      String temp = getBlobvalue( TableUtil.IETM_TOPIC, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1,
            "where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID()
                  + " and IETM_TOPIC_ID=" + lietmtopicID );

      Assert.assertTrue( "Check BLOB value.", temp.contains( iATTACH_ATTACH_BLOB_1 ) );

      // verify ietm_topic (topic)
      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iTECH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, iTECH_CMDLINE_PARM_LDESC_1, "1",
            iTECH_IETM_TYPE_CD_1, iTECH_DESC_LDESC_1, iTECH_TOPIC_NOTE_1, null, null, null,
            iTECH_TASKDEFN_CXT_LDESC_1, iTECH_TASK_CXT_LDESC_1, iTECH_APPLICABILITY_RANGE_1 );

      // Verify ietm_assmbl

      // check ACFT_CD1
      String lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='ACFT_T1'";
      Assert.assertTrue( "Check ACFT_CD1 is in the list.", RecordsExist( lcheck ) == true );

      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_2.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_2.getNO_ID() + " and ASSMBL_CD='ACFT_T1'";
      Assert.assertTrue( "Check ACFT_CD1 is in the list.", RecordsExist( lcheck ) == true );

      // check COMHW
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='COMHW'";
      Assert.assertTrue( "Check COMHW is in the list.", RecordsExist( lcheck ) == true );

      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_2.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_2.getNO_ID() + " and ASSMBL_CD='COMHW'";
      Assert.assertTrue( "Check COMHW is in the list.", RecordsExist( lcheck ) == true );

      // check TSE
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='TSE'";
      Assert.assertTrue( "Check TSE is in the list.", RecordsExist( lcheck ) == true );

      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_2.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_2.getNO_ID() + " and ASSMBL_CD='TSE'";
      Assert.assertTrue( "Check TSE is in the list.", RecordsExist( lcheck ) == true );

      // check APU_CD1
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='APU_CD1'";
      Assert.assertTrue( "Check APU_CD1 is in the list.", RecordsExist( lcheck ) == true );

      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_2.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_2.getNO_ID() + " and ASSMBL_CD='APU_CD1'";
      Assert.assertTrue( "Check APU_CD1 is in the list.", RecordsExist( lcheck ) == true );

   }


   /**
    * This test is to verify CFGIEM-12038: There exist same IETM in both the staging tables,
    * c_ietm_topic_tech_ref and c_ietm_topic_attach table').
    *
    */
   @Test
   public void testCFGIEM_12038_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1, null );

      // C_IETM_TOPIC_TECH_REF table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "'ATLD'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkIETMErrorCode( "testCFGIEM_12038_1", "CFGIEM-12038" );

   }


   /**
    * This test is to verify OPER-21963/CFGIEM-12038: There exist same IETM in both the staging
    * tables, c_ietm_topic_tech_ref and c_ietm_topic_attach table').
    *
    */
   @Test
   public void testOPER21963() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "'MXI'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1, null );

      // C_IETM_TOPIC_TECH_REF table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "'ATLD'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-25849:Baseline Loader IETM import is ignoring the
    * attach_content_type
    *
    */
   @Test
   public void testOPER_25849_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lietm = new LinkedHashMap<>();

      // C_IETM_IETM table
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "IETM_NAME", "\'" + iIETM_NAME_1 + "\'" );
      lietm.put( "IETM_LDESC", "\'" + iIETM_LDESC_1 + "\'" );
      lietm.put( "PREFIX_LDESC", "\'" + iIETM_PREFIX_LDESC_1 + "\'" );
      lietm.put( "ASSEMBLY_CD_LIST", "\'" + iIETM_ASSEMBLY_CD_LIST_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_IETM, lietm ) );

      // C_IETM_TOPIC_ATTACH table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iATTACH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iATTACH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iATTACH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iATTACH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iATTACH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "ATTACH_TYPE_CD", "\'" + iATTACH_ATTACH_TYPE_CD_1 + "\'" );
      // lietm.put( "ATTACH_FILENAME", "\'" + iATTACH_ATTACH_FILENAME_1 + "\'" );
      lietm.put( "ATTACH_FILENAME", "'acgi'" );
      // lietm.put( "ATTACH_CONTENT_TYPE", "\'" + iATTACH_ATTACH_CONTENT_TYPE_1 + "\'" );
      lietm.put( "ATTACH_CONTENT_TYPE", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_ATTACH, lietm ) );
      insertBlobvalue( TableUtil.C_IETM_TOPIC_ATTACH, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1, null );

      // C_IETM_TOPIC_TECH_REF table
      lietm.clear();
      lietm.put( "IETM_CD", "\'" + iIETM_CD_1 + "\'" );
      lietm.put( "TOPIC_SDESC", "\'" + iTECH_TOPIC_SDESC_1 + "\'" );
      lietm.put( "OPERATOR_CD_LIST", "\'" + iTECH_OPERATOR_CD_LIST_1 + "\'" );
      lietm.put( "CMDLINE_PARM_LDESC", "\'" + iTECH_CMDLINE_PARM_LDESC_1 + "\'" );
      lietm.put( "PRINT_BOOL", "\'" + iTECH_PRINT_BOOL_1 + "\'" );
      lietm.put( "DESC_LDESC", "\'" + iTECH_DESC_LDESC_1 + "\'" );
      lietm.put( "TOPIC_NOTE", "\'" + iTECH_TOPIC_NOTE_1 + "\'" );
      lietm.put( "IETM_TYPE_CD", "\'" + iTECH_IETM_TYPE_CD_1 + "\'" );
      lietm.put( "TASKDEFN_CXT_LDESC", "\'" + iTECH_TASKDEFN_CXT_LDESC_1 + "\'" );
      lietm.put( "TASK_CXT_LDESC", "\'" + iTECH_TASK_CXT_LDESC_1 + "\'" );
      lietm.put( "APPLICABILITY_RANGE", "\'" + iTECH_APPLICABILITY_RANGE_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_IETM_TOPIC_TECH_REF, lietm ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-25849:Baseline Loader IETM import is ignoring the
    * attach_content_type
    *
    */
   @Test
   public void testOPER_25849_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_25849_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iIETM_ID_1 = null;
      iIETM_ID_2 = null;

      // Verify item_ietm table
      iIETM_ID_1 = getIetmIds( iIETM_CD_1 );
      veryIetmIetm( iIETM_ID_1, iIETM_NAME_1, iIETM_LDESC_1, iIETM_PREFIX_LDESC_1 );

      // verify ietm_topic (attach)
      String lietmtopicID = getIetmTopicIds( iIETM_ID_1, iATTACH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, null, "0", null, iATTACH_DESC_LDESC_1,
            iATTACH_TOPIC_NOTE_1, iATTACH_ATTACH_TYPE_CD_1, "acgi", "text/html", null, null, null );

      String temp = getBlobvalue( TableUtil.IETM_TOPIC, "ATTACH_BLOB", iATTACH_ATTACH_BLOB_1,
            "where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID()
                  + " and IETM_TOPIC_ID=" + lietmtopicID );

      Assert.assertTrue( "Check BLOB value.", temp.contains( iATTACH_ATTACH_BLOB_1 ) );

      // verify ietm_topic (topic)
      lietmtopicID = getIetmTopicIds( iIETM_ID_1, iTECH_TOPIC_SDESC_1 );
      veryIetmTopic( iIETM_ID_1, lietmtopicID, iTECH_CMDLINE_PARM_LDESC_1, "1",
            iTECH_IETM_TYPE_CD_1, iTECH_DESC_LDESC_1, iTECH_TOPIC_NOTE_1, null, null, null,
            iTECH_TASKDEFN_CXT_LDESC_1, iTECH_TASK_CXT_LDESC_1, iTECH_APPLICABILITY_RANGE_1 );

      // Verify ietm_assmbl
      // check ACFT_CD1
      String lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='ACFT_T1'";
      Assert.assertTrue( "Check ACFT_CD1 is in the list.", RecordsExist( lcheck ) == true );

      // check COMHW
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='COMHW'";
      Assert.assertTrue( "Check COMHW is in the list.", RecordsExist( lcheck ) == true );

      // check TSE
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='TSE'";
      Assert.assertTrue( "Check TSE is in the list.", RecordsExist( lcheck ) == true );

      // check APU_CD1
      lcheck = "Select 1 from ietm_assmbl where IETM_DB_ID=" + iIETM_ID_1.getNO_DB_ID()
            + " and IETM_ID=" + iIETM_ID_1.getNO_ID() + " and ASSMBL_CD='APU_CD1'";
      Assert.assertTrue( "Check APU_CD1 is in the list.", RecordsExist( lcheck ) == true );

   }


   // =============================================================

   /**
    * This function is to verify from ietm_topic table.
    *
    */
   public void veryIetmTopic( simpleIDs aietmIds, String aIIETM_TOPIC_ID,
         String aCMDLINE_PARM_LDESC, String aPRINT_BOOL, String aIETM_TYPE_CD, String DESC_LDESC,
         String aTOPIC_NOTE, String aATTACH_TYPE_CD, String aATTACH_FILENAME,
         String aATTACH_CONTENT_TYPE, String aTASKDEFN_CXT_LDESC, String aTASK_CXT_LDESC,
         String aAPPL_EFF_LDESC ) {

      String[] iIds = { "CMDLINE_PARM_LDESC", "PRINT_BOOL", "IETM_TYPE_CD", "DESC_LDESC",
            "TOPIC_NOTE", "ATTACH_TYPE_CD", "ATTACH_FILENAME", "ATTACH_CONTENT_TYPE",
            "TASKDEFN_CXT_LDESC", "TASK_CXT_LDESC", "APPL_EFF_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_DB_ID", aietmIds.getNO_DB_ID() );
      lArgs.addArguments( "IETM_ID", aietmIds.getNO_ID() );
      lArgs.addArguments( "IETM_TOPIC_ID", aIIETM_TOPIC_ID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_TOPIC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aCMDLINE_PARM_LDESC != null ) {
         Assert.assertTrue( "CMDLINE_PARM_LDESC",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aCMDLINE_PARM_LDESC ) );
      }

      Assert.assertTrue( "PRINT_BOOL", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPRINT_BOOL ) );

      if ( aIETM_TYPE_CD != null ) {
         Assert.assertTrue( "IETM_TYPE_CD",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aIETM_TYPE_CD ) );
      }

      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 3 ).equalsIgnoreCase( DESC_LDESC ) );
      Assert.assertTrue( "TOPIC_NOTE", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTOPIC_NOTE ) );

      if ( aATTACH_TYPE_CD != null ) {
         Assert.assertTrue( "ATTACH_TYPE_CD",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aATTACH_TYPE_CD ) );
      }

      if ( aATTACH_FILENAME != null ) {
         Assert.assertTrue( "ATTACH_FILENAME",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aATTACH_FILENAME ) );
      }

      if ( aATTACH_CONTENT_TYPE != null ) {
         Assert.assertTrue( "ATTACH_CONTENT_TYPE",
               llists.get( 0 ).get( 7 ).equalsIgnoreCase( aATTACH_CONTENT_TYPE ) );
      }

      if ( aTASKDEFN_CXT_LDESC != null ) {
         Assert.assertTrue( "TASKDEFN_CXT_LDESC",
               llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTASKDEFN_CXT_LDESC ) );
      }

      if ( aTASK_CXT_LDESC != null ) {
         Assert.assertTrue( "TASK_CXT_LDESC",
               llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTASK_CXT_LDESC ) );
      }

      if ( aAPPL_EFF_LDESC != null ) {
         Assert.assertTrue( "APPL_EFF_LDESC",
               llists.get( 0 ).get( 10 ).equalsIgnoreCase( aAPPL_EFF_LDESC ) );
      }

   }


   /**
    * This function is to retrieve item_topic_id from ietm_topic table.
    *
    */
   public String getIetmTopicIds( simpleIDs aietmIds, String aTOPIC_SDESC ) {
      String[] iIds = { "IETM_TOPIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_DB_ID", aietmIds.getNO_DB_ID() );
      lArgs.addArguments( "IETM_ID", aietmIds.getNO_ID() );
      lArgs.addArguments( "TOPIC_SDESC", aTOPIC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_TOPIC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String lIds = llists.get( 0 ).get( 0 );

      return lIds;

   }


   /**
    * This function is to verify from ietm_ietm table.
    *
    */
   public void veryIetmIetm( simpleIDs aietmIds, String aIETM_NAME, String aIETM_LDESC,
         String aPREFIX_LDESC ) {

      String[] iIds = { "IETM_NAME", "IETM_LDESC", "PREFIX_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_DB_ID", aietmIds.getNO_DB_ID() );
      lArgs.addArguments( "IETM_ID", aietmIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_IETM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "IETM_NAME", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aIETM_NAME ) );
      Assert.assertTrue( "IETM_LDESC", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aIETM_LDESC ) );
      Assert.assertTrue( "PREFIX_LDESC",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPREFIX_LDESC ) );

   }


   /**
    * This function is to retrieve Ietm ID from ietm_ietm table.
    *
    */

   public simpleIDs getIetmIds( String aIETM_CD ) {

      String[] iIds = { "IETM_DB_ID", "IETM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_CD", aIETM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_IETM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * Calls check error code
    *
    *
    */
   protected void checkIETMErrorCode( String aTestName, String aValidationCode ) {

      String lquery = TestConstants.CIETM_ERROR_CHECK;

      checkErrorCode( lquery, aTestName, aValidationCode );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iIETM_ID_1 != null ) {

         // delete ietm_ietm
         lStrDelete = "delete from " + TableUtil.IETM_IETM + "  where IETM_DB_ID="
               + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete ietm_topic
         lStrDelete = "delete from " + TableUtil.IETM_TOPIC + "  where IETM_DB_ID="
               + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete ietm_assmbl
         lStrDelete = "delete from " + TableUtil.IETM_ASSMBL + "  where IETM_DB_ID="
               + iIETM_ID_1.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iIETM_ID_2 != null ) {

         // delete ietm_ietm
         lStrDelete = "delete from " + TableUtil.IETM_IETM + "  where IETM_DB_ID="
               + iIETM_ID_2.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete ietm_topic
         lStrDelete = "delete from " + TableUtil.IETM_TOPIC + "  where IETM_DB_ID="
               + iIETM_ID_2.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete ietm_assmbl
         lStrDelete = "delete from " + TableUtil.IETM_ASSMBL + "  where IETM_DB_ID="
               + iIETM_ID_2.getNO_DB_ID() + " and IETM_ID=" + iIETM_ID_2.getNO_ID();
         executeSQL( lStrDelete );

      }
   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN ietm_import.validate_ietm(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN ietm_import.import_ietm(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }

}
