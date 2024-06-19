package moe.protasis.yukicommons.api.player;

import lombok.Getter;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.util.List;

public abstract class PlayerComponent<T extends WrappedPlayer> {
    @Getter
    protected final T owner;
    public PlayerComponent(T owner) {
        this.owner = owner;
    }

    protected abstract void LoadData();
    protected abstract void Save();
}
