package com.geeker.marketing.dao.micro.generator.model;

import java.io.Serializable;
import java.util.Date;

public class OpDevice implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.mobile
     *
     * @mbg.generated
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.imsi
     *
     * @mbg.generated
     */
    private String imsi;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.imei
     *
     * @mbg.generated
     */
    private String imei;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.model
     *
     * @mbg.generated
     */
    private String model;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.cpu
     *
     * @mbg.generated
     */
    private String cpu;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.ram
     *
     * @mbg.generated
     */
    private String ram;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.mem
     *
     * @mbg.generated
     */
    private String mem;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.network_mode
     *
     * @mbg.generated
     */
    private String networkMode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.remark
     *
     * @mbg.generated
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.com_id
     *
     * @mbg.generated
     */
    private Integer comId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.group_id
     *
     * @mbg.generated
     */
    private Integer groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.create_user_id
     *
     * @mbg.generated
     */
    private Integer createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op_device.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table op_device
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.id
     *
     * @return the value of op_device.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.id
     *
     * @param id the value for op_device.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.mobile
     *
     * @return the value of op_device.mobile
     *
     * @mbg.generated
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.mobile
     *
     * @param mobile the value for op_device.mobile
     *
     * @mbg.generated
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.imsi
     *
     * @return the value of op_device.imsi
     *
     * @mbg.generated
     */
    public String getImsi() {
        return imsi;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.imsi
     *
     * @param imsi the value for op_device.imsi
     *
     * @mbg.generated
     */
    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.imei
     *
     * @return the value of op_device.imei
     *
     * @mbg.generated
     */
    public String getImei() {
        return imei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.imei
     *
     * @param imei the value for op_device.imei
     *
     * @mbg.generated
     */
    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.model
     *
     * @return the value of op_device.model
     *
     * @mbg.generated
     */
    public String getModel() {
        return model;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.model
     *
     * @param model the value for op_device.model
     *
     * @mbg.generated
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.cpu
     *
     * @return the value of op_device.cpu
     *
     * @mbg.generated
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.cpu
     *
     * @param cpu the value for op_device.cpu
     *
     * @mbg.generated
     */
    public void setCpu(String cpu) {
        this.cpu = cpu == null ? null : cpu.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.ram
     *
     * @return the value of op_device.ram
     *
     * @mbg.generated
     */
    public String getRam() {
        return ram;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.ram
     *
     * @param ram the value for op_device.ram
     *
     * @mbg.generated
     */
    public void setRam(String ram) {
        this.ram = ram == null ? null : ram.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.mem
     *
     * @return the value of op_device.mem
     *
     * @mbg.generated
     */
    public String getMem() {
        return mem;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.mem
     *
     * @param mem the value for op_device.mem
     *
     * @mbg.generated
     */
    public void setMem(String mem) {
        this.mem = mem == null ? null : mem.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.network_mode
     *
     * @return the value of op_device.network_mode
     *
     * @mbg.generated
     */
    public String getNetworkMode() {
        return networkMode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.network_mode
     *
     * @param networkMode the value for op_device.network_mode
     *
     * @mbg.generated
     */
    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode == null ? null : networkMode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.remark
     *
     * @return the value of op_device.remark
     *
     * @mbg.generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.remark
     *
     * @param remark the value for op_device.remark
     *
     * @mbg.generated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.com_id
     *
     * @return the value of op_device.com_id
     *
     * @mbg.generated
     */
    public Integer getComId() {
        return comId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.com_id
     *
     * @param comId the value for op_device.com_id
     *
     * @mbg.generated
     */
    public void setComId(Integer comId) {
        this.comId = comId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.group_id
     *
     * @return the value of op_device.group_id
     *
     * @mbg.generated
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.group_id
     *
     * @param groupId the value for op_device.group_id
     *
     * @mbg.generated
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.create_time
     *
     * @return the value of op_device.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.create_time
     *
     * @param createTime the value for op_device.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.create_user_id
     *
     * @return the value of op_device.create_user_id
     *
     * @mbg.generated
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.create_user_id
     *
     * @param createUserId the value for op_device.create_user_id
     *
     * @mbg.generated
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column op_device.status
     *
     * @return the value of op_device.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column op_device.status
     *
     * @param status the value for op_device.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table op_device
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
        sb.append(", mobile=").append(mobile);
        sb.append(", imsi=").append(imsi);
        sb.append(", imei=").append(imei);
        sb.append(", model=").append(model);
        sb.append(", cpu=").append(cpu);
        sb.append(", ram=").append(ram);
        sb.append(", mem=").append(mem);
        sb.append(", networkMode=").append(networkMode);
        sb.append(", remark=").append(remark);
        sb.append(", comId=").append(comId);
        sb.append(", groupId=").append(groupId);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}