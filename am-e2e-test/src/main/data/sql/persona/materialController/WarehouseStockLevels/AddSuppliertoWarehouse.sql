-- Set airport1/store2 as supplier of warehouse Airport1/Store
UPDATE INV_LOC SET HUB_LOC_DB_ID = 4650, HUB_LOC_ID = (SELECT LOC_ID FROM INV_LOC WHERE LOC_CD = 'AIRPORT1/STORE2') WHERE LOC_CD = 'AIRPORT1/STORE'; 
UPDATE INV_LOC SET HUB_LOC_DB_ID = 4650, HUB_LOC_ID = (SELECT LOC_ID FROM INV_LOC WHERE LOC_CD = 'SUPPLY2/STORE2') WHERE LOC_CD = 'SUPPLY2/STORE1'; 