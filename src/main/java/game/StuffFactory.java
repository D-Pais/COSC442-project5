package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import asciiPanel.AsciiPanel;

public class StuffFactory {
	private World world;
	private Map<String, Color> potionColors;
	private List<String> potionAppearances;
	
	public StuffFactory(World world){
		this.world = world;
		
		setUpPotionAppearances();
	}
	
	private void setUpPotionAppearances(){
		potionColors = new HashMap<String, Color>();
		potionColors.put("red potion", AsciiPanel.brightRed);
		potionColors.put("yellow potion", AsciiPanel.brightYellow);
		potionColors.put("green potion", AsciiPanel.brightGreen);
		potionColors.put("cyan potion", AsciiPanel.brightCyan);
		potionColors.put("blue potion", AsciiPanel.brightBlue);
		potionColors.put("magenta potion", AsciiPanel.brightMagenta);
		potionColors.put("dark potion", AsciiPanel.brightBlack);
		potionColors.put("grey potion", AsciiPanel.white);
		potionColors.put("light potion", AsciiPanel.brightWhite);

		potionAppearances = new ArrayList<String>(potionColors.keySet());
		Collections.shuffle(potionAppearances, world.random());
	}

	private String potionDescription(String appearance){
		switch (appearance){
		case "red potion": return "Scarlet liquid catches the light like a coal beneath ash.";
		case "yellow potion": return "Golden liquid clings to the glass in slow, honeyed ribbons.";
		case "green potion": return "A green haze curls through the bottle, then settles at its base.";
		case "cyan potion": return "Pale blue bubbles rise silently and vanish at the surface.";
		case "blue potion": return "A deep blue sheen ripples across the vial when turned.";
		case "magenta potion": return "A violet glimmer drifts through the liquid in lazy spirals.";
		case "dark potion": return "The nearly black liquid swallows the light around its edges.";
		case "grey potion": return "A silvery film shivers over the surface like moonlit water.";
		case "light potion": return "The clear liquid scatters pinpricks of light across the glass.";
		default: throw new IllegalArgumentException("Unknown potion appearance: " + appearance);
		}
	}
	
	public Creature newPlayer(List<String> messages, FieldOfView fov){
		Creature player = new Creature(world, '@', AsciiPanel.brightWhite, "player", 100, 20, 5);
		world.addAtEmptyLocation(player, 0);
		new PlayerAi(player, messages, fov);
		return player;
	}
	
	public Creature newFungus(int depth){
		Creature fungus = new Creature(world, 'f', AsciiPanel.green, "fungus", 10, 0, 0);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	public Creature newBat(int depth){
		Creature bat = new Creature(world, 'b', AsciiPanel.brightYellow, "bat", 15, 5, 0);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	public Creature newZombie(int depth, Creature player){
		Creature zombie = new Creature(world, 'z', AsciiPanel.white, "zombie", 50, 10, 10);
		world.addAtEmptyLocation(zombie, depth);
		new ZombieAi(zombie, player);
		return zombie;
	}

	public Creature newGoblin(int depth, Creature player){
		Creature goblin = new Creature(world, 'g', AsciiPanel.brightGreen, "goblin", 66, 15, 5);
		new GoblinAi(goblin, player);
		goblin.equip(randomWeapon(depth));
		goblin.equip(randomArmor(depth));
		world.addAtEmptyLocation(goblin, depth);
		return goblin;
	}
	
	public Item newRock(int depth){
		Item rock = new Item(',', AsciiPanel.yellow, "rock", null,
				"A palm-sized stone, worn smooth by water that has long since vanished.");
		rock.modifyThrownAttackValue(5);
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	public Item newVictoryItem(int depth){
		Item item = new Item('*', AsciiPanel.brightWhite, "teddy bear", null,
				"One button eye is missing, but its stitched smile refuses to fade.");
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBread(int depth){
		Item item = new Item('%', AsciiPanel.yellow, "bread", null,
				"A thick-crusted loaf, still carrying the warm scent of a village oven.");
		item.modifyFoodValue(400);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newFruit(int depth){
		Item item = new Item('%', AsciiPanel.brightRed, "apple", null,
				"Its red skin is polished by handling, with one small leaf at the stem.");
		item.modifyFoodValue(100);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newDagger(int depth){
		Item item = new Item(')', AsciiPanel.white, "dagger", null,
				"A narrow blade bears tiny notches from a dozen close escapes.");
		item.modifyAttackValue(5);
		item.modifyThrownAttackValue(5);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newSword(int depth){
		Item item = new Item(')', AsciiPanel.brightWhite, "sword", null,
				"The plain blade is well balanced and bright along its honed edge.");
		item.modifyAttackValue(10);
		item.modifyThrownAttackValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newStaff(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "staff", null,
				"Dark wood twists around a pale knot that resembles a watching eye.");
		item.modifyAttackValue(5);
		item.modifyDefenseValue(3);
		item.modifyThrownAttackValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newBow(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "bow", null,
				"Its yew limbs flex with a quiet creak; the grip is worn smooth.");
		item.modifyAttackValue(1);
		item.modifyRangedAttackValue(5);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newEdibleWeapon(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "baguette", null,
				"A crusty loaf with a sturdy shape and an unmistakable scent of butter.");
		item.modifyAttackValue(3);
		item.modifyFoodValue(100);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newLightArmor(int depth){
		Item item = new Item('[', AsciiPanel.green, "tunic", null,
				"A practical tunic, patched at the elbows and faded at the shoulders.");
		item.modifyDefenseValue(2);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newMediumArmor(int depth){
		Item item = new Item('[', AsciiPanel.white, "chainmail", null,
				"Thousands of iron rings whisper together with every movement.");
		item.modifyDefenseValue(4);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newHeavyArmor(int depth){
		Item item = new Item('[', AsciiPanel.brightWhite, "platemail", null,
				"Heavy plates bear the dents of battles long forgotten.");
		item.modifyDefenseValue(6);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item randomWeapon(int depth){
		switch (world.random().nextInt(3)){
		case 0: return newDagger(depth);
		case 1: return newSword(depth);
		case 2: return newBow(depth);
		default: return newStaff(depth);
		}
	}

	public Item randomArmor(int depth){
		switch (world.random().nextInt(3)){
		case 0: return newLightArmor(depth);
		case 1: return newMediumArmor(depth);
		default: return newHeavyArmor(depth);
		}
	}
	
	public Item newPotionOfHealth(int depth){
		String appearance = potionAppearances.get(0);
		final Item item = new Item('!', potionColors.get(appearance), "health potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(15, "Killed by a health potion?");
				creature.doAction(item, "look healthier");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfMana(int depth){
		String appearance = potionAppearances.get(1);
		final Item item = new Item('!', potionColors.get(appearance), "mana potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature){
				if (creature.mana() == creature.maxMana())
					return;
				
				creature.modifyMana(10);
				creature.doAction(item, "look restored");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfSlowHealth(int depth){
		String appearance = potionAppearances.get(2);
		final Item item = new Item('!', potionColors.get(appearance), "slow health potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(100){
			public void start(Creature creature){
				creature.doAction(item, "look a little better");
			}
			
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(1, "Killed by a slow health potion?");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfPoison(int depth){
		String appearance = potionAppearances.get(3);
		final Item item = new Item('!', potionColors.get(appearance), "poison potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.doAction(item, "look sick");
			}
			
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(-1, "Died of poison.");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfWarrior(int depth){
		String appearance = potionAppearances.get(4);
		final Item item = new Item('!', potionColors.get(appearance), "warrior's potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.modifyAttackValue(5);
				creature.modifyDefenseValue(5);
				creature.doAction(item, "look stronger");
			}
			public void end(Creature creature){
				creature.modifyAttackValue(-5);
				creature.modifyDefenseValue(-5);
				creature.doAction("look less strong");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfArcher(int depth){
		String appearance = potionAppearances.get(5);
		final Item item = new Item('!', potionColors.get(appearance), "archers potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.modifyVisionRadius(3);
				creature.doAction(item, "look more alert");
			}
			public void end(Creature creature){
				creature.modifyVisionRadius(-3);
				creature.doAction("look less alert");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfExperience(int depth){
		String appearance = potionAppearances.get(6);
		final Item item = new Item('!', potionColors.get(appearance), "experience potion", appearance,
				potionDescription(appearance));
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.doAction(item, "look more experienced");
				creature.modifyXp(creature.level() * 5);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item randomPotion(int depth){
		switch (world.random().nextInt(9)){
		case 0: return newPotionOfHealth(depth);
		case 1: return newPotionOfHealth(depth);
		case 2: return newPotionOfMana(depth);
		case 3: return newPotionOfMana(depth);
		case 4: return newPotionOfSlowHealth(depth);
		case 5: return newPotionOfPoison(depth);
		case 6: return newPotionOfWarrior(depth);
		case 7: return newPotionOfArcher(depth);
		default: return newPotionOfExperience(depth);
		}
	}
	
	public Item newWhiteMagesSpellbook(int depth) {
		Item item = new Item('+', AsciiPanel.brightWhite, "white mage's spellbook", null,
				"Silver-edged pages hold careful diagrams and densely annotated margins.");
		item.addWrittenSpell("minor heal", 4, new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(20, "Killed by a minor heal spell?");
				creature.doAction("look healthier");
			}
		});
		
		item.addWrittenSpell("major heal", 8, new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(50, "Killed by a major heal spell?");
				creature.doAction("look healthier");
			}
		});
		
		item.addWrittenSpell("slow heal", 12, new Effect(50){
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(2, "Killed by a slow heal spell?");
			}
		});

		item.addWrittenSpell("inner strength", 16, new Effect(50){
			public void start(Creature creature){
				creature.modifyAttackValue(2);
				creature.modifyDefenseValue(2);
				creature.modifyVisionRadius(1);
				creature.modifyRegenHpPer1000(10);
				creature.modifyRegenManaPer1000(-10);
				creature.doAction("seem to glow with inner strength");
			}
			public void update(Creature creature){
				super.update(creature);
				if (world.random().nextDouble() < 0.25)
					creature.modifyHp(1, "Killed by inner strength spell?");
			}
			public void end(Creature creature){
				creature.modifyAttackValue(-2);
				creature.modifyDefenseValue(-2);
				creature.modifyVisionRadius(-1);
				creature.modifyRegenHpPer1000(-10);
				creature.modifyRegenManaPer1000(10);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBlueMagesSpellbook(int depth) {
		Item item = new Item('+', AsciiPanel.brightBlue, "blue mage's spellbook", null,
				"The ink shifts through shades of blue; claw-shaped stains mark several pages.");

		item.addWrittenSpell("blood to mana", 1, new Effect(1){
			public void start(Creature creature){
				int amount = Math.min(creature.hp() - 1, creature.maxMana() - creature.mana());
				creature.modifyHp(-amount, "Killed by a blood to mana spell.");
				creature.modifyMana(amount);
			}
		});
		
		item.addWrittenSpell("blink", 6, new Effect(1){
			public void start(Creature creature){
				creature.doAction("fade out");
				
				int mx = 0;
				int my = 0;
				
				do
				{
					mx = world.random().nextInt(11) - 5;
					my = world.random().nextInt(11) - 5;
				}
				while (!creature.canEnter(creature.x+mx, creature.y+my, creature.z)
						&& creature.canSee(creature.x+mx, creature.y+my, creature.z));
				
				creature.moveBy(mx, my, 0);
				
				creature.doAction("fade in");
			}
		});
		
		item.addWrittenSpell("summon bats", 11, new Effect(1){
			public void start(Creature creature){
				for (int ox = -1; ox < 2; ox++){
					for (int oy = -1; oy < 2; oy++){
						int nx = creature.x + ox;
						int ny = creature.y + oy;
						if (ox == 0 && oy == 0 
								|| creature.creature(nx, ny, creature.z) != null)
							continue;
						
						Creature bat = newBat(0);
						
						if (!bat.canEnter(nx, ny, creature.z)){
							world.remove(bat);
							continue;
						}
						
						bat.x = nx;
						bat.y = ny;
						bat.z = creature.z;
						
						creature.summon(bat);
					}
				}
			}
		});
		
		item.addWrittenSpell("detect creatures", 16, new Effect(75){
			public void start(Creature creature){
				creature.doAction("look far off into the distance");
				creature.modifyDetectCreatures(1);
			}
			public void end(Creature creature){
				creature.modifyDetectCreatures(-1);
			}
		});
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	

	public Item randomSpellBook(int depth){
		switch (world.random().nextInt(2)){
		case 0: return newWhiteMagesSpellbook(depth);
		default: return newBlueMagesSpellbook(depth);
		}
	}
	
}
