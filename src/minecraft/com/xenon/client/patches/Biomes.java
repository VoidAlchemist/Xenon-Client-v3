package com.xenon.client.patches;

import net.minecraft.world.biome.*;
import static net.minecraft.world.biome.BiomeGenBase.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Class patch for Biomes, to prevent class loading deadlocks.
 * @author Zenon
 */
public class Biomes {


    public static final BiomeGenBase ocean,
            plains,
            desert,
            extremeHills,
            forest,
            taiga,
            swampland,
            river,
            hell,
            sky,
            frozenOcean,
            frozenRiver,
            icePlains,
            iceMountains,
            mushroomIsland,
            mushroomIslandShore,
            beach,
            desertHills,
            forestHills,
            taigaHills,
            extremeHillsEdge,
            jungle,
            jungleHills,
            jungleEdge,
            deepOcean,
            stoneBeach,
            coldBeach,
            birchForest,
            birchForestHills,
            roofedForest,
            coldTaiga,
            coldTaigaHills,
            megaTaiga,
            megaTaigaHills,
            extremeHillsPlus,
            savanna,
            savannaPlateau,
            mesa,
            mesaPlateau_F,
            mesaPlateau;

    private static final BiomeGenBase[] biomeList;
    public static final Map<String, BiomeGenBase> BIOME_ID_MAP;
    public static final Set<BiomeGenBase> explorationBiomesList;

    static {
        biomeList = new BiomeGenBase[256];
        BIOME_ID_MAP = new HashMap<>(61);
        explorationBiomesList = new HashSet<>(40);
        
        Consumer<BiomeGenBase> reg = b -> biomeList[b.biomeID] = b;


        reg.accept(ocean = new BiomeGenOcean(0).setColor(112)
                .setBiomeName("Ocean").setHeight(height_Oceans));

        reg.accept(plains = new BiomeGenPlains(1).setColor(9286496).setBiomeName("Plains"));

        reg.accept(desert = new BiomeGenDesert(2).setColor(16421912).setBiomeName("Desert")
                        .setDisableRain().setTemperatureRainfall(2.0F, 0.0F)
                .setHeight(height_LowPlains));

        reg.accept(extremeHills = new BiomeGenHills(3, false)
                        .setColor(6316128).setBiomeName("Extreme Hills").setHeight(height_MidHills)
                        .setTemperatureRainfall(0.2F, 0.3F));

        reg.accept(forest = new BiomeGenForest(4, 0)
                        .setColor(353825).setBiomeName("Forest"));

        reg.accept(taiga = new BiomeGenTaiga(5, 0).setColor(747097)
                        .setBiomeName("Taiga").setFillerBlockMetadata(5159473)
                        .setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains));

        reg.accept(swampland = new BiomeGenSwamp(6).setColor(522674)
                        .setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged)
                        .setTemperatureRainfall(0.8F, 0.9F));

        reg.accept(river = new BiomeGenRiver(7).setColor(255).setBiomeName("River")
                        .setHeight(height_ShallowWaters));

        reg.accept(hell = new BiomeGenHell(8).setColor(16711680).setBiomeName("Hell")
                        .setDisableRain().setTemperatureRainfall(2.0F, 0.0F));

        reg.accept(sky = new BiomeGenEnd(9).setColor(8421631)
                        .setBiomeName("The End").setDisableRain());

        reg.accept(frozenOcean = new BiomeGenOcean(10).setColor(9474208)
                        .setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans)
                        .setTemperatureRainfall(0.0F, 0.5F));

        reg.accept(frozenRiver = new BiomeGenRiver(11).setColor(10526975)
                        .setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters)
                        .setTemperatureRainfall(0.0F, 0.5F));

        reg.accept(icePlains = new BiomeGenSnow(12, false).setColor(16777215)
                        .setBiomeName("Ice Plains").setEnableSnow()
                        .setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains));

        reg.accept(iceMountains = new BiomeGenSnow(13, false).setColor(10526880)
                        .setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills)
                        .setTemperatureRainfall(0.0F, 0.5F));

        reg.accept(mushroomIsland = new BiomeGenMushroomIsland(14).setColor(16711935)
                        .setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F)
                        .setHeight(height_LowIslands));

        reg.accept(mushroomIslandShore = new BiomeGenMushroomIsland(15).setColor(10486015)
                        .setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F)
                        .setHeight(height_Shores));

        reg.accept(beach = new BiomeGenBeach(16).setColor(16440917).setBiomeName("Beach")
                        .setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Shores));

        reg.accept(desertHills = new BiomeGenDesert(17).setColor(13786898)
                        .setBiomeName("DesertHills").setDisableRain()
                        .setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills));

        reg.accept(forestHills = new BiomeGenForest(18, 0).setColor(2250012)
                        .setBiomeName("ForestHills").setHeight(height_LowHills));

        reg.accept(taigaHills = new BiomeGenTaiga(19, 0).setColor(1456435)
                        .setBiomeName("TaigaHills").setFillerBlockMetadata(5159473)
                        .setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills));

        reg.accept(extremeHillsEdge = new BiomeGenHills(20, true).setColor(7501978)
                        .setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate())
                        .setTemperatureRainfall(0.2F, 0.3F));

        reg.accept(jungle = new BiomeGenJungle(21, false).setColor(5470985)
                        .setBiomeName("Jungle").setFillerBlockMetadata(5470985)
                        .setTemperatureRainfall(0.95F, 0.9F));

        reg.accept(jungleHills = new BiomeGenJungle(22, false).setColor(2900485)
                        .setBiomeName("JungleHills").setFillerBlockMetadata(5470985)
                        .setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills));

        reg.accept(jungleEdge = new BiomeGenJungle(23, true).setColor(6458135)
                        .setBiomeName("JungleEdge").setFillerBlockMetadata(5470985)
                        .setTemperatureRainfall(0.95F, 0.8F));

        reg.accept(deepOcean = new BiomeGenOcean(24).setColor(48).setBiomeName("Deep Ocean")
                        .setHeight(height_DeepOceans));

        reg.accept(stoneBeach = new BiomeGenStoneBeach(25).setColor(10658436)
                        .setBiomeName("Stone Beach").setTemperatureRainfall(0.2F, 0.3F)
                        .setHeight(height_RockyWaters));

        reg.accept(coldBeach = new BiomeGenBeach(26).setColor(16445632).setBiomeName("Cold Beach")
                        .setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores)
                .setEnableSnow());

        reg.accept(birchForest = new BiomeGenForest(27, 2).setBiomeName("Birch Forest")
                        .setColor(3175492));

        reg.accept(birchForestHills = new BiomeGenForest(28, 2)
                        .setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills));

        reg.accept(roofedForest = new BiomeGenForest(29, 3).setColor(4215066)
                        .setBiomeName("Roofed Forest"));

        reg.accept(coldTaiga = new BiomeGenTaiga(30, 0).setColor(3233098)
                        .setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow()
                        .setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_MidPlains)
                        .func_150563_c(16777215));

        reg.accept(coldTaigaHills = new BiomeGenTaiga(31, 0).setColor(2375478)
                        .setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow()
                        .setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_LowHills)
                        .func_150563_c(16777215));

        reg.accept(megaTaiga = new BiomeGenTaiga(32, 1).setColor(5858897)
                        .setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473)
                        .setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains));

        reg.accept(megaTaigaHills = new BiomeGenTaiga(33, 1).setColor(4542270)
                        .setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473)
                        .setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills));

        reg.accept(extremeHillsPlus = new BiomeGenHills(34, true).setColor(5271632)
                        .setBiomeName("Extreme Hills+").setHeight(height_MidHills)
                        .setTemperatureRainfall(0.2F, 0.3F));

        reg.accept(savanna = new BiomeGenSavanna(35).setColor(12431967)
                        .setBiomeName("Savanna").setTemperatureRainfall(1.2F, 0.0F)
                        .setDisableRain().setHeight(height_LowPlains));

        reg.accept(savannaPlateau = new BiomeGenSavanna(36).setColor(10984804)
                        .setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0F, 0.0F)
                        .setDisableRain().setHeight(height_HighPlateaus));

        reg.accept(mesa = new BiomeGenMesa(37, false, false)
                        .setColor(14238997).setBiomeName("Mesa"));

        reg.accept(mesaPlateau_F = new BiomeGenMesa(38, false, true)
                        .setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus));

        reg.accept(mesaPlateau = new BiomeGenMesa(39, false, false)
                        .setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus));
        
        
        
        reg.accept(plains.createMutation());
        reg.accept(desert.createMutation());
        reg.accept(forest.createMutation());
        reg.accept(taiga.createMutation());
        reg.accept(swampland.createMutation());
        reg.accept(icePlains.createMutation());
        reg.accept(jungle.createMutation());
        reg.accept(jungleEdge.createMutation());
        reg.accept(coldTaiga.createMutation());
        reg.accept(savanna.createMutation());
        reg.accept(savannaPlateau.createMutation());
        reg.accept(mesa.createMutation());
        reg.accept(mesaPlateau_F.createMutation());
        reg.accept(mesaPlateau.createMutation());
        reg.accept(birchForest.createMutation());
        reg.accept(birchForestHills.createMutation());
        reg.accept(roofedForest.createMutation());
        reg.accept(megaTaiga.createMutation());
        reg.accept(extremeHills.createMutation());
        reg.accept(extremeHillsPlus.createMutation());
        reg.accept(megaTaiga.createMutatedBiome(megaTaigaHills.biomeID + 128)
                .setBiomeName("Redwood Taiga Hills M"));


        for (BiomeGenBase biome : biomeList) if (biome != null) {
            BiomeGenBase prev = BIOME_ID_MAP.put(biome.biomeName, biome);
            if (prev != null)
                throw new RuntimeException(String.format("Biome \"%s\" is defined as both ID %d and %d",
                        biome.biomeName, prev.biomeID, biome.biomeID));

            if (biome.biomeID < 128)
                explorationBiomesList.add(biome);
        }
        explorationBiomesList.remove(hell);
        explorationBiomesList.remove(sky);
        explorationBiomesList.remove(frozenOcean);
        explorationBiomesList.remove(extremeHillsEdge);

    }



    public static BiomeGenBase[] getBiomeGenArray() {
        return biomeList;
    }
}
