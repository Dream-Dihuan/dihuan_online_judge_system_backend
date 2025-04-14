package com.dihuan.common.utils.jsonWebTokenUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo{
    private Long id;
    private String username;
}
