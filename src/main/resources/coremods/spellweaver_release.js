// Goety's cast events precede its affordability/target checks. Observe the result, not item-use completion.
function initializeCoreMod() {
    var Op = Java.type('org.objectweb.asm.Opcodes');
    var List = Java.type('org.objectweb.asm.tree.InsnList');
    var Load = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var Call = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var api = 'com/Polarice3/Goety/api/magic/';
    var fx = 'com/mint/goetyoutlands/common/magic/SpellweaverCastFx';
    var stack = 'Lnet/minecraft/world/item/ItemStack;', living = 'Lnet/minecraft/world/entity/LivingEntity;';
    var level = 'Lnet/minecraft/world/level/Level;', server = 'Lnet/minecraft/server/level/ServerLevel;';
    var player = 'Lnet/minecraft/world/entity/player/Player;', hand = 'Lnet/minecraft/world/InteractionHand;';
    var ctx = 'Lnet/minecraft/world/item/context/UseOnContext;', result = 'Lnet/minecraft/world/InteractionResult;';
    var stat = 'Lcom/Polarice3/Goety/common/magic/SpellStat;';
    return { 'goety_outlands_staff_release': {
        target: {type: 'CLASS', name: 'com.Polarice3.Goety.common.items.magic.DarkWand'},
        transformer: function(cls) {
            for (var i = 0; i < cls.methods.size(); ++i) {
                var m = cls.methods.get(i), owner, name, desc, who;
                if (m.name == 'MagicResults' && m.desc == '(' + stack + level + living + 'L' + api + 'ISpell;)V') {
                    owner = 'ISpell'; name = 'SpellResult'; desc = server + living + stack + stat; who = 3;
                } else if (m.desc == '(' + stack + player + living + hand + ')' + result) {
                    owner = 'ITouchSpell'; name = 'touchResult'; desc = server + living + living + stack + stat; who = 2;
                } else if (m.desc == '(' + ctx + ')' + result) {
                    owner = 'IBlockSpell'; name = 'blockResult'; desc = server + living + stack + 'Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;' + stat; who = 0;
                } else continue;
                for (var n = m.instructions.getFirst(); n !== null; n = n.getNext()) {
                    if (n.getOpcode() !== Op.INVOKEINTERFACE || n.owner != api + owner || n.name != name || n.desc != '(' + desc + ')V') continue;
                    var hook = new List();
                    hook.add(new Load(Op.ALOAD, 1));
                    if (who) hook.add(new Load(Op.ALOAD, who));
                    hook.add(new Call(Op.INVOKESTATIC, fx, who ? 'released' : 'releasedOnBlock', '(' + (who ? stack + living : ctx) + ')V', false));
                    m.instructions.insert(n, hook);
                }
            }
            return cls;
        }
    }};
}
