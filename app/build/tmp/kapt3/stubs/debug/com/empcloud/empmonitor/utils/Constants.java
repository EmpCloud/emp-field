package com.empcloud.empmonitor.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0015\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0003\b\u00f8\u0001\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010X\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010[\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010g\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010h\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010i\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010j\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010k\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010l\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010m\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010o\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010p\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010q\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010s\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010u\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010v\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010w\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010x\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010y\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010z\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010{\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010|\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010}\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010~\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u007f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0080\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0081\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0082\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0083\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0084\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0085\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0086\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0087\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0088\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0089\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008a\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008b\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008c\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008d\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008e\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008f\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0090\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0091\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0092\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0093\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0094\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0095\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0096\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0097\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0098\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0099\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009a\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009b\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009c\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009d\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009e\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009f\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00aa\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ab\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ac\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ad\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ae\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00af\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ba\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00bb\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00bc\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00bd\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00be\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00bf\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00c9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ca\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00cb\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00cc\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00cd\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ce\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00cf\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00d9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00da\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00db\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00dc\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00dd\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00de\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00df\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00e9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ea\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00eb\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ec\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ed\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ee\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ef\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f0\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f1\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f2\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f3\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f4\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f5\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f6\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f7\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f8\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00f9\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00fa\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00fb\u0001\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u00fc\u0001"}, d2 = {"Lcom/empcloud/empmonitor/utils/Constants;", "", "()V", "ADDRESS", "", "ADDRESSSECOND", "ADDRESSSECOND_1", "ADDRESS_CLIENT", "ADDRESS_CLIENT_1", "ADDTASK_CLIENTIT", "ADDTASK_CLIENT_NAME", "ADDTASK_PICDESCRIPTION", "ADDTASK_PICRESPONSE", "ADDTASK_TASKDESC", "ADDTASK_TASKNAME", "ADDTASK_TIME", "ADDTASK_TIME2", "ADDTASK_UPLOADDOSC_1", "ADDTASK_UPLOADDOSC_2", "ATTENDANCE", "AUTH_TOKEN", "AUTO_CHECK_IN_BY_GEO_FENCING", "AUTO_CHECK_IN_BY_MOBILE", "AUTO_CHECK_IN_TIME", "AUTO_CHECK_OUT_ACTION", "AUTO_CHECK_OUT_PENDING", "BACKNO", "BITMAP_RECIEVE", "BITMAP_RECIEVE_UPDATE", "BITMAP_USER_PIC", "CATEGORY_CLIENT", "CHANGED_FRAGMENT_FIRST_TIME", "CHECK_IN_METHOD", "CHECK_IN_METHOD_MAP", "CHECK_IN_METHOD_MOBILE", "CITY", "CITY_CLIENT", "CITY_UPDATE", "CITY_UPDATE_1", "CLIENTIT_RESEDN", "CLIENTIT_SEDN", "CLIENT_ADDRES1", "CLIENT_ADDRESS2", "CLIENT_BITMAP_RECIEVE", "CLIENT_CATEGORY", "CLIENT_DETAILS", "CLIENT_DETIALS_BUNDLE", "CLIENT_EDIT_UPDATE_PROFILE", "CLIENT_EMAIL", "CLIENT_ID", "CLIENT_ID_CREATED", "CLIENT_NAME", "CLIENT_NAME_RESEND", "CLIENT_NAME_SELECTED", "CLIENT_NAME_SEND", "CLIENT_NAME_START", "CLIENT_NAME_TASK", "CLIENT_PHONE", "CLIENT_PHONE_COUTRY_CODE", "CLIENT_PHONE_COUTRY_NAME_CODE", "CLIENT_PROFILE_URL", "CLIENT_UPLOAD_PROFLE_IMAGE", "COOKIES_WEB", "COUNT", "COUNTRY", "COUNTRYCODE", "COUNTRYCODE_1", "COUNTRYNAME", "COUNTRYNAME_1", "COUNTRY_CLIENT", "COUNTRY_CLIENT_NEW", "COUNTRY_CODE", "CREATE_CLIENT", "CREATE_LEAVE", "CREATE_PROFILE_BITMAP_PIC", "CREATE_SECTION", "CREATE_TASK", "CREATE_USERNAME", "CREATE_USER_PHONE", "CREATE_USER_PROFILE", "CURRENCYSELECTION1", "CURRENCY_SELECTION", "DELETE_LEAVE", "DIAL_NUMBER", "DIRECTION_ADDRESS", "DIRECTION_NAME", "DOCS_URI_1", "DOCS_URI_2", "DOC_URI", "EDIT_ATTENDANCE", "EDIT_BACK", "EDIT_BACK_FULL", "EDIT_LEAVE", "EMAIL", "ENCRYPT_EMAIL_DATA", "FENCE_RADIUS", "FETCH_ATTENDANCE", "FETCH_CLEINT", "FETCH_LEAVES", "FETCH_PROFILE", "FETCH_TASK", "FILENAME_DOCS_1", "FILENAME_DOCS_2", "FILTER_TASK", "FIRST_HOME_CLICK", "FIRST_RUN", "FORGOT_PASSWORD", "FORGOT_PASS_OTP", "FRAGMENT_NAME", "FRAGMENT_NO", "FREQUENCY", "GEO_LAT", "GEO_LOG_STATUS", "GEO_LON", "GEO_PREF", "GEO_RADIUS", "GET_LEAVE_TYPE", "GET_TASK_STATE_TAGS", "GET_TRACKING_SETTINGS", "HOLIDAT_FETCH", "ISHANDLERSTOP", "ISTASK_STARTED", "IS_CHECKEDIN", "IS_CHECKED_OUT", "IS_GEO_FENCING_ON", "IS_GLOBAL_USER", "IS_PENDING_TASK", "IS_PENDING_TASK_RESCHEDULE", "ITEM_DATE", "LAST_MODE_SELECTED", "LAT", "LATITUDE_CLIENT", "LATITUDE_CLIENT_NEW", "LAT_CLIENT_ADD", "LAT_SEC", "LAT_UPDATE", "LAT_UPDATED", "LAT_UPDATE_1", "LEAVE_ID", "LOCATION_LIST", "LOGIN_NAME_PERSON", "LOGIN_ROLE_PERSON", "LOGIN_TYPE", "LON", "LONGITUDE_CLIENT", "LONGITUDE_CLIENT_NEW", "LON_CLIENT_ADD", "LON_SEC", "LON_UPDATE", "LON_UPDATED", "LON_UPDATE_1", "MARK_ATTENDANCE", "MOBILE_LOGIN", "MOBILE_NUMBER", "MODE_TRANSPORT_SELECTION", "NAME", "NAME_FULL", "NOTIFICATION", "NOTIFICATION_ALL_READ", "ORG_LATITUDE", "ORG_LONGITUDE", "ORG_RADIUS", "PASSWORD", "PHONE_NUMBER", "PICRESPONSE_GALLERY", "PICS_DISC_1", "PICS_DISC_2", "PICS_DISC_3", "PICS_DISC_4", "PICS_UPLOAD_MULTIPLE", "PICS_URL_1", "PICS_URL_2", "PICS_URL_3", "PICS_URL_4", "PIC_DESC", "PIC_DESC_NEW", "PIC_RESPONSE", "PROFILE_PIC_URL_USER", "PROFILE_URL", "RESET_PASSWORD", "ROLE", "SELECTED_MODEL_ITEM", "SELECTED_MODEL_ITEM_1", "SELECTED_MODEL_ITEM_2", "SELECTED_MODEL_ITEM_3", "SEND_LOCATION", "STARTDONEBACK", "STATE", "STATE_CLIENT", "STATE_UPDATE", "STATE_UPDATE_1", "STATUS_SETTINGS", "STATUS_TASK", "TASKVALUE1", "TASKVOLUME1", "TASK_ADDRESS", "TASK_ID", "TASK_NAME", "TASK_NAME_ADD", "TASK_STATUS", "TASK_TIME_SCHEDULE", "TASK_TIME_SCHEDULE2", "TASK_TIMING", "TASK_VALUE", "TASK_VOLUME", "TOKEN_ID", "UMN", "UPDATE_ADDRESS", "UPDATE_AGE", "UPDATE_CITY", "UPDATE_CLIENT", "UPDATE_CLIENT_BACK_MAP", "UPDATE_EMAIL", "UPDATE_GENDER", "UPDATE_LAT", "UPDATE_LON", "UPDATE_MOBILENO", "UPDATE_NAME", "UPDATE_PROFILE", "UPDATE_PROFILE_DATA", "UPDATE_PROFILE_LAT", "UPDATE_PROFILE_LON", "UPDATE_PROFILE_NEW", "UPDATE_RESCHEDULE", "UPDATE_STATE", "UPDATE_TASK", "UPDATE_TASK_FILES", "UPDATE_ZIP", "UPLOAD_PROFILE_IMAGE", "USER_AGE", "USER_CITY", "USER_CREATE_ADDRES1", "USER_CREATE_ADDRESS2", "USER_CREATE_EMAIL", "USER_FULL_NAME", "USER_GENDER", "USER_ID", "USER_LAT", "USER_LOGIN", "USER_LONG", "USER_MAIL_ID", "USER_NAME", "USER_OUNTRY", "USER_PHONE_MOBIILE", "USER_STATE", "USER_ZIP", "VERIFY_EMAIL", "VERIFY_PHONE", "ZIP", "ZIP_CLIENT", "ZIP_UPDATE", "ZIP_UPDATE_1", "app_debug"})
public final class Constants {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTH_TOKEN = "auth_token";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_ID = "USER_ID";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIRST_RUN = "FIRST_RUN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOCATION_LIST = "LOCATION_LIST";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String VERIFY_EMAIL = "open-user/verify-email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_LOGIN = "open-user/user-login";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORGOT_PASSWORD = "open-user/forgot-password";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RESET_PASSWORD = "open-user/reset-password";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String VERIFY_PHONE = "open-user/verify-phone";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FETCH_PROFILE = "profile/fetchProfile";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_PROFILE = "profile/updateProfile";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MARK_ATTENDANCE = "attendance/mark-attendance";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FETCH_ATTENDANCE = "attendance/attendance";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPLOAD_PROFILE_IMAGE = "profile/uploadProfileImage";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HOLIDAT_FETCH = "holiday/get-holiday";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FETCH_LEAVES = "leaves/get-leaves";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ATTENDANCE = "attendance/fetch-attendance";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_LEAVE = "leaves/create-leave";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EDIT_LEAVE = "leaves/update-leaves";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DELETE_LEAVE = "leaves/delete-leaves";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_CLIENT = "client/create";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FETCH_CLEINT = "client/fetch";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EDIT_ATTENDANCE = "attendance/attendance-request";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FETCH_TASK = "task/fetch";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_TASK = "task/update-taskStatus";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_TASK = "task/create";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_UPLOAD_PROFLE_IMAGE = "client/clientUploadProfileImage";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SEND_LOCATION = "track/get-location";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_TASK_FILES = "task/uploadTask-files";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_RESCHEDULE = "task/update";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FILTER_TASK = "task/filterTask";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_CLIENT = "client/update";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GET_LEAVE_TYPE = "leaves/fetch-leave-type";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MODE_TRANSPORT_SELECTION = "profile/Update-Emp-mode-of-transport";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIFICATION = "task/getNotification";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GET_TASK_STATE_TAGS = "tags/getTags";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GET_TRACKING_SETTINGS = "open-user/get-tracking-settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTO_CHECK_IN_BY_MOBILE = "AUTO_CHECK_IN_BY_MOBILE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTO_CHECK_IN_BY_GEO_FENCING = "AUTO_CHECK_IN_BY_GEO_FENCING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_GEO_FENCING_ON = "IS_GEO_FENCING_ON";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ORG_LATITUDE = "ORG_LATITUDE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ORG_LONGITUDE = "ORG_LONGITUDE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ORG_RADIUS = "ORG_RADIUS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTO_CHECK_IN_TIME = "AUTO_CHECK_IN_TIME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTO_CHECK_OUT_PENDING = "AUTO_CHECK_OUT_PENDING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTO_CHECK_OUT_ACTION = "com.empcloud.empmonitor.AUTO_CHECK_OUT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHECK_IN_METHOD = "CHECK_IN_METHOD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHECK_IN_METHOD_MAP = "MAP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHECK_IN_METHOD_MOBILE = "MOBILE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GEO_LOG_STATUS = "GEO_LOG_STATUS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PHONE_NUMBER = "phone_number";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EMAIL = "Email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PASSWORD = "password";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORGOT_PASS_OTP = "forgot_pass_otp";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOGIN_TYPE = "login_type";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CITY = "city";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRY = "country";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESS = "address";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE = "state";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ZIP = "zip";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CITY_CLIENT = "city_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRY_CLIENT = "country_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESS_CLIENT = "address_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESS_CLIENT_1 = "address_CLIENT_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_CLIENT = "state_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ZIP_CLIENT = "zip_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LATITUDE_CLIENT = "latitude_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LONGITUDE_CLIENT = "longitude_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRY_CLIENT_NEW = "COUNTRY_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BITMAP_USER_PIC = "user_pic";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_PROFILE_BITMAP_PIC = "create_profile_bitmap";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_FULL_NAME = "full_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NAME_FULL = "NAME_FULL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ROLE = "role";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_SECTION = "create_section";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LEAVE_ID = "leave_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NAME = "NAME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_DETAILS = "client_details";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME = "client_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_EMAIL = "client_email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_PHONE = "client_phone";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_CATEGORY = "client_category";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_ADDRES1 = "client_addres1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_ADDRESS2 = "client_address2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_PHONE_COUTRY_CODE = "CLIENT_PHONE_COUTRY_CODE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_PHONE_COUTRY_NAME_CODE = "CLIENT_PHONE_COUTRY_NAME_CODE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_USER_PROFILE = "create_user_client";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_USERNAME = "username";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_CREATE_EMAIL = "useremail";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CREATE_USER_PHONE = "userphone";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_CREATE_ADDRES1 = "user_create_addres1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_CREATE_ADDRESS2 = "user_create_address2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_CITY = "user_city";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_OUNTRY = "user_country";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_STATE = "user_state";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_ZIP = "user_zip";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_AGE = "user_age";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_GENDER = "user_gender";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_LAT = "user_lat";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_LONG = "user_long";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DIAL_NUMBER = "Dial Number";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DIRECTION_NAME = "direction_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DIRECTION_ADDRESS = "direction_address";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT = "direction_lat";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON = "direction_lon";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT_SEC = "direction_lat_sec";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON_SEC = "direction_lon_sec";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_ID = "client_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME_SELECTED = "client_name_selected";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_NAME = "task_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME_START = "clinet_name_start";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_TIMING = "task_timing";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_ADDRESS = "task_address";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ISTASK_STARTED = "istask_starteed";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_ID = "task_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME_TASK = "client_name_task";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GEO_PREF = "geo_pref";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GEO_LAT = "geo_lat";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GEO_LON = "geo_lon";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GEO_RADIUS = "geo_radius";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_TASK = "status_Task";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRY_CODE = "country_code";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_NAME = "name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_MAIL_ID = "mail_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_PHONE_MOBIILE = "phone_no";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROFILE_URL = "profile_url";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BITMAP_RECIEVE = "bitmap_recieve";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BITMAP_RECIEVE_UPDATE = "bitmap_recieve_update";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_BITMAP_RECIEVE = "CLIENT_BITMAP_RECIEVE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOBILE_LOGIN = "mobile_login";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_PROFILE_DATA = "update_profile_data";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_ADDRESS = "update_address";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_CITY = "update_city";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_STATE = "update_state";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_ZIP = "update_zip";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_LAT = "update_lat";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_LON = "update_lon";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_NAME = "update_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_AGE = "update_age";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_MOBILENO = "update_mobile";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_EMAIL = "update_email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_GENDER = "update_gender";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_PROFILE_NEW = "update_profile_new";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_SETTINGS = "status_setitings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UMN = "UMN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PIC_RESPONSE = "pic_response";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PIC_DESC = "pic_desc";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_NAME_ADD = "task_add";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EDIT_BACK = "edit_back";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EDIT_BACK_FULL = "edit_back_full";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_ID_CREATED = "created_client_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_DETIALS_BUNDLE = "client_details_bundle";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_UPDATE = "STATE_UPDATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ZIP_UPDATE = "ZIP_UPDATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CITY_UPDATE = "CITY_UPDATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT_UPDATE = "LAT_UPDATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON_UPDATE = "LON_UPDATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESSSECOND = "ADDRESSSECOND";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRYCODE = "COUNTRYCODE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRYNAME = "COUNTRYNAME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_UPDATE_1 = "STATE_UPDATE_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ZIP_UPDATE_1 = "ZIP_UPDATE_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CITY_UPDATE_1 = "CITY_UPDATE_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT_UPDATE_1 = "LAT_UPDATE_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON_UPDATE_1 = "LON_UPDAT_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESSSECOND_1 = "ADDRESSSECOND_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRYCODE_1 = "COUNTRYCODE_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRYNAME_1 = "COUNTRYNAME_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ISHANDLERSTOP = "ISHANDLERSTOP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FREQUENCY = "FREQUENCY";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FENCE_RADIUS = "FENCE_RADIUS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_STATUS = "TASK_STATUS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_CLIENTIT = "ADDTASK_CLIENTIT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_CLIENT_NAME = "ADDTASK_CLIENT_NAME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_PICRESPONSE = "ADDTASK_PICRESPONSE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_PICDESCRIPTION = "ADDTASK_PICDESCRIPTION";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_TIME = "ADDTASK_TIME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_TIME2 = "ADDTASK_TIME2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_TASKNAME = "ADDTASK_TASKNAME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_TASKDESC = "ADDTASK_TASKDESC";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENTIT_SEDN = "CLIENTIT_SEDN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME_SEND = "CLIENT_NAME_SEND";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENTIT_RESEDN = "CLIENTIT_RESEDN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_NAME_RESEND = "CLIENT_NAME_RESEND";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANGED_FRAGMENT_FIRST_TIME = "CHANGED_FRAGMENT_FIRST_TIME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_UPLOADDOSC_1 = "ADDTASK_UPLOADDOSC1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDTASK_UPLOADDOSC_2 = "ADDTASK_UPLOADDOSC2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FILENAME_DOCS_1 = "FILENAME_DOCS1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FILENAME_DOCS_2 = "FILENAME_DOCS2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SELECTED_MODEL_ITEM = "SELECTED_MODEL_ITEM";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SELECTED_MODEL_ITEM_1 = "SELECTED_MODEL_ITEM_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SELECTED_MODEL_ITEM_2 = "SELECTED_MODEL_ITEM_2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SELECTED_MODEL_ITEM_3 = "SELECTED_MODEL_ITEM_3";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FRAGMENT_NO = "FRAGMENT_NO";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BACKNO = "BACKNO";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICRESPONSE_GALLERY = "PICRESPONSE_GALLERY";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PIC_DESC_NEW = "PIC_DESC_NEW";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STARTDONEBACK = "STARTDONEBACK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIRST_HOME_CLICK = "FIRST_HOME_CLICK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT_UPDATED = "LAT_UPDATED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON_UPDATED = "LON_UPDATED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COOKIES_WEB = "COOKIES_WEB";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TOKEN_ID = "TOKEN_ID";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ENCRYPT_EMAIL_DATA = "ENCRYPT_EMAIL_DATA";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAT_CLIENT_ADD = "LAT_CLIENT_ADD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LON_CLIENT_ADD = "LON_CLIENT_ADD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_PROFILE_URL = "CLIENT_PROFILE_URL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CATEGORY_CLIENT = "CATEGORY_CLIENT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOGIN_NAME_PERSON = "LOGIN_NAME_PERSON";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOGIN_ROLE_PERSON = "LOGIN_ROLE_PERSON";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_TIME_SCHEDULE = "TASK_TIME_SCHEDULE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_TIME_SCHEDULE2 = "TASK_TIME_SCHEDULE2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LATITUDE_CLIENT_NEW = "LATITUDE_CLIENT_NEW";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LONGITUDE_CLIENT_NEW = "LONGITUDE_CLIENT_NEW";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LAST_MODE_SELECTED = "LAST_MODE_SELECTED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_PROFILE_LAT = "UPDATE_PROFILE_LAT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_PROFILE_LON = "UPDATE_PROFILE_LON";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROFILE_PIC_URL_USER = "PROFILE_PIC_URL_USER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CLIENT_EDIT_UPDATE_PROFILE = "CLIENT_EDIT_UPDATE_PROFILE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_CHECKEDIN = "IS_CHECKEDIN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DOC_URI = "DOC_URI";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_VOLUME = "TASK_VOLUME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASK_VALUE = "TASK_VALUE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CURRENCY_SELECTION = "CURRENCY_SELECTION";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASKVALUE1 = "TASKVALUE1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TASKVOLUME1 = "TASKVOLUME1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CURRENCYSELECTION1 = "CURRENCYSELECTION1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DOCS_URI_1 = "DOCS_URI_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DOCS_URI_2 = "DOCS_URI_2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNT = "COUNT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_UPLOAD_MULTIPLE = "PICS_UPLOAD_MULTIPLE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_URL_1 = "PICS_URL_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_URL_2 = "PICS_URL_2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_URL_3 = "PICS_URL_3";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_URL_4 = "PICS_URL_4";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_DISC_1 = "PICS_DISC_1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_DISC_2 = "PICS_DISC_2";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_DISC_3 = "PICS_DISC_3";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PICS_DISC_4 = "PICS_DISC_4";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIFICATION_ALL_READ = "NOTIFICATION_ALL_READ";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_GLOBAL_USER = "IS_GLOBAL_USER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ITEM_DATE = "ITEM_DATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FRAGMENT_NAME = "FRAGMENT_NAME";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOBILE_NUMBER = "MOBILE_NUMBER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_PENDING_TASK = "IS_PENDING_TASK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_PENDING_TASK_RESCHEDULE = "IS_PENDING_TASK_RESCHEDULE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_CHECKED_OUT = "IS_CHECKED_OUT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UPDATE_CLIENT_BACK_MAP = "UPDATE_CLIENT_BACK_MAP";
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.utils.Constants INSTANCE = null;
    
    private Constants() {
        super();
    }
}