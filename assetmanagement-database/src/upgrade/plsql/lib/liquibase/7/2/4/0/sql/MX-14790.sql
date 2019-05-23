--liquibase formatted sql


--changeSet MX-14790:1 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_name'
WHERE  t.alert_type_id  = 140 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_name';

--changeSet MX-14790:2 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_description'
WHERE  t.alert_type_id  = 140 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_description';

--changeSet MX-14790:3 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_message'
WHERE  t.alert_type_id  = 140 and
       t.message = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_ATTACH_CTX_message';

--changeSet MX-14790:4 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_name'
WHERE  t.alert_type_id  = 141 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_name';

--changeSet MX-14790:5 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_description'
WHERE  t.alert_type_id  = 141 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_description';

--changeSet MX-14790:6 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_message'
WHERE  t.alert_type_id  = 141 and
       t.message = 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_EDIT_INV_CTX_message';

--changeSet MX-14790:7 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_name'
WHERE  t.alert_type_id  = 142 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_name';

--changeSet MX-14790:8 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_description'
WHERE  t.alert_type_id  = 142 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_description';

--changeSet MX-14790:9 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_message'
WHERE  t.alert_type_id  = 142 and
       t.message = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_ATTACH_CTX_message';

--changeSet MX-14790:10 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_name'
WHERE  t.alert_type_id  = 143 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_name';

--changeSet MX-14790:11 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_description'
WHERE  t.alert_type_id  = 143 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_description';

--changeSet MX-14790:12 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_message'
WHERE  t.alert_type_id  = 143 and
       t.message = 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_EDIT_INV_CTX_message';

--changeSet MX-14790:13 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_NOT_APPLICABLE_name'
WHERE  t.alert_type_id  = 144 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPLICABLE_name';

--changeSet MX-14790:14 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_NOT_APPLICABLE_description'
WHERE  t.alert_type_id  = 144 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPLICABLE_description';

--changeSet MX-14790:15 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_NOT_APPLICABLE_message'
WHERE  t.alert_type_id  = 144 and
       t.message = 'core.alert.INSTALL_PART_NOT_APPLICABLE_ATTACH_CTX_message';

--changeSet MX-14790:16 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_NOT_APPLICABLE_name'
WHERE  t.alert_type_id  = 145 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPLICABLE_name';

--changeSet MX-14790:17 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_NOT_APPLICABLE_description'
WHERE  t.alert_type_id  = 145 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPLICABLE_description';

--changeSet MX-14790:18 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_NOT_APPLICABLE_message'
WHERE  t.alert_type_id  = 145 and
       t.message = 'core.alert.INSTALL_PART_NOT_APPLICABLE_EDIT_INV_CTX_message';

--changeSet MX-14790:19 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_name'
WHERE  t.alert_type_id  = 146 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPLICABLE_name';

--changeSet MX-14790:20 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_description'
WHERE  t.alert_type_id  = 146 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPLICABLE_description';

--changeSet MX-14790:21 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_message'
WHERE  t.alert_type_id  = 146 and
       t.message = 'core.alert.INSTALL_PART_GROUP_NOT_APPLICABLE_ATTACH_CTX_message';

--changeSet MX-14790:22 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_name'
WHERE  t.alert_type_id  = 147 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPLICABLE_name';

--changeSet MX-14790:23 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_description'
WHERE  t.alert_type_id  = 147 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPLICABLE_description';

--changeSet MX-14790:24 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_message'
WHERE  t.alert_type_id  = 147 and
       t.message = 'core.alert.INSTALL_PART_GROUP_NOT_APPLICABLE_EDIT_INV_CTX_message';

--changeSet MX-14790:25 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_name'
WHERE  t.alert_type_id  = 148 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_name';

--changeSet MX-14790:26 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_description'
WHERE  t.alert_type_id  = 148 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_description';

--changeSet MX-14790:27 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_message'
WHERE  t.alert_type_id  = 148 and
       t.message = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_ATTACH_CTX_message';

--changeSet MX-14790:28 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_name'
WHERE  t.alert_type_id  = 149 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_name';

--changeSet MX-14790:29 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_description'
WHERE  t.alert_type_id  = 149 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_description';

--changeSet MX-14790:30 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_message'
WHERE  t.alert_type_id  = 149 and
       t.message = 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_EDIT_INV_CTX_message';

--changeSet MX-14790:31 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_name'
WHERE  t.alert_type_id  = 150 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_name';

--changeSet MX-14790:32 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_description'
WHERE  t.alert_type_id  = 150 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_description';

--changeSet MX-14790:33 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_message'
WHERE  t.alert_type_id  = 150 and
       t.message = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_ATTACH_CTX_message';

--changeSet MX-14790:34 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_name  = 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_name'
WHERE  t.alert_type_id  = 151 and
       t.alert_name  = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_name';

--changeSet MX-14790:35 stripComments:false
UPDATE utl_alert_type t
SET    t.alert_ldesc = 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_description'
WHERE  t.alert_type_id  = 151 and
       t.alert_ldesc = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_description';

--changeSet MX-14790:36 stripComments:false
UPDATE utl_alert_type t
SET    t.message = 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_message'
WHERE  t.alert_type_id  = 151 and
       t.message = 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_EDIT_INV_CTX_message';