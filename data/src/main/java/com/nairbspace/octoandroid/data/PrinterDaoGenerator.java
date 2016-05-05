package com.nairbspace.octoandroid.data;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class PrinterDaoGenerator {
    private static final int DB_VERSION = 1;
    private static final String DEFAULT_JAVA_PACKAGE = "com.nairbspace.octoandroid.data.db";
    private static final String OUT_DIR = "./app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DB_VERSION, DEFAULT_JAVA_PACKAGE);

        addTables(schema);

        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    private static void addTables(Schema schema) {

        /** Entities */
        Entity printer = addPrinter(schema);
//        Entity version = addVersion(schema);

        /** Properties */
//        Property printerIdForVersion = version.addLongProperty("printerId").getProperty();
//        Property versionIdForPrinter = printer.addLongProperty("versionId").getProperty();

        /** Relationship between Entities */
//        printer.addToOne(version, versionIdForPrinter); // one-to-one printer.getVersion()
//        version.addToOne(printer, printerIdForVersion); // one-to-one version.getPrinter()
    }

    private static Entity addPrinter(Schema schema) {
        Entity printer = schema.addEntity("Printer");
        printer.addIdProperty().primaryKey();
        printer.addStringProperty("name").notNull().unique();
        printer.addStringProperty("apiKey").columnName("api_key").notNull();
        printer.addStringProperty("scheme").notNull();
        printer.addStringProperty("host").notNull();
        printer.addIntProperty("port").notNull();
        printer.addStringProperty("versionJson").columnName("version_json");
        printer.addStringProperty("connectionJson").columnName("connection_json");
        printer.addStringProperty("printerStateJson").columnName("printer_state_json");
        return printer;
    }

    private static Entity addVersion(Schema schema) {
        Entity version = schema.addEntity("Version");
        version.addIdProperty().primaryKey();
        version.addStringProperty("api").notNull();
        version.addStringProperty("server").notNull();

        return version;
    }
}
