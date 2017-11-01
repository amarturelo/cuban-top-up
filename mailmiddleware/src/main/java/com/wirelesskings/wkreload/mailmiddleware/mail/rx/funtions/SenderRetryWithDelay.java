package com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class SenderRetryWithDelay implements Function<Flowable<? extends Throwable>, Publisher<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public SenderRetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }


    @Override
    public Publisher<?> apply(@NonNull Flowable<? extends Throwable> flowable) throws Exception {
        return flowable
                .flatMap(new Function<Throwable, Publisher<?>>() {
                    @Override
                    public Flowable<?> apply(final Throwable throwable) {
                        if (maxRetries == 0 || ++retryCount < maxRetries) {
                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).
                            return Flowable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }

                        // Max retries hit. Just pass the error along.
                        return Flowable.error(throwable);
                    }
                });
    }
}