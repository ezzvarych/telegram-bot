package com.telegram.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilesProvider {

    @Value("${images.folder}")
    private String filesFolderPath;

    private List<String> imgPaths;

    @PostConstruct
    public void init() {
        imgPaths = getImgFiles();

    }

    public String getPathByIndex(int index) {
        return imgPaths.get(index);
    }

    public int getAmountOfFiles() {
        return imgPaths.size();
    }

    @SneakyThrows
    private List<String> getImgFiles() {
        File f = new ClassPathResource(filesFolderPath + File.separatorChar).getFile().getCanonicalFile();
        return Optional.ofNullable(f.list())
                .stream()
                .flatMap(Stream::of)
                .map(file -> {
                    String absPath = f.getAbsolutePath();
                    return absPath + File.separatorChar + file;
                })
                .collect(Collectors.toList());
    }
}
