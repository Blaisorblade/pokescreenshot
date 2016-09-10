package com.braisgabin.pokescreenshot.processing;

import com.google.auto.value.AutoValue;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuesserUnitTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testCalculateCp() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.calculateCp(coreStats, 17, 14, 15, 15), is(732));
  }

  @Test
  public void testCalculateCp_min10() {
    final CoreStats coreStats = IvImplementation.create(100, 100, 100);
    assertThat(Guesser.calculateCp(coreStats, 1, 0, 0, 0), is(10));
  }

  @Test
  public void testCalculateHP() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.calculateHp(coreStats, 17, 15), is(63));
  }

  @Test
  public void testCalculateHP_min10() {
    final CoreStats coreStats = IvImplementation.create(100, 100, 100);
    assertThat(Guesser.calculateHp(coreStats, 1, 0), is(10));
  }

  @Test
  public void testGetPokemon_eevee() {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 482, 63, 17), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_vaporeon() {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 1173, 142, 15), is(coreStats[1]));
  }

  @Test
  public void testGetPokemon_jolteon() {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 736, 64, 13), is(coreStats[2]));
  }

  @Test
  public void testGetPokemon_flareon() {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 1415, 82, 19), is(coreStats[3]));
  }

  @Test
  @Ignore("I must find a solution")
  public void testGetPokemon_caterpie() {
    final CoreStats[] coreStats = {
        IvImplementation.create(62, 66, 90),
        IvImplementation.create(56, 86, 100),
        IvImplementation.create(144, 144, 120),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 150, 54, 16), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_none() {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("Unknown pokémon.");
    Guesser.getPokemon(Arrays.asList(coreStats), 50, 82, 19);
  }

  @Test
  public void testGetPokemon_tooMuch() {
    final CoreStats[] coreStats = {
        IvImplementation.create(238, 178, 130),
        IvImplementation.create(238, 178, 130),
    };
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("Multiple Pokémon.");
    Guesser.getPokemon(Arrays.asList(coreStats), 1415, 82, 19);
  }

  @Test
  public void testIv_eevee() {
    final CoreStats coreStats = IvImplementation.create(114, 128, 110);
    assertThat(Guesser.iv(coreStats, 482, 63, 17), is(new int[][]{
        {10, 15, 5},
        {11, 13, 5},
        {14, 6, 5},
        {15, 4, 5},
        {10, 14, 6},
        {13, 7, 6},
        {14, 5, 6},
        {15, 3, 6},
    }));
  }

  @Test
  public void testIv_eevee_wrong() {
    final CoreStats coreStats = IvImplementation.create(114, 128, 110);
    assertThat(Guesser.iv(coreStats, 482, 63, 15), is(new int[][]{}));
  }

  @Test
  public void testIv_ponyta() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.iv(coreStats, 732, 63, 17), is(new int[][]{
        {14, 15, 15},
    }));
  }

  @AutoValue
  abstract static class IvImplementation implements CoreStats {
    static CoreStats create(int atk, int def, int stam) {
      return new AutoValue_GuesserUnitTest_IvImplementation(atk, def, stam);
    }

    @Override
    public abstract int atk();

    @Override
    public abstract int def();

    @Override
    public abstract int stam();
  }
}
