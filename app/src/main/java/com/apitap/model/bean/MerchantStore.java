package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MerchantStore {

    @SerializedName("_192")
    @Expose
    private String _192;
    @SerializedName("_11")
    @Expose
    private String _11;
    @SerializedName("_122_17")
    @Expose
    private Boolean _12217;
    @SerializedName("_122_18")
    @Expose
    private String _12218;
    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = null;

    public String get192() {
        return _192;
    }

    public void set192(String _192) {
        this._192 = _192;
    }

    public String get11() {
        return _11;
    }

    public void set11(String _11) {
        this._11 = _11;
    }

    public Boolean get12217() {
        return _12217;
    }

    public void set12217(Boolean _12217) {
        this._12217 = _12217;
    }

    public String get12218() {
        return _12218;
    }

    public void set12218(String _12218) {
        this._12218 = _12218;
    }

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        this.rESULT = rESULT;
    }


public class ME {

    @SerializedName("_114_1")
    @Expose
    private String _1141;
    @SerializedName("_53")
    @Expose
    private String _53;
    @SerializedName("_114_70")
    @Expose
    private String _11470;
    @SerializedName("_121_170")
    @Expose
    private String _121170;
    @SerializedName("_114_9")
    @Expose
    private String _1149;

    public String get1141() {
        return _1141;
    }

    public void set1141(String _1141) {
        this._1141 = _1141;
    }

    public String get53() {
        return _53;
    }

    public void set53(String _53) {
        this._53 = _53;
    }

    public String get11470() {
        return _11470;
    }

    public void set11470(String _11470) {
        this._11470 = _11470;
    }

    public String get121170() {
        return _121170;
    }

    public void set121170(String _121170) {
        this._121170 = _121170;
    }

    public String get1149() {
        return _1149;
    }

    public void set1149(String _1149) {
        this._1149 = _1149;
    }

}
    public class RESULT {

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
        private List<RESULT_> rESULT = null;

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

        public List<RESULT_> getRESULT() {
            return rESULT;
        }

        public void setRESULT(List<RESULT_> rESULT) {
            this.rESULT = rESULT;
        }
        public class RESULT_ {

            @SerializedName("_114_93")
            @Expose
            private String _11493;
            @SerializedName("_120_45")
            @Expose
            private String _12045;
            @SerializedName("_120_44")
            @Expose
            private String _12044;
            @SerializedName("_122_21")
            @Expose
            private String _12221;
            @SerializedName("_114_121")
            @Expose
            private String _114121;
            @SerializedName("ME")
            @Expose
            private List<ME> mE = null;

            public String get11493() {
                return _11493;
            }

            public void set11493(String _11493) {
                this._11493 = _11493;
            }

            public String get12045() {
                return _12045;
            }

            public void set12045(String _12045) {
                this._12045 = _12045;
            }

            public String get12044() {
                return _12044;
            }

            public void set12044(String _12044) {
                this._12044 = _12044;
            }

            public String get12221() {
                return _12221;
            }

            public void set12221(String _12221) {
                this._12221 = _12221;
            }

            public String get114121() {
                return _114121;
            }

            public void set114121(String _114121) {
                this._114121 = _114121;
            }

            public List<ME> getME() {
                return mE;
            }

            public void setME(List<ME> mE) {
                this.mE = mE;
            }

        }
    }
}