package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class StuffFactoryTest {
    @Test
    void createsExpectedEquipmentAndPlacesItInTheWorld() {
        World world = GameTestSupport.floorWorld(20, 20, 1);
        StuffFactory factory = new StuffFactory(world);

        Item dagger = factory.newDagger(0);
        Item bow = factory.newBow(0);
        Item armor = factory.newHeavyArmor(0);
        Item rock = factory.newRock(0);

        assertEquals("dagger", dagger.name());
        assertEquals(5, dagger.attackValue());
        assertEquals(6, dagger.thrownAttackValue());
        assertEquals(5, bow.rangedAttackValue());
        assertEquals(6, armor.defenseValue());
        assertEquals(6, rock.thrownAttackValue());
        assertTrue(isInWorld(world, dagger));
        assertTrue(isInWorld(world, bow));
        assertTrue(isInWorld(world, armor));
        assertTrue(isInWorld(world, rock));
    }

    @Test
    void createsPlayerAndEnemiesWithTheirExpectedIdentityAndAi() {
        World world = GameTestSupport.floorWorld(20, 20, 1);
        StuffFactory factory = new StuffFactory(world);

        Creature player = factory.newPlayer(new java.util.ArrayList<>(), new FieldOfView(world));
        Creature bat = factory.newBat(0);
        Creature zombie = factory.newZombie(0, player);
        Creature goblin = factory.newGoblin(0, player);
        Creature fungus = factory.newFungus(0);

        assertEquals("@", Character.toString(player.glyph()));
        assertEquals("bat", bat.name());
        assertEquals("zombie", zombie.name());
        assertEquals("goblin", goblin.name());
        assertEquals("fungus", fungus.name());
        assertNotNull(world.creature(player.x, player.y, 0));
        assertTrue(goblin.weapon() != null || goblin.armor() != null);
    }

    @Test
    void producesFoodPotionsAndSpellbooksWithUsableEffects() {
        World world = GameTestSupport.floorWorld(20, 20, 1);
        StuffFactory factory = new StuffFactory(world);
        Item bread = factory.newBread(0);
        Item fruit = factory.newFruit(0);
        Item healthPotion = factory.newPotionOfHealth(0);
        Item whiteBook = factory.newWhiteMagesSpellbook(0);
        Item blueBook = factory.newBlueMagesSpellbook(0);
        Item victoryItem = factory.newVictoryItem(0);

        assertEquals(400, bread.foodValue());
        assertEquals(100, fruit.foodValue());
        assertNotNull(healthPotion.quaffEffect());
        assertEquals(4, whiteBook.writtenSpells().size());
        assertEquals(4, blueBook.writtenSpells().size());
        assertEquals("teddy bear", victoryItem.name());
        assertNotNull(victoryItem.color());
    }

    private static boolean isInWorld(World world, Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                if (world.item(x, y, 0) == item) {
                    return true;
                }
            }
        }
        return false;
    }
}
