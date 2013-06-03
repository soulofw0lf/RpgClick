package com.vartala.soulofw0lf.rpgclick;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;




public class RpgClick extends JavaPlugin implements Listener{
	public String bob = "bob";
	RpgClick plugin;
	@Override
	public void onEnable(){
		plugin = this;
		getLogger().info("Rpg Click has been enabled!");
		getCommand("click").setExecutor(new clickHandler(this));
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}
	@Override
	public void onDisable(){
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClick(InventoryClickEvent event){
		Player player = (Player)event.getWhoClicked();
		if (!(event.getInventory().getTitle().equalsIgnoreCase("Click:"))){
			return;
		}
		ItemStack item = event.getCurrentItem();
		event.setCancelled(true);
		if (item != null){
			ItemMeta im = item.getItemMeta();
			String mname = im.getDisplayName();
			for(String index : getConfig().getConfigurationSection("Commands").getKeys(false)){
				String iname = getConfig().getString("Commands." + index + ".Name");
				if (iname.equalsIgnoreCase(mname)){
					player.closeInventory();
					waitCommand(player, index);
				}
			}
		}

	}
	public void waitCommand(final Player player, final String index){
		
		
		
		new BukkitRunnable(){
			
		
		
	
		
		@Override
		public void run() {
			if (getConfig().getBoolean("Commands." + index + ".Console") == true){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),(getConfig().getString("Commands." + index + ".Command").replaceAll("@p", player.getName()).replaceAll("@t", bob)));
			} else{
				player.performCommand(getConfig().getString("Commands." + index + ".Command").replaceAll("@p", player.getName()).replaceAll("@t", bob));
			}
		}	
		}.runTaskLater(plugin, 5);
	}
	
	
	
	@EventHandler
	public void onClick(PlayerInteractEntityEvent event){
		if (!(event.getRightClicked() instanceof Player)){
			return;
		}
		Player p = (Player) event.getRightClicked();
		Player player = event.getPlayer();
		Boolean npc = true;
		for (Player Online : Bukkit.getOnlinePlayers())
		{
			if (p.getName().equalsIgnoreCase(Online.getName())){
			npc = false;
			}
		}
		if (npc == true){
			return;
		}
		if (!(player.isSneaking())){
			return;
		}
		if (!(player.hasPermission(getConfig().getString("PlayerClick.Permission")))){
			return;
		}	
		clickCommand(player, p);
		event.setCancelled(true);
		return;
	}
	public void clickCommand(Player player, Player p){
		Integer slots = getConfig().getInt("PlayerClick.Rows") * 9;
		bob = p.getName();
		if (slots >= 45){
			slots = 45;
		}
		Inventory inv = Bukkit.createInventory(null, slots, "Click:");
		Integer i = 0;
		for(String key : getConfig().getConfigurationSection("PlayerClick.Items").getKeys(false)){
				ItemStack itmStack = new ItemStack(getConfig().getInt("PlayerClick.Items." + key + ".Item Type"));
				ItemMeta im = itmStack.getItemMeta();
				im.setDisplayName(getConfig().getString("PlayerClick.Items." + key + ".Name"));
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(getConfig().getString("PlayerClick.Items." + key + ".Description").replaceAll("@t", p.getName()).replaceAll("@p", player.getName()).replaceAll("&", "§"));
				im.setLore(lore);
				itmStack.setItemMeta(im);
				String dur = getConfig().getString("PlayerClick.Items." + key + ".Durability");
				Short dura = Short.parseShort(dur);
				itmStack.setDurability(dura);
				inv.setItem(i, itmStack);
				i++;
		}
		player.openInventory(inv);
	}


}
