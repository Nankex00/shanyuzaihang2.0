package com.fushuhealth.recovery.common.constant;

public enum QuestionSubjectEnum {

    UNKNOWN((byte)0, "默认", ""),
    AUTISM_SOCIAL_CONTACT((byte)1, "社交", ""),
    AUTISM_LANGUAGE((byte)2, "语言", ""),
    AUTISM_COGNITION((byte)3, "认知", ""),
    AUTISM_SPORTS((byte)4, "运动", ""),
    AUTISM_SEEK_MEDICAL_ATTENTION((byte)5, "需尽快就医", ""),
    SENSORY_INTEGRATION_VESTIBULAR_BALANCE((byte)6, "前庭平衡", "前庭功能：地心引力对人类的影响最大，人的翻、爬、坐、站、跑的学习与前庭功能关系密切。前庭功能影响了身体和周围环境协调。由于胎位不正、爬行不足及早年活动不足都会引起前庭功能不足。失常的儿童会表现为喜欢旋转或绕圈子跑、手脚笨拙、容易跌倒、常碰撞桌椅;爬上爬下、不安地乱动;组织力不佳，经常弄乱东西，不喜欢整理自己的环境。"),
    SENSORY_INTEGRATION_TACTILE_DEFENSE((byte)7, "触觉防御", "触觉防御：人的触觉反应系统有两种:一种是自卫性或保护性反应，另一种是辨别性反应。有触觉防御障碍的儿童，当外界刺激作用于皮肤时，就会做出过分的触觉防御性反应。有些儿童由于早期的不良因素，如;早产、剖腹产及活动限制，这些均会引起儿童的触觉过分防御性反应。日常生活中触觉过分防御可表现为;胆小、害怕陌生环境、害羞、不安、粘妈妈、怕黑:咬指甲、偏食、挑食、独占性强。"),
    SENSORY_INTEGRATION_PROPRIOCEPTION((byte)8, "本体感", "本体感：指身体运动的协调能力，这方面存在问题，会导致运动障碍。儿童早期会表现穿脱衣裤。扣钮扣，拉链，系鞋带动作缓慢及笨拙;运动协调不佳:吃饭时常掉饭粒:由于控制小肌肉及手眼协调的肌肉发育欠\n" +
            "佳，影响舌头及唇部肌肉、呼吸和声带的运动，会造成发音及语言表达能力不佳。运动协调不良是由感觉统合障碍所致;在学习困难儿童中，较正常儿童更为多见。"),
    SENSORY_INTEGRATION_LEARNING_ABILITY((byte)9, "学习能力", "学习能力：可表现为不同形式，主要涉及视知觉问题，一方面可能与躯体感觉过程有关，另一方面与右脑半球的功能有关。这类障碍在儿童可表现为对空间距离知觉不准确，左右分辩不清，易迷失方向。儿童还会还会表现视觉的不平顺。视觉的跳动原本是婴幼儿的自然现象，人的视觉天生是不稳定的，所以婴幼儿最喜欢看车子外的移动物体，如跳动的物体比静止的东西更容易引起他的注意。随着年龄的增长，视觉也逐渐地稳定，便能做左右\n" +
            "或上下的移动，这也是阅读的开始。儿童若视觉不稳定，便无法做平顺移动，所以看书会跳字、跳行，严重的无法进行阅读，做功课眼睛也容易疲劳，造成学习能力的不足。"),
    SENSORY_INTEGRATION_OLDER_CHILDREN((byte)10, "大年龄儿童", ""),
    CEREBRAL_PALSY_abnormal_1((byte)11, "自发姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_2((byte)12, "由仰卧扶持髋部翻动躯干呈侧卧激发的姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_3((byte)13, "由侧卧扶持髋部翻动躯干呈俯卧激发的姿势运动异常(4个月后查)", ""),
    CEREBRAL_PALSY_abnormal_4((byte)14, "由仰卧拉坐激发的姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_5((byte)15, "由仰卧扶持髋部翻动躯干呈侧卧激发的姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_6((byte)16, "扶持双腋立位悬垂激发的姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_7((byte)17, "立位悬垂后足轻碰台面激发的姿势运动异常", ""),
    CEREBRAL_PALSY_abnormal_8((byte)18, "立位足踏台面后扶持迈步激发的姿势运动异常(扶持腋下左右重心转换促迈步)", ""),
    A_LYING_AND_TURNING((byte)19, "A：卧位与翻身", ""),
    B_SITING((byte)20, "B：坐", ""),
    C_CRAWL_AND_KNEEL((byte)21, "C: 爬与跪", ""),
    D_STANDING((byte)22, "D：站", ""),
    E_WALK_RUN_AND_JUMP((byte)23, "E：走、跑和跳", ""),
    LANGUAGE((byte)24, "言语语言", ""),
    LOGICAL_MATHEMATICAL ((byte)25, "逻辑数理", ""),
    MUSIC_RHYTHM((byte)26, "音乐韵律", ""),
    INTERPERSONAL_COMMUNICATION ((byte)27, "人际沟通", ""),
    SELF_AWARE((byte)28, "自我认知", ""),
    BODY_MOVEMENT((byte)29, "身体运动", ""),
    VISUAL_SPATIAL((byte)30, "视觉空间", ""),
    NATURAL_OBSERVATION((byte)31, "自然观察", ""),
    SLEEP_TIME((byte)32, "睡眠时间", ""),
    SLEEP_BEHAVIOR((byte)33, "睡眠行为", ""),
    BEDTIME_HABITS((byte)34, "就寝习惯", ""),
    SLEEP_ANXIETY((byte)35, "睡眠焦虑", ""),
    SLEEP_DURATION((byte)36, "睡眠持续时间", ""),
    SLEEP_DISORDERED_BREATHING((byte)37, "睡眠呼吸障碍", ""),
    PARASOMNIA((byte)38, "异态睡眠", ""),
    DAYTIME_SLEEPINESS((byte)39, "白天嗜睡", ""),
    WAKE_UP_AT_NIGHT((byte)40, "夜醒", ""),
    SLEEP_LATENCY((byte)41, "入睡潜伏期", ""),
    VISUAL_PERCEPTION_ABILITY((byte)42, "视知觉能力", ""),
    SPEECH_ABILITY((byte)43, "言语能力", ""),
    SOCIAL_SKILLS((byte)44, "社交能力", ""),
    COMPREHENSION((byte)45, "理解能力", ""),
    BEHAVIORAL_PROBLEMS((byte)46, "行为问题", ""),
    BIG_MOVEMENT((byte)47, "大运动", ""),
    FINE_MOTOR((byte)48, "精细动作", ""),
    DISTRACTION((byte)49, "注意分散", ""),
    HYPERACTIVE_IMPULSIVE_BEHAVIOR((byte)50, "多动-冲动行为", ""),
    //个人社会
    DDST_PERSONAL_SOCIAL((byte)51, "个人社会", ""),
    //精细动作
    DDST_FINE_MOTION_ABILITY((byte)52, "精细动作", ""),
    //语言
    DDST_LANGUAGE((byte)53, "语言", ""),
    //大运动
    DDST_BIG_MOTION((byte)54, "大运动", ""),;

    public static QuestionSubjectEnum getQuestionSubject(byte type) {
        for (QuestionSubjectEnum questionSubjectEnum : QuestionSubjectEnum.values()) {
            if (questionSubjectEnum.getCode() == type) {
                return questionSubjectEnum;
            }
        }
        return UNKNOWN;
    }

    private byte code;

    private String desc;

    private String remark;

    QuestionSubjectEnum(byte code, String desc, String remark) {
        this.code = code;
        this.desc = desc;
        this.remark = remark;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getRemark() {
        return remark;
    }
}
