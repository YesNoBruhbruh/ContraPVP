package me.maanraj514.contrapvp;

import com.grinderwolf.swm.api.SlimePlugin;
import games.negative.framework.BasePlugin;
import lombok.Getter;
import me.maanraj514.contrapvp.commands.JoinCommand;
import me.maanraj514.contrapvp.commands.PartyCommand;
import me.maanraj514.contrapvp.commands.SetLobbyCommand;
import me.maanraj514.contrapvp.commands.SetupCommand;
import me.maanraj514.contrapvp.database.DatabaseConnection;
import me.maanraj514.contrapvp.database.IConnectedCallback;
import me.maanraj514.contrapvp.database.MapDB;
import me.maanraj514.contrapvp.database.TableBuilder;
import me.maanraj514.contrapvp.game.GameManager;
import me.maanraj514.contrapvp.object.GamePlayer;
import me.maanraj514.contrapvp.party.PartyListener;
import me.maanraj514.contrapvp.party.PartyManager;
import me.maanraj514.contrapvp.setupwizard.WizardListener;
import me.maanraj514.contrapvp.setupwizard.WizardManager;
import me.maanraj514.contrapvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class ContraPVP extends BasePlugin {

    @Getter
    private final SlimePlugin slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    @Getter
    private GameManager gameManager;

    @Getter
    private WizardManager wizardManager;

    @Getter
    private PartyManager partyManager;

    @Getter
    private PlayerRollBackManager playerRollBackManager;

    @Getter
    private DatabaseConnection connection;
    @Getter
    private MapDB mapDB;

    private final List<GamePlayer> gamePlayers = new ArrayList<>();

    private BukkitTask lobbySetupTask;

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();

        if (slime == null) {
            for (int i = 0; i < 10; i++) {
                System.out.println(ChatColor.RED + "PLEASE INSTALL SLIMEWORLDMANAGER PLEASE IM BEGGING YOU");
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }
        registerClasses();

        registerCommands(new SetLobbyCommand(this), new SetupCommand(this), new PartyCommand(this), new JoinCommand());
        registerListeners(new PartyListener(this), new WizardListener(this));

        connectToDataBase();

        startLobbySetupTask();

        sendServerEnableMessage();
    }

    @Override
    public void onDisable() {
        sendServerDisableMessage();
    }

    private void connectToDataBase(){
        this.connection = new DatabaseConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"), getConfig().getBoolean("mysql.useSSL"), new IConnectedCallback() {

            @Override
            public void onConnected(Connection connection) {
                new TableBuilder("player_stats")
                        .addField("UUID", TableBuilder.DataType.VARCHAR, 100)
                        .addField("KILLS", TableBuilder.DataType.INT, 100)
                        .addField("DEATHS", TableBuilder.DataType.INT, 100)
                        .addField("WINS", TableBuilder.DataType.INT, 100)
                        .addField("LOSSES", TableBuilder.DataType.INT, 100)
                        .setPrimaryKey("UUID")
                        .execute(connection);
                new TableBuilder("mapdata")
                        .addField("NAME", TableBuilder.DataType.VARCHAR, 100)
                        .addField("DATA", TableBuilder.DataType.VARCHAR, 10000)
                        .setPrimaryKey("NAME")
                        .execute(connection);

                mapDB = new MapDB(connection);
            }
            @Override
            public void onDisconnect() {
            }
        });
    }

    private void startLobbySetupTask() {
        lobbySetupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (!isLobbySetup()){
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("ContraPVP.op")){
                        MessageUtils.sendActionbar("&cPLEASE SETUP LOBBY! DO /setlobby", player);
                    }
                }
            }else{
                lobbySetupTask.cancel();
            }
        }, 0, 20);
    }

    private boolean isLobbySetup() {
        return getConfig().getString("lobby-location.world") != null &&
                getConfig().getString("lobby-location.x") != null &&
                getConfig().getString("lobby-location.y") != null &&
                getConfig().getString("lobby-location.z") != null &&
                getConfig().getString("lobby-location.yaw") != null &&
                getConfig().getString("lobby-location.pitch") != null;
    }

    private void registerClasses() {
        this.gameManager = new GameManager(this);
        this.wizardManager = new WizardManager(this);
        this.partyManager = new PartyManager();
        this.playerRollBackManager = new PlayerRollBackManager(this);
    }

    public GamePlayer getGamePlayer(Player bukkitPlayer) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getPlayer() != null){
                if (gamePlayer.getPlayer().equals(bukkitPlayer)) return gamePlayer;
            }
        }
        System.out.println("test");
        return newGamePlayer(bukkitPlayer);
    }

    private GamePlayer newGamePlayer(Player player) {
        try{
            GamePlayer gamePlayer = new GamePlayer(player);
            gamePlayers.add(gamePlayer);
            return gamePlayer;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private void sendServerEnableMessage() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&a=-=-=-=-=-=-=-=-="));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&ePlugin made by --Maanraj514"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&b&mRework of the plugin Lepvp"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&aCheck it out here!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("https://www.spigotmc.org/resources/lepvp.102239/"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&a=-=-=-=-=-=-=-=-="));
    }

    private void sendServerDisableMessage() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&c=-=-=-=-=-=-=-=-="));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&cPlugin made by --Maanraj514"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.color("&c=-=-=-=-=-=-=-=-="));
    }
}