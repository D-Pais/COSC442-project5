package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Effect;
import game.Spell;

class CastSpellScreenTest {
    @Test
    void appliesSpellToSelectedCreatureAndSpendsMana() {
        game.World world = UiTestSupport.floorWorld(7, 5);
        Creature player = UiTestSupport.creature(world, 1, 2, '@', "player");
        Creature target = UiTestSupport.creature(world, 4, 2, 'g', "goblin");
        player.modifyMana(-3);
        Spell spell = new Spell("zap", 2, new Effect(2)) {
            @Override
            public Effect effect() {
                return new Effect(2) {
                    @Override
                    public void start(Creature creature) {
                        creature.modifyHp(-7, "zapped");
                    }
                };
            }
        };
        CastSpellScreen screen = new CastSpellScreen(player, "Cast", 1, 2, spell);

        screen.selectWorldCoordinate(4, 2, 4, 2);

        assertEquals(93, target.hp());
        assertEquals(0, player.mana());
    }
}
