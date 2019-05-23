/**************************************************
** Refresh fleet due list projection table.
***************************************************/

BEGIN
    mt_inv_ac_auth_inv_inv_pkg.populate_data;
END;
/