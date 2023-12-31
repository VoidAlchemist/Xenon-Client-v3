package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
    protected boolean isJumping;
    private EntityLiving entity;

    public EntityJumpHelper(EntityLiving entityIn) {
        this.entity = entityIn;
    }

    public void setJumping() {
        this.isJumping = true;
    }

    /**
     * Called to actually make the entity jump if isJumping is true.
     */
    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
