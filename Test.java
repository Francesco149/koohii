import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

class Test {

private static
void print_score(TestSuite.Score s)
{
    Koohii.info(
        "%d +%s %dx %dx300 %dx100 %dx50 %dxmiss %spp\n",
        s.id, Koohii.mods_str(s.mods), s.max_combo, s.n300, s.n100,
        s.n50, s.nmiss, s.pp
    );
}

/**
* pp can be off by +- 2%.
* margin is actually 3x for under 100pp, 2x for 100-200, 1.5x for
* 200-300.
*/
private static final double ERROR_MARGIN = 0.02;

public static
void main(String[] args) throws java.io.IOException
{
    Koohii.Parser parse = new Koohii.Parser();
    Koohii.Map beatmap = new Koohii.Map();
    Koohii.DiffCalc stars = new Koohii.DiffCalc();

    parse.beatmap = beatmap;
    stars.beatmap = beatmap;

    try
    {
        for (TestSuite.Score s : TestSuite.scores)
        {
            print_score(s);

            BufferedReader reader = new BufferedReader(
                new FileReader(
                    String.format("test_suite/%d.osu", s.id)
                )
            );

            parse.map(reader);
            stars.calc(s.mods);

            Koohii.PPv2Parameters p = new Koohii.PPv2Parameters();
            p.beatmap = beatmap;
            p.aim_stars = stars.aim;
            p.speed_stars = stars.speed;
            p.mods = s.mods;
            p.n300 = s.n300;
            p.n100 = s.n100;
            p.n50 = s.n50;
            p.nmiss = s.nmiss;
            p.combo = s.max_combo;
            Koohii.PPv2 pp = new Koohii.PPv2(p);

            double margin = s.pp * ERROR_MARGIN;

            if (s.pp < 100) {
                margin *= 3;
            }
            else if (s.pp < 200) {
                margin *= 2;
            }
            else if (s.pp < 300) {
                margin *= 1.5;
            }

            if (Math.abs(pp.total - s.pp) >= margin)
            {
                Koohii.info(
                    "failed test: got %spp, expected %s\n%s\n",
                    pp.total, s.pp, stars
                );

                System.exit(1);
            }

            reader.close();
        }
    }

    catch (FileNotFoundException e)
    {
        Koohii.info(
            "please download the test suite by running " +
            "./download_suite\n"
        );

        System.exit(1);
    }

    catch (Exception e) {
        Koohii.info("something went wrong:\n%s\n", parse);
    }
}

}
