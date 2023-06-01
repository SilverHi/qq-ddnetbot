package com.silver.ddrtools.common.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_cmd
 * @author 
 */
@Data
public class SysCmd implements Serializable {
    private Long id;

    private String cmdName;

    private String className;

    private static final long serialVersionUID = 1L;
}