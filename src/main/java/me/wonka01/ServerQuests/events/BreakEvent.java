package me.wonka01.ServerQuests.events;

import me.knighthat.apis.utils.Utils;
import me.wonka01.ServerQuests.ServerQuests;
import me.wonka01.ServerQuests.enums.ObjectiveType;
import me.wonka01.ServerQuests.questcomponents.ActiveQuests;
import me.wonka01.ServerQuests.questcomponents.QuestController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BreakEvent extends QuestListener implements Listener {

    private final String BROKEN = "BROKEN";
    private final MetadataValue meta = new FixedMetadataValue(JavaPlugin.getPlugin(ServerQuests.class), true);


    public BreakEvent(ActiveQuests activeQuests) {
        super(activeQuests);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata(BROKEN)) {
            return;
        }


        List<QuestController> controllers = tryGetControllersOfEventType(ObjectiveType.BLOCK_BREAK);
        for (QuestController controller : controllers) {
            List<String> materials = controller.getEventConstraints().getMaterialNames();

            if (materials.isEmpty() || Utils.contains(materials, event.getBlock().getType())) {
                event.getBlock().setMetadata(BROKEN, meta);
                updateQuest(controller, event.getPlayer(), 1);
                break;
            }
        }
    }
}
