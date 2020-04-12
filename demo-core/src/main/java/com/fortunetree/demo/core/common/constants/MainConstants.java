package com.fortunetree.demo.core.common.constants;

public final class MainConstants {

    public static final int DEF_INVALID = -1;
    public static final int DEF_ZERO = 0;
    public static final int DEF_ONE = 1;
    public static final int DEF_TWO = 2;
    public static final int DEF_THREE = 3;

    public static final String DEF_ZERO_STR = "0";
    public static final String DEF_ONE_STR = "1";
    public static final String DEF_TWO_STR = "2";
    public static final String DEF_THREE_STR = "3";

    public static final int SATUS_ON = 1;
    public static final int SATUS_OFF = 0;

    public static final Byte SATUS_BON= Byte.valueOf("1");
    public static final Byte SATUS_BOFF = Byte.valueOf("0");

    public static final int DELETE_YES = 1;
    public static final int DELETE_NO = 0;

    public static final String BANNER = "1";
    public static final String BANNER_SUB = "0";

    public static final Byte SATUS_ZERO = Byte.valueOf("0");
    public static final Byte SATUS_ONE = Byte.valueOf("1");
    public static final Byte SATUS_TWO = Byte.valueOf("2");
    public static final Byte SATUS_THREE = Byte.valueOf("3");


    // 文件上传相关
    public interface File{
        public String imgRootPrefix = "/upload";
    }

    public static final String REFUNDBALAND = "REFUNDBALAND";
    public static final String REFUNDWEIXIN = "REFUNDWEIXIN";
    public static final String CLEANbALANCE = "CLEANbALANCE";


    /**
     * 用户回收系数
     */
    public static final double RECYCLE_DISCOUNT = 0.8;


}
