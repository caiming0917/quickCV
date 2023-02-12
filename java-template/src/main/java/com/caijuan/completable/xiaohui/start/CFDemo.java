package com.caijuan.completable.xiaohui.start;

import com.caijuan.utils.SmallTool;

import java.util.concurrent.CompletableFuture;

public class CFDemo {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            SmallTool.info("查询订单");
            return "订单信息";
        }).thenCompose(order -> CompletableFuture.supplyAsync(() -> {
            SmallTool.info("支付");
            return "账单信息";
        }).thenCompose(bill -> CompletableFuture.supplyAsync(() -> {
            SmallTool.info("支付");
            return "邮件";
        })));
        SmallTool.info(cf.join());
    }
}
