package me.kmkushad.spigotplugin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.bukkit.enchantments.Enchantment.IMPALING;


public final class MoreItems extends JavaPlugin implements Listener {

    Material temp_material = Material.CAVE_AIR;
    Location temp_location = new Location(Bukkit.getServer().getWorld("world"), 0, -64, 0);

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

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent e) throws InterruptedException {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if (temp_material != Material.CAVE_AIR) {
            return;
        }

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.getType() == Material.FISHING_ROD && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Soul Whip")) {
                World w = player.getWorld();

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

                Location temp_location = location.clone().add(0, 1, 0);

                for(int i = 0; i < 20; i++) {
                    w.spawnParticle(Particle.SMOKE_LARGE, temp_location.add(direction).subtract(0,  0, 0), 0);
                    Collection<Entity> entities = w.getNearbyEntities(temp_location, 0.5,0.5, 0.5);

                    for(Entity entity : entities) {
                        if(entity instanceof LivingEntity) {
                            ((LivingEntity) entity).damage(200);
                        }
                    }
                }

                e.setCancelled(true);
            }
        }
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (item.getType() == Material.COMPARATOR && item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Portable Nuclear Device")) {
                World w = player.getWorld();
                Block target = getTargetBlock(player, 30);


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
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onFall (EntityChangeBlockEvent e){
        if (e.getEntity() instanceof FallingBlock) {
            if (e.getBlockData().getMaterial() == Material.GOLD_BLOCK) {
                Location location = e.getEntity().getLocation();
                System.out.println(location);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill <x1> <y1> <z1> <x2> <y2> <z2> <block> [dataValue] replace <newTileName> [newDataValue]");
                World world = e.getEntity().getWorld();

                world.createExplosion(location, 6, false, false);
                Collection<Entity> entities = world.getNearbyEntities(location, 10, 10, 10);

                for (Entity ent : entities) {
                    if (ent.getType() == EntityType.FALLING_BLOCK) {
                        ent.remove();
                    }
                }

                world.setBlockData(temp_location, temp_material.createBlockData());
                world.setBlockData(temp_location.add(0, 1, 0), Material.AIR.createBlockData());

                temp_material = Material.CAVE_AIR;
                temp_location = new Location(Bukkit.getServer().getWorld("world"), 0, -64, 0);

                System.out.println(location.toString());
            }
        }
    }

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
    public boolean onCommand (CommandSender sender, Command command, String label, String[]args){
        if (command.getName().equalsIgnoreCase("create")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length != 1) {
                    return false;
                }

                player.sendMessage(args[0]);

                if (Objects.equals(args[0], "nuke")) {
                    player.sendMessage("nuke incoming");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.COMPARATOR, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.RED + "Portable Nuclear Device");
                    is.setItemMeta(im);
                    i.addItem(is);

                    return true;
                }

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

                    is.setItemMeta(im);
                    i.addItem(is);


                    return true;
                }
                if (Objects.equals(args[0], "soul_whip")) {
                    player.sendMessage("it's whipping time bois");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.FISHING_ROD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GOLD + "Soul Whip");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GOLD + "Ability: Flay " + ChatColor.YELLOW + "RIGHT CLICK");
                    lore.add(ChatColor.GRAY + "Flay your whip in an arc,");
                    lore.add(ChatColor.GRAY + "dealing your melee damage to all");
                    lore.add(ChatColor.GRAY + "enemies in it's path.");
                    lore.add(ChatColor.GRAY + " ");
                    lore.add(ChatColor.GOLD + "§lLEGENDARY SWORD");
                    im.setLore(lore);
                    im.addEnchant(IMPALING, 5, false);

                    is.setItemMeta(im);
                    i.addItem(is);

                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }


}