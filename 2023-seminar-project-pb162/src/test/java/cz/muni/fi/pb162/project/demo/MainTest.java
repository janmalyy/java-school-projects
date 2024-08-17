package cz.muni.fi.pb162.project.demo;

import cz.muni.fi.pb162.project.Chess;
import cz.muni.fi.pb162.project.helper.OutputTester;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static cz.muni.fi.pb162.project.GameWritable.GSON;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing print output of Main class.
 *
 * @author Alzbeta Strompova
 */
class MainTest {

    private static final String EXPECTED_OUTPUT =
            """
                    {
                      "mementoHistory": [],
                      "board": {
                        "squares": [
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ],
                          [
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                          ]
                        ],
                        "round": 0
                      },
                      "playerOne": {
                        "name": "Mat",
                        "color": "WHITE"
                      },
                      "playerTwo": {
                        "name": "Pat",
                        "color": "BLACK"
                      },
                      "stateOfGame": "PLAYING"
                    }""";

    @Test
    void testMainOutput() throws IOException {
        //changed in order to be more flexible (@author pseudo)
        assertThat(GSON.fromJson(actualOutput(), Chess.class)).isEqualTo(GSON.fromJson(EXPECTED_OUTPUT, Chess.class));
    }

    private String actualOutput() throws IOException {
        OutputTester ot = new OutputTester();
        ot.captureOutput();
        Main.main(null);
        ot.releaseOutput();
        return ot.getOutput();
    }

}
