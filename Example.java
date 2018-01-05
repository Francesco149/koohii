package com.example;

/* cat file.osu | java com.example.Example +HDDT 95% 300x 1m */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.github.francesco149.koohii.*;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

class Example {

public static
void main(String[] args) throws java.io.IOException
{
    int mods = 0;
    float acc_percent = 100.0f;
    int combo = -1;
    int nmiss = 0;

    /* get mods, acc, combo, misses from command line arguments
    format: +HDDT 95% 300x 1m */
    for (int i = 0; i < args.length; ++i)
    {
        if (args[i].startsWith("+")) {
            mods = Koohii.mods_from_str(args[i].substring(1));
        }

        else if (args[i].endsWith("%")) {
            acc_percent = parseFloat(
                args[i].substring(0, args[i].length() - 1)
            );
        }

        else if (args[i].endsWith("x")) {
            combo = parseInt(
                args[i].substring(0, args[i].length() - 1)
            );
        }

        else if (args[i].endsWith("m")) {
            nmiss = parseInt(
                args[i].substring(0, args[i].length() - 1)
            );
        }
    }

    /* parse beatmap */
    BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in)
    );

    Koohii.Map beatmap = new Koohii.Parser().map(stdin);

    System.out.printf("%s - %s [%s] +%s\n",
        beatmap.artist, beatmap.title, beatmap.version,
        Koohii.mods_str(mods));

    System.out.printf("OD%g AR%g CS%g HP%g\n", beatmap.od,
        beatmap.ar, beatmap.cs, beatmap.hp);

    /* calculate star rating */
    Koohii.DiffCalc stars =
        new Koohii.DiffCalc().calc(beatmap, mods);

    System.out.println(stars);

    /* round accuracy % to closest amount of 300, 100, 50 */
    Koohii.Accuracy acc = new Koohii.Accuracy(
        acc_percent, beatmap.objects.size(), nmiss
    );

    /* calculate pp */
    Koohii.PPv2Parameters params = new Koohii.PPv2Parameters();
    params.beatmap = beatmap;
    params.aim_stars = stars.aim;
    params.speed_stars = stars.speed;
    params.mods = mods;
    params.combo = combo;
    params.n300 = acc.n300;
    params.n100 = acc.n100;
    params.n50 = acc.n50;
    params.nmiss = nmiss;

    Koohii.PPv2 pp = new Koohii.PPv2(params);

    System.out.printf("%gpp\n", pp.total);
}

}

