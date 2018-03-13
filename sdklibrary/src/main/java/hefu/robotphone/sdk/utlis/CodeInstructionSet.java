package hefu.robotphone.sdk.utlis;

/**
 * Created by lenovo on 2016/3/11.
 */
public class CodeInstructionSet {

    public static final int
            DESTINATION_ID = 0,
    /// Common. 范围: 0x01 ~ 0x3F

    /* 用户获取服务器列表，参数为'username password' */
    BUF_ACTION_USER_QUERY_SERVER_LIST = 0x1,
    /* 设备获取服务器列表 */
    BUF_ACTION_DEVICE_QUERY_SERVER_LIST = 0x2,
    /* 配置导入,参数为'config_table' */
    BUF_ACTION_CONFIG_IMPORT = 0x3,
    /* 配置更新,参数为'config_table' */
    BUF_ACTION_CONFIG_UPDATE = 0x4,
    /* 用户登录，参数为'username password' */
    BUF_ACTION_USER_LOGIN = 0x5,
    /* 设备登录 */
    BUF_ACTION_DEVICE_LOGIN = 0x6,
    /* 用户退出 */
    BUF_ACTION_USER_LOGOUT = 0x7,
    /* 设备退出 */
    BUF_ACTION_DEVICE_LOGOUT = 0x8,
    /* 配置查询 */
    BUF_ACTION_CONFIG_QUERY = 0x9,
    /* 配置变更通知，参数无 */
    BUF_ACTION_CONFIG_CHANGE_NOTIFY = 0xA,
    /* 取消一键呼救 */
    BUF_ACTION_SOS_CANCEL = 0xB,
    /* 修改音量,参数为'volume_value' */
    BUF_ACTION_ADJUST_VOL = 0xC,
    /* 一键呼救 */
    BUF_ACTION_SOS_NOTIFY = 0xD,
    /* 状态上报,参数为'status_list' */
    BUF_ACTION_STATUS_REPORT = 0xE,
    /* 障碍上报，参数为'barrier_value'，其中超声/跌落/碰撞分别为1/2/3 */
    BUF_ACTION_BARRIER_NOTIFY = 0xF,

    /*请求状态上报开始*/
    BUF_ACTION_REQUEST_STATE_REPORT_START=0x10,

    /*请求状态上报结束*/
    BUF_ACTION_REQUEST_STATE_REPORT_STOP=0x11,

    /* 开始录像 */
    BUF_ACTION_REC_START = 0x20,
    /* 结束录像 */
    BUF_ACTION_REC_STOP = 0x21,
    /* 拷贝录像 */
    BUF_ACTION_REC_FILES_COPY = 0x22,
    /* 灯带控制，参数为 'value flash_mode' */
    BUF_ACTION_LED_FLASH = 0x23,
    /* 机器人头的水平绝对角度 */
    BUF_ACTION_ROBOT_HEAD_TURN_ABSTRACT = 0x24,
    /* 机器人头的水平相对角度 */
    BUF_ACTION_ROBOT_HEAD_TURN_RELATIVE = 0x25,
    /* 打开手臂 */
    BUF_ACTION_ROBOT_ARM_OPEN = 0x26,
    /* 关闭手臂 */
    BUF_ACTION_ROBOT_ARM_CLOSE = 0x27,

    BUF_ACTION_ROBOT_ARM_SHAKE = 0x28,

    /* 身份证刷卡结果通知 */
    BUF_ACTION_ID_CARD_RESULT_NOTIFY = 0x30,
    /* 语音广播 */
    BUF_ACTION_VOICE_BROADCAST = 0x31,
    /* 手动升级 */
    BUF_ACTION_FORCE_UPGRADE = 0x32,
    /* 升级状态上报，参数为'status_code' */
    BUF_ACTION_UPGRADE_STATUS_REPORT = 0x33,
    /*修改密码*/
    BUF_ACTION_MODIFY_PASSWORD = 0x34,


    BUF_ACTION_INTRUSION = 0x37,
    BUF_ACTION_INTRUSION_NOTIFY = 0x38,


    /// 空闲状态. 范围: 0x40 ~ 0x4F


    /// 遥控状态. 范围: 0x50 ~ 0x5F

    /* 往前,参数为'speed' */
    BUF_ACTION_FORWARD = 0x50,
    /* 往后,参数为'speed' */
    BUF_ACTION_BACK = 0x51,
    /* 往左,参数为'speed' */
    BUF_ACTION_LEFT = 0x52,
    /* 往右,参数为'speed' */
    BUF_ACTION_RIGHT = 0x53,
    /* 针对所有设备关机或重新开机，参数为'mode' */
    BUF_ACTION_SHUTDOWN = 0x54,


    ///导航. 范围: 0x60~0x6F

    // 导航模式设置，参数是 mode(单点、多点和禁入区)
    BUF_ACTION_NAVI_MODE = 0x60,
    // 地图上点击响应，参数是 goal_result(验证结果)
    BUF_ACTION_NAVI_POINT_CLK_RESPONSE = 0x61,
    // 导航（单点和多点）运行后的结果汇报,参数 nav_result(导航结果)
    BUF_ACTION_NAVI_RESULT_RESPONSE = 0x62,
    // 导航暂停，参数无
    BUF_ACTION_NAVI_FREEZE = 0x63,
    // 导航继续，参数无
    BUF_ACTION_NAVI_CONTINUE = 0x64,
    // 导航取消，参数无
    BUF_ACTION_NAVI_CANCEL = 0x65,
    // 导航开始，参数为 time_peroid(导航时长)
    BUF_ACTION_NAVI_START = 0x66,
    // 导航事件上传和修改. 参数为 'name time_start period point_list'
    BUF_ACTION_NAVI_EVENT_ADD = 0x67,
    // 导航事件删除，参数为'core_id name'
    BUF_ACTION_NAVI_EVENT_DELETE = 0x68,
    // 导航事件查询，参数无
    BUF_ACTION_NAVI_EVENT_QUERY = 0x69,

    BUF_ACTION_NAVI_EVENT_CHANGE_NOTIFY = 0x6A,
    /* 导航任务修改. 参数为 'robot_id index name time_start time_end point_list'
     * */
    BUF_ACTION_NAVI_EVENT_UPDATE = 0x6B,
    /*初始化坐标点*/
    BUF_ACTION_NAVI_UPDATE_POSITION = 0x6D,

    /// 充电. 范围: 0x70 ~ 0x7F

    /* 自动充电 */
    BUF_ACTION_AUTO_CHARGE = 0x70,
    /* 手动充电 */
    BUF_ACTION_MANUAL_CHARGE = 0x71,

    /* 充电子状态上报 */
    // 参数为 charging_state(值 0x01/0x02/0x03/0x04/0x05/0x06
    // 分别为寻充/正在充电/充电完毕/充电失败/找不到充电器/充电过程被中断)
    BUF_ACTION_CHARGING_SUBSTATE_REPORT = 0x72,
    // 设置充电桩的位置，参数为'x y'
    BUF_ACTION_CHARGING_POINT_SET = 0x73,

    /// 建图. 范围: 0x80 ~ 0x8F

    /* GPS位置上报 */
    BUF_ACTION_GPS_POSITION_REPORT = 0x80,
    /* GPS位置请求开始 */
    BUF_ACTION_GPS_POSITION_GET_START = 0x81,
    /* GPS位置请求结束 */
    BUF_ACTION_GPS_POSITION_GET_STOP = 0x82,
    /* 开始建图 */
    BUF_ACTION_MAKE_MAP_START = 0x83,
    /* 保存地图 */
    BUF_ACTION_MAKE_MAP_SAVE = 0x84,
    /* 取消建图 */
    BUF_ACTION_MAKE_MAP_CANCEL = 0x85,
    /*地图传输局域网握手*/
    BUF_ACTION_MAKE_MAP_PHONE_HANDSHAKE = 0x86,
    /*局域网预览地图传输*/
    BUF_ACTION_MAKE_MAP_PHONE_OREVIEW = 0x87,
    /*局域网最终地图*/
    BUF_ACTION_MAKE_MAP_FINAL_MAP = 0x88,
    /*局域网实时位子*/
    BUF_ACTION_MAKE_MAP_REALTIME_POS = 0x89,

    BUF_ACTION_MAP_SYNC = 0X8B,

    BUF_ACTION_NAVLIST_SAVE = 0X8D,


    /// 错误处理. 范围: 0x90 ~ 0x9F


    /// 制暴状态. 范围: 0xA0 ~ 0xAF

    /* 开启制暴模式 */
    BUF_ACTION_AT_START = 0xA0,
    /* 打开叉子 */
    BUF_ACTION_SHOW_CRACK_FORK = 0xA1,
    /* 关闭叉子 */
    BUF_ACTION_HIDE_CRACK_FORK = 0xA2,
    /* 放电 */
    BUF_ACTION_TOUCH_ELE_STICK = 0xA3,
    /* 打开警报 */
    BUF_ACTION_TURN_ON_ALARM = 0xA4,
    /* 关闭警报 */
    BUF_ACTION_TURN_OFF_ALARM = 0xA5,
    /* 关闭制暴模式 */
    BUF_ACTION_AT_STOP = 0xA6,
    /*开启跟随*/
    BUF_ACTION_START_FOLLOW = 0xA7,
    /*停止跟随*/
    BUF_ACTION_STOP_FOLLOW = 0xA8,

    /// 交互. 范围: 0xB0 ~ 0xBF

    /* 开启交互 */
    BUF_ACTION_CHAT_START = 0xB0,
    /* 结束交互 */
    BUF_ACTION_CHAT_STOP = 0xB1,
    /* 音视频通话的帐号更新，参数为'username password' */
    BUF_ACTION_CHAT_ACCOUNT_UPDATE = 0xB2,
    /* 请求通话 */
    BUF_ACTION_CHAT_ASK = 0xB3,
    /* 回复请求通话 */
    BUF_ACTION_CHAT_RESPONSE_ASK = 0xB4,
    /* 音视频通话的帐号查询，参数为'robot_id device_type' */
    BUF_ACTION_CHAT_ACCOUNT_QUERY = 0xB5,
    /* 音视频通话挂断，参数为'device_type' */
    BUF_ACTION_CHAT_VOICE_HANG_UP = 0xB6,

    /// 急停. 范围: 0xC0 ~ 0xCF

    /* 急停按钮按下 */
    BUF_ACTION_HW_STOP_BTN_PRESS = 0xC0,
    /* 急停按钮恢复 */
    BUF_ACTION_HW_STOP_BTN_RELEASE = 0xC1,


    /// 业务系统. 范围: 0xD0 ~ 0xEF

    /* 业务数据更新, 参数 'data_type' */
    BUF_ACITON_APPLICATION_DATA_UPDATE_NOTIFY = 0xD0,

    /* 迎宾模式设置，参数为'mode' */
    BUF_ACTION_WELCOME_MODE = 0xD1,

    /* 迎宾相关的文字播报，参数为 'data_type' */
    BUF_ACTION_WELCOME_TEXT_PLAY = 0xD2,

    /* 迎宾点设置，参数为'robot_id point_type degree,x,y' */
    BUF_ACTION_WELCOME_POSITION_SET = 0xD3,

    /* 迎宾点获取，参数为'robot_id' */
    BUF_ACTION_WELCOME_POSITION_GET = 0xD4,

    /* 去某个迎宾点，参数为'point_type' */
    BUF_ACTION_WELCOME_POSITION_GO = 0xD5,

    /// 未使用. 范围: 0xF0 ~ 0xF9

    /// 调试相关. 范围: 0xFA ~ 0xFF

    /*心跳数据*/
    BUF_ACTION_HEARTBEAT =0xFA,
    /* 显示单元进行演示或场景拍摄，参数为'type' */
    BUF_ACTION_DISPLAYER_DEMO = 0xFC,

    /* 查询所有配置信息 */
    BUF_ACTION_DEBUG_QUERY = 0xFD,

    /* 更新数据库内容 */
    BUF_ACTION_UPDATE_DB_DATA = 0xFE,

    /* API 版本查询 */
    BUF_ACTION_API_VER_QUERY = 0xFF,

  //          =======================================================





     TYPE_ROBOT_ACTIVATE = 10001,//机器人激活
     TYPE_GET_ROBOTINFO_OK_FINISH = 10002,//获取机器人信息
     TYPE_RECEIVE_FROM_SERVER = 10003,//从服务器接收到数据
     TYPE_UPDATE_NAVI_MAP_IMG = 10004,//地图上传
     TYPE_FAMILY_HAS_MODIFICATION = 10005 ;//家庭成员有改动







}
