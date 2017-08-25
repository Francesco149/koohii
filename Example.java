import java.io.BufferedReader;
import java.io.InputStreamReader;

class Example {

public static
void main(String[] args) throws java.io.IOException
{
    BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in)
    );

    Koohii.Map beatmap = new Koohii.Parser().map(stdin);
    Koohii.DiffCalc stars = new Koohii.DiffCalc().calc(beatmap);
    System.out.printf("%s stars\n", stars.total);

    Koohii.PPv2 pp = new Koohii.PPv2(
        stars.aim, stars.speed, beatmap
    );

    System.out.printf("%s pp\n", pp.total);
}

}

