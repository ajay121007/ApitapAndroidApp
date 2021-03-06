package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shami on 28/2/2018.
 */

public class RatingBean {

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

    }
    public class RESULT_ {

        @SerializedName("_121_80")
        @Expose
        private String _12180;
        @SerializedName("_114_3")
        @Expose
        private String _1143;
        @SerializedName("_114_5")
        @Expose
        private String _1145;
        @SerializedName("_114_138")
        @Expose
        private String _11438;
        @SerializedName("_122_129")
        @Expose
        private String _122129;
        @SerializedName("_120_83")
        @Expose
        private String _12083;

        public String get12180() {
            return _12180;
        }

        public String get_11438() {
            return _11438;
        }


        public String get_122129() {
            return _122129;
        }


        public void set12180(String _12180) {
            this._12180 = _12180;
        }

        public void set_122129(String _122129) {
            this._122129 = _122129;
        }


        public String get12083() {
            return _12083;
        }


        public String get_1143() {
            return _1143;
        }

        public String get_1145() {
            return _1145;
        }


        public void set12083(String _12083) {
            this._12083 = _12083;
        }

    }
}