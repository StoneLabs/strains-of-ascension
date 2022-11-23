package net.stone_labs.strainsofascension.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ResourceLoader
{
    public static String LoadResource(String path)
    {
        InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
        assert stream != null;

        return new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
    }
}
