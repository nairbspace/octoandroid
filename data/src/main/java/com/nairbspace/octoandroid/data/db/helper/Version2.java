package com.nairbspace.octoandroid.data.db.helper;

import android.database.sqlite.SQLiteDatabase;

import com.nairbspace.octoandroid.data.mapper.PrinterDbEntityMapper;

// Remove _ID NOT NULL constraint and add websocket path and webcam path and query.
public final class Version2 {
    private static final String PRINTER = "\"PRINTER\"";
    private static final String DUMMY_PRINTER = "\"DUMMY_PRINTER\"";
    private static final String _ID = "\"_id\"";
    private static final String NAME = "\"NAME\"";
    private static final String API_KEY = "\"api_key\"";
    private static final String SCHEME = "\"SCHEME\"";
    private static final String HOST = "\"HOST\"";
    private static final String PORT = "\"PORT\"";
    private static final String WEBSOCKET_PATH = "\"websocket_path\"";
    private static final String WEBCAM_PATH_QUERY = "\"webcam_path_query\"";
    private static final String VERSION_JSON = "\"version_json\"";
    private static final String CONNECTION_JSON = "\"connection_json\"";
    private static final String PRINTER_STATE_JSON = "\"printer_state_json\"";
    private static final String FILES_JSON = "\"files_json\"";

    private static final String CREATE_DUMMY_PRINTER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DUMMY_PRINTER + " (" + //
                    _ID + " INTEGER PRIMARY KEY UNIQUE, " + // 0: id
                    NAME + " TEXT NOT NULL UNIQUE, " + // 1: name
                    API_KEY + " TEXT NOT NULL, " + // 2: apiKey
                    SCHEME + " TEXT NOT NULL, " + // 3: scheme
                    HOST + " TEXT NOT NULL, " + // 4: host
                    PORT + " INTEGER NOT NULL, " + // 5: port
                    WEBSOCKET_PATH + " TEXT DEFAULT '" + PrinterDbEntityMapper.DEFAULT_WEBSOCKET_PATH + "', " +
                    WEBCAM_PATH_QUERY + " TEXT DEFAULT '" + PrinterDbEntityMapper.DEFAULT_WEBCAM_PATH_QUERY + "', " +
                    VERSION_JSON + " TEXT, " + // 6: versionJson
                    CONNECTION_JSON + " TEXT, " + // 7: connectionJson
                    PRINTER_STATE_JSON + " TEXT, " + // 8: printerStateJson
                    FILES_JSON + " TEXT);"; // 9: filesJson

    private static final String INSERT_INTO_DUMMY_PRINTER =
            "INSERT INTO " + DUMMY_PRINTER + " (" +
                    _ID + ", " +
                    NAME + ", " +
                    API_KEY + ", " +
                    SCHEME + ", " +
                    HOST + ", " +
                    PORT + ", " +
                    VERSION_JSON + ", " +
                    CONNECTION_JSON + ", " +
                    PRINTER_STATE_JSON + ", " +
                    FILES_JSON + ")";

    private static final String SELECT_COLUMNS_FROM_PRINTER =
            "SELECT " +
                    _ID + ", " +
                    NAME + ", " +
                    API_KEY + ", " +
                    SCHEME + ", " +
                    HOST + ", " +
                    PORT + ", " +
                    VERSION_JSON + ", " +
                    CONNECTION_JSON + ", " +
                    PRINTER_STATE_JSON + ", " +
                    FILES_JSON + " " +
                    "FROM " + PRINTER + ";";

    private static final String DROP_OLD_TABLE = "DROP TABLE IF EXISTS " + PRINTER + ";";
    private static final String RENAME_DUMMY_PRINTER = "ALTER TABLE " + DUMMY_PRINTER + " RENAME TO " + PRINTER + ";";
    private static final String DROP_DUMMY_PRINTER_TABLE = "DROP TABLE IF EXISTS " + DUMMY_PRINTER + ";";

    private static final String CREATE_DUMMY_PRINTER_TABLE_2 =
            "CREATE TABLE IF NOT EXISTS " + DUMMY_PRINTER + " (" + //
                    _ID + " INTEGER PRIMARY KEY UNIQUE, " + // 0: id
                    NAME + " TEXT NOT NULL UNIQUE, " + // 1: name
                    API_KEY + " TEXT NOT NULL, " + // 2: apiKey
                    SCHEME + " TEXT NOT NULL, " + // 3: scheme
                    HOST + " TEXT NOT NULL, " + // 4: host
                    PORT + " INTEGER NOT NULL, " + // 5: port
                    WEBSOCKET_PATH + " TEXT NOT NULL, " + // 6: websocketPath
                    WEBCAM_PATH_QUERY + " TEXT NOT NULL, " + // 7: webcamPathQuery
                    VERSION_JSON + " TEXT, " + // 6: versionJson
                    CONNECTION_JSON + " TEXT, " + // 7: connectionJson
                    PRINTER_STATE_JSON + " TEXT, " + // 8: printerStateJson
                    FILES_JSON + " TEXT);"; // 9: filesJson

    private static final String INSERT_INTO_DUMMY_PRINTER_2 =
            "INSERT INTO " + DUMMY_PRINTER + " (" +
                    _ID + ", " +
                    NAME + ", " +
                    API_KEY + ", " +
                    SCHEME + ", " +
                    HOST + ", " +
                    PORT + ", " +
                    WEBSOCKET_PATH + ", " +
                    WEBCAM_PATH_QUERY + ", " +
                    VERSION_JSON + ", " +
                    CONNECTION_JSON + ", " +
                    PRINTER_STATE_JSON + ", " +
                    FILES_JSON + ")";

    private static final String SELECT_COLUMNS_FROM_PRINTER_2 =
            "SELECT " +
                    _ID + ", " +
                    NAME + ", " +
                    API_KEY + ", " +
                    SCHEME + ", " +
                    HOST + ", " +
                    PORT + ", " +
                    WEBSOCKET_PATH + ", " +
                    WEBCAM_PATH_QUERY + ", " +
                    VERSION_JSON + ", " +
                    CONNECTION_JSON + ", " +
                    PRINTER_STATE_JSON + ", " +
                    FILES_JSON + " " +
                    "FROM " + PRINTER + ";";

    public static void update(SQLiteDatabase db) {
        db.execSQL(DROP_DUMMY_PRINTER_TABLE);
        db.execSQL(CREATE_DUMMY_PRINTER_TABLE);
        db.execSQL(INSERT_INTO_DUMMY_PRINTER + SELECT_COLUMNS_FROM_PRINTER);
        db.execSQL(DROP_OLD_TABLE);
        db.execSQL(RENAME_DUMMY_PRINTER);
        db.execSQL(DROP_DUMMY_PRINTER_TABLE);

        db.execSQL(CREATE_DUMMY_PRINTER_TABLE_2);
        db.execSQL(INSERT_INTO_DUMMY_PRINTER_2 + SELECT_COLUMNS_FROM_PRINTER_2);
        db.execSQL(DROP_OLD_TABLE);
        db.execSQL(RENAME_DUMMY_PRINTER);
        db.execSQL(DROP_DUMMY_PRINTER_TABLE);
    }
}
