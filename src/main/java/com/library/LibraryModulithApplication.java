package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

@Modulith(
    systemName = "Library Management System",
    sharedModules = "shared"
)
@SpringBootApplication
public class LibraryModulithApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryModulithApplication.class, args);
    }
}
