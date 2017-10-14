package org.memes.lolisamurai.koohii;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VersusScoresTest {
  @Parameters(name = "{0}")
  public static Iterable<? extends Object> data() {
    return TestSuite.scores;
  }

  private static void print_score(Score s) {
    Koohii.info("%d +%s %dx %dx300 %dx100 %dx50 %dxmiss %spp\n", s.id, Koohii.mods_str(s.mods),
        s.max_combo, s.n300, s.n100, s.n50, s.nmiss, s.pp);
  }

  /**
   * pp can be off by +- 2%. margin is actually 3x for under 100pp, 2x for 100-200, 1.5x for
   * 200-300.
   */
  private static final double ERROR_MARGIN = 0.02;

  private final Score expected;

  public VersusScoresTest(Score expected) {
    super();
    this.expected = expected;
  }

  @Test
  public void runTest() throws java.io.IOException {
    Koohii.Parser parse = new Koohii.Parser();
    Koohii.Map beatmap = new Koohii.Map();
    Koohii.DiffCalc stars = new Koohii.DiffCalc();

    parse.beatmap = beatmap;
    stars.beatmap = beatmap;

    print_score(expected);

    InputStream is =
        ClassLoader.getSystemResourceAsStream(String.format("test_suite/%d.osu", expected.id));
    if (is == null) {
      throw new FileNotFoundException("please download the test suite");
    }

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

      parse.map(reader);
    } finally {
      is.close();
    }

    stars.calc(expected.mods);

    Koohii.PPv2Parameters p = new Koohii.PPv2Parameters();
    p.beatmap = beatmap;
    p.aim_stars = stars.aim;
    p.speed_stars = stars.speed;
    p.mods = expected.mods;
    p.n300 = expected.n300;
    p.n100 = expected.n100;
    p.n50 = expected.n50;
    p.nmiss = expected.nmiss;
    p.combo = expected.max_combo;
    Koohii.PPv2 pp = new Koohii.PPv2(p);

    double margin = expected.pp * ERROR_MARGIN;

    if (expected.pp < 100) {
      margin *= 3;
    } else if (expected.pp < 200) {
      margin *= 2;
    } else if (expected.pp < 300) {
      margin *= 1.5;
    }

    assertEquals(expected.toString(), expected.pp, pp.total, margin);
  }
}
