package parts.lost.mc.scriptexecutor.java;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ScriptExecutor extends JavaPlugin {

    public ScriptExecutor() {

    }

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Enabling ScriptExecutor");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabling ScriptExecutor");
    }
}
