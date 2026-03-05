package moe.protasis.yukicommons.util.promise;

import moe.protasis.yukicommons.api.scheduler.PooledScheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AsyncPromise<T> {
    private final CompletableFuture<T> future;
    private final PooledScheduler scheduler;

    public AsyncPromise(PooledScheduler scheduler, CompletableFuture<T> future) {
        this.scheduler = scheduler;
        this.future = future;
    }

    public AsyncPromise<T> Then(Consumer<T> consumer) {
        future.thenAcceptAsync(consumer);
        return this;
    }

    public AsyncPromise<T> ThenOnMainThread(Consumer<T> consumer) {
        future.thenAcceptAsync(c -> scheduler.JoinMain(() -> {
            consumer.accept(c);
        }));
        return this;
    }

    public AsyncPromise<T> Err(Consumer<Throwable> consumer) {
        future.exceptionally(e -> {
            consumer.accept(e);
            return null;
        });
        return this;
    }

    public AsyncPromise<T> ErrOnMainThread(Consumer<Throwable> consumer) {
        future.exceptionally(e -> {
            scheduler.JoinMain(() -> {
                consumer.accept(e);
            });
            return null;
        });
        return this;
    }

    public AsyncPromise<T> Finally(Runnable runnable) {
        future.whenComplete((res, ex) -> runnable.run());
        return this;
    }

    public AsyncPromise<T> FinallyOnMainThread(Runnable runnable) {
        future.whenComplete((res, ex) -> {
            scheduler.JoinMain(runnable);
        });
        return this;
    }

    public AsyncPromise<T> FinallySync(Runnable runnable) {
        future.whenComplete((res, ex) -> {
            scheduler.JoinMain(runnable);
        });
        return this;
    }

    public T Join() throws ExecutionException, InterruptedException {
        return future.get();
    }
}
