package me.maanraj514.contrapvp.setupwizard.gui;

import games.negative.framework.gui.GUI;
import me.maanraj514.contrapvp.setupwizard.Session;
import me.maanraj514.contrapvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WizardGui extends GUI {
    public WizardGui(Session session) {
        super("&b&lChoose an option", 3);

        ItemStack addSpawn = new ItemBuilder(Material.ENDER_PEARL).setDisplayName("&eAdd Spawn Point").build();
        ItemStack setSpectator = new ItemBuilder(Material.ENDER_EYE).setDisplayName("&eSpectator spawn").build();

        setItem(11, player -> addSpawn);
        setItem(15, player -> setSpectator);

        setItemClickEvent(11, player -> addSpawn, (player, event) -> {
            session.addSpawnPoint();
        });
        setItemClickEvent(15, player -> setSpectator, (player, event) -> {
            session.setSpectatorSpawn();
        });
    }
}
