CREATE TABLE Alternatives (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Criteria (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    benefit BOOLEAN NOT NULL
);

CREATE TABLE Weights (
    id SERIAL PRIMARY KEY,
    criteria_id INTEGER NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_criteria
        FOREIGN KEY (criteria_id)
        REFERENCES Criteria(id)
        ON DELETE CASCADE
);

CREATE TABLE Values (
    id SERIAL PRIMARY KEY,
    alternative_id INTEGER NOT NULL,
    criteria_id INTEGER NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_alternative
        FOREIGN KEY (alternative_id)
        REFERENCES Alternatives(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_criteria
        FOREIGN KEY (criteria_id)
        REFERENCES Criteria(id)
        ON DELETE CASCADE
);

CREATE TABLE Results (
    id SERIAL PRIMARY KEY,
    alternative_id INTEGER NOT NULL,
    rank INTEGER NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_alternative
        FOREIGN KEY (alternative_id)
        REFERENCES Alternatives(id)
        ON DELETE CASCADE
);