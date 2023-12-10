package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserProfileRespose implements Serializable {
        @SerializedName("error")
        @Expose
        public String error;
        @SerializedName("data")
        @Expose
        public UserProfileData data;
        @SerializedName("usercategory")
        @Expose
        public List<UserProfileCategory> usercategory = null;
        @SerializedName("userstyles")
        @Expose
        public List<UserProfileStyle> userstyles = null;
        @SerializedName("creator")
        @Expose
        private List<ProfileCreator> creator = null;
        @SerializedName("collection")
        @Expose
        private List<ProfileCollections> collection = null;
        @SerializedName("peices")
        @Expose
        public Integer peices;
        @SerializedName("supported")
        @Expose
        public String supported;
        @SerializedName("supporting")
        @Expose
        public String supporting;
        @SerializedName("message")
        @Expose
        public String message;

        public String getError() {
                return error;
        }

        public UserProfileData getData() {
                return data;
        }

        public List<UserProfileCategory> getUsercategory() {
                return usercategory;
        }

        public List<UserProfileStyle> getUserstyles() {
                return userstyles;
        }

        public List<ProfileCreator> getCreator() {
                return creator;
        }

        public List<ProfileCollections> getCollection() {
                return collection;
        }

        public void setCollection(List<ProfileCollections> collection) {
                this.collection = collection;
        }

        public Integer getPeices() {
                return peices;
        }

        public String getSupported() {
                return supported;
        }

        public String getSupporting() {
                return supporting;
        }

        public String getMessage() {
                return message;
        }
}
