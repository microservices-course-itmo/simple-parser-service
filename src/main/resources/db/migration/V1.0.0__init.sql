
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    content TEXT
);

CREATE TABLE Grapes (
    grapeid UUID PRIMARY KEY UNIQUE,
    grapename TEXT NOT NULL UNIQUE
);

CREATE TABLE Brands (
    brandID UUID PRIMARY KEY UNIQUE,
    brandName TEXT NOT NULL UNIQUE
);

CREATE TABLE Countries (
    countryID UUID PRIMARY KEY UNIQUE,
    countryName TEXT NOT NULL UNIQUE
);

CREATE TABLE WineGrapesInfo (
                                ID uuid PRIMARY KEY,
                                wineGrapesID UUID,
                                grapeID UUID REFERENCES Grapes(grapeID)  NOT NULL
);

CREATE TABLE Wine (
    wineID UUID PRIMARY KEY,
    picture BYTEA NOT NULL,
    branID UUID REFERENCES Brands(brandID)  NOT NULL,
    countryID UUID REFERENCES Countries(countryID)  NOT NULL,
    volume FLOAT NOT NULL,
    abv FLOAT NOT NULL,
    colorType TEXT NOT NULL,
    sugarType TEXT NOT NULL,
    wineGrapesID UUID REFERENCES WineGrapesInfo(wineGrapesID)  NOT NULL
);
