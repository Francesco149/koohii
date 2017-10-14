package com.github.francesco149.koohii;

public class Score {
  public int id, max_combo, n300, n100, n50, nmiss, mods;
  public double pp;

  @Override
  public String toString() {
    return String.format("%d +%s %dx %dx300 %dx100 %dx50 %dxmiss %spp\n", id, Koohii.mods_str(mods),
        max_combo, n300, n100, n50, nmiss, pp);
  }
}
