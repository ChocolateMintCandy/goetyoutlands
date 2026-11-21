package com.mint.goetyoutlands.Register;

import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, goety_outlands.MOD_ID);
    public static final RegistryObject<SimpleParticleType> SOUL_BOLT_TRAIL =
            PARTICLES.register("ominous_soul_bolt_trail", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SOUL_BOLT_IMPACT =
            PARTICLES.register("ominous_soul_bolt_impact", () -> new SimpleParticleType(false));

    private ModParticles() { }
}
