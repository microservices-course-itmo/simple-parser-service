ALTER TABLE wine
	ADD price FLOAT DEFAULT 0 NOT NULL,
	ADD discount INTEGER DEFAULT 0,
	ADD year INTEGER DEFAULT -1,
	ADD grape_type TEXT NOT NULL;