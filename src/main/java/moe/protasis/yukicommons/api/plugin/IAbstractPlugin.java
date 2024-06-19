package moe.protasis.yukicommons.api.plugin;

import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;

import java.util.logging.Logger;

public interface IAbstractPlugin {
    IAbstractScheduler GetScheduler();
    Logger GetLogger();
}
