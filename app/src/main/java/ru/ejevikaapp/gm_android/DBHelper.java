package ru.ejevikaapp.gm_android;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.content.Context.MODE_PRIVATE;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 18;
    public static final String DATABASE_NAME = "srv112238_test1";

    private Context mContext;

    public static final String TABLE_RGZBN_GM_CEILING_ANALYTICS_CANVASES = "rgzbn_gm_ceiling_analytics_canvases";
    public static final String KEY_ID = "_id";
    public static final String KEY_ID_CANVAS = "id_canvas";
    public static final String KEY_LENGTH = "length";
    public static final String KEY_DATE_UPDATE = "date_update";
    public static final String KEY_PRICE = "price";
    public static final String KEY_TYPE_CLIENT = "type_client";
    public static final String KEY_USER_ID = "user_id";

    public static final String TABLE_RGZBN_GM_CEILING_ANALYTICS_COMPONENTS = "rgzbn_gm_ceiling_analytics_components";
    public static final String KEY_COMPONENT_OPTION_ID = "component_option_id";
    public static final String KEY_COUNT = "count";

    public static final String TABLE_RGZBN_GM_CEILING_CALCULATIONS = "rgzbn_gm_ceiling_calculations";
    public static final String KEY_ORDERING = "ordering";
    public static final String KEY_STATE = "state";
    public static final String KEY_CHECKED_OUT = "checked_out";
    public static final String KEY_CHECKED_OUT_TIME = "checked_out_time";
    public static final String KEY_CREATED_BY = "created_by";
    public static final String KEY_MODIFIED_BY = "modified_by";
    public static final String KEY_DEALER_ID = "dealer_id";
    public static final String KEY_CALCULATION_TITLE = "calculation_title";
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String KEY_N3 = "n3";   //  ширина
    public static final String KEY_N4 = "n4";   //  площадь
    public static final String KEY_N5 = "n5";   //  периметр
    public static final String KEY_N6 = "n6";   //
    public static final String KEY_N7 = "n7";   //
    public static final String KEY_N8 = "n8";   //
    public static final String KEY_N9 = "n9";   // углы
    public static final String KEY_N10 = "n10";     //
    public static final String KEY_N11 = "n11";     //
    public static final String KEY_N12 = "n12";     //
    public static final String KEY_N16 = "n16";     //
    public static final String KEY_N17 = "n17";     //
    public static final String KEY_N18 = "n18";     //  дополнительный крепеж
    public static final String KEY_N19 = "n19";     //  установка диффузора
    public static final String KEY_N20 = "n20";     //  обработка одного угла, если больше 4-ых углов
    public static final String KEY_N21 = "n21";     //  криволинейный участок для ПВХ
    public static final String KEY_N24 = "n24";     //  сложность к доступу к месту установки
    public static final String KEY_N25 = "n25";     //
    public static final String KEY_N27 = "n27";     //  шторный карниз
    public static final String KEY_N28 = "n28";     //  багет
    public static final String KEY_N30 = "n30";     //  парящий потолок
    public static final String KEY_N31 = "n31";
    public static final String KEY_N32 = "n32";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_COMPONENTS_SUM = "components_sum";   //
    public static final String KEY_CANVASES_SUM = "canvases_sum";   //
    public static final String KEY_MOUNTING_SUM = "mounting_sum"; //
    public static final String KEY_DEALER_MOUNTING_SUM = "dealer_mounting_sum"; //
    public static final String KEY_DEALER_COMPONENTS_SUM = "dealer_components_sum"; //
    public static final String KEY_DEALER_CANVASES_SUM = "dealer_canvases_sum"; //
    public static final String KEY_DOP_KREPEZH = "dop_krepezh"; //
    public static final String KEY_EXTRA_COMPONENTS = "extra_components";   //
    public static final String KEY_EXTRA_MOUNTING = "extra_mounting";   //
    public static final String KEY_COLOR = "color"; //
    public static final String KEY_DETAILS = "details"; //
    public static final String KEY_CALC_IMAGE = "calc_image";   //
    public static final String KEY_CALC_DATA = "calc_data"; //
    public static final String KEY_CALC_POINT = "calc_point";   //
    public static final String KEY_CUT_IMAGE = "cut_image"; //
    public static final String KEY_CUT_DATA = "cut_data";   //
    public static final String KEY_OFFCUT_SQUARE = "offcut_square";   //
    public static final String KEY_ORIGINAL_SKETCH = "original_sketch";   //

    public static final String TABLE_RGZBN_GM_CEILING_CALC_GUILD = "rgzbn_gm_ceiling_calc_guild";
    public static final String KEY_CALC_ID = "calc_id";
    public static final String KEY_GUILD_ID = "guild_id";

    public static final String TABLE_RGZBN_GM_CEILING_CANVASES = "rgzbn_gm_ceiling_canvases";
    public static final String KEY_TEXTURE_ID = "texture_id";
    public static final String KEY_COLOR_ID = "color_id";
    public static final String KEY_MANUFACTURER_ID = "manufacturer_id";
    public static final String KEY_WIDTH = "width";

    public static final String TABLE_RGZBN_GM_CEILING_CANVASES_MANUFACTURERS = "rgzbn_gm_ceiling_canvases_manufacturers";
    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTRY = "country";

    public static final String TABLE_RGZBN_GM_CEILING_CANVASES_DEALER_PRICE = "rgzbn_gm_ceiling_canvases_dealer_price";
    public static final String KEY_CANVAS_ID = "canvas_id";
    public static final String KEY_VALUE = "value";

    public static final String TABLE_RGZBN_GM_CEILING_CLIENTS = "rgzbn_gm_ceiling_clients";
    public static final String KEY_CLIENT_NAME = "client_name";
    public static final String KEY_CLIENT_DATA_ID = "client_data_id";
    public static final String KEY_TYPE_ID = "type_id";
    public static final String KEY_MANAGER_ID = "manager_id";
    public static final String KEY_SEX = "sex";

    public static final String TABLE_RGZBN_GM_CEILING_CLIENTS_DATA = "rgzbn_gm_ceiling_clients_data";

    public static final String TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS = "rgzbn_gm_ceiling_clients_contacts";

    public static final String TABLE_RGZBN_GM_CEILING_CLIENTS_TYPE = "rgzbn_gm_ceiling_clients_type";
    public static final String KEY_TITLE = "title";

    public static final String TABLE_RGZBN_GM_CEILING_COLORS = "rgzbn_gm_ceiling_colors";
    public static final String KEY_HEX = "hex";

    public static final String TABLE_RGZBN_GM_CEILING_COMPONENTS = "rgzbn_gm_ceiling_components";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_CODE = "code";

    public static final String TABLE_RGZBN_GM_CEILING_COMPONENTS_DEALER_PRICE = "rgzbn_gm_ceiling_components_dealer_price";

    public static final String TABLE_RGZBN_GM_CEILING_COMPONENTS_OPTION = "rgzbn_gm_ceiling_components_option";
    public static final String KEY_COMPONENT_ID = "component_id";
    public static final String KEY_COUNT_SALE = "count_sale";

    public static final String TABLE_RGZBN_GM_CEILING_CORNICE = "rgzbn_gm_ceiling_cornice";     // корниз шторный покупка у ГМ
    public static final String KEY_CALCULATION_ID = "calculation_id";
    public static final String KEY_N15_COUNT = "n15_count";
    public static final String KEY_N15_TYPE = "n15_type";
    public static final String KEY_N15_SIZE = "n15_size";

    public static final String TABLE_RGZBN_GM_CEILING_DEALER_INFO = "rgzbn_gm_ceiling_dealer_info";
    public static final String KEY_DEALER_CANVASES_MARGIN = "dealer_canvases_margin";
    public static final String KEY_DEALER_COMPONENTS_MARGIN = "dealer_components_margin";
    public static final String KEY_DEALER_MOUNTING_MARGIN = "dealer_mounting_margin";
    public static final String KEY_GM_CANVASES_MARGIN = "gm_canvases_margin";
    public static final String KEY_GM_COMPONENTS_MARGIN = "gm_components_margin";
    public static final String KEY_GM_MOUNTING_MARGIN = "gm_mounting_margin";
    public static final String KEY_DEALER_TYPE = "dealer_type";
    public static final String KEY_DISCOUNT = "discount";
    public static final String KEY_CITY = "city";

    public static final String TABLE_RGZBN_GM_CEILING_DIFFUSERS = "rgzbn_gm_ceiling_diffusers";     // диффузор
    public static final String KEY_N23_COUNT = "n23_count";
    public static final String KEY_N23_SIZE = "n23_size";

    public static final String TABLE_RGZBN_GM_CEILING_ECOLA = "rgzbn_gm_ceiling_ecola";     // светильники-экола
    public static final String KEY_N26_COUNT = "n26_count";
    public static final String KEY_N26_ILLUMINATOR = "n26_illuminator";
    public static final String KEY_N26_LAMP = "n26_lamp";

    public static final String TABLE_RGZBN_GM_CEILING_FIXTURES = "rgzbn_gm_ceiling_fixtures";   //  светильники
    public static final String KEY_N13_COUNT = "n13_count";
    public static final String KEY_N13_TYPE = "n13_type";
    public static final String KEY_N13_SIZE = "n13_size";

    public static final String TABLE_RGZBN_GM_CEILING_GROUPS = "rgzbn_gm_ceiling_groups";
    public static final String KEY_BRIGADIR_ID = "brigadir_id";

    public static final String TABLE_RGZBN_GM_CEILING_GROUPS_MAP = "rgzbn_gm_ceiling_groups_map";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_MOUNTER_ID = "mounter_id";

    public static final String TABLE_RGZBN_GM_CEILING_GUILD = "rgzbn_gm_ceiling_guild";

    public static final String TABLE_RGZBN_GM_CEILING_HOODS = "rgzbn_gm_ceiling_hoods";     //  вентиляция
    public static final String KEY_N22_COUNT = "n22_count";
    public static final String KEY_N22_TYPE = "n22_type";
    public static final String KEY_N22_SIZE = "n22_size";

    public static final String TABLE_RGZBN_GM_CEILING_MOUNT = "rgzbn_gm_ceiling_mount";
    public static final String KEY_MP1 = "mp1";
    public static final String KEY_MP2 = "mp2";
    public static final String KEY_MP3 = "mp3";
    public static final String KEY_MP4 = "mp4";
    public static final String KEY_MP5 = "mp5";
    public static final String KEY_MP6 = "mp6";
    public static final String KEY_MP7 = "mp7";
    public static final String KEY_MP8 = "mp8";
    public static final String KEY_MP9 = "mp9";
    public static final String KEY_MP10 = "mp10";
    public static final String KEY_MP11 = "mp11";
    public static final String KEY_MP12 = "mp12";
    public static final String KEY_MP13 = "mp13";
    public static final String KEY_MP14 = "mp14";
    public static final String KEY_MP15 = "mp15";
    public static final String KEY_MP16 = "mp16";
    public static final String KEY_MP17 = "mp17";
    public static final String KEY_MP18 = "mp18";
    public static final String KEY_MP19 = "mp19";
    public static final String KEY_MP20 = "mp20";
    public static final String KEY_MP21 = "mp21";
    public static final String KEY_MP22 = "mp22";
    public static final String KEY_MP23 = "mp23";
    public static final String KEY_MP24 = "mp24";
    public static final String KEY_MP25 = "mp25";
    public static final String KEY_MP26 = "mp26";
    public static final String KEY_MP27 = "mp27";
    public static final String KEY_MP28 = "mp28";
    public static final String KEY_MP29 = "mp29";
    public static final String KEY_MP30 = "mp30";
    public static final String KEY_MP31 = "mp31";
    public static final String KEY_MP32 = "mp32";
    public static final String KEY_MP33 = "mp33";
    public static final String KEY_MP34 = "mp34";
    public static final String KEY_MP35 = "mp35";
    public static final String KEY_MP36 = "mp36";
    public static final String KEY_MP37 = "mp37";
    public static final String KEY_MP38 = "mp38";
    public static final String KEY_MP39 = "mp39";
    public static final String KEY_MP40 = "mp40";
    public static final String KEY_MP41 = "mp41";
    public static final String KEY_MP42 = "mp42";
    public static final String KEY_MP43 = "mp43";
    public static final String KEY_MIN_SUM = "min_sum";
    public static final String KEY_MIN_COMPONENTS_SUM = "min_components_sum";

    public static final String TABLE_RGZBN_GM_CEILING_OUTLAY = "rgzbn_gm_ceiling_outlay";
    public static final String KEY_CASH = "cash";
    public static final String KEY_STATUS = "status";

    public static final String TABLE_RGZBN_GM_CEILING_PAYMENT = "rgzbn_gm_ceiling_payment";
    public static final String KEY_METHOD = "method";

    public static final String TABLE_RGZBN_GM_CEILING_PHONE = "rgzbn_gm_ceiling_phone";
    public static final String KEY_PHONE = "phone";

    public static final String TABLE_RGZBN_GM_CEILING_PIPES = "rgzbn_gm_ceiling_pipes";     // трубы
    public static final String KEY_N14_COUNT = "n14_count";
    public static final String KEY_N14_SIZE = "n14_size";

    public static final String TABLE_RGZBN_GM_CEILING_PROJECTS = "rgzbn_gm_ceiling_projects";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_PROJECT_INFO = "project_info";
    public static final String KEY_PROJECT_STATUS = "project_status";
    public static final String KEY_PROJECT_MOUNTING_DATE = "project_mounting_date";
    public static final String KEY_PROJECT_MOUNTING_START = "project_mounting_start";
    public static final String KEY_PROJECT_MOUNTING_END = "project_mounting_end";
    public static final String KEY_PROJECT_MOUNTER = "project_mounter";
    public static final String KEY_PROJECT_NOTE = "project_note";
    public static final String KEY_GM_CALCULATOR_NOTE = "gm_calculator_note";
    public static final String KEY_DEALER_CALCULATOR_NOTE = "dealer_calculator_note";
    public static final String KEY_GM_MANAGER_NOTE = "gm_manager_note";
    public static final String KEY_GM_CHIEF_NOTE = "gm_chief_note";
    public static final String KEY_DEALER_CHIEF_NOTE = "dealer_chief_note";
    public static final String KEY_DEALER_MANAGER_NOTE = "dealer_manager_note";
    public static final String KEY_BUH_NOTE = "buh_note";
    public static final String KEY_PROJECT_CALCULATION_DATE = "project_calculation_date";
    public static final String KEY_PROJECT_CALCULATOR = "project_calculator";
    public static final String KEY_WHO_CALCULATE = "who_calculate";
    public static final String KEY_WHO_MOUNTING = "who_mounting";
    public static final String KEY_PROJECT_VERDICT = "project_verdict";
    public static final String KEY_PROJECT_DISCOUNT = "project_discount";
    public static final String KEY_CREATED = "created";
    public static final String KEY_CLOSED = "closed";
    public static final String KEY_PROJECT_CHECK = "project_check";
    public static final String KEY_SUM_CHECK = "sum_check";
    public static final String KEY_COST_CHECK = "cost_check";
    public static final String KEY_SPEND_CHECK = "spend_check";
    public static final String KEY_MOUNTING_CHECK = "mounting_check";
    public static final String KEY_NEW_PROJECT_SUM = "new_project_sum";
    public static final String KEY_NEW_PROJECT_SPEND = "new_project_spend";
    public static final String KEY_NEW_PROJECT_MOUNTING = "new_project_mounting";
    public static final String KEY_NEW_EXTRA_SPEND = "new_extra_spend";
    public static final String KEY_PROJECT_SUM = "project_sum";
    public static final String KEY_SALARY_SUM = "salary_sum";
    public static final String KEY_EXTRA_SPEND = "extra_spend";
    public static final String KEY_PENALTY = "penalty";
    public static final String KEY_BONUS = "bonus";
    public static final String KEY_CALCULATED_BY = "calculated_by";
    public static final String KEY_APPROVED_BY = "approved_by";
    public static final String KEY_CHECKED_BY = "checked_by";
    public static final String KEY_READ_BY_MANAGER = "read_by_manager";
    public static final String KEY_API_PHONE_ID = "api_phone_id";
    public static final String KEY_READ_BY_MOUNTER = "read_by_mounter";
    public static final String KEY_CHANGE_TIME = "change_time";
    public static final String KEY_NEW_MOUNT_SUM = "new_mount_sum";
    public static final String KEY_NEW_MATERIAL_SUM = "new_material_sum";
    public static final String KEY_TRANSPORT = "transport";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DISTANCE_COL = "distance_col";

    public static final String TABLE_RGZBN_GM_CEILING_PROJECTS_HISTORY = "rgzbn_gm_ceiling_projects_history";
    public static final String KEY_CHANGE_VIEW = "change_view";
    public static final String KEY_CHANGE_TEXT = "change_text";
    public static final String KEY_DATE_OF_CHANGE = "date_of_change";

    public static final String TABLE_RGZBN_GM_CEILING_SPENDS = "rgzbn_gm_ceiling_spends";
    public static final String KEY_SPEND_SUM = "spend_sum";
    public static final String KEY_SPEND_NOTE = "spend_note";

    public static final String TABLE_RGZBN_GM_CEILING_STATUS = "rgzbn_gm_ceiling_status";

    public static final String TABLE_RGZBN_GM_CEILING_TEXTURES = "rgzbn_gm_ceiling_textures";
    public static final String KEY_TEXTURE_TITLE = "texture_title";
    public static final String KEY_TEXTURE_COLORED = "texture_colored";

    public static final String TABLE_RGZBN_GM_CEILING_TYPE = "rgzbn_gm_ceiling_type";
    public static final String KEY_PARENT = "parent";

    public static final String TABLE_RGZBN_GM_CEILING_TYPE_OPTION = "rgzbn_gm_ceiling_type_option";
    public static final String KEY_DEFAULT_COMP_OPTION_ID = "default_comp_option_id";

    public static final String TABLE_MOUNTING_DATA = "mounting_data";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_GM_SALARY = "gm_salary";
    public static final String KEY_GM_SALARY_TOTAL = "gm_salary_total";
    public static final String KEY_DEALER_SALARY = "dealer_salary";
    public static final String KEY_DEALER_SALARY_TOTAL = "dealer_salary_total";
    public static final String KEY_PRICE_WITH_GM_MARGIN = "price_with_gm_margin";
    public static final String KEY_TOTAL_WITH_GM_MARGIN = "total_with_gm_margin";
    public static final String KEY_PRICE_WITH_GM_DEALER_MARGIN = "price_with_gm_dealer_margin";
    public static final String KEY_TOTAL_WITH_GM_DEALER_MARGIN = "total_with_gm_dealer_margin";
    public static final String KEY_PRICE_WITH_DEALER_MARGIN = "price_with_dealer_margin";
    public static final String KEY_TOTAL_WITH_DEALER_MARGIN = "total_with_dealer_margin";

    public static final String TABLE_COMPONENT_ITEM = "component_item";
    public static final String KEY_COMP_ID = "id";
    public static final String KEY_STACK = "stack";
    public static final String KEY_SELF_PRICE = "self_price";
    public static final String KEY_SELF_TOTAL = "self_total";
    public static final String KEY_GM_PRICE = "gm_price";
    public static final String KEY_GM_TOTAL = "gm_total";
    public static final String KEY_DEALER_PRICE = "dealer_price";
    public static final String KEY_DEALER_TOTAL = "dealer_total";

    public static final String TABLE_USERS = "rgzbn_users";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BLOCK = "block";
    public static final String KEY_SENDEMAIL = "sendEmail";
    public static final String KEY_REGISTERDATE = "registerDate";
    public static final String KEY_LASTVISITDATE = "lastvisitDate";
    public static final String KEY_ACTIVATION = "activation";
    public static final String KEY_PARAMS = "params";
    public static final String KEY_LASTRESETTIME = "lastResetTime";
    public static final String KEY_RESETCOUNT = "resetCount";
    public static final String KEY_OTPKEY = "otpKey";
    public static final String KEY_OTEP = "otep";
    public static final String KEY_REQUIRERESET = "requireReset";
    public static final String KEY_WAGES = "wages";

    public static final String TABLE_RGZBN_USER_USERGROUP_MAP = "rgzbn_user_usergroup_map";

    public static final String TABLE_RGZBN_USERGROUPS = "rgzbn_usergroups";
    public static final String KEY_PARENT_ID = "parent_id";
    public static final String KEY_LFT = "lft";
    public static final String KEY_RGT = "rgt";

    public static final String HISTORY_SEND_TO_SERVER = "history_send_to_server";
    public static final String KEY_ID_NEW = "id_new";
    public static final String KEY_ID_OLD = "id_old";
    public static final String KEY_NAME_TABLE = "name_table";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_TYPE = "type";

    public static final String HISTORY_IMPORT_TO_SERVER = "history_import_to_server";

    public static final String TABLE_RGZBN_GM_CEILING_MOUNTERS = "rgzbn_gm_ceiling_mounters";
    public static final String KEY_PASPORT = "pasport";

    public static final String TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP = "rgzbn_gm_ceiling_mounters_map";
    public static final String KEY_ID_MOUNTER = "id_mounter";
    public static final String KEY_ID_BRIGADE = "id_brigade";

    public static final String TABLE_RGZBN_GM_CEILING_PROFIL = "rgzbn_gm_ceiling_profil";
    public static final String KEY_N29_COUNT = "n29_count";
    public static final String KEY_N29_TYPE = "n29_type";
    public static final String KEY_N29_PROFILE = "n29_profile";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_profil (_id INTEGER, " +
                "calculation_id TEXT, n29_count TEXT, n29_type TEXT, n29_profile TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_mounters (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, phone TEXT, pasport TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_mounters_map (id_mounter INTEGER, id_brigade INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS history_import_to_server (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, title TEXT, change_time TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_timing_sync (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type_table INTEGER, id_records TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_analytics_canvases (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_canvas INTEGER, length TEXT, date_update TEXT, price TEXT, type_client INTEGER, user_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_analytics_components (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "component_option_id INTEGER, count INTEGER, price TEXT, date_update TEXT, type_client INTEGER, user_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_calculations (_id INTEGER, " +
                "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                "calculation_title TEXT DEFAULT NULL, project_id INTEGER, n3 INTEGER DEFAULT NULL, " +
                "n4 TEXT DEFAULT NULL, n5 TEXT DEFAULT NULL, n6 TEXT DEFAULT NULL, n7 TEXT DEFAULT NULL, n8 TEXT DEFAULT NULL, n9 TEXT DEFAULT NULL, " +
                "n10 TEXT DEFAULT NULL, n11 TEXT DEFAULT NULL, n12 INTEGER DEFAULT NULL, n16 TEXT DEFAULT NULL, n17 TEXT DEFAULT NULL, " +
                "n18 TEXT DEFAULT NULL, n19 TEXT DEFAULT NULL, n20 TEXT DEFAULT NULL, " +
                "n21 TEXT DEFAULT NULL, n24 TEXT DEFAULT NULL, n25 INTEGER DEFAULT NULL, n27 TEXT DEFAULT NULL, n28 TEXT DEFAULT NULL, " +
                "n30 TEXT DEFAULT NULL, n31 TEXT DEFAULT NULL, n32 TEXT DEFAULT NULL, height TEXT DEFAULT NULL, " +
                "components_sum TEXT DEFAULT NULL, canvases_sum TEXT DEFAULT NULL, " +
                "mounting_sum INTEGER DEFAULT NULL, dealer_components_sum TEXT DEFAULT NULL, dealer_canvases_sum TEXT DEFAULT NULL, " +
                "dop_krepezh TEXT DEFAULT NULL, extra_components TEXT DEFAULT NULL, extra_mounting TEXT DEFAULT NULL, color INTEGER DEFAULT NULL, " +
                "details TEXT DEFAULT NULL, calc_image TEXT DEFAULT NULL, calc_data TEXT DEFAULT NULL, calc_point TEXT DEFAULT NULL, " +
                "cut_image TEXT DEFAULT NULL, cut_data TEXT DEFAULT NULL," +
                " offcut_square TEXT DEFAULT NULL, original_sketch TEXT DEFAULT NULL, discount TEXT DEFAULT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_calc_guild (calc_id INTEGER, guild_id INTEGER, user_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases (_id INTEGER, " +
                "texture_id INTEGER, color_id INTEGER, manufacturer_id INTEGER, width TEXT, price TEXT, count INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases_manufacturers (_id INTEGER, " +
                "name TEXT, country TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases_dealer_price (user_id INTEGER, " +
                "canvas_id INTEGER, price FLOAT, value FLOAT, type INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_clients (_id INTEGER, " +
                "client_name TEXT, client_data_id INTEGER, type_id INTEGER, dealer_id INTEGER, manager_id INTEGER, " +
                "created TEXT, sex TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_clients_contacts (_id INTEGER, " +
                "client_id INTEGER, phone TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_clients_data (_id INTEGER PRIMARY KEY AUTOINCREMENT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_clients_type (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_colors (_id INTEGER, " +
                "title TEXT, file TEXT, hex TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_components (_id INTEGER, " +
                "title TEXT, unit TEXT, code TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_components_dealer_price (user_id INTEGER, " +
                "component_id INTEGER, price FLOAT, value FLOAT, type INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_components_option (_id INTEGER, " +
                "component_id INTEGER, title TEXT, price TEXT, count INTEGER, count_sale TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_cornice (_id INTEGER, " +
                "calculation_id INTEGER, n15_count INTEGER, n15_type INTEGER, n15_size INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_dealer_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dealer_canvases_margin INTEGER, dealer_components_margin INTEGER, dealer_mounting_margin INTEGER, " +
                "gm_canvases_margin INTEGER, " +
                "gm_components_margin INTEGER, gm_mounting_margin INTEGER, dealer_id INTEGER, dealer_type INTEGER, discount INTEGER, " +
                "city TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_diffusers (_id INTEGER, " +
                "calculation_id INTEGER, n23_count INTEGER, n23_size INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_ecola (_id INTEGER, " +
                "calculation_id INTEGER, n26_count INTEGER, n26_illuminator INTEGER, n26_lamp INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_fixtures (_id INTEGER, " +
                "calculation_id INTEGER, n13_count INTEGER, n13_type INTEGER, n13_size INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_groups (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, brigadir_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_groups_map (group_id INTEGER, mounter_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_guild (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, price TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_hoods (_id INTEGER, " +
                "calculation_id INTEGER, n22_count INTEGER, n22_type INTEGER, n22_size INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_mount (_id INTEGER, " +
                "mp1 TEXT, mp2 TEXT, mp3 TEXT, mp4 TEXT, mp5 TEXT, mp6 TEXT, mp7 TEXT, mp8 TEXT, mp9 TEXT, mp10 TEXT, " +
                "mp11 TEXT, mp12 TEXT, mp13 TEXT, mp14 TEXT, mp15 TEXT, mp16 TEXT, mp17 TEXT, mp18 TEXT, " +
                "mp19 TEXT, mp20 TEXT, mp21 TEXT, mp22 TEXT, mp23 TEXT, mp24 TEXT, mp25 TEXT, mp26 TEXT, mp27 TEXT, mp28 TEXT" +
                ", mp29 TEXT, mp30 TEXT, mp31 TEXT, mp32 TEXT, mp33 TEXT, mp34 TEXT, mp35 TEXT, mp36 TEXT, mp37 TEXT, mp38 TEXT" +
                ", mp39 TEXT, mp40 TEXT, mp41 TEXT, mp42 TEXT, mp43 TEXT, min_sum INTEGER, min_components_sum INTEGER," +
                " transport TEXT, user_id INTEGER, distance TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_outlay (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, cash TEXT, status TEXT, date TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_phone (user_id TEXT, phone TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_pipes (_id INTEGER, " +
                "calculation_id INTEGER, n14_count INTEGER, n14_size INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_projects (_id INTEGER, " +
                "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                "client_id INTEGER, project_info TEXT, project_status INTEGER, project_mounting_date TEXT, " +
                "project_mounting_start TEXT, project_mounting_end TEXT, project_mounter INTEGER, project_note TEXT, " +
                "gm_calculator_note TEXT, dealer_calculator_note TEXT, gm_manager_note TEXT, gm_chief_note TEXT, " +
                "dealer_chief_note TEXT, dealer_manager_note TEXT, buh_note TEXT, project_calculation_date TEXT, " +
                "project_calculator INTEGER, who_calculate TEXT, project_verdict INTEGER, project_discount INTEGER, " +
                "created TEXT, closed TEXT, project_check TEXT, sum_check TEXT, cost_check TEXT, spend_check TEXT, " +
                "mounting_check TEXT, new_project_sum TEXT, new_project_spend TEXT, new_project_mounting TEXT, new_extra_spend TEXT, " +
                "gm_canvases_margin INTEGER, gm_components_margin INTEGER, gm_mounting_margin INTEGER, dealer_canvases_margin INTEGER, " +
                "dealer_components_margin INTEGER, dealer_mounting_margin INTEGER, project_sum TEXT, salary_sum TEXT, extra_spend TEXT, " +
                "penalty TEXT, bonus TEXT, calculated_by INTEGER, approved_by INTEGER, checked_by INTEGER, read_by_manager INTEGER, api_phone_id TEXT, read_by_mounter TEXT, " +
                " change_time TEXT, new_mount_sum TEXT, new_material_sum TEXT, transport TEXT, distance TEXT, distance_col TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_projects_history (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "project_id INTEGER, change_view TEXT, change_text TEXT, date_of_change TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_spends (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                "project_id INTEGER, spend_sum TEXT, spend_note TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_status (_id INTEGER, " +
                "title TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_textures (_id INTEGER, " +
                "texture_title TEXT, texture_colored TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_type (_id INTEGER, " +
                "parent INTEGER, title TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_type_option (_id INTEGER, " +
                "type_id INTEGER, component_id INTEGER, default_comp_option_id INTEGER, count INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS mounting_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " title TEXT, quantity TEXT, gm_salary TEXT, gm_salary_total TEXT, dealer_salary TEXT, dealer_salary_total TEXT, " +
                " price_with_gm_margin TEXT, total_with_gm_margin TEXT, price_with_gm_dealer_margin TEXT, total_with_gm_dealer_margin TEXT, " +
                " price_with_dealer_margin TEXT, total_with_dealer_margin TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS component_item (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " title TEXT, unit TEXT, id TEXT, quantity TEXT, stack TEXT, self_price TEXT, " +
                " self_total TEXT, gm_price TEXT, gm_total TEXT, dealer_price TEXT, dealer_total TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS table_other_comp (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "calculation_id INTEGER, name TEXT, price TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS table_other_work (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "calculation_id INTEGER, name TEXT, price TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_users (_id INTEGER, " +
                "name TEXT, username TEXT, email TEXT, password TEXT, block INTEGER, sendEmail INTEGER, registerDate TEXT, " +
                "lastvisitDate TEXT, activation TEXT, params TEXT, lastResetTime TEXT, resetCount INTEGER, otpKey TEXT, otep TEXT, " +
                "requireReset INTEGER, dealer_id INTEGER, discount INTEGER, dealer_type INTEGER, wages TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_usergroups (_id INTEGER, parent_id INTEGER, lft INTEGER, rgt INTEGER, title TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_user_usergroup_map (_id INTEGER, user_id TEXT, group_id TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS history_send_to_server (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_new INTEGER, id_old INTEGER, " +
                "name_table TEXT, sync INTEGER, type TEXT, date TEXT, date_sync TEXT, status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 14) {
            db.execSQL("ALTER TABLE  rgzbn_gm_ceiling_calculations ADD COLUMN dealer_components_sum TEXT");
            db.execSQL("ALTER TABLE  rgzbn_gm_ceiling_calculations ADD COLUMN dealer_canvases_sum TEXT");
        }
        if (oldVersion < 15) {
            db.execSQL("ALTER TABLE  rgzbn_gm_ceiling_dealer_info ADD COLUMN city TEXT");
        }

        if (oldVersion < 16) {
            db.execSQL("DROP TABLE IF EXISTS rgzbn_gm_ceiling_calculations");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_calculations (_id INTEGER, " +
                    "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                    "calculation_title TEXT DEFAULT NULL, project_id INTEGER, n1 INTEGER DEFAULT NULL, n2 INTEGER DEFAULT NULL, n3 INTEGER DEFAULT NULL, " +
                    "n4 TEXT DEFAULT NULL, n5 TEXT DEFAULT NULL, n6 TEXT DEFAULT NULL, n7 TEXT DEFAULT NULL, n8 TEXT DEFAULT NULL, n9 TEXT DEFAULT NULL, " +
                    "n10 TEXT DEFAULT NULL, n11 TEXT DEFAULT NULL, n12 INTEGER DEFAULT NULL, n16 TEXT DEFAULT NULL, n17 TEXT DEFAULT NULL, " +
                    "n18 TEXT DEFAULT NULL, n19 TEXT DEFAULT NULL, n20 TEXT DEFAULT NULL, " +
                    "n21 TEXT DEFAULT NULL, n24 TEXT DEFAULT NULL, n25 INTEGER DEFAULT NULL, n27 TEXT DEFAULT NULL, n28 TEXT DEFAULT NULL, " +
                    "n30 TEXT DEFAULT NULL, n31 TEXT DEFAULT NULL, n32 TEXT DEFAULT NULL, height TEXT DEFAULT NULL, " +
                    "components_sum TEXT DEFAULT NULL, canvases_sum TEXT DEFAULT NULL, " +
                    "mounting_sum INTEGER DEFAULT NULL, dealer_components_sum TEXT DEFAULT NULL, dealer_canvases_sum TEXT DEFAULT NULL, " +
                    "dop_krepezh TEXT DEFAULT NULL, extra_components TEXT DEFAULT NULL, extra_mounting TEXT DEFAULT NULL, color INTEGER DEFAULT NULL, " +
                    "details TEXT DEFAULT NULL, calc_image TEXT DEFAULT NULL, calc_data TEXT DEFAULT NULL, calc_point TEXT DEFAULT NULL, " +
                    "cut_image TEXT DEFAULT NULL, cut_data TEXT DEFAULT NULL," +
                    " offcut_square TEXT DEFAULT NULL, original_sketch TEXT DEFAULT NULL, discount TEXT DEFAULT NULL)");


            SharedPreferences SP_end = mContext.getSharedPreferences("user_id", MODE_PRIVATE);
            String user_id = SP_end.getString("", "");
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf("0000-00-00 00:00:00"));
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
        }

        if (oldVersion < 18) {

            //1
            db.execSQL("DROP TABLE IF EXISTS rgzbn_gm_ceiling_canvases");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases (_id INTEGER, " +
                    "texture_id INTEGER, color_id INTEGER, manufacturer_id INTEGER, width TEXT, price TEXT, count INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases_manufacturers (_id INTEGER, " +
                    "name TEXT, country TEXT)");

            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf("0000-00-00 00:00:00"));
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"material"});

            //2
            db.execSQL("DROP TABLE IF EXISTS rgzbn_gm_ceiling_calculations");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_calculations (_id INTEGER, " +
                    "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                    "calculation_title TEXT DEFAULT NULL, project_id INTEGER, n3 INTEGER DEFAULT NULL, " +
                    "n4 TEXT DEFAULT NULL, n5 TEXT DEFAULT NULL, n6 TEXT DEFAULT NULL, n7 TEXT DEFAULT NULL, n8 TEXT DEFAULT NULL, n9 TEXT DEFAULT NULL, " +
                    "n10 TEXT DEFAULT NULL, n11 TEXT DEFAULT NULL, n12 INTEGER DEFAULT NULL, n16 TEXT DEFAULT NULL, n17 TEXT DEFAULT NULL, " +
                    "n18 TEXT DEFAULT NULL, n19 TEXT DEFAULT NULL, n20 TEXT DEFAULT NULL, " +
                    "n21 TEXT DEFAULT NULL, n24 TEXT DEFAULT NULL, n25 INTEGER DEFAULT NULL, n27 TEXT DEFAULT NULL, n28 TEXT DEFAULT NULL, " +
                    "n30 TEXT DEFAULT NULL, n31 TEXT DEFAULT NULL, n32 TEXT DEFAULT NULL, height TEXT DEFAULT NULL, " +
                    "components_sum TEXT DEFAULT NULL, canvases_sum TEXT DEFAULT NULL, " +
                    "mounting_sum INTEGER DEFAULT NULL, dealer_components_sum TEXT DEFAULT NULL, dealer_canvases_sum TEXT DEFAULT NULL, " +
                    "dop_krepezh TEXT DEFAULT NULL, extra_components TEXT DEFAULT NULL, extra_mounting TEXT DEFAULT NULL, color INTEGER DEFAULT NULL, " +
                    "details TEXT DEFAULT NULL, calc_image TEXT DEFAULT NULL, calc_data TEXT DEFAULT NULL, calc_point TEXT DEFAULT NULL, " +
                    "cut_image TEXT DEFAULT NULL, cut_data TEXT DEFAULT NULL," +
                    " offcut_square TEXT DEFAULT NULL, original_sketch TEXT DEFAULT NULL, discount TEXT DEFAULT NULL)");


            SharedPreferences SP_end = mContext.getSharedPreferences("user_id", MODE_PRIVATE);
            String user_id = SP_end.getString("", "");
            values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf("0000-00-00 00:00:00"));
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
        }

        if (oldVersion < 19) {
            //1
            db.execSQL("DROP TABLE IF EXISTS rgzbn_gm_ceiling_projects");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_projects (_id INTEGER, " +
                    "ordering INTEGER, state TEXT, checked_out INTEGER, checked_out_time TEXT, created_by INTEGER, modified_by INTEGER, " +
                    "client_id INTEGER, project_info TEXT, project_status INTEGER, project_mounting_date TEXT, " +
                    "project_mounting_start TEXT, project_mounting_end TEXT, project_mounter INTEGER, project_note TEXT, " +
                    "gm_calculator_note TEXT, dealer_calculator_note TEXT, gm_manager_note TEXT, gm_chief_note TEXT, " +
                    "dealer_chief_note TEXT, dealer_manager_note TEXT, buh_note TEXT, project_calculation_date TEXT, " +
                    "project_calculator INTEGER, who_calculate TEXT, project_verdict INTEGER, project_discount INTEGER, " +
                    "created TEXT, closed TEXT, project_check TEXT, sum_check TEXT, cost_check TEXT, spend_check TEXT, " +
                    "mounting_check TEXT, new_project_sum TEXT, new_project_spend TEXT, new_project_mounting TEXT, new_extra_spend TEXT, " +
                    "gm_canvases_margin INTEGER, gm_components_margin INTEGER, gm_mounting_margin INTEGER, dealer_canvases_margin INTEGER, " +
                    "dealer_components_margin INTEGER, dealer_mounting_margin INTEGER, project_sum TEXT, salary_sum TEXT, extra_spend TEXT, " +
                    "penalty TEXT, bonus TEXT, calculated_by INTEGER, approved_by INTEGER, checked_by INTEGER, read_by_manager INTEGER, api_phone_id TEXT, read_by_mounter TEXT, " +
                    " change_time TEXT, new_mount_sum TEXT, new_material_sum TEXT, transport TEXT, distance TEXT, distance_col TEXT)");


            SharedPreferences SP_end = mContext.getSharedPreferences("user_id", MODE_PRIVATE);
            String user_id = SP_end.getString("", "");
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf("0000-00-00 00:00:00"));
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});

            //2
            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_canvases_dealer_price (user_id INTEGER, " +
                    "canvas_id INTEGER, price INTEGER, value INTEGER, type INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_gm_ceiling_components_dealer_price (user_id INTEGER, " +
                    "canvas_id INTEGER, price INTEGER, value INTEGER, type INTEGER)");

            //3
            db.execSQL("DROP TABLE IF EXISTS rgzbn_user_usergroup_map");

            db.execSQL("CREATE TABLE IF NOT EXISTS rgzbn_user_usergroup_map (_id INTEGER, user_id TEXT, group_id TEXT)");

            values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf("0000-00-00 00:00:00"));
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"dealer"});
        }
    }

}