
package com.mxi.am.stepdefn.persona.engineering.systemeng.data;

public class ResetUsageDefinitionData {

   public static final String DB_ID = "4650";
   public static final String ASSEMBLY_NAME = "ACFTENG4";
   public static final String DATA_SOURCE_DB_ID = "10";
   public static final String DATA_SOURCE_NAME = "BULK - ACFTENG4";
   public static final String USAGE_DEFINITION_BULK = "BULK";
   public static final String USAGE_DEFINITION_MXFL = "MXFL";

   public static final String CREATE_USAGE_DEFN =
         "INSERT INTO eqp_data_source (assmbl_db_id, assmbl_cd, data_source_db_id, data_source_cd, data_source_name, revision_db_id) "
               + "SELECT " + DB_ID + ", '" + ASSEMBLY_NAME + "', " + DATA_SOURCE_DB_ID + ", '"
               + USAGE_DEFINITION_BULK + "', '" + DATA_SOURCE_NAME + "', " + DB_ID + " FROM dual "
               + "WHERE NOT EXISTS (SELECT * FROM eqp_data_source WHERE assmbl_cd = ? AND data_source_cd = ?)";

   public static final String DELETE_USAGE_DEFN =
         "DELETE FROM eqp_data_source WHERE assmbl_cd = ? AND data_source_cd = ?";


   private ResetUsageDefinitionData() {

   }

}
