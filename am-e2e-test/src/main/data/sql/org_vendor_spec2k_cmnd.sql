-- Add Spec 2000 command S1BOOKED to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  1,
  0,
  'S1BOOKED'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '20002';

  -- Add Spec 2000 command S1ORDEXC to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  2,
  0,
  'S1ORDEXC'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '20002';


  -- Add Spec 2000 command S1BOOKED to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  1,
  0,
  'S1BOOKED'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '20016';

  -- Add Spec 2000 command S1SHIPPD to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  2,
  0,
  'S1SHIPPD'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '20016';

  -- Add Spec 2000 command S1BOOKED to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  1,
  0,
  'S1BOOKED'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '52000';

  -- Add Spec 2000 command S1ORDEXC to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  2,
  0,
  'S1ORDEXC'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '52000';

  -- Add Spec 2000 command S1BOOKED to vendors for Spec 2000 message testing
INSERT INTO org_vendor_spec2k_cmnd ( vendor_db_id, vendor_id, vendor_cmnd_id, spec2k_cmnd_db_id, spec2k_cmnd_cd)
SELECT
  vendor_db_id,
  vendor_id,
  3,
  0,
  'S1INVCE'
FROM
  org_vendor
WHERE
  org_vendor.vendor_cd = '52000';