package io.github.metalturtle18.scanblockhunt.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;

public class MiscUtils {
    public static String getDisplayName(Material material) {
        return WordUtils.capitalize(material.name().replace("_", " ").toLowerCase());
    }
}
