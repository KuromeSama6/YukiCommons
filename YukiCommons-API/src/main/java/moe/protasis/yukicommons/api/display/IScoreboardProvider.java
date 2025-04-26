package moe.protasis.yukicommons.api.display;

import java.util.Collection;

public interface IScoreboardProvider extends IDisplayProvider {
    boolean IsVisible();
    String GetTitle();
    Collection<String> GetLines();
}
