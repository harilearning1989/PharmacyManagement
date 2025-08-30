package com.web.pharma.inventory.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class JsonFileReaderUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> List<T> readListFromJson(InputStream inputStream, String rootNode, Class<T> clazz)
            throws IOException {
        JsonNode root = mapper.readTree(inputStream);
        return mapper.readerForListOf(clazz).readValue(root.get(rootNode));
    }

    public <T> List<T> readListFromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        log.debug("Listing all medicines readListFromJson");
        return mapper.readerForListOf(clazz).readValue(inputStream);
    }
}
