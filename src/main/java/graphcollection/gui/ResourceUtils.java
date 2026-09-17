package graphcollection.gui;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class ResourceUtils {

    @SneakyThrows
    public static String load(String fileName) {
        @Cleanup InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(fileName);
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
