package com.geeker.marketing.dao.micro.generator.model;

import java.io.Serializable;
import java.util.Date;

public class WxEvent implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.wx_uin_id
     *
     * @mbg.generated
     */
    private String wxUinId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.wx_id
     *
     * @mbg.generated
     */
    private String wxId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.event_cd
     *
     * @mbg.generated
     */
    private String eventCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wx_event.receive_time
     *
     * @mbg.generated
     */
    private Date receiveTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table wx_event
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.id
     *
     * @return the value of wx_event.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.id
     *
     * @param id the value for wx_event.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.wx_uin_id
     *
     * @return the value of wx_event.wx_uin_id
     *
     * @mbg.generated
     */
    public String getWxUinId() {
        return wxUinId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.wx_uin_id
     *
     * @param wxUinId the value for wx_event.wx_uin_id
     *
     * @mbg.generated
     */
    public void setWxUinId(String wxUinId) {
        this.wxUinId = wxUinId == null ? null : wxUinId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.wx_id
     *
     * @return the value of wx_event.wx_id
     *
     * @mbg.generated
     */
    public String getWxId() {
        return wxId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.wx_id
     *
     * @param wxId the value for wx_event.wx_id
     *
     * @mbg.generated
     */
    public void setWxId(String wxId) {
        this.wxId = wxId == null ? null : wxId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.event_cd
     *
     * @return the value of wx_event.event_cd
     *
     * @mbg.generated
     */
    public String getEventCd() {
        return eventCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.event_cd
     *
     * @param eventCd the value for wx_event.event_cd
     *
     * @mbg.generated
     */
    public void setEventCd(String eventCd) {
        this.eventCd = eventCd == null ? null : eventCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.create_time
     *
     * @return the value of wx_event.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.create_time
     *
     * @param createTime the value for wx_event.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.status
     *
     * @return the value of wx_event.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.status
     *
     * @param status the value for wx_event.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wx_event.receive_time
     *
     * @return the value of wx_event.receive_time
     *
     * @mbg.generated
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wx_event.receive_time
     *
     * @param receiveTime the value for wx_event.receive_time
     *
     * @mbg.generated
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_event
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxUinId=").append(wxUinId);
        sb.append(", wxId=").append(wxId);
        sb.append(", eventCd=").append(eventCd);
        sb.append(", createTime=").append(createTime);
        sb.append(", status=").append(status);
        sb.append(", receiveTime=").append(receiveTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}