package com.example.animecalendar.base;

public class Resource<T> {

    enum Status { SUCCESS, ERROR, LOADING }

    private final Resource.Status status;
    private final T data;
    private final Throwable exception;

    private Resource(Status status, T data, Throwable exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean hasError() {
        return status == Status.ERROR;
    }

    public boolean hasSuccess() {
        return status == Status.SUCCESS;
    }

    public T getData() {
        return data;
    }

    public Throwable getException() {
        return exception;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable exception) {
        return new Resource<>(Status.ERROR, null, exception);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

}