BEGIN TRANSACTION;
CREATE TABLE "stanza" (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`numero`	INTEGER NOT NULL,
	`num_max_persone`	INTEGER,
	`edificio`	INTEGER
);
INSERT INTO `stanza` VALUES (1,1,80,1),
 (2,2,85,1);
CREATE TABLE "ruolo" (
	`nome`	TEXT NOT NULL,
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT
);
INSERT INTO `ruolo` VALUES ('docente ordinario',1),
 ('ricercatore',2);
CREATE TABLE "persona" (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`nome`	TEXT NOT NULL,
	`cognome`	TEXT NOT NULL,
	`ruolo`	INTEGER NOT NULL,
	`stanza`	INTEGER
);
INSERT INTO `persona` VALUES (2,'nicola','bicocchi',1,1),
 (3,'nico','ferrari',2,1),
 (4,'maurizio','vincini',1,2);
CREATE TABLE "edificio" (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`nome`	TEXT NOT NULL
);
INSERT INTO `edificio` VALUES (1,'
tecnopolo'),
 (2,'matematica');
COMMIT;
