package com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ReceivedRetryWithDelay implements Function<Observable<? extends Throwable>, ObservableSource<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public ReceivedRetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }


    @Override
    public ObservableSource<?> apply(@NonNull Observable<? extends Throwable> flowable) throws Exception {
        return flowable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public Observable<?> apply(final Throwable throwable) {
                        if (maxRetries == 0 || ++retryCount < maxRetries) {
                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }

                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}