package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatureTest {
    private World world;
    private Creature creature;
    private RecordingAi ai;

    @BeforeEach
    void setUp() {
        Tile[][][] tiles = new Tile[21][21][2];

        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                tiles[x][y][0] = Tile.FLOOR;
                tiles[x][y][1] = Tile.FLOOR;
            }
        }

        world = new World(tiles);
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        creature.x = 10;
        creature.y = 10;
        creature.z = 0;

        ai = new RecordingAi(creature);
        world.add(creature);
    }

    @Test
    void constructorAndAccessorValuesAreInitialized() {
        assertEquals('@', creature.glyph());
        assertSame(Color.GRAY, creature.color());
        assertEquals("josh", creature.name());
        assertEquals(100, creature.maxHp());
        assertEquals(100, creature.hp());
        assertEquals(10, creature.attackValue());
        assertEquals(5, creature.defenseValue());
        assertEquals(9, creature.visionRadius());
        assertEquals(1000, creature.maxFood());
        assertEquals(5, creature.mana());
        assertEquals(1, creature.level());
        assertEquals(20, creature.inventory().getItems().length);
        assertNull(creature.weapon());
        assertNull(creature.armor());
        assertTrue(creature.effects().isEmpty());
        assertTrue(creature.isPlayer());
        assertEquals("  level:1  attack:10  defense:5  hp:100", creature.details());
    }

    @Test
    void maxHpManaVisionAndRegenModifiersAreApplied() {
        creature.modifyMaxHp(20);
        creature.modifyMaxMana(5);
        creature.modifyVisionRadius(3);
        creature.modifyRegenHpPer1000(10);
        creature.modifyRegenManaPer1000(-10);

        assertEquals(120, creature.maxHp());
        assertEquals(10, creature.maxMana());
        assertEquals(12, creature.visionRadius());
    }

    @Test
    void regularHpChangesAndCauseOfDeathAreRecorded() {
        creature.modifyHp(-51, "Meaningful Damage");
        assertEquals(49, creature.hp());
        assertEquals("Meaningful Damage", creature.causeOfDeath());

        creature.modifyHp(13, "Meaningful Healing");
        assertEquals(62, creature.hp());
        assertEquals("Meaningful Healing", creature.causeOfDeath());

        creature.modifyHp(-61, "Just Above Death");
        assertEquals(1, creature.hp());
        assertEquals("Just Above Death", creature.causeOfDeath());
        assertSame(creature, world.creature(10, 10, 0));
    }

    @Test
    void overhealIsClampedToMaxHp() {
        creature.modifyHp(-40, "Setup");
        creature.modifyHp(50, "Overheal");

        assertEquals(100, creature.hp());
        assertEquals("Overheal", creature.causeOfDeath());
        assertSame(creature, world.creature(10, 10, 0));
    }

    @Test
    void exactAndOverLethalDamageRemoveCreatureAndCreateCorpse() {
        creature.modifyHp(-100, "Exact Lethal");

        assertEquals(0, creature.hp());
        assertEquals("Exact Lethal", creature.causeOfDeath());
        assertNull(world.creature(10, 10, 0));
        assertTrue(world.item(10, 10, 0).name().endsWith(" corpse"));
    }

    @Test
    void inventoryItemsDropAsCorpseCleanupRuns() {
        Item sword = new Item(')', Color.WHITE, "sword", null);
        creature.inventory().add(sword);
        creature.equip(sword);

        creature.modifyHp(-100, "Lethal");

        Item corpse = world.item(10, 10, 0);
        assertTrue(corpse.name().endsWith(" corpse"));
        boolean swordDropped = false;
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                for (int z = 0; z < world.depth(); z++) {
                    if (world.item(x, y, z) == sword)
                        swordDropped = true;
                }
            }
        }
        assertTrue(swordDropped);
    }

    @Test
    void positiveAndNegativeXpNotifyAndNegativeXpDoesNotLevel() {
        creature.modifyXp(-10);
        assertEquals(-10, creature.xp());
        assertEquals(1, creature.level());
        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("lose")));

        creature.modifyXp(20);
        assertEquals(10, creature.xp());
        assertEquals(1, creature.level());
    }

    @Test
    void xpAboveLevelThresholdLevelsAndNotifiesAi() {
        creature.modifyXp(26);

        assertEquals(2, creature.level());
        assertEquals(1, ai.levelUps);
        assertEquals(100, creature.hp());
    }

    @Test
    void meleeAttackDamagesDefenderAndConsumesFood() {
        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        defender.x = 11;
        defender.y = 10;
        defender.z = 0;
        new RecordingAi(defender);
        world.add(defender);

        creature.meleeAttack(defender);

        assertTrue(defender.hp() >= 95 && defender.hp() <= 99);
    }

    @Test
    void killingCreatureAwardsXpAndRemovesIt() {
        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 1, 5, 0);
        defender.x = 11;
        defender.y = 10;
        defender.z = 0;
        new RecordingAi(defender);
        world.add(defender);

        creature.meleeAttack(defender);

        assertNull(world.creature(11, 10, 0));
        assertTrue(creature.xp() > 0);
    }

    @Test
    void rangedWeaponAttackUsesRangedWeaponValue() {
        Item bow = new Item('}', Color.WHITE, "bow", null);
        bow.modifyRangedAttackValue(8);
        creature.inventory().add(bow);
        creature.equip(bow);

        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        defender.x = 11;
        defender.y = 10;
        defender.z = 0;
        new RecordingAi(defender);
        world.add(defender);

        creature.rangedWeaponAttack(defender);

        assertTrue(defender.hp() >= 92 && defender.hp() <= 99);
    }

    @Test
    void zeroMovementDoesNotChangePositionOrConsumeFood() {
        creature.moveBy(0, 0, 0);

        assertEquals(10, creature.x);
        assertEquals(10, creature.y);
        assertEquals(0, creature.z);
    }

    @Test
    void movementEntersFloorAndConsumesFood() {
        creature.moveBy(1, 0, 0);

        assertEquals(11, creature.x);
        assertEquals(10, creature.y);
        assertEquals(0, creature.z);
    }

    @Test
    void movementIntoWallDoesNotChangePosition() {
        world = new World(new Tile[][][] {{{Tile.WALL}}});
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        creature.x = 0;
        creature.y = 0;
        creature.z = 0;
        ai = new RecordingAi(creature);
        world.add(creature);

        creature.moveBy(1, 0, 0);

        assertEquals(0, creature.x);
        assertEquals(0, creature.y);
        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("bump")));
    }

    @Test
    void stairsRequireMatchingTiles() {
        Tile[][][] tiles = new Tile[5][5][2];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                tiles[x][y][0] = Tile.FLOOR;
                tiles[x][y][1] = Tile.FLOOR;
            }
        }
        tiles[2][2][1] = Tile.STAIRS_UP;
        tiles[2][2][0] = Tile.STAIRS_DOWN;

        world = new World(tiles);
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        ai = new RecordingAi(creature);
        creature.x = 2;
        creature.y = 2;
        creature.z = 0;
        world.add(creature);

        creature.moveBy(0, 0, 1);
        assertEquals(1, creature.z);

        creature.moveBy(0, 0, -1);
        assertEquals(0, creature.z);
    }

    @Test
    void digConvertsDiggableTileAndConsumesFood() {
        world = new World(createTilesWithWall());
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        ai = new RecordingAi(creature);
        creature.x = 10;
        creature.y = 10;
        creature.z = 0;
        world.add(creature);

        creature.dig(11, 10, 0);
        assertEquals(Tile.FLOOR, world.tile(11, 10, 0));
    }

    @Test
    void pickingUpItemAddsItToInventoryAndRemovesWorldItem() {
        Item potion = new Item('!', Color.RED, "potion", null);
        world.addAtEmptySpace(potion, 10, 10, 0);

        creature.pickup();

        assertTrue(creature.inventory().contains(potion));
        assertNull(world.item(10, 10, 0));
    }

    @Test
    void pickupOnEmptyGroundAndFullInventoryAreHandled() {
        creature.pickup();
        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("grab")));

        for (int i = 0; i < 20; i++)
            creature.inventory().add(new Item('?', Color.WHITE, "item" + i, null));

        Item ground = new Item('!', Color.RED, "ground item", null);
        world.addAtEmptySpace(ground, 10, 10, 0);
        creature.pickup();

        assertSame(ground, world.item(10, 10, 0));
        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("grab")));
    }

    @Test
    void dropMovesItemToWorldAndUnequipsIt() {
        Item armor = new Item('[', Color.WHITE, "armor", null);
        armor.modifyDefenseValue(3);
        creature.inventory().add(armor);
        creature.equip(armor);

        creature.drop(armor);

        assertSame(armor, world.item(10, 10, 0));
        assertFalse(creature.inventory().contains(armor));
        assertNull(creature.armor());
    }

    @Test
    void equippingWeaponAndArmorUpdatesCombatValues() {
        Item sword = new Item(')', Color.WHITE, "sword", null);
        sword.modifyAttackValue(4);
        Item armor = new Item('[', Color.WHITE, "chainmail", null);
        armor.modifyDefenseValue(3);

        creature.equip(sword);
        creature.equip(armor);

        assertSame(sword, creature.weapon());
        assertSame(armor, creature.armor());
        assertEquals(14, creature.attackValue());
        assertEquals(8, creature.defenseValue());
    }

    @Test
    void equippingFullInventoryRejectsOutsideItem() {
        for (int i = 0; i < 20; i++)
            creature.inventory().add(new Item('?', Color.WHITE, "item" + i, null));

        Item outside = new Item(')', Color.WHITE, "sword", null);
        outside.modifyAttackValue(4);

        creature.equip(outside);

        assertFalse(creature.inventory().contains(outside));
        assertNull(creature.weapon());
        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("holding too much")));
    }

    @Test
    void manaModificationClampsAtZeroAndMaxMana() {
        creature.modifyMana(-20);
        assertEquals(0, creature.mana());

        creature.modifyMana(100);
        assertEquals(5, creature.mana());

        creature.modifyMana(-2);
        assertEquals(3, creature.mana());
    }

    @Test
    void updateRegeneratesHealthManaFoodAndCallsAi() {
        creature.modifyHp(-50, "Damage");
        creature.modifyMana(-3);

        creature.update();

        assertEquals(51, creature.hp());
        assertEquals(3, creature.mana());
        assertEquals(1, ai.updates);
    }

    @Test
    void repeatedUpdatesApplyEffectOncePerTickAndRemoveCompletedEffect() {
        CountingEffect effect = new CountingEffect(2);
        creature.effects().add(effect);

        creature.update();
        assertEquals(1, effect.updateCount);
        assertFalse(effect.isDone());
        assertTrue(creature.effects().contains(effect));

        creature.update();
        assertEquals(2, effect.updateCount);
        assertEquals(1, effect.endCount);
        assertFalse(creature.effects().contains(effect));
    }

    @Test
    void foodOvereatingReducesHealthAndChangesCapacity() {
        creature.modifyFood(1000);

        assertEquals(1333, creature.maxFood());
        assertEquals(99, creature.hp());
    }

    @Test
    void playerStarvesButMonsterDoesNot() {
        creature.modifyFood(-667);

        assertTrue(creature.hp() < 1);
        assertNull(world.creature(10, 10, 0));

        Creature monster = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        monster.x = 11;
        monster.y = 10;
        monster.z = 0;
        new RecordingAi(monster);
        world.add(monster);
        monster.modifyFood(-667);

        assertEquals(100, monster.hp());
        assertSame(monster, world.creature(11, 10, 0));
    }

    @Test
    void eatingAndQuaffingConsumeItemAndApplyQuaffEffect() {
        Item potion = new Item('!', Color.RED, "potion", null);
        potion.modifyFoodValue(10);
        CountingEffect effect = new CountingEffect(2);
        potion.setQuaffEffect(effect);
        creature.inventory().add(potion);

        creature.quaff(potion);

        assertFalse(creature.inventory().contains(potion));
        assertTrue(creature.effects().contains(effect));
        assertEquals(1, effect.startCount);
    }

    @Test
    void detectCreaturesRevealsCreaturesOutsideAiVisibility() {
        Creature hidden = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        hidden.x = 10;
        hidden.y = 11;
        hidden.z = 0;
        new RecordingAi(hidden);
        world.add(hidden);

        ai.visibleDistance = 0;
        assertFalse(creature.canSee(hidden.x, hidden.y, hidden.z));

        creature.modifyDetectCreatures(1);
        assertTrue(creature.canSee(hidden.x, hidden.y, hidden.z));
        assertSame(hidden, creature.creature(hidden.x, hidden.y, hidden.z));
    }

    @Test
    void tileAndCreatureViewMethodsRespectVisibility() {
        ai.visibleDistance = 1;
        Creature nearby = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        nearby.x = 11;
        nearby.y = 10;
        nearby.z = 0;
        new RecordingAi(nearby);
        world.add(nearby);

        assertEquals(Tile.FLOOR, creature.tile(11, 10, 0));
        assertSame(nearby, creature.creature(11, 10, 0));

        ai.visibleDistance = 0;
        assertEquals(Tile.UNKNOWN, creature.tile(11, 10, 0));
        assertNull(creature.creature(11, 10, 0));
    }

    @Test
    void doActionSendsFirstPersonAndThirdPersonNotifications() {
        Creature observer = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        observer.x = 11;
        observer.y = 10;
        observer.z = 0;
        RecordingAi observerAi = new RecordingAi(observer);
        world.add(observer);

        creature.doAction("attack the goblin");

        assertTrue(ai.messages.stream().anyMatch(message -> message.contains("You")));
        assertTrue(observerAi.messages.stream().anyMatch(message -> message.contains("attacks")));
    }

    @Test
    void itemNamesAreLearnedAndReturned() {
        Item potion = new Item('!', Color.RED, "health potion", "red potion");

        assertEquals("red potion", creature.nameOf(potion));
        creature.learnName(potion);
        assertEquals("health potion", creature.nameOf(potion));
    }

    @Test
    void throwingAtTargetDamagesItAndAppliesEffect() {
        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        defender.x = 11;
        defender.y = 10;
        defender.z = 0;
        new RecordingAi(defender);
        world.add(defender);

        Item potion = new Item('!', Color.RED, "potion", null);
        potion.setQuaffEffect(new CountingEffect(2));

        creature.throwItem(potion, 11, 10, 0);

        assertTrue(defender.hp() < 100);
        assertFalse(defender.effects().isEmpty());
        assertFalse(creature.inventory().contains(potion));
        assertNull(world.item(11, 10, 0));
    }

    @Test
    void throwingIntoWallStopsAtWallAndLeavesItemAtFinalGroundTile() {
        world = new World(createTilesWithWall());
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        ai = new RecordingAi(creature);
        creature.x = 10;
        creature.y = 10;
        creature.z = 0;
        world.add(creature);

        Item item = new Item(')', Color.WHITE, "dagger", null);
        creature.throwItem(item, 15, 10, 0);

        assertNull(world.item(15, 10, 0));
        assertSame(item, world.item(10, 10, 0));
    }

    @Test
    void castingAffordableSpellAtTargetAddsCopyAndCostsMana() {
        Creature target = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        target.x = 11;
        target.y = 10;
        target.z = 0;
        new RecordingAi(target);
        world.add(target);

        Spell spell = new Spell("Heal", 3, new Effect(2));
        creature.castSpell(spell, 11, 10);

        assertEquals(2, creature.mana());
        assertFalse(target.effects().isEmpty());
        assertEquals(2, target.effects().get(0).duration);
    }

    @Test
    void castingUnaffordableSpellOrAtEmptyTileDoesNothing() {
        Spell expensive = new Spell("Expensive", 6, new Effect(1));
        creature.castSpell(expensive, 11, 10);
        assertEquals(5, creature.mana());

        Spell cheap = new Spell("Heal", 2, new Effect(1));
        creature.castSpell(cheap, 12, 10);
        assertEquals(5, creature.mana());
    }

    @Test
    void summonAddsCreatureToWorld() {
        Creature summoned = new Creature(world, 'b', Color.BLUE, "bat", 20, 4, 1);
        new RecordingAi(summoned);

        creature.summon(summoned);

        assertSame(summoned, world.creature(summoned.x, summoned.y, summoned.z));
    }

    @Test
    void detectCreatureModifierCanBeAddedAndRemoved() {
        creature.modifyDetectCreatures(2);
        creature.modifyDetectCreatures(-1);

        Creature hidden = new Creature(world, 'g', Color.GREEN, "goblin", 100, 5, 5);
        hidden.x = 15;
        hidden.y = 15;
        hidden.z = 0;
        new RecordingAi(hidden);
        ai.visibleDistance = 0;
        world.add(hidden);

        assertTrue(creature.canSee(hidden.x, hidden.y, hidden.z));
    }

    private Tile[][][] createTilesWithWall() {
        Tile[][][] tiles = new Tile[21][21][2];
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                tiles[x][y][0] = Tile.FLOOR;
                tiles[x][y][1] = Tile.FLOOR;
            }
        }
        tiles[11][10][0] = Tile.WALL;
        for (int y = 8; y < 13; y++)
            tiles[12][y][0] = Tile.WALL;
        return tiles;
    }

    private static class RecordingAi extends CreatureAi {
        private final List<String> messages = new ArrayList<>();
        private int updates;
        private int levelUps;
        private int visibleDistance = 21;

        RecordingAi(Creature creature) {
            super(creature);
        }

        @Override
        public void onEnter(int x, int y, int z, Tile tile) {
            if (tile.isGround()) {
                creature.x = x;
                creature.y = y;
                creature.z = z;
            } else {
                creature.doAction("bump into a wall");
            }
        }

        @Override
        public void onNotify(String message) {
            messages.add(message);
        }

        @Override
        public void onUpdate() {
            updates++;
        }

        @Override
        public void onGainLevel() {
            levelUps++;
        }

        @Override
        public boolean canSee(int wx, int wy, int wz) {
            int dx = creature.x - wx;
            int dy = creature.y - wy;
            return creature.z == wz && dx * dx + dy * dy <= visibleDistance;
        }

        @Override
        public Tile rememberedTile(int wx, int wy, int wz) {
            return Tile.UNKNOWN;
        }
    }

    private static class CountingEffect extends Effect {
        private int startCount;
        private int updateCount;
        private int endCount;

        CountingEffect(int duration) {
            super(duration);
        }

        @Override
        public void start(Creature creature) {
            startCount++;
        }

        @Override
        public void update(Creature creature) {
            super.update(creature);
            updateCount++;
        }

        @Override
        public void end(Creature creature) {
            endCount++;
        }
    }
}
