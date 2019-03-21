
package zuzusoft.com.bagbag.get_start.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reg_code")
    @Expose
    private Object regCode;
    @SerializedName("reg_status")
    @Expose
    private String regStatus;
    @SerializedName("presetcode")
    @Expose
    private Object presetcode;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("private_key")
    @Expose
    private String privateKey;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("last_login")
    @Expose
    private Object lastLogin;
    @SerializedName("user_token")
    @Expose
    private String userToken;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("social_provider")
    @Expose
    private String socialProvider;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("latlan")
    @Expose
    private String latlan;
    @SerializedName("location")
    @Expose
    private Object location;
    private final static long serialVersionUID = 6236821567322881331L;

    public String getLatlan() {
        return latlan;
    }

    public void setLatlan(String latlan) {
        this.latlan = latlan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRegCode() {
        return regCode;
    }

    public void setRegCode(Object regCode) {
        this.regCode = regCode;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public Object getPresetcode() {
        return presetcode;
    }

    public void setPresetcode(Object presetcode) {
        this.presetcode = presetcode;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Object getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Object lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialProvider() {
        return socialProvider;
    }

    public void setSocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

}
