package kr.apartribebackend.global.config.converter;


import kr.apartribebackend.article.domain.Level;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;


@Slf4j
public class LevelConverter implements Converter<String, Level> {

    @Override
    public Level convert(String source) {
        return Arrays.stream(Level.values())
                .filter(level -> level.getName().equals(source))
                .findFirst()
                .orElse(Level.ALL);
    }
}
