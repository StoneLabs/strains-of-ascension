package net.stone_labs.strainsofascension.utils;

import net.stone_labs.strainsofascension.ArtifactManager;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceLoader
{
    public static String LoadResource(String path)
    {
        InputStream stream = ArtifactManager.class.getClassLoader().getResourceAsStream(path);
        assert stream != null;

        return new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
    }
}
