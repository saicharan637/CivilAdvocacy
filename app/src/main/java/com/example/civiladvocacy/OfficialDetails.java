package com.example.civiladvocacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OfficialDetails implements Serializable {
    public String name;
    private String office;
    public String party;
    public String address;
    private String phone;
    private String email;
    private String website;
    public String photo;
    private Map<String,String> social_media;

    public OfficialDetails(String name, String office, String party, String address, String phone, String email, String website, String photo, Map<String, String> social_media) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.photo = photo;
        this.social_media = social_media;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Map<String, String> getSocial_media() {
        return social_media;
    }

    public void setSocial_media(Map<String, String> social_media) {
        this.social_media = social_media;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

}
