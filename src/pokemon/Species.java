package pokemon;

import type.Type;

import java.util.ArrayList;

/*
 * Pokemon abstract class. implements pokemonInterface because we want those values.
 * Here is where a lot of the setup is done as we can have a lot of info to be defined at a later instance.
 * The reason this exists, is that there is a lot of information that is common to pokemon species.
 *
 * e.g. every charizard has the same name, type, base stats, but can have different natures, ivs, evs, etc.
 *
 * this class does everything that is common to all pokemon of the same species
 *
 */


public abstract class Species implements PokemonInterface {
    // this is the parent class for all the pokemon in the battle

    private final String name; // the pokemons name

    private final ArrayList<Type> types; // the types of the pokemon.

    // base stats are universal to all pokemon of the same species.
    private final int[] baseStats;


    // constructor here, so we can use super
    public Species(String name, ArrayList<Type> types, int[] baseStats) {
        this.name = name;
        this.types = types;
        this.baseStats = baseStats;

    }





    public String getName() {
        return name;
    }


    public int[] getBaseStats() {
        return baseStats;
    }


    public ArrayList<Type> getTypes() {
        return types;
    }

    public String displayTypes() {  // we want to be able to call this method, and return the type's of the pokemon in a readable manner.
        String typeText = types.get(0).getTypeName();
        return types.size() == 1 ? typeText + " type" : typeText +  " and " + types.get(1).getTypeName() + " type";
    }

    public String toString() {
        // we don't need to do the moves, that's taken care of elsewhere
        return "Name: " + name + "\n" + displayTypes();

    }

}

