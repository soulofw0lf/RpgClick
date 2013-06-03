package com.vartala.soulofw0lf.rpgclick;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class clickHandler implements CommandExecutor {

	RpgClick Rpgc;
	public clickHandler(RpgClick rpgc){
		this.Rpgc = rpgc;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)){
			return true;
		}
		if (args[0].equalsIgnoreCase("name")){
			player.sendMessage(this.Rpgc.getConfig().getString("PlayerClick.Items.1.Name"));
			return true;
		}
		if (args.length != 3){
			player.sendMessage("improper usage, please use /click open clickname targetname");
			return true;
		}
		if (args[0].equalsIgnoreCase("open")){
			if (!(this.Rpgc.getConfig().contains(args[1]))){
				player.sendMessage("This click set does not exist!");
				return true;
			}
			String perm = this.Rpgc.getConfig().getString(args[1] + ".Permission");
			if (!(player.hasPermission(perm))){
				player.sendMessage("You don't have permission to open this Click set");
				return true;
			}
			if (Bukkit.getPlayer(args[2]) == null){
				player.sendMessage("This player could not be found!");
				return true;
			}
			Integer slots = this.Rpgc.getConfig().getInt(args[1] + ".Rows") * 9;
			this.Rpgc.bob = args[2];
			if (slots >= 45){
				slots = 45;
			}
			Player p = Bukkit.getPlayer(args[2]);
			Inventory inv = Bukkit.createInventory(null, slots, "Click:");
			Integer i = 0;
			for(String key : this.Rpgc.getConfig().getConfigurationSection(args[1] + ".Items").getKeys(false)){
				ItemStack itmStack = new ItemStack(this.Rpgc.getConfig().getInt(args[1]+ ".Items." + key + ".Item Type"));
				ItemMeta im = itmStack.getItemMeta();
				im.setDisplayName(this.Rpgc.getConfig().getString(args[1] + ".Items." + key + ".Name"));
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(this.Rpgc.getConfig().getString(args[1]+ ".Items." + key + ".Description").replaceAll("@t", p.getName()).replaceAll("@p", player.getName()).replaceAll("&", "§"));
				im.setLore(lore);
				itmStack.setItemMeta(im);
				String dur = this.Rpgc.getConfig().getString(args[1]+ ".Items." + key + ".Durability");
				Short dura = Short.parseShort(dur);
				itmStack.setDurability(dura);
				inv.setItem(i, itmStack);
				i++;
			}
			player.openInventory(inv);
			return true;
		}



		return false;
	}

}
