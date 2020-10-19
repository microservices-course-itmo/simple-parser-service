DROP TABLE IF EXISTS messages CASCADE;

ALTER TABLE wine
	ADD price FLOAT DEFAULT 0 NOT NULL,
	ADD discount FLOAT DEFAULT 0,
	ADD year INTEGER DEFAULT -1,
	ADD region TEXT,
	ADD grape_type TEXT NOT NULL,
	ADD sparkling BOOL,
	ADD taste TEXT,
	ADD gastronomy TEXT,
	ADD link TEXT,
	ADD rating FLOAT DEFAULT 0,
	ALTER COLUMN picture TYPE text;

