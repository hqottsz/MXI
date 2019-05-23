/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */
package com.mxi.dataloader.database.baseline;

/**
 * A baseline module
 */
public enum BaselineModule {
   finance_import( "validate_finance", "import_finance" ),
   manufact_import( "validate_manufact", "import_manufact" ),
   assembly_import( "validate_assembly", "import_assembly" ),
   subassembly_import( "validate_subassembly", "import_subassembly" ),
   part_import( "validate_part", "import_part" ), tool_import( "validate_tool", "import_tool" ),
   comhw_import( "validate_comhw", "import_comhw" ), bl_kit_import( "validate", "import" ),
   usage_import( "validate_usage", "import_usage" ), bl_calc_import( "validate", "import" ),
   bl_part_incompat_import( "validate", "import" ), ietm_import( "validate_ietm", "import_ietm" ),
   jic_import( "validate_jic", "import_jic" ),
   c_comp_jic_import( "validate_comp_jic", "import_comp_jic" ),
   req_import( "validate_req", "import_req" ),
   c_comp_req_import( "validate_comp_req", "import_comp_req" ),
   c_comp_ref_doc_import( "validate_comp_ref_document", "import_comp_ref_document" ),
   c_ref_document_import( "validate_reference_document", "import_ref_document" ),
   block_import( "validate_block", "import_block" ),
   task_link_import( "validate_task_link", "import_task_link" ),
   maint_prgm_import( "validate_maint_prgm", "import_maint_prgm" ),
   fault_import( "validate_fault", "import_fault" ),
   bl_deferral_references_import( "validate", "import" ),
   owner_import( "validate_owner", "import_owner" ),
   location_import( "validate_location", "import_location" ),
   materials_import( "validate_materials", "import_materials" ),
   vendor_import( "validate_vendor", "import_vendor" ),
   c_vendor_purchase_import( "validate_agreement", "import_agreement" ),
   c_vendor_repair_import( "validate_agreement", "import_agreement" ),
   c_vendor_exchg_import( "validate_vendor_exchange", "import_vendor_exchange" ),
   c_stock_header_import( "validate_stock_header", "import_stock_header" ),
   c_stock_details_import( "validate_stock_attribute", "import_stock_attribute" ),
   c_stock_part_asgn_import( "validate_stock_part_asgn", "import_stock_part_asgn" ),
   c_stock_lvl_alloc_import( "validate_allocation", "import_allocation" ),
   c_stock_lvl_attr_import( "validate_attribute", "import_attribute" ),
   dept_import( "validate_dept", "import_dept" ),
   po_flow_import( "validate_po_flow", "import_po_flow" ),
   user_import( "validate_user", "import_user" ),
   licdefn_import( "validate_licdefn", "import_licdefn" ),
   usrlic_import( "validate_usrlic", "import_usrlic" );

   private final String iValidateMethod;
   private final String iImportMethod;


   private BaselineModule(String aValidateMethod, String aImportMethod) {
      iValidateMethod = aValidateMethod;
      iImportMethod = aImportMethod;
   }


   public String getValidateMethod() {
      return iValidateMethod;
   }


   public String getImportMethod() {
      return iImportMethod;
   }

}
