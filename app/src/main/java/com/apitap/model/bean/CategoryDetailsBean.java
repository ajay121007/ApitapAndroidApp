package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourcefuse on 18/11/16.
 */

public class CategoryDetailsBean {

    @SerializedName("_101")
    @Expose
    private String _101;
    @SerializedName("_39")
    @Expose
    private String _39;
    @SerializedName("_44")
    @Expose
    private String _44;
    @SerializedName("_127_41")
    @Expose
    private Integer _12741;
    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = null;

    public String get101() {
        return _101;
    }

    public void set101(String _101) {
        this._101 = _101;
    }

    public String get39() {
        return _39;
    }

    public void set39(String _39) {
        this._39 = _39;
    }

    public String get44() {
        return _44;
    }

    public void set44(String _44) {
        this._44 = _44;
    }

    public Integer get12741() {
        return _12741;
    }

    public void set12741(Integer _12741) {
        this._12741 = _12741;
    }

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        this.rESULT = rESULT;
    }

    public class RESULT {

        @SerializedName("_114_144")
        @Expose
        private String _114144;
        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_121_170")
        @Expose
        private String _121170;
        @SerializedName("_114_98")
        @Expose
        private String _11498;
        @SerializedName("_122_158")
        @Expose
        private String _122158;
        @SerializedName("_114_112")
        @Expose
        private String _114112;
        @SerializedName("SP")
        @Expose
        private SP sP;
        @SerializedName("_114_9")
        @Expose
        private String _1149;

        public String get114144() {
            return _114144;
        }

        public void set114144(String _114144) {
            this._114144 = _114144;
        }

        public String get12083() {
            return _12083;
        }

        public void set12083(String _12083) {
            this._12083 = _12083;
        }

        public String get121170() {
            return _121170;
        }

        public void set121170(String _121170) {
            this._121170 = _121170;
        }

        public String get11498() {
            return _11498;
        }

        public void set11498(String _11498) {
            this._11498 = _11498;
        }

        public String get122158() {
            return _122158;
        }

        public void set122158(String _122158) {
            this._122158 = _122158;
        }

        public String get114112() {
            return _114112;
        }

        public void set114112(String _114112) {
            this._114112 = _114112;
        }

        public SP getSP() {
            return sP;
        }

        public void setSP(SP sP) {
            this.sP = sP;
        }

        public String get1149() {
            return _1149;
        }

        public void set1149(String _1149) {
            this._1149 = _1149;
        }

    }

    public class SL {

        @SerializedName("_127_89")
        @Expose
        private String _12789;
        @SerializedName("_127_90")
        @Expose
        private String _12790;
        @SerializedName("_127_91")
        @Expose
        private String _12791;
        @SerializedName("_120_12")
        @Expose
        private String _12012;
        @SerializedName("_127_64")
        @Expose
        private String _12764;
        @SerializedName("_127_65")
        @Expose
        private String _12765;

        public String get12789() {
            return _12789;
        }

        public void set12789(String _12789) {
            this._12789 = _12789;
        }

        public String get12790() {
            return _12790;
        }

        public void set12790(String _12790) {
            this._12790 = _12790;
        }

        public String get12791() {
            return _12791;
        }

        public void set12791(String _12791) {
            this._12791 = _12791;
        }

        public String get12012() {
            return _12012;
        }

        public void set12012(String _12012) {
            this._12012 = _12012;
        }

        public String get12764() {
            return _12764;
        }

        public void set12764(String _12764) {
            this._12764 = _12764;
        }

        public String get12765() {
            return _12765;
        }

        public void set12765(String _12765) {
            this._12765 = _12765;
        }

    }

    public class SP {

        @SerializedName("_114_138")
        @Expose
        private String _114138;
        @SerializedName("_114_139")
        @Expose
        private String _114139;
        @SerializedName("SL")
        @Expose
        private List<SL> sL = null;

        public String get114138() {
            return _114138;
        }

        public void set114138(String _114138) {
            this._114138 = _114138;
        }

        public String get114139() {
            return _114139;
        }

        public void set114139(String _114139) {
            this._114139 = _114139;
        }

        public List<SL> getSL() {
            return sL;
        }

        public void setSL(List<SL> sL) {
            this.sL = sL;
        }
    }
}
