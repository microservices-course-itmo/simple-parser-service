DROP TABLE IF EXISTS messages CASCADE;

ALTER TABLE wine
	ALTER COLUMN discount TYPE FLOAT,
	ADD region TEXT,
	ADD sparkling BOOL,
	ADD taste TEXT,
	ADD gastronomy TEXT,
	ADD link TEXT,
	ADD rating FLOAT DEFAULT 0,
	ALTER COLUMN picture TYPE text;