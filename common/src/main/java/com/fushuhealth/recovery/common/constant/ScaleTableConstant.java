package com.fushuhealth.recovery.common.constant;

import java.util.ArrayList;
import java.util.List;

public class ScaleTableConstant {

    public enum ScaleTableType {
        UNKNOWN((byte)0, "未知"),
        SELF_TEST((byte)1, "自测量表"),
        DIAGNOSIS((byte)2, "诊断量表");

        public static ScaleTableType getScaleTableType(byte type) {
            for (ScaleTableType scaleTableType : ScaleTableType.values()) {
                if (scaleTableType.getCode() == type) {
                    return scaleTableType;
                }
            }
            return UNKNOWN;
        }

        private byte code;

        private String desc;

        ScaleTableType(byte code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public byte getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ScaleTableClassification {
        UNKNOWN((byte)0, "未知"),
        AUTISM((byte)1, "儿童自闭症"),
        SENSORY_INTEGRATION((byte)2, "儿童感觉统合"),
        DEVELOPMENT_MILESTONES((byte)3, "儿童发育里程碑"),
        PREGNANT_WOMAN_GAIT((byte)4, "孕妇步态"),
        CEREBRAL_PALSY((byte)5, "发育评估"),
        LEIBO_CEREBRAL_PALSY_GMS((byte)6, "发育评估"),
        GRIFFITHS((byte)7, "格里菲斯发育评估（Griffiths）"),
        CHILDREN_NEURO_BEHAVIORAL((byte)8, "儿童神经行为综合评估"),
        SLEEP_HABITS((byte)9, "睡眠习惯"),
        MULTIPLE_INTELLIGENCE_TEST((byte)10, "多元智能测试"),
        LEARNING_DISABILITY((byte)11, "学习障碍"),
        DDST((byte)12, "丹佛发育筛查(DDST)"),;

        public static ScaleTableClassification getScaleTableClassification(byte code) {
            for (ScaleTableClassification scaleTableClassification : ScaleTableClassification.values()) {
                if (scaleTableClassification.getCode() == code) {
                    return scaleTableClassification;
                }
            }
            return UNKNOWN;
        }

        private byte code;

        private String desc;

        ScaleTableClassification(byte code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public byte getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ScaleTableCode {
        UNKNOWN((byte)0, (byte)0,"未知", ""),
        M_CHAT_R((byte)1, (byte)1,"改良版幼儿孤独症筛查量表（M-CHAT-R）", ""),
        CABS((byte)2, (byte)1,"克氏孤独症行为量表（CABS）", ""),
        ABC((byte)3, (byte)1,"行为量表ABC", ""),
        CARS((byte)4, (byte)1,"儿童自闭症评定量表", ""),
        BEHAVIOURAL_CALCULATION((byte)5, (byte)1,"行为计算量表", ""),
        SENSORY_INTEGRATION((byte)6, (byte)2,"儿童感觉统合量表", ""),
        DEVELOPMENT_MILESTONES((byte)7, (byte)3,"儿童发育里程碑", ""),
        PREGNANT_WOMAN_GAIT((byte)8, (byte)4,"孕妇步态分析", ""),
        LEIBO_CEREBRAL_PALSY((byte)9, (byte)5,"婴幼儿脑瘫危险程度量表", ""),
        GMS((byte)10, (byte)5,"全身运动质量评估（GMs）", ""),
        GMS_LEIBO((byte)11, (byte)6,"全身运动质量评估（GMs）与婴幼儿脑瘫危险程度量表", ""),
        LEIBO_CEREBRAL_SELF((byte)12, (byte)6,"婴幼儿神经运动7项", "婴儿姿势运动检查是婴儿脑瘫等神经肌肉疾病早期认出和诊断的方法。包括“自发姿势运动”和“激发姿势运动”检查。其精准的定位、定性、定级的方法。可有效指导和确定康复治疗的起点、方案、目标。其特点，一是提升了准确性，二是注重了早期应用，三是节省诊断时间，四是降低操作风险，五是易于普及，六是可以指导临床干预治疗。\n" +
                "家长在家庭生活场景中用手机拍摄宝宝自然活动视频，通过云端AI技术与远程专家双保险的方式，可早期识别婴幼儿特异性的神经学症状、早期预测大脑发育异常的发生，诸如脑性瘫痪、脑损伤、神经肌肉疾病等。“自发姿势运动”视频拍摄同GMs，“激发姿势运动”拍摄可遵照视频拍摄指导进行。"),
        LEIBO_BODY_GMS((byte)13, (byte)6,"0-1岁发育风险评估", "“0-1岁儿童发育风险评估”：是儿童大脑发育智能筛查、精准评估与数字干预服务平台提供的一项综合性评估服务。它融合了多项儿童神经发育评估工具，包括简化0~1岁20项神经运动检查、Vojta七项姿势反射检查，以及全身运动（GMs）自发姿势检查等，并利用脑科学领域最前沿的计算神经行为学分析方法。这项服务的核心目标在于早期识别那些在神经发育上稍显迟缓或异常的宝宝，以便为他们提供及时的干预和帮助。"),
        GRIFFITHS((byte)14, (byte)7,"格里菲斯发育评估（Griffiths）", ""),
        WEISHI_TODDLER_INTELLIGENCE((byte)15, (byte)8,"韦氏幼儿智力量表（2:6~3:11）", ""),
        WEISHI_CHILD_INTELLIGENCE((byte)16, (byte)8,"韦氏儿童智力量表（6:0~16:11）", ""),
        GMS_8((byte)17, (byte)8,"GMs全身运动质量评估", ""),
        HINE_8((byte)18, (byte)8,"HINE婴幼儿神经系统检查", ""),
        AIMS_8((byte)19, (byte)8,"Alberta婴儿运动量表（AIMS）", ""),
        BSID_8((byte)20, (byte)8,"Bayley婴幼儿发展量表（BSID）", ""),
        PEABODY_8((byte)21, (byte)8,"Peabody运动发育评估报告", ""),
        GESELL_8((byte)22, (byte)8,"Gesell发育诊断报告", ""),
        MOTOR_FUNCTION_8((byte)23, (byte)8,"运动功能评估", ""),
        ARTICULATION_LANGUAGE_ABILITY_8((byte)24, (byte)8,"构音语言能力评估（口腔运动、发音）", ""),
        SOCIAL_BEHAVIOR_8((byte)25, (byte)8,"社交行为评估", ""),
        ADHD_8((byte)26, (byte)8,"多动症评估", ""),
        AUTISM_BEHAVIOR_PANEL_8((byte)27, (byte)8,"自闭症行为检查组合", ""),
        TODDLER_AUTISM_SPECTRUM_DISORDER_8((byte)28, (byte)8,"幼儿自闭症谱系障碍筛查", ""),
        PERSONALITY_TEMPERAMENT_ASSESSMENT_8((byte)29, (byte)8,"人格、气质评估测评", ""),
        CHILDREN_SENSORY_INTEGRATION_ASSESSMENT_8((byte)30, (byte)8,"儿童感觉统合测评", ""),
        EXPERT_TEAM_DISEASE_MANAGEMENT_GROUP((byte)31, (byte)8,"专家团队疾病管理组", ""),
        WEISHI_TODDLER_INTELLIGENCE_40_611((byte)32, (byte)8,"韦氏幼儿智力量表（4:0~6:11）", ""),
        GMFM88((byte)33, (byte)8,"GMFM88项粗大运动评分量表", ""),
        NEWCASTLE_0_24((byte)34, (byte)8,"纽卡斯尔儿童线粒体病量表0-24月", ""),
        NEWCASTLE_2_11_YEAR((byte)35, (byte)8,"纽卡斯尔儿童线粒体病量表2-11岁", ""),
        CAREGIVER_BURDEN((byte)36, (byte)8,"看护者负担评估", ""),
        ATAXIA_ASSESSMENT ((byte)37, (byte)8,"共济失调评估量表", ""),
        BARRY_ALBRIGHT((byte)38, (byte)8,"Barry-Albright肌张力障碍量表", ""),
        NINE_WELL_TEST((byte)39, (byte)8,"九孔试验", ""),
        SLEEP_HABITS((byte)40, (byte)9,"睡眠习惯", ""),
        MULTIPLE_INTELLIGENCE_TEST((byte)41, (byte)10,"多元智能测试", ""),
        SLEEP_ASSESSMENT_QUESTIONNAIRE((byte)42, (byte)9,"3-12岁儿童睡眠评估问卷", ""),
        CONCENTRATION_TEST_7_MINUTE((byte)43, (byte)10,"7分钟学习专注力测试", "专注力测试是评估孩子注意力集中能力的有效工具，具有重要的临床评估意义。它帮助家长和医生了解孩子的专注水平，制定个性化提升计划，从而提高学习效率和社交技能。本测试通过简单的数列找配对游戏，在7分钟内评估孩子的专注力。测试结果指导家长和医生采取相应措施，促进孩子全面发展。\n"),
        LEARNING_ABILITY_ASSESSMENT((byte)44, (byte)10,"学习能力测评", "儿童学习能力评估是一项重要的评估工具，它能够帮助家长和医生了解孩子在不同学习领域的发展情况。通过这份评估，我们可以识别孩子在阅读、写作、数学、语言表达和社交能力等方面的优势和潜在困难。评估结果不仅有助于家长对孩子进行个性化的教育支持，而且对于制定针对性的干预措施也至关重要。"),
        DDST_3_MONTH((byte)45, (byte)12,"3月龄筛查", ""),
        DDST_6_MONTH((byte)46, (byte)12,"6月龄筛查", ""),
        DDST_8_MONTH((byte)47, (byte)12,"8月龄筛查", ""),
        DDST_10_MONTH((byte)48, (byte)12,"10月龄筛查", ""),
        DDST_12_MONTH((byte)49, (byte)12,"12月龄筛查", ""),
        DDST_15_MONTH((byte)50, (byte)12,"15月龄筛查", ""),
        DDST_18_MONTH((byte)51, (byte)12,"18月龄筛查", ""),
        DDST_24_MONTH((byte)52, (byte)12,"24月龄筛查", ""),
        DDST_30_MONTH((byte)53, (byte)12,"30月龄筛查", ""),
        DDST_36_MONTH((byte)54, (byte)12,"36月龄筛查", ""),
        STUTTERING((byte)99, (byte)6,"口吃", ""),;
//        GMS_LEIBO((byte)11, "GMs全身运动质量评估", ""),;

        public static ScaleTableCode getScaleTableType(byte code) {
            for (ScaleTableCode scaleTableCode : ScaleTableCode.values()) {
                if (scaleTableCode.getCode() == code) {
                    return scaleTableCode;
                }
            }
            return UNKNOWN;
        }

        public static List<ScaleTableCode> getScaleTableByClassification(ScaleTableClassification classification) {
            ArrayList<ScaleTableCode> scaleTableCodes = new ArrayList<>();
            for (ScaleTableCode scaleTableCode : ScaleTableCode.values()) {
                if (scaleTableCode.getClassification() == classification.getCode()) {
                    scaleTableCodes.add(scaleTableCode);
                }
            }
            return scaleTableCodes;
        }

        private byte code;

        private byte classification;

        private String desc;

        private String remark;

        ScaleTableCode(byte code, byte classification, String desc, String remark) {
            this.code = code;
            this.classification = classification;
            this.desc = desc;
            this.remark = remark;
        }

        public byte getCode() {
            return code;
        }

        public byte getClassification() {
            return classification;
        }

        public String getDesc() {
            return desc;
        }

        public String getRemark() {
            return remark;
        }
    }
}
