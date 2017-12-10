package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.github.francesco149.koohii.*;

class Example {

public static
void main(String[] args) throws java.io.IOException
{
    BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in)
    );

    Koohii.Map beatmap = new Koohii.Parser().map(stdin);
    Koohii.DiffCalc stars = new Koohii.DiffCalc().calc(beatmap);
    System.out.println(stars);

    Koohii.PPv2 pp = new Koohii.PPv2(
        stars.aim, stars.speed, beatmap
    );

    System.out.println(pp);
}

}

