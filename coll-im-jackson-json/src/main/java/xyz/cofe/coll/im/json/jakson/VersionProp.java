package xyz.cofe.coll.im.json.jakson;

import xyz.cofe.coll.im.ImList;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

public class VersionProp {
    private static final Properties props;

    static {
        var url = VersionProp.class.getResource("/version.properties");
        if (url == null) {
            props = new Properties();
        } else {
            var txt = "";
            try (var strm = url.openStream()) {
                txt = new String(strm.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                System.err.println("can't read " + url + ": " + ex);
            }
            props = new Properties();
            try {
                props.load(new StringReader(txt));
            } catch (IOException ex) {
                System.err.println("can't read " + url + ": " + ex);
            }
        }
    }

    private static final Map<String, String> propMap;

    static {
        propMap = new HashMap<>();
        for (var k : props.stringPropertyNames()) {
            var v = props.get(k);
            if (v instanceof String s) {
                propMap.put(k, s);
            }
        }
    }

    public static Map<String, String> getProperties() {
        return new HashMap<>(propMap);
    }

    public static Optional<String> versionString() {
        return Optional.ofNullable(propMap.get("version"));
    }

    public static Optional<String> groupIdString() {
        return Optional.ofNullable(propMap.get("groupId"));
    }

    public static Optional<String> artifactIdString() {
        return Optional.ofNullable(propMap.get("artifactId"));
    }

    private static final Pattern VersionPattern = Pattern.compile("(?<nums>\\d+(\\.\\d+)*)(-(?<snapshotMarker>.+))?");

    public record Version(ImList<Integer> numbers, Optional<String> snapshotMarker) {
        public static Optional<Version> parse(String string) {
            if (string == null) throw new IllegalArgumentException("string==null");

            var m = VersionPattern.matcher(string);
            if (!m.matches()) return Optional.empty();

            var numsStr = m.group("nums");
            var marker = m.group("snapshotMarker");

            var nums =
                ImList.from(
                    Arrays.stream(numsStr.split("\\.")).map(Integer::parseInt).toList()
                );

            return
                Optional.of(new Version(nums, Optional.ofNullable(marker)));
        }

        public Optional<Integer> getMajor(){
            return numbers.get(0);
        }

        public Optional<Integer> getMinor(){
            return numbers.get(1);
        }

        public Optional<Integer> getPatch(){
            return numbers.get(2);
        }
    }

    private static final Optional<Version> version;
    static {
        version = versionString().flatMap(Version::parse);
    }

    public static Optional<Version> version(){ return version; }
}
