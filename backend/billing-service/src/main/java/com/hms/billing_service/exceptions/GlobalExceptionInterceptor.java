package com.hms.billing_service.exceptions;

import io.grpc.*;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Status;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Component
@GrpcGlobalServerInterceptor
public class GlobalExceptionInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> delegateListener = next.startCall(
                new SimpleForwardingServerCall<>(call) {
                    @Override
                    public void sendMessage(RespT message) {
                        try {
                            super.sendMessage(message);
                        } catch (Exception e) {
                            closeCallWithError(call, e);
                        }
                    }
                }, headers);

        return new SimpleForwardingServerCallListener<>(delegateListener) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (ResourceNotFoundException e) {
                    closeCallWithStatus(call, Status.NOT_FOUND.withDescription(e.getMessage()));
                } catch (IllegalArgumentException e) {
                    closeCallWithStatus(call, Status.INVALID_ARGUMENT.withDescription(e.getMessage()));
                } catch (Exception e) {
                    closeCallWithStatus(call, Status.INTERNAL.withDescription("Internal server error").withCause(e));
                }
            }
        };
    }

    private <RespT> void closeCallWithStatus(ServerCall<?, RespT> call, Status status) {
        call.close(status, new Metadata());
    }

    private <RespT> void closeCallWithError(ServerCall<?, RespT> call, Throwable t) {
        Status status = Status.INTERNAL.withDescription(t.getMessage()).withCause(t);
        call.close(status, new Metadata());
    }
}

