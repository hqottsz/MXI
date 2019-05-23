package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * Validations for jic step skill
 *
 */
public class JicStepSkill extends BaselineLoaderTest {

   // # Fields
   private static final String FIELD_RESULT_CD = "result_cd";
   private String iErrorCode = "";
   private ResultSet iQs;
   ValidationAndImport validationandimport;


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
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
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10706</li>
    * <li>C_JIC_STEP_SKILL.ASSMBL_CD must be provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10706() throws Exception {

      iErrorCode = "CFGJIC-10706";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", null );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10707</li>
    * <li>C_JIC_STEP_SKILL.JIC_ATA_CD must be provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10707() throws Exception {

      iErrorCode = "CFGJIC-10707";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", null );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10708</li>
    * <li>C_JIC_STEP_SKILL.JIC_TASK_CD must be provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10708() throws Exception {

      iErrorCode = "CFGJIC-10708";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", null );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10709</li>
    * <li>C_JIC_STEP_SKILL.JIC_STEP_ORD must be provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10709() throws Exception {

      iErrorCode = "CFGJIC-10709";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", null );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10710</li>
    * <li>C_JIC_STEP_SKILL.STEP_SKILL_CD must be provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10710() throws Exception {

      iErrorCode = "CFGJIC-10710";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", null );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10711</li>
    * <li>C_JIC_STEP_SKILL.ASSMBL_CD / JIC_ATA_CD / JIC_TASK_CD / JOB_STEP_ORD /STEP_SKILL_CD exists
    * multiple times in staging area C_JIC_STEP_SKILL.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10711() throws Exception {

      iErrorCode = "CFGJIC-10711";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10712</li>
    * <li>C_JIC_STEP_SKILL.ASSMBL_CD/JIC_ATA_CD/JIC_TASK_CD/JOB_STEP_ORD must exist in staging table
    * C_JIC_STEP.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10712() throws Exception {

      iErrorCode = "CFGJIC-10712";

      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();
      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10713</li>
    * <li>C_JIC_STEP_SKILL.ASSMBL_CD/JIC_ATA_CD/JIC_TASK_CD/JOB_STEP_ORD/STEP_SKILL_CD must exist in
    * staging table C_JIC_LABOR.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10713() throws Exception {

      iErrorCode = "CFGJIC-10713";

      Map<String, String> lJicStepMap = new LinkedHashMap<>();
      lJicStepMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepMap.put( "JOB_STEP_ORD", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lJicStepMap ) );

      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();
      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10714</li>
    * <li>When a skill is marked as Requires Independent Inspection, the labor row for the
    * corresponding skill must also be marked as Independent Inspection Required.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10714() throws Exception {

      iErrorCode = "CFGJIC-10714";

      Map<String, String> lJicStepMap = new LinkedHashMap<>();
      lJicStepMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepMap.put( "JOB_STEP_ORD", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lJicStepMap ) );

      Map<String, String> lJicLaborMap = new LinkedHashMap<>();
      lJicLaborMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicLaborMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicLaborMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicLaborMap.put( "LABOR_SKILL_CD", "\'LINE\'" );
      lJicLaborMap.put( "SCHED_INSP_HRS", null );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lJicLaborMap ) );

      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();
      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );
      lJicStepSkillMap.put( "INSP_BOOL", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10715</li>
    * <li>The labor row for each skill on the job card must be marked as Certification
    * Required.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10715() throws Exception {

      iErrorCode = "CFGJIC-10715";

      Map<String, String> lJicStepMap = new LinkedHashMap<>();
      lJicStepMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepMap.put( "JOB_STEP_ORD", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lJicStepMap ) );

      Map<String, String> lJicLaborMap = new LinkedHashMap<>();
      lJicLaborMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicLaborMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicLaborMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicLaborMap.put( "LABOR_SKILL_CD", "\'LINE\'" );
      lJicLaborMap.put( "SCHED_INSP_HRS", null );
      lJicLaborMap.put( "SCHED_CERT_HRS", null );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lJicLaborMap ) );

      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();
      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10716</li>
    * <li>Either all steps on the job card must have skills added, or no steps on the job card have
    * can have skills added.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10716() throws Exception {

      iErrorCode = "CFGJIC-10716";

      Map<String, String> lJicStepMap = new LinkedHashMap<>();
      lJicStepMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepMap.put( "JOB_STEP_ORD", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lJicStepMap ) );

      lJicStepMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepMap.put( "JOB_STEP_ORD", "\'2\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lJicStepMap ) );

      Map<String, String> lJicLaborMap = new LinkedHashMap<>();
      lJicLaborMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicLaborMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicLaborMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicLaborMap.put( "LABOR_SKILL_CD", "\'LINE\'" );
      lJicLaborMap.put( "SCHED_CERT_HRS", "\'1\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lJicLaborMap ) );

      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();
      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'LINE\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Load the test data into the baseline loader staging table (C_JIC_STEP_SKILL)</li>
    * <li>Execute the validate_jic process.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Validation error should be generated:</li>
    * <li>Error Code: CFGJIC-10717</li>
    * <li>C_JIC_STEP_SKILL.STEP_SKILL_CD must be valid value from REF_LABOUR_SKILL.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testJICStepSkill_Validation_CFGJIC_10717() throws Exception {

      iErrorCode = "CFGJIC-10717";

      // prepare the staging data
      Map<String, String> lJicStepSkillMap = new LinkedHashMap<>();

      lJicStepSkillMap.put( "ASSMBL_CD", "\'ACFT\'" );
      lJicStepSkillMap.put( "JIC_ATA_CD", "\'JIC-ATA\'" );
      lJicStepSkillMap.put( "JIC_TASK_CD", "\'JICTask\'" );
      lJicStepSkillMap.put( "JOB_STEP_ORD", "\'1\'" );
      lJicStepSkillMap.put( "STEP_SKILL_CD", "\'SKILL\'" );

      // insert the staged data into the table
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP_SKILL, lJicStepSkillMap ) );

      assertData();

   }


   /**
    * assert the step skill data after the validation
    *
    * @throws SQLException
    */
   private void assertData() throws SQLException {
      // run the baseline loader validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // retrieve erroneous data and then assert
      List<String> lFields = Arrays.asList( FIELD_RESULT_CD );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( FIELD_RESULT_CD, iErrorCode );

      iQs = runQuery( TableUtil.buildTableQuery( TableUtil.C_JIC_STEP_SKILL, lFields, lArgs ) );
      iQs.next();
      Assert.assertEquals( "Unexpected error code returned.", iErrorCode,
            iQs.getString( FIELD_RESULT_CD ) );
   }


   /**
    * This function is to ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      validationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCall;

            try {

               lPrepareCall = getConnection()
                     .prepareCall( "BEGIN  jic_import.validate_jic(on_retcode =>?); END;" );

               lPrepareCall.registerOutParameter( 1, Types.INTEGER );
               lPrepareCall.execute();
               commit();
               lReturn = lPrepareCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCall;

            try {

               lPrepareCall = getConnection()
                     .prepareCall( "BEGIN jic_import.import_jic(on_retcode =>?); END;" );

               lPrepareCall.registerOutParameter( 1, Types.INTEGER );

               lPrepareCall.execute();
               commit();
               lReturn = lPrepareCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? validationandimport.runValidation( allornone )
            : validationandimport.runImport( allornone );

      return lrtValue;
   }

}
