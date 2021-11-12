package fr.insalyon.pldagile.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XMLFileOpenerTest {
    XMLFileOpener xmlFileOpener;

    @BeforeEach
    void setup() throws Exception {
        this.xmlFileOpener = new XMLFileOpener();
    }

    @Test
    public void testAccept() throws Exception {
        class TestCase {
            final File f;
            final boolean expected;

            public TestCase(File f, boolean expected) {
                this.f = f;
                this.expected = expected;
            }
        }
        TestCase[] tests = {
                new TestCase(new File(getClass().getClassLoader().getResource("xml/testMap.xml").toURI()), true),
                new TestCase(new File("notADIr"), false),
                new TestCase(new File(getClass().getClassLoader().getResource("xml/badFile").toURI()), false),
                new TestCase(new File(getClass().getClassLoader().getResource("xml/badFile.badExtension").toURI()),
                        false),
                new TestCase(null, false), };
        for (TestCase tc : tests) {

            boolean actual = xmlFileOpener.accept(tc.f);
            assertEquals(tc.expected, actual);
        }
    }

}
