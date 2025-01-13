/*
 * Copyright (C) 2022-2025 AUIOC.ORG
 *
 * This file is part of ArnicaLib, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

function initializeCoreMod() {
    Java.type('net.neoforged.coremod.api.ASMAPI').loadFile('coremods/util/utils.js');

    return {
        'FishingRodItem#use': {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.item.FishingRodItem',
                methodName: 'use',
                methodDesc: '(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;'
            },
            transformer: function (methodNode) {
                var insns = methodNode.instructions;

                var startLabel = label();
                var endLabel = label();

                addLocalVariable(methodNode,
                    'event', 'Lorg/auioc/mcmod/arnicalib/game/event/FishingRodCastEvent;', null,
                    startLabel, endLabel, 8
                );

                var injects = [
                    startLabel,
                    aLoad(2), // p_41291_   (player)
                    aLoad(4), // itemstack  (fishing rod)
                    iLoad(6), // j          (time reduction)
                    iLoad(7), // k          (luck bonus)
                    invokeStatic(
                        'org/auioc/mcmod/arnicalib/mod/event/AHEventHooks',
                        'preFishingRodCast',
                        '(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;II)Lorg/auioc/mcmod/arnicalib/game/event/FishingRodCastEvent;'
                    ),
                    aStore(8), // [+] event FishingRodCastEvent
                    aLoad(8),
                    invokeVirtual(
                        'org/auioc/mcmod/arnicalib/game/event/FishingRodCastEvent',
                        'getTimeReduction',
                        '()I'
                    ),
                    iStore(6),
                    aLoad(8),
                    invokeVirtual(
                        'org/auioc/mcmod/arnicalib/game/event/FishingRodCastEvent', 'getLuckBonus',
                        '()I'
                    ),
                    iStore(7),
                    endLabel
                ];

                var at = findNodeByR(insns, isNewObject('net/minecraft/world/entity/projectile/FishingHook'), -1);
                insns.insertBefore(at, toInsnList(injects));

                setMaxLocals(methodNode, 8);

                // printMethodNode(methodNode);
                return methodNode;
            }
        }
    };
}

//! LocalVariableTable
/*
    Slot    Name            Signature
    5       i               I
    6       original        Lnet/minecraft/world/item/ItemStack;
    6       j               I
    7       k               I
    5       serverlevel     Lnet/minecraft/server/level/ServerLevel;
    0       this            Lnet/minecraft/world/item/FishingRodItem;
    1       p_41290_        Lnet/minecraft/world/level/Level;
    2       p_41291_        Lnet/minecraft/world/entity/player/Player;
    3       p_41292_        Lnet/minecraft/world/InteractionHand;
    4       itemstack       Lnet/minecraft/world/item/ItemStack;
+   8       event           Lorg/auioc/mcmod/arnicalib/game/event/FishingRodCastEvent;
*/

//! Code
/*
    public InteractionResult use(Level p_41290_, Player p_41291_, InteractionHand p_41292_) {
        //_ ...
            int j = (int)(EnchantmentHelper.getFishingTimeReduction(serverlevel, itemstack, p_41291_) * 20.0F);
            int k = EnchantmentHelper.getFishingLuckBonus(serverlevel, itemstack, p_41291_);
+           var event = AHEventHooks.preFishingRodCast(p_41291_, itemstack, j, k);
+           j = event.getTimeReduction();
+           k = event.getLuckBonus();
            Projectile.spawnProjectile(new FishingHook(p_41291_, p_41290_, k, j), serverlevel, itemstack);
        //_ ...
    }
*/
