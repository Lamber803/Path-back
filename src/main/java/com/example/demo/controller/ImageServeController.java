package com.example.demo.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ImageServeController {

    @GetMapping("/images/{filename}")
    public ResponseEntity<FileSystemResource> serveImage(@PathVariable String filename) {
        File file = new File("/Users/crystlawang/eclipse-workspace/pathfinder/pathfinder-backend/uploads/" + filename);
        if (file.exists()) {
            return ResponseEntity.ok(new FileSystemResource(file));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
