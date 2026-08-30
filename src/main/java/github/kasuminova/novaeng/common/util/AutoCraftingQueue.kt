package github.kasuminova.novaeng.common.util

import ae2.api.networking.security.IActionHost
import ae2.api.networking.crafting.CalculationStrategy
import ae2.me.helpers.PlayerSource
import github.kasuminova.novaeng.common.container.ContainerNEWCraftConfirm
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import java.util.Queue

class AutoCraftingQueue {

    companion object {
        private val allQueue = Object2ObjectOpenHashMap<EntityPlayer, AutoCraftingQueue>()

        fun setQueueAndStrat(itemQueue: Queue<ItemStack>, player: EntityPlayer) {
            val q: AutoCraftingQueue = allQueue.computeIfAbsent(player) { p -> AutoCraftingQueue() }
            q.queue = itemQueue
            q.executionQueue(player)
        }

        fun getQueue(player: EntityPlayer): AutoCraftingQueue? {
            return allQueue[player]
        }
    }

    private var queue: Queue<ItemStack> = EmptyQueue.empty()

    fun clearQueue() {
        queue.clear()
    }

    fun executionQueue(player: EntityPlayer): Boolean {
        val item = queue.poll() ?: return false
        val container = player.openContainer as? ContainerNEWCraftConfirm ?: return false
        val host = container.target as? IActionHost ?: return false
        val node = host.actionableNode ?: return false
        val key = ae2.api.stacks.AEItemKey.of(item) ?: return false
        val source = PlayerSource(player, host)
        val job = node.grid().craftingService.beginCraftingCalculation(
            player.world,
            { source },
            key,
            item.count.toLong(),
            CalculationStrategy.REPORT_MISSING_ITEMS
        )
        container.isAutoStart = false
        container.setJob(job)
        container.detectAndSendChanges()
        return true
    }
}