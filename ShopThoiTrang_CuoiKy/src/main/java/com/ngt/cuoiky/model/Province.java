package com.ngt.cuoiky.model;

import jakarta.persistence.*;

@Entity
@Table(name = "provinces")
public class Province {
    @Id
    @Column(name="code", length = 20, nullable = false)
    private String code;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="full_name", length = 255)
    private String fullName;

    @Column(name="code_name", length = 255)
    private String codeName;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCodeName() {
        return codeName;
    }
    
}
