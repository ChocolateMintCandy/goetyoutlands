// Goety ISpell has no cast-time event. Extend its boolean decision rather than halving the duration twice.
function initializeCoreMod() {
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    return {
        'goety_outlands_magic_cast_time': {
            target: { type: 'METHOD', class: 'com.Polarice3.Goety.api.magic.ISpell',
                methodName: 'ReduceCastTime', methodDesc: '(Lnet/minecraft/world/entity/LivingEntity;)Z' },
            transformer: function(method) {
                for (var node = method.instructions.getFirst(); node !== null; node = node.getNext()) {
                    if (node.getOpcode() !== Opcodes.IRETURN) continue;
                    var args = new InsnList();
                    args.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    args.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    args.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                        'com/mint/goetyoutlands/common/magic/SpellweaverCasting', 'reduceCastTime',
                        '(ZLcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/world/entity/LivingEntity;)Z', false));
                    method.instructions.insertBefore(node, args);
                }
                method.maxStack = Math.max(method.maxStack, 3);
                return method;
            }
        }
    };
}
