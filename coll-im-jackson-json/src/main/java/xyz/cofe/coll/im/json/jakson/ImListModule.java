package xyz.cofe.coll.im.json.jakson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import xyz.cofe.coll.im.ImList;

public class ImListModule extends SimpleModule {
    @Override
    public String getModuleName() {
        return "ImListModule";
    }

    @Override
    public Version version() {
        return version;
    }

    private static final Version version; // = new Version(3,0,0,null,"groupId", "artifactId");

    static {
        version = VersionProp.version().map( v -> new Version(
            v.getMajor().orElse(3),
            v.getMinor().orElse(0),
            v.getPatch().orElse(0),
            v.snapshotMarker().orElse(null),
            VersionProp.groupIdString().orElse("ImListModule-groupId"),
            VersionProp.artifactIdString().orElse("ImListModule-artifactId")
        )).orElse(
            new Version(3,0,0,null,"ImListModule-groupId", "ImListModule-artifactId")
        );
    }

    public ImListModule(){
        addSerializer(ImList.class, new ImListSerializer());
    }
}
