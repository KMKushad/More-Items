package me.kmkushad.spigotplugin;

import org.bukkit.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE;
import static org.bukkit.enchantments.Enchantment.DAMAGE_ALL;
import static org.bukkit.enchantments.Enchantment.IMPALING;
import static org.bukkit.inventory.ItemFlag.*;


public final class MoreItems extends JavaPlugin implements Listener {

    Material temp_material = Material.CAVE_AIR;
    Location temp_location = new Location(Bukkit.getServer().getWorld("world"), 0, -64, 0);

    HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    @Override
    public void onEnable () {
        // Plugin startup logic
        System.out.println("It is alive");

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage("More Items 0.1.4 has been loaded. Welcome! " + player.getDisplayName());
    }
    private boolean isSafeLocation(Location location) {
        Block feetBlock = location.getBlock();
        Block headBlock = feetBlock.getRelative(BlockFace.UP);
        return feetBlock.getType().isAir() && headBlock.getType().isAir();
    }
    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent e) throws InterruptedException {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if (temp_material != Material.CAVE_AIR) {
            e.setCancelled(true);
            return;
        }
        //NUKE CODE :))))
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.getType() == Material.COMPARATOR && item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Portable Nuclear Device")) {
                e.setCancelled(true);
                World w = player.getWorld();

                Date date = new Date();
                long curr_time = date.getTime();

                if(!cooldowns.containsKey("nuke")) {
                    cooldowns.put("nuke", 0L);
                }

                if(curr_time - cooldowns.get("nuke") < 1000) {
                    return;
                }

                cooldowns.put("nuke", curr_time);

                Block target = getTargetBlock(player, 70);


                if (target.getBlockData().getMaterial() == Material.AIR) {
                    player.sendMessage("Not in range!");
                    return;

                }
                if (temp_material == Material.BAMBOO) {
                    player.sendMessage("Targeted block is a red flag (Bamboo)");
                    return;
                }

                temp_location = target.getLocation();
                temp_material = target.getType();

                target.setBlockData(Material.GOLD_BLOCK.createBlockData());

                w.spawnFallingBlock(temp_location.add(1, 21, 0), Material.IRON_BLOCK.createBlockData()).setDropItem(false);
                w.spawnFallingBlock(temp_location.add(-2, 0, 0), Material.IRON_BLOCK.createBlockData()).setDropItem(false);
                w.spawnFallingBlock(temp_location.add(1, 0, -1), Material.IRON_BLOCK.createBlockData()).setDropItem(false);
                w.spawnFallingBlock(temp_location.add(0, 0, 2), Material.IRON_BLOCK.createBlockData()).setDropItem(false);
                w.spawnFallingBlock(temp_location.add(0, -0.5, -1), Material.GOLD_BLOCK.createBlockData()).setDropItem(false);

                temp_location.add(0, -20.5, 0);
                /* class nukecd implements CommandExecutor {


                    private final HashMap<UUID, Long> cooldown;

                    public nukecd() {
                        this.cooldown = new HashMap<>();
                    }


                    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


                        if (!(sender instanceof Player)) {
                            return false;
                        }

                        Player player = (Player) sender;


                        if (!cooldown.containsKey(player.getUniqueId()) || System.currentTimeMillis() - cooldown.get(player.getUniqueId()) > 10000) {

                            cooldown.put(player.getUniqueId(), System.currentTimeMillis());

                            player.sendMessage("bombing civilians");

                        }else{

                            player.sendMessage("You can't commit war crimes again for another " + (10000 - (System.currentTimeMillis() - cooldown.get(player.getUniqueId()))) + " milliseconds!");
                        }

                        return true;
                    }


                }
                 */
            }
        }
        //SOUL WHIP CODE!!
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.getType() == Material.FISHING_ROD && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Soul Whip")) {
                e.setCancelled(true);
                World w = player.getWorld();

                Date date = new Date();
                long curr_time = date.getTime();

                if(!cooldowns.containsKey("soul_whip")) {
                    cooldowns.put("soul_whip", 0L);
                }

                if(curr_time - cooldowns.get("soul_whip") < 500) {
                    return;
                }

                cooldowns.put("soul_whip", curr_time);

                /*
                double yaw_angle = ((player.getEyeLocation().getYaw() * -1) + 450) % 360;
                player.sendMessage("\n\n\n");
                player.sendMessage("Angle: " + yaw_angle);
                player.sendMessage("Sin: " + String.valueOf(Math.sin(yaw_angle) * 10));
                player.sendMessage("Cos" + String.valueOf(Math.cos(yaw_angle) * 10));

                //double pitch = player.getEyeLocation().getPitch();

                player.spawnParticle(Particle.SMOKE_NORMAL, player.getEyeLocation().add(1, 0, 0), 5);
                 */

                Location location = player.getLocation();
                Vector direction = player.getLocation().getDirection().normalize();

                Location temp_location = location.clone().add(0, 0.6, 0).add(direction);

                for(float i = 0; i < 20; i++) {
                    w.spawnParticle(Particle.SMOKE_LARGE, temp_location.add(direction).add(0,  (1 - i / 8) / 4.5, 0), 0);
                    Collection<Entity> entities = w.getNearbyEntities(temp_location, 0.5,0.5, 0.5);

                    for(Entity entity : entities) {
                        if(entity instanceof LivingEntity) {
                            ((LivingEntity) entity).damage(6);
                        }
                    }
                }
            }
        }
        //hype code
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.getItemMeta() != null) {
                item.getItemMeta().getDisplayName();
                if (item.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Hyperion")) {
                    Date date = new Date();
                    long curr_time = date.getTime();

                    if(!cooldowns.containsKey("hyperion")) {
                        cooldowns.put("hyperion", 0L);
                    }

                    if(curr_time - cooldowns.get("hyperion") < 1000) {
                        return;
                    }

                    cooldowns.put("hyperion", curr_time);

                    // funny wither impact
                    Location playerLocation = player.getLocation();
                    Block target = getTargetBlock(player, 30);

                    World w = player.getWorld();

                    List<Entity> nearbyEntities = player.getNearbyEntities(6, 6, 6);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player) {
                            Player nearbyPlayer = (Player) entity;
                            if (nearbyPlayer != player) {
                                nearbyPlayer.damage(6.0);
                            }
                        }
                    }

                    //teleportation
                    Location targetLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(10));
                    if (isSafeLocation(targetLocation)) {
                        player.teleport(targetLocation);
                    } else if (playerLocation.distance(target.getLocation()) <= 10 && !isSafeLocation(target.getLocation())) {
                        player.teleport(target.getLocation().add(0, 1, 0));
                    } else {
                        player.sendMessage("Cannot teleport to that location. It is not safe.");
                        return;
                    }

                    w.spawnParticle(Particle.FLAME, targetLocation, 30, 0.5, 0.5, 0.5);
                    w.spawnParticle(Particle.SMOKE_LARGE, targetLocation, 30, 0.5, 0.5, 0.5);

                    //boom boom
                    player.getWorld().createExplosion(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), 6.0f, false, false);

                    //yellow hearts
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1000, 4));

                    // Cancel damage event
                    e.setCancelled(true);
                    player.setNoDamageTicks(100); // Set a delay to prevent further damage
                    player.playSound(player.getLocation(), Sound.BLOCK_SCAFFOLDING_BREAK, 200, 1);
                }
            }
        }
    }


    @EventHandler
    public void onEDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 150, 2);
        }
    }

    @EventHandler
    //nuke handler
    public void onFall (EntityChangeBlockEvent e){
        if (e.getEntity() instanceof FallingBlock) {
            if (e.getBlockData().getMaterial() == Material.GOLD_BLOCK) {
                Location location = e.getEntity().getLocation();
                System.out.println(location);

                World world = e.getEntity().getWorld();

                world.createExplosion(location, 12, false, true);
                Collection<Entity> entities = world.getNearbyEntities(location, 10, 10, 10);

                for (Entity ent : entities) {
                    if (ent.getType() == EntityType.FALLING_BLOCK) {
                        ent.remove();
                    }
                }

                world.setBlockData(temp_location, Material.AIR.createBlockData());
                world.setBlockData(temp_location.add(0, 1, 0), Material.AIR.createBlockData());

                temp_material = Material.CAVE_AIR;
                temp_location = new Location(Bukkit.getServer().getWorld("world"), 0, -64, 0);

                e.setCancelled(true);
            }
        }
    }


    //funny target block
    public final Block getTargetBlock (Player player,int range){
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }


    @Override
    //this is where all of our items are "/create"d
    public boolean onCommand (CommandSender sender, Command command, String label, String[]args){
        if (command.getName().equalsIgnoreCase("create")) {
            //checking if the sender is a player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length != 1) {
                    return false;
                }
                //creating a modifier
                AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"GENERIC.ATTACKDAMAGE", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);


                player.sendMessage(args[0]);
                //tester weapon :)
                if (Objects.equals(args[0], "nuke")) {
                    player.sendMessage("nuke incoming");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.COMPARATOR, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.RED + "Portable Nuclear Device");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GRAY + "§oDon't let NATO know...");
                    lore.add(" ");
                    lore.add(ChatColor.RED + "§lSPECIAL ITEM");
                    im.setLore(lore);
                    is.setItemMeta(im);
                    i.addItem(is);
                    im.addItemFlags(HIDE_ENCHANTS, HIDE_ATTRIBUTES, HIDE_UNBREAKABLE);
                    im.addAttributeModifier(GENERIC_ATTACK_DAMAGE, modifier);

                    return true;
                }
                //funny guardian blade
                if (Objects.equals(args[0], "prismarine_blade")) {
                    player.sendMessage("time to kill the big ugly feesh");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.PRISMARINE_SHARD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GREEN + "Prismarine Blade");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GRAY + "Deals " + ChatColor.GREEN + "+12.5" + ChatColor.GRAY + " damage to Guardians");
                    lore.add(" ");
                    lore.add(ChatColor.GREEN + "§lUNCOMMON SWORD");
                    im.setLore(lore);
                    im.addEnchant(IMPALING, 5, true);
                    im.addEnchant(DAMAGE_ALL, 5, true);
                    im.addItemFlags(HIDE_ENCHANTS, HIDE_ATTRIBUTES, HIDE_UNBREAKABLE);
                    im.addAttributeModifier(GENERIC_ATTACK_DAMAGE, modifier);
                    is.setItemMeta(im);
                    i.addItem(is);


                    return true;
                }
                //"get back to the cotton field"
                if (Objects.equals(args[0], "soul_whip")) {
                    player.sendMessage("it's whipping time bois");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.FISHING_ROD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GOLD + "Soul Whip");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GOLD + "Ability: Flay " + ChatColor.YELLOW + "§lRIGHT CLICK");
                    lore.add(ChatColor.GRAY + "Flay your whip in an arc,");
                    lore.add(ChatColor.GRAY + "dealing your melee damage to all");
                    lore.add(ChatColor.GRAY + "enemies in it's path.");
                    lore.add(ChatColor.GRAY + " ");
                    lore.add(ChatColor.GOLD + "§lLEGENDARY SWORD");
                    im.setLore(lore);
                    im.addAttributeModifier(GENERIC_ATTACK_DAMAGE, modifier);
                    im.addItemFlags(HIDE_ENCHANTS, HIDE_ATTRIBUTES, HIDE_UNBREAKABLE);


                    is.setItemMeta(im);
                    i.addItem(is);

                    return true;
                }
                //funny aotj
                if (Objects.equals(args[0], "jerry")) {
                    player.sendMessage("no parley? real.");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.WOODEN_SWORD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GRAY + "Aspect of the Jerry");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GOLD + "Ability: Parley" + ChatColor.YELLOW + "RIGHT CLICK");
                    lore.add(ChatColor.GRAY + "Flay your whip in an arc,");
                    lore.add(ChatColor.GRAY + "dealing your melee damage to all");
                    lore.add(ChatColor.GRAY + "enemies in it's path.");
                    lore.add(ChatColor.GRAY + " ");
                    lore.add(ChatColor.GOLD + "§lLEGENDARY SWORD");
                    im.setLore(lore);
                    im.addAttributeModifier(GENERIC_ATTACK_DAMAGE, modifier);
                    im.addItemFlags(HIDE_ENCHANTS, HIDE_ATTRIBUTES, HIDE_UNBREAKABLE);


                    is.setItemMeta(im);
                    i.addItem(is);

                    return true;

                }
                //hype code
                if (Objects.equals(args[0], "hyperion")) {
                    player.sendMessage("uh oh 2b weapon!");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.IRON_SWORD, 1);
                    ItemMeta im = is.getItemMeta();
                    assert im != null;
                    im.setDisplayName(ChatColor.LIGHT_PURPLE + "Hyperion");
                    ArrayList<String> lore = new ArrayList<String>();

                    lore.add(ChatColor.GOLD + "Wither Impact" + ChatColor.YELLOW + "§lRIGHT CLICK");

                    lore.add(ChatColor.GRAY + "Teleport " + ChatColor.GREEN + "10 blocks " +ChatColor.GRAY + "ahead of");
                    lore.add(ChatColor.GRAY + "You. Then implode dealing");
                    lore.add(ChatColor.RED + "8♥" + ChatColor.GRAY + " of damage to nearby");
                    lore.add(ChatColor.GRAY + "enemies. This also grants you");
                    lore.add(ChatColor.YELLOW + "5♥" + ChatColor.GRAY+ " absorption hearts for");
                    lore.add(ChatColor.GRAY + "10 seconds.");
                    lore.add("");
                    lore.add(ChatColor.LIGHT_PURPLE + "§lMYTHIC SWORD");
                    im.setLore(lore);
                    im.addItemFlags(HIDE_ENCHANTS, HIDE_ATTRIBUTES, HIDE_UNBREAKABLE);


                    is.setItemMeta(im);
                    i.addItem(is);

                    return true;
                }
            }
            return false;
        }
        return false;
    }
}