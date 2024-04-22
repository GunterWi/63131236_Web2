package com.ngt.cuoiky.model;

import jakarta.persistence.*;

@Entity
@Table(name = "districts")
public class District {
    @Id
    @Column(name="code", length = 20, nullable = false)
    private String code;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="full_name", length = 255)
    private String fullName;

    @Column(name="code_name", length = 255)
    private String codeName;

    @ManyToOne
    @JoinColumn(name="province_code")
    private Province province;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    
}
