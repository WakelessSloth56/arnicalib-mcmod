/*
 * Copyright (C) 2024-2025 AUIOC.ORG
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

var ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var LocalVariableNode = Java.type('org.objectweb.asm.tree.LocalVariableNode');

function printMethodNode(method) {
    print(ASMAPI.methodNodeToString(method));
}

/**
 * @typedef {(any)=>boolean} NodePredicate
 */

/**
 * @param {string} name
 * @param {string} owner
 * @return NodePredicate
 */
function isFieldGet(owner, name) {
    return function (node) {
        return node instanceof FieldInsnNode && node.getOpcode() === Opcodes.GETFIELD
            && node.owner === owner && node.name === name;
    };
}

/**
 * @param {string} name
 * @param {string} owner
 * @param {string} desc
 * @return NodePredicate
 */
function isInvoke(owner, name, desc) {
    return function (node) {
        return node instanceof MethodInsnNode
            && node.owner === owner && node.name === name && node.desc === desc;
    };
}

/**
 * @param {string} desc
 * @return NodePredicate
 */
function isNewObject(desc) {
    return function (node) {
        return node instanceof TypeInsnNode && node.getOpcode() === Opcodes.NEW
            && node.desc === desc;
    };
}

/**
 * @param {number} index
 * @return NodePredicate
 */
function isALoad(index) {
    return function (node) {
        return node instanceof VarInsnNode && node.getOpcode() === Opcodes.ALOAD
            && node.var === index;
    };
}


/**
 * @param {number} index
 * @return NodePredicate
 */
function isILoad(index) {
    return function (node) {
        return node instanceof VarInsnNode && node.getOpcode() === Opcodes.ILOAD
            && node.var === index;
    };
}

/**
 * @return NodePredicate
 */
function isReturn() {
    return function (node) {
        return node instanceof InsnNode && node.getOpcode() === Opcodes.RETURN;
    };
}

/**
 * @param {object[]} nodes
 * @return InsnList
 */
function toInsnList(nodes) {
    var insnList = new InsnList();
    for (var i = 0; i < nodes.length; i++) {
        insnList.add(nodes[i]);
    }
    return insnList;
}

/**
 * @param {InsnList} insns
 * @param {NodePredicate} predicate
 * @param {number} offset
 * @return any
 */
function findNodeBy(insns, predicate, offset) {
    for (var i = 0; i < insns.size(); i++) {
        var node = insns.get(i);
        if (predicate(node)) {
            return insns.get(i + offset);
        }
    }
    throw new Error('Failed to find node');
}

/**
 * @param {InsnList} insns
 * @param {NodePredicate} predicate
 * @param {number} offset
 * @return any
 */
function findNodeByR(insns, predicate, offset) {
    for (var i = insns.size() - 1; i >= 0; i--) {
        var node = insns.get(i);
        if (predicate(node)) {
            return insns.get(i + offset);
        }
    }
    throw new Error('Failed to find node');
}

/**
 * @param {InsnList} insns
 * @param {NodePredicate} predicate
 * @param {number[]} offsets
 * @return any[]
 */
function findNodesByR(insns, predicate, offsets) {
    for (var i = insns.size() - 1; i >= 0; i--) {
        var node = insns.get(i);
        if (predicate(node)) {
            var result = [];
            for (var j = 0; j < offsets.length; j++) {
                result[j] = insns.get(i + offsets[j]);
            }
            return result;
        }
    }
    throw new Error('Failed to find nodes');
}

/**
 * @param {MethodNode} method
 * @param {string} name
 * @param {string} desc
 * @param {string|null} sig
 * @param {LabelNode} start
 * @param {LabelNode} end
 * @param {number} index
 */
function addLocalVariable(method, name, desc, sig, start, end, index) {
    method.localVariables.add(new LocalVariableNode(name, desc, sig, start, end, index));
}

function setMaxLocals(method, max) {
    method.visitMaxs(method.maxStack, max);
}

function label() {
    return new LabelNode();
}

function aLoad(index) {
    return new VarInsnNode(Opcodes.ALOAD, index);
}

function aStore(index) {
    return new VarInsnNode(Opcodes.ASTORE, index);
}

function iLoad(index) {
    return new VarInsnNode(Opcodes.ILOAD, index);
}

function iStore(index) {
    return new VarInsnNode(Opcodes.ISTORE, index);
}

function ifNotEqual(label) {
    return new JumpInsnNode(Opcodes.IFNE, label);
}

function ifEqual(label) {
    return new JumpInsnNode(Opcodes.IFEQ, label);
}

function invokeSpecial(owner, name, desc) {
    return new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, desc, false);
}

function invokeStatic(owner, name, desc) {
    return new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false);
}

function invokeVirtual(owner, name, desc) {
    return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, desc, false);
}

function getField(owner, name, desc) {
    return new FieldInsnNode(Opcodes.GETFIELD, owner, name, desc);
}

function newObject(desc) {
    return new TypeInsnNode(Opcodes.NEW, desc);
}

function dup() {
    return new InsnNode(Opcodes.DUP);
}

function frameChop() {
    return new FrameNode(Opcodes.F_CHOP, 1, null, 0, null);
}
