package com.example.okhttpdemo.Sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SetuItem {
    private String mPid;
    private String mUid;
    private String mTitle;
    private String mAuthor;
    private String mImgFileName;

    @Generated(hash = 1723148604)
    public SetuItem(String mPid, String mUid, String mTitle, String mAuthor,
                    String mImgFileName) {
        this.mPid = mPid;
        this.mUid = mUid;
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mImgFileName = mImgFileName;
    }

    @Generated(hash = 2109923230)
    public SetuItem() {
    }

    public String getMPid() {
        return this.mPid;
    }

    public void setMPid(String mPid) {
        this.mPid = mPid;
    }

    public String getMUid() {
        return this.mUid;
    }

    public void setMUid(String mUid) {
        this.mUid = mUid;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMAuthor() {
        return this.mAuthor;
    }

    public void setMAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getMImgFileName() {
        return this.mImgFileName;
    }

    public void setMImgFileName(String mImgFileName) {
        this.mImgFileName = mImgFileName;
    }
}

