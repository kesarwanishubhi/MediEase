package com.shubhi.mediease.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadRequest {
    private MultipartFile file;
}
