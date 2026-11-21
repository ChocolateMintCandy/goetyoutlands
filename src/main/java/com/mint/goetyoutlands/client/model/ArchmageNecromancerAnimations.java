package com.mint.goetyoutlands.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import static net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM;
import static net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR;
import static net.minecraft.client.animation.AnimationChannel.Targets.POSITION;
import static net.minecraft.client.animation.AnimationChannel.Targets.ROTATION;
import static net.minecraft.client.animation.KeyframeAnimations.degreeVec;
import static net.minecraft.client.animation.KeyframeAnimations.posVec;

/** Blockbench archmage_necromancer clips, in Java model coordinates. */
@OnlyIn(Dist.CLIENT)
public final class ArchmageNecromancerAnimations {
    // A single authored revolution is retimed per clip; SUMMON ends partway through it.
    private static final float[] SPIN_Y = {
            0F, -9.932590911F, -19.784326675F, -29.35698086F, -38.47806441F, -47.01406736F, -54.88034283F, -62.046797634F,
            -68.538914843F, -74.434035333F, -79.853233245F, -84.949499112F, -89.893261731F, -94.856508139F, -99.996880882F, -105.443132601F,
            -111.283199924F, -117.555931791F, -124.247191304F, -131.290672117F, -138.573362934F, -145.945191867F, -153.232020958F, -160.25087108F,
            -166.826063892F, -172.804886987F, -178.071428021F, -182.557379853F, -186.248878239F, -189.188774036F, -191.474133837F, -193.249172816F,
            -194.694215961F, -196.01162505F, -197.409889286F, -199.087235102F, -201.216151552F, -203.930148601F, -207.313873132F, -211.397418327F,
            -216.155301118F, -221.510180628F, -227.340982717F, -233.494716849F, -239.80095423F, -246.087707822F, -252.19733508F, -258.00108336F,
            -263.411016037F, -268.38828417F, -272.947024658F, -277.153543844F, -281.120853028F, -284.999024094F, -288.962195004F, -293.193344881F,
            -297.868152069F, -303.139328974F, -309.12278794F, -315.886836109F, -323.445337723F, -331.755441925F, -340.720082124F, -350.195043145F,
            -360F
    };
    private static final float[] TILT_X = {
            0F, -5.430071201F, -10.165706314F, -13.937932653F, -16.530488868F, -17.794275786F, -17.657346415F, -16.129684331F,
            -13.302413404F, -9.341504257F, -4.476459735F, 1.015160699F, 6.826209697F, 12.640032241F, 18.150672862F, 23.081741075F,
            27.202597907F, 30.34076224F, 32.38975508F, 33.311975566F, 33.136602942F, 31.952910206F, 29.89972567F, 27.152059519F,
            23.906101554F, 20.363879446F, 16.71883899F, 13.143473721F, 9.779904377F, 6.734010673F, 4.073375903F, 1.828949964F,
            0F, -1.438370353F, -2.526067216F, -3.308275893F, -3.82623335F, -4.109503464F, -4.170580693F, -4.002396417F,
            -3.578997189F, -2.859333818F, -1.793775237F, -0.332671732F, 1.563934762F, 3.916498384F, 6.717144963F, 9.922866879F,
            13.451610824F, 17.181888044F, 20.956227181F, 24.58844171F, 27.874328006F, 30.605077775F, 32.582410168F, 33.634229959F,
            33.629517769F, 32.49116736F, 30.205604712F, 26.828246042F, 22.484159895F, 17.363667432F, 11.713015001F, 5.820650811F,
            0F
    };
    private static final float[] ROLL_Z = {
            0F, -0.315152725F, -0.621465198F, -0.86336584F, -0.990028652F, -0.957726384F, -0.731885187F, -0.288758189F,
            0.383349649F, 1.283355471F, 2.397201818F, 3.698199149F, 5.1478963F, 6.69744696F, 8.289420688F, 9.859989545F,
            11.341406472F, 12.664679955F, 13.762341487F, 14.571198367F, 15.034964497F, 15.106666136F, 14.750727823F, 13.94465559F,
            12.680249761F, 10.964297345F, 8.818713811F, 6.280124896F, 3.398900445F, 0.237673161F, -3.130605189F, -6.624999881F,
            -10.159236362F, -13.644553342F, -16.992660656F, -20.118692577F, -22.944047347F, -25.399007834F, -27.425046491F, -28.976729612F,
            -30.023151045F, -30.548843272F, -30.554133497F, -30.054933367F, -29.081972299F, -27.679505377F, -25.903546542F, -23.819695612F,
            -21.500642833F, -19.02344661F, -16.466688357F, -13.907612673F, -11.419361221F, -9.068404641F, -6.912268867F, -4.997640512F,
            -3.358921088F, -2.017282267F, -0.980254855F, -0.241863401F, 0.216702831F, 0.425912533F, 0.42625832F, 0.266233225F,
            0F
    };

    public static final AnimationDefinition CRYSTAL_SPIN = crystalSpin();
    public static final AnimationDefinition ALERT = alert();
    public static final AnimationDefinition WALK = walk();
    public static final AnimationDefinition ATTACK = attack();
    public static final AnimationDefinition SUMMON = summon();
    public static final AnimationDefinition SPELL = spell();

    private ArchmageNecromancerAnimations() { }

    private static Keyframe rot(float time, float x, float y, float z, AnimationChannel.Interpolation curve) {
        return new Keyframe(time, degreeVec(x, y, z), curve);
    }

    private static Keyframe pos(float time, float x, float y, float z, AnimationChannel.Interpolation curve) {
        return new Keyframe(time, posVec(x, y, z), curve);
    }

    // Only needed where the authored walk seams have different tangents from vanilla Catmull-Rom.
    // The handles sit at 1/3 and 2/3 of the segment, so elapsed time is the Bezier parameter.
    private static AnimationChannel.Interpolation bezier(Vector3f outgoing, Vector3f incoming) {
        return (out, t, frames, from, to, weight) -> {
            Vector3f a = frames[from].target(), b = frames[to].target();
            float u = 1 - t, p = 3 * u * u * t, q = 3 * u * t * t;
            return out.set(a).mul(u * u * u + p).fma(p, outgoing)
                    .fma(q, incoming).fma(q + t * t * t, b).mul(weight);
        };
    }

    private static float sample(float[] cycle, int index, float fraction) {
        int i = index % 64;
        return cycle[i] + fraction * (cycle[i + 1] - cycle[i]) + (index / 64) * cycle[64];
    }

    private static void addCrystal(AnimationDefinition.Builder b, float length, float period, int frames) {
        Keyframe[] spin = new Keyframe[frames], tilt = new Keyframe[frames], roll = new Keyframe[frames];
        for (int i = 0; i < frames; i++) {
            float time = Math.min(length, i * period / 64);
            // Only a clipped final key can lie between authored samples.
            float phase = i == frames - 1 ? Math.min(i, length * 64 / period) : i;
            int index = (int)phase;
            float fraction = phase - index;
            spin[i] = rot(time, 0, sample(SPIN_Y, index, fraction), 0, LINEAR);
            tilt[i] = rot(time, sample(TILT_X, index, fraction), 0, 0, LINEAR);
            roll[i] = rot(time, 0, 0, sample(ROLL_Z, index, fraction), LINEAR);
        }
        b.addAnimation("staffhead", new AnimationChannel(ROTATION, spin));
        b.addAnimation("tilt", new AnimationChannel(ROTATION, tilt));
        b.addAnimation("roll", new AnimationChannel(ROTATION, roll));
    }

    private static AnimationDefinition crystalSpin() {
        var b = AnimationDefinition.Builder.withLength(5.12F).looping();
        addCrystal(b, 5.12F, 5.12F, 65);
        return b.build();
    }

    private static AnimationDefinition alert() {
        var b = AnimationDefinition.Builder.withLength(3.5F).looping();
        b.addAnimation("head", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, CATMULLROM),
                rot(1.375F, 0F, 10F, 0F, CATMULLROM),
                rot(1.875F, 0F, 10F, 0F, CATMULLROM),
                rot(2.25F, 0F, -25F, 0F, CATMULLROM),
                rot(2.75F, 0F, -25F, 0F, CATMULLROM),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("body", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, CATMULLROM),
                rot(1.375F, 0F, 10F, 0F, CATMULLROM),
                rot(1.875F, 0F, 10F, 0F, CATMULLROM),
                rot(2.25F, 0F, -10F, 0F, CATMULLROM),
                rot(2.75F, 0F, -10F, 0F, CATMULLROM),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, CATMULLROM),
                rot(1.375F, 0F, -10F, -1.5F, CATMULLROM),
                rot(1.875F, 0F, -10F, -1.5F, CATMULLROM),
                rot(2.25F, 0F, 5F, 1.5F, CATMULLROM),
                rot(2.75F, 0F, 5F, 1.5F, CATMULLROM),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(3.5F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("staff", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(3.5F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("left_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, CATMULLROM),
                rot(1.375F, 0F, 0F, -5F, CATMULLROM),
                rot(1.875F, 0F, 0F, -5F, CATMULLROM),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, LINEAR),
                rot(3.5F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("left_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, LINEAR),
                rot(3.5F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("right_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(3.5F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("cape", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, CATMULLROM),
                rot(1.5F, 5F, 0F, 0F, CATMULLROM),
                rot(2F, 0F, 0F, 0F, CATMULLROM),
                rot(2.5F, 5F, 0F, 0F, CATMULLROM),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM)));
        addCrystal(b, 3.5F, 3.5F, 65);
        return b.build();
    }

    private static AnimationDefinition walk() {
        var b = AnimationDefinition.Builder.withLength(7F).looping();
        b.addAnimation("skeleton", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(0.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(0.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(0.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(0.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(0.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(1F, 0F, 0F, 0F, CATMULLROM),
                pos(1.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(1.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(1.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(1.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(1.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(1.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(2F, 0F, 0F, 0F, CATMULLROM),
                pos(2.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(2.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(2.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(2.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(2.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(2.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(3F, 0F, 0F, 0F, CATMULLROM),
                pos(3.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(3.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(3.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(3.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(3.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(3.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(4F, 0F, 0F, 0F, CATMULLROM),
                pos(4.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(4.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(4.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(4.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(4.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(4.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(5F, 0F, 0F, 0F, CATMULLROM),
                pos(5.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(5.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(5.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(5.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(5.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(5.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(6F, 0F, 0F, 0F, CATMULLROM),
                pos(6.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(6.25F, 0F, -0.25F, 0F, CATMULLROM),
                pos(6.3333F, 0F, 0F, 0F, CATMULLROM),
                pos(6.6667F, 0F, 0F, 0F, CATMULLROM),
                pos(6.75F, 0F, -0.25F, 0F, CATMULLROM),
                pos(6.8333F, 0F, 0F, 0F, CATMULLROM),
                pos(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("head", new AnimationChannel(ROTATION,
                rot(0F, 0F, -15F, 0F, LINEAR),
                rot(0.25F, 0F, -22.5F, 0F, CATMULLROM),
                rot(0.5F, 0F, -15F, 0F, CATMULLROM),
                rot(0.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(1F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(1.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, -15F, 0F, CATMULLROM),
                rot(1.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(2F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(2.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, -15F, 0F, CATMULLROM),
                rot(2.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(3F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(3.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, -15F, 0F, CATMULLROM),
                rot(3.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(4F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(4.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, -15F, 0F, CATMULLROM),
                rot(4.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(5F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(5.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, -15F, 0F, CATMULLROM),
                rot(5.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(6F, 0F, -15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, 1.25F, 0F))),
                rot(6.25F, 0F, -22.5F, 0F, bezier(degreeVec(0F, -1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, -15F, 0F, CATMULLROM),
                rot(6.75F, 0F, -7.5F, 0F, CATMULLROM),
                rot(7F, 0F, -15F, 0F, CATMULLROM)));
        b.addAnimation("body", new AnimationChannel(ROTATION,
                rot(0F, 0F, 15F, 0F, LINEAR),
                rot(0.25F, 0F, 22.5F, 0F, CATMULLROM),
                rot(0.5F, 0F, 15F, 0F, CATMULLROM),
                rot(0.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(1F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(1.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, 15F, 0F, CATMULLROM),
                rot(1.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(2F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(2.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, 15F, 0F, CATMULLROM),
                rot(2.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(3F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(3.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, 15F, 0F, CATMULLROM),
                rot(3.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(4F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(4.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, 15F, 0F, CATMULLROM),
                rot(4.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(5F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(5.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, 15F, 0F, CATMULLROM),
                rot(5.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(6F, 0F, 15F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(0F, -1.25F, 0F))),
                rot(6.25F, 0F, 22.5F, 0F, bezier(degreeVec(0F, 1.25F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, 15F, 0F, CATMULLROM),
                rot(6.75F, 0F, 7.5F, 0F, CATMULLROM),
                rot(7F, 0F, 15F, 0F, CATMULLROM)));
        b.addAnimation("right_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 5F, -2.5F, 0F, CATMULLROM),
                rot(0.5F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 10F, 10F, 3F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(1.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, 0F, 0F, CATMULLROM),
                rot(1.75F, 10F, 10F, 3F, CATMULLROM),
                rot(2F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(2.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, 0F, 0F, CATMULLROM),
                rot(2.75F, 10F, 10F, 3F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(3.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM),
                rot(3.75F, 10F, 10F, 3F, CATMULLROM),
                rot(4F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(4.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, 0F, 0F, CATMULLROM),
                rot(4.75F, 10F, 10F, 3F, CATMULLROM),
                rot(5F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(5.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, 0F, 0F, CATMULLROM),
                rot(5.75F, 10F, 10F, 3F, CATMULLROM),
                rot(6F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 1.666666667F, 0.5F))),
                rot(6.25F, 5F, -2.5F, 0F, bezier(degreeVec(0.833333333F, -0.416666667F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, 0F, 0F, CATMULLROM),
                rot(6.75F, 10F, 10F, 3F, CATMULLROM),
                rot(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, LINEAR),
                rot(2F, 0F, 0F, 0F, LINEAR),
                rot(3F, 0F, 0F, 0F, LINEAR),
                rot(4F, 0F, 0F, 0F, LINEAR),
                rot(5F, 0F, 0F, 0F, LINEAR),
                rot(6F, 0F, 0F, 0F, LINEAR),
                rot(7F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("staff", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(1F, 0F, 0F, 0F, LINEAR),
                pos(2F, 0F, 0F, 0F, LINEAR),
                pos(3F, 0F, 0F, 0F, LINEAR),
                pos(4F, 0F, 0F, 0F, LINEAR),
                pos(5F, 0F, 0F, 0F, LINEAR),
                pos(6F, 0F, 0F, 0F, LINEAR),
                pos(7F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("left_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -12.5F, 0F, 0F, CATMULLROM),
                rot(0.5F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(1.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, 0F, 0F, CATMULLROM),
                rot(1.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(2F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(2.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, 0F, 0F, CATMULLROM),
                rot(2.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(3.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM),
                rot(3.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(4F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(4.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, 0F, 0F, CATMULLROM),
                rot(4.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(5F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(5.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, 0F, 0F, CATMULLROM),
                rot(5.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(6F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(6.25F, -12.5F, 0F, 0F, bezier(degreeVec(-2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, 0F, 0F, CATMULLROM),
                rot(6.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, -2.5F, LINEAR),
                rot(0.1667F, -15F, 0F, -2.5F, CATMULLROM),
                rot(0.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(0.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(1F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(1.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(1.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(1.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(2F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(2.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(2.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(2.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(3F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(3.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(3.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(3.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(4F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(4.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(4.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(4.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(5F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(5.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(5.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(5.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(6F, 0F, 0F, -2.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(4.166666667F, 0F, 0F))),
                rot(6.1667F, -15F, 0F, -2.5F, bezier(degreeVec(-2.5F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(6.25F, -15F, 0F, -2.5F, CATMULLROM),
                rot(6.75F, 25F, 0F, -2.5F, CATMULLROM),
                rot(7F, 0F, 0F, -2.5F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(0.25F, 0F, -1F, 0F, CATMULLROM),
                pos(0.75F, 0F, 0F, 0F, CATMULLROM),
                pos(1F, 0F, 0F, 0F, CATMULLROM),
                pos(1.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(1.25F, 0F, -1F, 0F, CATMULLROM),
                pos(1.75F, 0F, 0F, 0F, CATMULLROM),
                pos(2F, 0F, 0F, 0F, CATMULLROM),
                pos(2.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(2.25F, 0F, -1F, 0F, CATMULLROM),
                pos(2.75F, 0F, 0F, 0F, CATMULLROM),
                pos(3F, 0F, 0F, 0F, CATMULLROM),
                pos(3.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(3.25F, 0F, -1F, 0F, CATMULLROM),
                pos(3.75F, 0F, 0F, 0F, CATMULLROM),
                pos(4F, 0F, 0F, 0F, CATMULLROM),
                pos(4.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(4.25F, 0F, -1F, 0F, CATMULLROM),
                pos(4.75F, 0F, 0F, 0F, CATMULLROM),
                pos(5F, 0F, 0F, 0F, CATMULLROM),
                pos(5.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(5.25F, 0F, -1F, 0F, CATMULLROM),
                pos(5.75F, 0F, 0F, 0F, CATMULLROM),
                pos(6F, 0F, 0F, 0F, CATMULLROM),
                pos(6.1667F, 0F, 0F, 0F, CATMULLROM),
                pos(6.25F, 0F, -1F, 0F, CATMULLROM),
                pos(6.75F, 0F, 0F, 0F, CATMULLROM),
                pos(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 7.5F, LINEAR),
                rot(0.25F, 25F, 0F, 7.5F, CATMULLROM),
                rot(0.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(0.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(1F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(1.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(1.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(1.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(2F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(2.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(2.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(2.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(3F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(3.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(3.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(3.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(4F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(4.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(4.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(4.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(5F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(5.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(5.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(5.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(6F, 0F, 0F, 7.5F, bezier(degreeVec(2.5F, 0F, 0F), degreeVec(-2.5F, 0F, 0F))),
                rot(6.25F, 25F, 0F, 7.5F, bezier(degreeVec(4.166666667F, 0F, 0F), degreeVec(2.5F, 0F, 0F))),
                rot(6.75F, -15F, 0F, 7.5F, CATMULLROM),
                rot(6.8333F, -15F, 0F, 7.5F, CATMULLROM),
                rot(7F, 0F, 0F, 7.5F, CATMULLROM)));
        b.addAnimation("left_leg", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.75F, 0F, 0F, 0F, CATMULLROM),
                pos(0.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(1F, 0F, 0F, 0F, CATMULLROM),
                pos(1.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(1.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(2F, 0F, 0F, 0F, CATMULLROM),
                pos(2.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(2.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(3F, 0F, 0F, 0F, CATMULLROM),
                pos(3.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(3.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(4F, 0F, 0F, 0F, CATMULLROM),
                pos(4.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(4.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(5F, 0F, 0F, 0F, CATMULLROM),
                pos(5.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(5.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(6F, 0F, 0F, 0F, CATMULLROM),
                pos(6.75F, 0F, 0F, 0F, bezier(posVec(0F, 0F, 0F), posVec(0F, 0.166666667F, 0F))),
                pos(6.8333F, 0F, -1F, 0F, CATMULLROM),
                pos(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("middle", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 10F, 0F, 0F, CATMULLROM),
                rot(0.5F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 10F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(1.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, 0F, 0F, CATMULLROM),
                rot(1.75F, 10F, 0F, 0F, CATMULLROM),
                rot(2F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(2.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, 0F, 0F, CATMULLROM),
                rot(2.75F, 10F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(3.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM),
                rot(3.75F, 10F, 0F, 0F, CATMULLROM),
                rot(4F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(4.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, 0F, 0F, CATMULLROM),
                rot(4.75F, 10F, 0F, 0F, CATMULLROM),
                rot(5F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(5.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, 0F, 0F, CATMULLROM),
                rot(5.75F, 10F, 0F, 0F, CATMULLROM),
                rot(6F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(1.666666667F, 0F, 0F))),
                rot(6.25F, 10F, 0F, 0F, bezier(degreeVec(1.666666667F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, 0F, 0F, CATMULLROM),
                rot(6.75F, 10F, 0F, 0F, CATMULLROM),
                rot(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("cape", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 12.5F, 0F, 0F, CATMULLROM),
                rot(0.5F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(1.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(1.5F, 0F, 0F, 0F, CATMULLROM),
                rot(1.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(2F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(2.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(2.5F, 0F, 0F, 0F, CATMULLROM),
                rot(2.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(3.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(3.5F, 0F, 0F, 0F, CATMULLROM),
                rot(3.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(4F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(4.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(4.5F, 0F, 0F, 0F, CATMULLROM),
                rot(4.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(5F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(5.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(5.5F, 0F, 0F, 0F, CATMULLROM),
                rot(5.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(6F, 0F, 0F, 0F, bezier(degreeVec(0F, 0F, 0F), degreeVec(2.083333333F, 0F, 0F))),
                rot(6.25F, 12.5F, 0F, 0F, bezier(degreeVec(2.083333333F, 0F, 0F), degreeVec(0F, 0F, 0F))),
                rot(6.5F, 0F, 0F, 0F, CATMULLROM),
                rot(6.75F, 12.5F, 0F, 0F, CATMULLROM),
                rot(7F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(1F, 0F, 0F, 0F, LINEAR),
                rot(2F, 0F, 0F, 0F, LINEAR),
                rot(3F, 0F, 0F, 0F, LINEAR),
                rot(4F, 0F, 0F, 0F, LINEAR),
                rot(5F, 0F, 0F, 0F, LINEAR),
                rot(6F, 0F, 0F, 0F, LINEAR),
                rot(7F, 0F, 0F, 0F, LINEAR)));
        addCrystal(b, 7F, 3.5F, 129);
        return b.build();
    }

    private static AnimationDefinition attack() {
        var b = AnimationDefinition.Builder.withLength(1F).looping();
        b.addAnimation("right_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, -55F, -45F, -50F, CATMULLROM),
                rot(0.375F, -55F, -45F, -50F, CATMULLROM),
                rot(0.5F, -10F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, -65F, -30F, -20F, CATMULLROM),
                rot(0.375F, -65F, -30F, -20F, CATMULLROM),
                rot(0.5F, 45F, 25F, -25F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.375F, 7.5F, 0F, 0F, CATMULLROM),
                rot(0.5F, 72.5F, 0F, 0F, CATMULLROM),
                rot(0.75F, 72.5F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.125F, 1F, 1F, -10.5F, CATMULLROM),
                pos(0.375F, 1F, 1F, -10.5F, CATMULLROM),
                pos(0.5F, 0F, 0F, -5.5F, CATMULLROM),
                pos(0.75F, 0F, 0F, -5.5F, CATMULLROM),
                pos(0.875F, 0F, 0F, -4.5F, CATMULLROM),
                pos(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("body", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, -15F, 60F, -15F, CATMULLROM),
                rot(0.375F, -15F, 60F, -15F, CATMULLROM),
                rot(0.5F, 30F, -10F, -20F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("head", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, 15F, -60F, 0F, CATMULLROM),
                rot(0.375F, 15F, -60F, 0F, CATMULLROM),
                rot(0.5F, 16.5F, 13.5F, 12F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, 20F, 0F, 0F, CATMULLROM),
                rot(0.375F, 20F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, -17.5F, 0F, 0F, CATMULLROM),
                rot(0.375F, -17.5F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("pants", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, 1F, 0F, -10F, CATMULLROM),
                rot(0.375F, 1F, 0F, -10F, CATMULLROM),
                rot(0.5F, -25F, 10F, 15F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("middle", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, -7.5F, 0F, -12.5F, CATMULLROM),
                rot(0.375F, 0F, 0F, 0F, CATMULLROM),
                rot(0.5F, -17.02F, 0F, 0.8F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("cape", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, 0F, 0F, 0F, CATMULLROM),
                rot(0.375F, 10F, 0F, 0F, CATMULLROM),
                rot(0.5F, 70F, 10F, -5F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.125F, 50F, 0F, 0F, CATMULLROM),
                rot(0.375F, 50F, 0F, 0F, CATMULLROM),
                rot(1F, 0F, 0F, 0F, CATMULLROM)));
        addCrystal(b, 1F, 1F, 65);
        return b.build();
    }

    private static AnimationDefinition summon() {
        var b = AnimationDefinition.Builder.withLength(1.7917F);
        b.addAnimation("head", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, -15F, 0F, CATMULLROM),
                rot(0.75F, -8.9426F, 14.9685F, 7.1139F, CATMULLROM),
                rot(1.4167F, -8.9426F, 14.9685F, 7.1139F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, -10F, -15F, 0F, CATMULLROM),
                rot(0.5833F, -10F, -15F, 0F, CATMULLROM),
                rot(0.75F, -32.5209F, 15.7337F, 5.6273F, CATMULLROM),
                rot(1.4167F, -32.5209F, 15.7337F, 5.6273F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, -105F, 0F, -85F, CATMULLROM),
                rot(0.5833F, -105F, 0F, -85F, CATMULLROM),
                rot(0.75F, -50F, 0F, -32.5F, CATMULLROM),
                rot(1.4167F, -50F, 0F, -32.5F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, 7.5F, -90F, 0F, CATMULLROM),
                rot(0.5833F, 7.5F, -90F, 0F, CATMULLROM),
                rot(0.75F, 7.5F, 0F, 0F, LINEAR),
                rot(1.0833F, 7.5F, 0F, 0F, LINEAR),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.3333F, 0F, 1F, -5.5F, CATMULLROM),
                pos(0.5833F, 0F, 1F, -5.5F, CATMULLROM),
                pos(0.75F, 0F, 1F, -10.5F, CATMULLROM),
                pos(1.4167F, 0F, 1F, -10.5F, CATMULLROM),
                pos(1.6667F, 0F, 1F, -5.17F, CATMULLROM),
                pos(1.75F, 0F, 0F, 1.5F, CATMULLROM),
                pos(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("body", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, 80F, 15F, 0F, CATMULLROM),
                rot(0.5833F, 80F, 15F, 0F, CATMULLROM),
                rot(0.75F, -12.5F, -15F, 0F, CATMULLROM),
                rot(1.4167F, -12.5F, -15F, 0F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, -12.5F, 0F, 0F, CATMULLROM),
                rot(1.4167F, -12.5F, 0F, 0F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.75F, 0F, 0F, -1F, CATMULLROM),
                pos(1.4167F, 0F, 0F, -1F, CATMULLROM),
                pos(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 0F, -30F, 7.5F, CATMULLROM),
                rot(1.4167F, 0F, -30F, 7.5F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_leg", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.75F, 0F, 0F, 1F, CATMULLROM),
                pos(1.4167F, 0F, 0F, 1F, CATMULLROM),
                pos(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("pants", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.3333F, -80F, 0F, 0F, CATMULLROM),
                rot(0.5833F, -80F, 0F, 0F, CATMULLROM),
                rot(0.75F, 10F, 0F, 0F, CATMULLROM),
                rot(1.4167F, 10F, 0F, 0F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("middle", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, -6.9776F, -5.2483F, -11.3608F, CATMULLROM),
                rot(0.8333F, -7.5995F, -4.2954F, -3.8956F, CATMULLROM),
                rot(1.0417F, -6.9776F, -5.2483F, -11.3608F, CATMULLROM),
                rot(1.2083F, -7.5995F, -4.2954F, -3.8956F, CATMULLROM),
                rot(1.4167F, -6.9776F, -5.2483F, -11.3608F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, 0F, 0F, CATMULLROM),
                rot(0.75F, 10F, -2F, -45F, CATMULLROM),
                rot(1.4167F, 10F, -2F, -45F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("cape", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.5833F, 0F, 0F, 0F, LINEAR),
                rot(0.75F, 90F, 0F, 0F, CATMULLROM),
                rot(0.875F, 70F, 0F, 0F, CATMULLROM),
                rot(1.0417F, 77.5F, 0F, 0F, CATMULLROM),
                rot(1.25F, 45F, 0F, 0F, CATMULLROM),
                rot(1.4167F, 40F, 0F, 0F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.1667F, 52.7476F, -15.7627F, 12.8426F, CATMULLROM),
                rot(0.3333F, 180F, -80F, -115F, CATMULLROM),
                rot(0.5833F, 180F, -80F, -115F, CATMULLROM),
                rot(0.75F, 30F, -15F, 70F, CATMULLROM),
                rot(1.4167F, 30F, -15F, 70F, CATMULLROM),
                rot(1.7917F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_pauldron", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.3333F, 0F, 0F, -1F, CATMULLROM),
                pos(0.5833F, 0F, 0F, -2F, CATMULLROM),
                pos(0.75F, 0F, 0F, 0F, CATMULLROM)));
        addCrystal(b, 1.7917F, 5.12F, 24);
        return b.build();
    }

    private static AnimationDefinition spell() {
        var b = AnimationDefinition.Builder.withLength(3F);
        b.addAnimation("body", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -15F, 0F, 5F, CATMULLROM),
                rot(2.75F, -15F, 0F, 5F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("head", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -15F, 30F, 0F, CATMULLROM),
                rot(2.75F, -15F, 30F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -45F, -5F, -10F, CATMULLROM),
                rot(2.75F, -45F, -5F, -10F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 55F, -2.5F, -5F, CATMULLROM),
                rot(2.75F, 55F, -2.5F, -5F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("staff", new AnimationChannel(POSITION,
                pos(0F, 0F, 0F, 0F, LINEAR),
                pos(0.125F, 0F, 1F, -3.25F, LINEAR),
                pos(0.25F, 0F, -1F, -6.5F, LINEAR),
                pos(2.75F, 0F, -1F, -6.5F, LINEAR),
                pos(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_pauldron", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 30F, 5F, 15F, CATMULLROM),
                rot(2.75F, 30F, 5F, 15F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("left_arm", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -45F, -10F, -5F, CATMULLROM),
                rot(2.75F, -45F, -10F, -5F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("pants", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 0F, 0F, 0F, CATMULLROM),
                rot(0.5F, 7.5F, 0F, 0F, CATMULLROM),
                rot(0.75F, 0F, 0F, 0F, CATMULLROM),
                rot(1F, 7.5F, 0F, 0F, CATMULLROM),
                rot(1.25F, 0F, 0F, 0F, CATMULLROM),
                rot(1.5F, 7.5F, 0F, 0F, CATMULLROM),
                rot(1.75F, 0F, 0F, 0F, CATMULLROM),
                rot(2F, 7.5F, 0F, 0F, CATMULLROM),
                rot(2.25F, 0F, 0F, 0F, CATMULLROM),
                rot(2.5F, 7.5F, 0F, 0F, CATMULLROM),
                rot(2.75F, 0F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("middle", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, -10F, -2.5F, -15F, CATMULLROM),
                rot(2.75F, -10F, -2.5F, -15F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("cape", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 35F, 0F, 0F, CATMULLROM),
                rot(0.5F, 45F, 0F, 0F, CATMULLROM),
                rot(0.75F, 35F, 0F, 0F, CATMULLROM),
                rot(1F, 45F, 0F, 0F, CATMULLROM),
                rot(1.25F, 35F, 0F, 0F, CATMULLROM),
                rot(1.5F, 45F, 0F, 0F, CATMULLROM),
                rot(1.75F, 35F, 0F, 0F, CATMULLROM),
                rot(2F, 45F, 0F, 0F, CATMULLROM),
                rot(2.25F, 35F, 0F, 0F, CATMULLROM),
                rot(2.5F, 45F, 0F, 0F, CATMULLROM),
                rot(2.75F, 35F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        b.addAnimation("right_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 0F, 0F, 0F, LINEAR),
                rot(2.75F, 0F, 0F, 0F, LINEAR),
                rot(3F, 0F, 0F, 0F, LINEAR)));
        b.addAnimation("left_leg", new AnimationChannel(ROTATION,
                rot(0F, 0F, 0F, 0F, LINEAR),
                rot(0.25F, 20F, 0F, 0F, CATMULLROM),
                rot(2.75F, 20F, 0F, 0F, CATMULLROM),
                rot(3F, 0F, 0F, 0F, CATMULLROM)));
        addCrystal(b, 3F, 1F, 193);
        return b.build();
    }
}
