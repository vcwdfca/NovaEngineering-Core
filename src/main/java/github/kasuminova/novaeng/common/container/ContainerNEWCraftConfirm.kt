package github.kasuminova.novaeng.common.container

import ae2.container.implementations.ContainerCraftConfirm
import ae2.api.storage.ISubGuiHost
import github.kasuminova.novaeng.common.util.AutoCraftingQueue
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer

class ContainerNEWCraftConfirm(ip: InventoryPlayer, te: ISubGuiHost) : ContainerCraftConfirm(ip, te) {
    override fun canInteractWith(playerIn: net.minecraft.entity.player.EntityPlayer): Boolean {
        return true
    }

    override fun startJob() {
        val player = getPlayerInventory().player
        if (isClientSide || player !is EntityPlayerMP) {
            super.startJob()
            return
        }

        val result = getResult()
        if (result == null || result.simulation() || !result.missingItems().isEmpty()) {
            super.startJob()
            return
        }

        super.startJob()
        val submitResult = submitError.result()
        if (!isValidContainer || (submitResult != null && !submitResult.successful())) {
            return
        }

        AutoCraftingQueue.getQueue(player)?.let {
            if (!it.executionQueue(player)) {
                player.closeContainer()
            }
        }
    }

    override fun goBack() {
        val player = getPlayerInventory().player
        if (player !is EntityPlayerMP) {
            super.goBack()
            return
        }

        AutoCraftingQueue.getQueue(player)?.let {
            if (!it.executionQueue(player)) {
                player.closeContainer()
            }
        } ?: player.closeContainer()
    }

}
