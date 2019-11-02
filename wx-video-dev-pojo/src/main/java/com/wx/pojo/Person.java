package com.wx.pojo;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象", description = "这是用户对象")
public class Person {
	
	@ApiModelProperty(hidden = true)
    @Id
    private String id;

    @ApiModelProperty(value = "用户名", name = "username", example = "wxname", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;

	@ApiModelProperty(hidden = true)
    @Column(name = "face_image")
    private String faceImage;

    private String nickname;

    @Column(name = "fans_counts")
    private Integer fansCounts;

    @Column(name = "follow_counts")
    private Integer followCounts;

    @Column(name = "receive_like_counts")
    private Integer receiveLikeCounts;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return face_image
     */
    public String getFaceImage() {
        return faceImage;
    }

    /**
     * @param faceImage
     */
    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return fans_counts
     */
    public Integer getFansCounts() {
        return fansCounts;
    }

    /**
     * @param fansCounts
     */
    public void setFansCounts(Integer fansCounts) {
        this.fansCounts = fansCounts;
    }

    /**
     * @return follow_counts
     */
    public Integer getFollowCounts() {
        return followCounts;
    }

    /**
     * @param followCounts
     */
    public void setFollowCounts(Integer followCounts) {
        this.followCounts = followCounts;
    }

    /**
     * @return receive_like_counts
     */
    public Integer getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    /**
     * @param receiveLikeCounts
     */
    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }
}