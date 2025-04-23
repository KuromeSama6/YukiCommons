package moe.protasis.yukicommons.api.player;

import moe.protasis.yukicommons.api.entity.IAbstractLivingEntity;

public interface IWrappedPlayer extends INamedHumanoid {
    IAbstractPlayer getPlayer();

    @Override
    default boolean IsOnline() {
        return getPlayer().IsOnline();
    }

    @Override
    default IAbstractLivingEntity GetEntity() {
        return getPlayer();
    }

    @Override
    default String GetName() {
        return getPlayer().GetName();
    }
}
