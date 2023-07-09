package me.kmkushad.spigotplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
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

import java.util.Collection;
import java.util.Objects;


public final class MoreItems extends JavaPlugin implements Listener {

    Material temp_material;
    Location temp_location;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("It is alive");

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage("Kill yourself " + player.getDisplayName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && item.getType() == Material.IRON_SWORD && item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Portable Nuclear Device")) {
            World w = player.getWorld();
            Block target = getTargetBlock(player, 30);

            if(target.getBlockData().getMaterial() == Material.AIR) {
                player.sendMessage("Not in range!");
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
        }
    }

    @EventHandler
    public void onFall(EntityChangeBlockEvent e) {
        if(e.getEntity() instanceof FallingBlock) {
            if(e.getBlockData().getMaterial() == Material.GOLD_BLOCK) {
                Location location = e.getEntity().getLocation();
                World world = e.getEntity().getWorld();

                world.createExplosion(location, 6, false, false);
                Collection<Entity> entities = world.getNearbyEntities(location, 10, 10, 10);

                for(Entity ent : entities) {
                    if(ent.getType() == EntityType.FALLING_BLOCK) {
                        ent.remove();
                    }
                }

                world.setBlockData(temp_location, temp_material.createBlockData());
                world.setBlockData(temp_location.add(0,1,0), Material.AIR.createBlockData());

                System.out.println(location.toString());
            }
        }
    }

    public final Block getTargetBlock(Player player, int range) {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("create")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length != 1) {
                    return false;
                }

                player.sendMessage(args[0]);

                if(Objects.equals(args[0], "nuke")) {
                    player.sendMessage("nuke incoming");
                    Inventory i = player.getInventory();
                    ItemStack is = new ItemStack(Material.IRON_SWORD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.RED + "Portable Nuclear Device");
                    is.setItemMeta(im);
                    i.addItem(is);
                }
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("It is dead");
    }
}
