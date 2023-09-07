package com.ht.so5.git.server.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GitConfiguration {

    private String name;
    private String password;

}
