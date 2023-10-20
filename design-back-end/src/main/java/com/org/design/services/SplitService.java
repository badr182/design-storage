package com.org.design.services;

import com.org.design.entities.Chunks;
import com.org.design.entities.OriginalFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface SplitService {

    OriginalFile split(MultipartFile file) throws Exception;
}
