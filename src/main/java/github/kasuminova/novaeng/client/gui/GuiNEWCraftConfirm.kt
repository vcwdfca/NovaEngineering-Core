package github.kasuminova.novaeng.client.gui

import ae2.client.gui.me.crafting.GuiCraftConfirm
import ae2.client.gui.style.GuiStyle
import github.kasuminova.novaeng.common.container.ContainerNEWCraftConfirm
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.text.ITextComponent

open class GuiNEWCraftConfirm(
    container: ContainerNEWCraftConfirm,
    ip: InventoryPlayer,
    title: ITextComponent?,
    style: GuiStyle
) : GuiCraftConfirm(container, ip, title, style) {
}
