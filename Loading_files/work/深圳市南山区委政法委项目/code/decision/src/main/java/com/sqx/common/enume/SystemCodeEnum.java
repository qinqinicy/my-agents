package com.sqx.common.enume;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author lhp
 * @date 2019/8/5
 */
public enum SystemCodeEnum {
    SPA_PANS_LZZX("SPA-PANS-LZZX", "流转平台"),
    PANS_WGH("PANS-WGH", "网格化+"),
    PANS_SJFB("PANS-SJFB", "事件分拨"),
    PANS_ZFPA("PANS-ZFPA", "政法平安"),
    PANS_JCFX("PANS-JCFX", "决策分析"),
    PANS_MSSQ("PANS-MSSQ", "民生诉求"),
    PANS_DYGZ("PANS-DYGZ", "多元共治"),
    SPA_ZHWG("SPA-ZHWG", "市综合网格"),
    PANS_DZTZ("PANS-DZTZ", "南山电子台账"),
    PANS_ZHXJ("PANS-ZHXJ", "南山智慧巡检"),
    ;

    private final String code;

    private final String msg;

    SystemCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static String getName(String index) {
        for (SystemCodeEnum c : SystemCodeEnum.values()) {
            if (c.getCode().equals(index)) {
                return c.msg;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
