package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
 public class FeedResponse {
     @SerializedName("error")
     @Expose
     private String error;
     @SerializedName("data")
     @Expose
     private List<FeedData> data = null;
     @SerializedName("total_Pages")
     @Expose
     private Integer totalPages;
     @SerializedName("next_page")
     @Expose
     private Integer nextPage;
     @SerializedName("message")
     @Expose
     private String message;

     public String getError() {
         return error;
     }

     public void setError(String error) {
         this.error = error;
     }

     public List<FeedData> getData() {
         return data;
     }

     public void setData(List<FeedData> data) {
         this.data = data;
     }

     public Integer getTotalPages() {
         return totalPages;
     }

     public void setTotalPages(Integer totalPages) {
         this.totalPages = totalPages;
     }

     public Integer getNextPage() {
         return nextPage;
     }

     public void setNextPage(Integer nextPage) {
         this.nextPage = nextPage;
     }

     public String getMessage() {
         return message;
     }

     public void setMessage(String message) {
         this.message = message;
     }
}
