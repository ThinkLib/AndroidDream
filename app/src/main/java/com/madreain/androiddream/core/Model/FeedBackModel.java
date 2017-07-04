package com.madreain.androiddream.core.Model;

import cn.bmob.v3.BmobObject;

/**
 * 意见反馈
 * @author madreain
 * @desc
 * @time 2017/4/21
 */

public class FeedBackModel extends BmobObject {
    //反馈内容
    String feedbackContent;
    //反馈联系方式
    String feedbackContact;

    public FeedBackModel() {
    }

    public FeedBackModel(String feedbackContent, String feedbackContact) {
        this.feedbackContent = feedbackContent;
        this.feedbackContact = feedbackContact;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackContact() {
        return feedbackContact;
    }

    public void setFeedbackContact(String feedbackContact) {
        this.feedbackContact = feedbackContact;
    }

    @Override
    public String toString() {
        return "FeedBackModel{" +
                "feedbackContent='" + feedbackContent + '\'' +
                ", feedbackContact='" + feedbackContact + '\'' +
                '}';
    }
}
