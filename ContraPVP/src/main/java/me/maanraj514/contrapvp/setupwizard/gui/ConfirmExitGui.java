package me.maanraj514.contrapvp.setupwizard.gui;

import games.negative.framework.gui.GUI;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfirmExitGui extends GUI {
    public ConfirmExitGui(ContraPVP plugin) {
        super("&a&lDo you want to save?", 1);

        ItemStack yes = new ItemBuilder(Material.GREEN_WOOL).setDisplayName("&aSave Changes").addLoreLine("&7Changes will be saved to the database.").build();
        ItemStack no = new ItemBuilder(Material.RED_WOOL).setDisplayName("&cDont Save Changes").addLoreLine("&7Nothing will be saved. Changes are lost forever!").build();

        setItem(11, player -> yes);
        setItem(15, player -> no);

        setItemClickEvent(11, player -> yes, (player, event) -> {
            plugin.getWizardManager().endSession(player, true);
            player.closeInventory();
        });
        setItemClickEvent(15, player -> no, (player, event) -> {
            plugin.getWizardManager().endSession(player, false);
            player.closeInventory();
        });
    }
}
