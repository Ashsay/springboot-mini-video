package com.wx.pojo.vo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象", description = "这是用户对象")
public class PersonVo {
	
	@ApiModelProperty(hidden = true)
    @Id
    private String id;
	
	private boolean isFollow;

	@ApiModelProperty(hidden = true)
	private String userToken;
	
    @ApiModelProperty(value = "用户名", name = "username", example = "wxname", required = true)
    private String username;

    @JsonIgnore
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;

	@ApiModelProperty(hidden = true)
    private String faceImage;

    private String nickname;

	@ApiModelProperty(hidden = true)
    private Integer fansCounts;

	@ApiModelProperty(hidden = true)
    private Integer followCounts;

	@ApiModelProperty(hidden = true)
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
     * @return
     */
    public boolean isFollow() {
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
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
    
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

    /**
     * @param receiveLikeCounts
     */
    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }
}