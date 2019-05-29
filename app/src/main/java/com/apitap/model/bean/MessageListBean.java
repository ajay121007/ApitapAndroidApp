package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class MessageListBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public class RESULT {
        @SerializedName("_44")
        @Expose
        private String status;

        @SerializedName("RESULT")
        @Expose
        private List<MessageData> rESULT = new ArrayList<MessageData>();

        public List<MessageData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class MessageData implements Serializable{

            @SerializedName("_122_114")
            @Expose
            public String id;
            @SerializedName("_122_128")
            @Expose
            public String subject;
            @SerializedName("_114_143")
            @Expose
            public String status;
            @SerializedName("_53")
            @Expose
            public String userId;

            @SerializedName("_127_87")
            @Expose
            public String replied;

            @SerializedName("_114_53")
            @Expose
            public String merchantName;


            @SerializedName("_114_179")
            @Expose

            public String merchantReceiver;
            @SerializedName("_122_181")
            @Expose
            public String name;
            @SerializedName("_114_150")
            @Expose
            public String parentId;
            @SerializedName("_114_138")
            @Expose
            public String date;
            @SerializedName("_120_16")
            @Expose
            public String type;
            @SerializedName("_120_157")
            @Expose
            public String contextData;
            @SerializedName("_121_75")
            @Expose
            public String invoiceId;
            @SerializedName("_121_170")
            @Expose
            public String logoImage;

            public String getId() {
                return id;
            }

            public String getSubject() {
                return subject;
            }

            public String getStatus() {
                return status;
            }

            public String getUserId() {
                return userId;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public String getReplied() {
                return replied;
            }


            public String getMerchantReceiver() {
                return merchantReceiver;
            }

            public String getName() {
                return name;
            }

            public String getParentId() {
                return parentId;
            }

            public String getDate() {
                return date;
            }

            public String getType() {
                return type;
            }

            public String getContextData() {
                return contextData;
            }

            public String getInvoiceId() {
                return invoiceId;
            }

            public String getLogoImage() {
                return logoImage;
            }
        }

    }
}
