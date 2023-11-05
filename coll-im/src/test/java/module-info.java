open module xyz.cofe.coll.im.test {
    exports xyz.cofe.coll.im.test;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.platform.commons;
    requires xyz.cofe.coll.im;
}